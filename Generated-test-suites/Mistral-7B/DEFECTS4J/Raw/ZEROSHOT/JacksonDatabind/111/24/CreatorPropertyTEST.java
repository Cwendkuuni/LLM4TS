package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link CreatorProperty}.
 */
public class CreatorPropertyTest {

    private ObjectNode rootNode;
    private ObjectMapper mapper;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        rootNode = mapper.createObjectNode();
    }

    @Test
    public void testCreatorPropertyCreation() {
        // Test creation with no annotations
        JavaType intType = mapper.getTypeFactory().constructType(int.class);
        AnnotatedMember member = Annotations.findMemberAnnotations(intType, "value");
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("value"), intType, null, null,
                Annotations.empty(), null, 0, null, PropertyMetadata.STANDARD_EMPTY);
        assertEquals("value", property.getName().getText());
        assertNull(property._annotated);
        assertFalse(property.isIgnorable());

        // Test creation with a single annotation
        JsonInclude.Include includeAnnotation = Annotations.findAnnotation(JsonInclude.class, intType);
        AnnotatedMember includeMember = Annotations.wrap(member, includeAnnotation);
        CreatorProperty propertyWithAnnotation = new CreatorProperty(
                PropertyName.construct("value"), intType, null, null,
                Annotations.empty(), includeMember, 0, null, PropertyMetadata.STANDARD_EMPTY);
        assertEquals("value", propertyWithAnnotation.getName().getText());
        assertSame(includeAnnotation, propertyWithAnnotation.getAnnotation(JsonInclude.class));
        assertFalse(propertyWithAnnotation.isIgnorable());

        // Test creation with multiple annotations
        JsonInclude.Include includeAnnotation1 = Annotations.findAnnotation(JsonInclude.class, intType);
        JsonInclude.Include includeAnnotation2 = Annotations.findAnnotation(JsonInclude.class, intType, "NON_DEFAULT");
        AnnotatedMember includeMember1 = Annotations.wrap(member, includeAnnotation1);
        AnnotatedMember includeMember2 = Annotations.wrap(member, includeAnnotation2);
        CreatorProperty propertyWithMultipleAnnotations = new CreatorProperty(
                PropertyName.construct("value"), intType, null, null,
                Annotations.empty(), includeMember1, 0, null, PropertyMetadata.STANDARD_EMPTY);
        assertEquals("value", propertyWithMultipleAnnotations.getName().getText());
        assertSame(includeAnnotation1, propertyWithMultipleAnnotations.getAnnotation(JsonInclude.class));
        assertSame(includeAnnotation2, propertyWithMultipleAnnotations.getAnnotation(JsonInclude.class, "NON_DEFAULT"));
        assertFalse(propertyWithMultipleAnnotations.isIgnorable());
    }

    @Test
    public void testGetAnnotation() throws IOException {
        JavaType javaClass = mapper.getTypeFactory().constructType(TestClassWithCreatorProperty.class);
        AnnotatedMember member = Annotations.findMemberAnnotations(javaClass, "creatorProperty");
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("creatorProperty"), javaClass.getComponentType(), null, null,
                Annotations.empty(), member, 0, null, PropertyMetadata.STANDARD_EMPTY);

        JsonInclude annotation = property.getAnnotation(JsonInclude.class);
        assertNotNull(annotation);
        assertEquals(JsonInclude.Include.NON_NULL, annotation.value());

        JsonProperty annotation2 = property.getAnnotation(JsonProperty.class);
        assertNotNull(annotation2);
        assertEquals("creatorProperty2", annotation2.value());
    }

    @Test
    public void testSetFallbackSetter() throws IOException {
        JavaType javaClass = mapper.getTypeFactory().constructType(TestClassWithCreatorProperty.class);
        AnnotatedMember member = Annotations.findMemberAnnotations(javaClass, "creatorProperty");
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("creatorProperty"), javaClass.getComponentType(), null, null,
                Annotations.empty(), member, 0, null, PropertyMetadata.STANDARD_EMPTY);

        SettableBeanProperty fallbackSetter = new SettableBeanProperty(
                PropertyName.construct("fallbackSetter"), javaClass.getComponentType(), null, null,
                Annotations.empty(), null, null, PropertyMetadata.STANDARD_EMPTY);
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    @Test
    public void testIsIgnorable() throws IOException {
        JavaType javaClass = mapper.getTypeFactory().constructType(TestClassWithCreatorProperty.class);
        AnnotatedMember member = Annotations.findMemberAnnotations(javaClass, "creatorProperty");
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("creatorProperty"), javaClass.getComponentType(), null, null,
                Annotations.empty(), member, 0, null, PropertyMetadata.STANDARD_EMPTY);

        assertFalse(property.isIgnorable());

        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetterThrowsException() throws IOException {
        JavaType javaClass = mapper.getTypeFactory().constructType(TestClassWithCreatorProperty.class);
        AnnotatedMember member = Annotations.findMemberAnnotations(javaClass, "creatorPropertyMissingSetter");
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("creatorPropertyMissingSetter"), javaClass.getComponentType(), null, null,
                Annotations.empty(), member, 0, null, PropertyMetadata.STANDARD_EMPTY);

        try {
            property.deserializeAndSet(mapper.getFactory().getParserForObject(), null, null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }
}

class TestClassWithCreatorProperty {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("creatorProperty2")
    private int creatorProperty;

    @JsonInclude
    @JsonProperty("creatorProperty")
    private TestInnerClass creatorProperty;
}

class TestInnerClass {

    private String value;
}