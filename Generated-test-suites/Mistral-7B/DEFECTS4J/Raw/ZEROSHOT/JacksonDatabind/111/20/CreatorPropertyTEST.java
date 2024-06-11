package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class CreatorPropertyTest extends TestBase {

    private static final Object INJECTABLE_VALUE_ID = new Object() {};

    @org.junit.Test
    public void testCreatorPropertyConstruction() throws JsonMappingException, IOException {
        // Create a mock context
        ObjectContext context = mock(ObjectContext.class);

        // Create a mock DeserializationContext
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.getAnnotationInstaller()).thenReturn(context);

        // Create a mock JsonParser
        JsonParser jsonParser = mock(JsonParser.class);

        // Create a mock AnnotatedParameter
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(annotatedParameter.getName()).thenReturn(PropertyName.construct("testProperty"));

        // Create a CreatorProperty instance
        JavaType javaType = createSimpleJavaType("java.lang.Object");
        CreatorProperty creatorProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                javaType,
                null,
                null,
                null,
                annotatedParameter,
                1,
                INJECTABLE_VALUE_ID,
                null);

        // Test basic properties
        assertEquals("testProperty", creatorProperty.getName().getShortName());
        assertEquals(javaType, creatorProperty.getType());
        assertNull(creatorProperty._annotated);
        assertEquals(1, creatorProperty.getCreatorIndex());
        assertNull(creatorProperty._injectableValueId);
        assertNull(creatorProperty._fallbackSetter);
        assertFalse(creatorProperty.isIgnorable());

        // Test injectableValueId property
        creatorProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                javaType,
                null,
                null,
                null,
                annotatedParameter,
                1,
                INJECTABLE_VALUE_ID,
                null);
        assertTrue(creatorProperty.isIgnorable());
        assertEquals(INJECTABLE_VALUE_ID, creatorProperty.getInjectableValueId());

        // Test setFallbackSetter method
        creatorProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                javaType,
                null,
                null,
                null,
                annotatedParameter,
                1,
                INJECTABLE_VALUE_ID,
                null);
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, creatorProperty._fallbackSetter);

        // Test constructor with null parameter
        creatorProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                javaType,
                null,
                null,
                null,
                null,
                1,
                null,
                null);
        assertNull(creatorProperty._annotated);
    }

    @org.junit.Test
    public void testCreatorPropertyDeserializeAndSet() throws JsonMappingException, IOException {
        // Create a mock context
        ObjectContext context = mock(ObjectContext.class);

        // Create a mock DeserializationContext
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.getAnnotationInstaller()).thenReturn(context);

        // Create a mock JsonParser
        JsonParser jsonParser = mock(JsonParser.class);

        // Create a mock AnnotatedParameter
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(annotatedParameter.getName()).thenReturn(PropertyName.construct("testProperty"));

        // Create a mock TypeDeserializer
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        JavaType javaType = createSimpleJavaType("java.lang.Object");
        CreatorProperty creatorProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                javaType,
                null,
                typeDeserializer,
                null,
                annotatedParameter,
                1,
                null,
                null);

        // Test normal deserialization and set
        Object value = new Object();
        when(typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext, javaType)).thenReturn(value);
        creatorProperty.deserializeAndSet(jsonParser, deserializationContext, null);
        assertNotNull(creatorProperty.getBean());
        assertEquals(value, creatorProperty.getBean());

        // Test deserialization and set with null value
        when(typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext, javaType)).thenReturn(null);
        creatorProperty.deserializeAndSet(jsonParser, deserializationContext, null);
        assertNull(creatorProperty.getBean());

        // Test deserialization and set with empty string value
        when(typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext, javaType)).thenReturn("");
        creatorProperty.deserializeAndSet(jsonParser, deserializationContext, null);
        assertNull(creatorProperty.getBean());
    }

    @org.junit.Test
    public void testCreatorPropertyFindInjectableValue() throws JsonMappingException, IOException {
        // Create a mock context
        ObjectContext context = mock(ObjectContext.class);

        // Create a mock DeserializationContext
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.getAnnotationInstaller()).thenReturn(context);

        // Create a CreatorProperty instance
        CreatorProperty creatorProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                createSimpleJavaType("java.lang.Object"),
                null,
                null,
                null,
                null,
                1,
                INJECTABLE_VALUE_ID,
                null);

        // Test findInjectableValue method with null context
        assertNull(creatorProperty.findInjectableValue(null, null));

        // Test findInjectableValue method with valid context and id
        Object injectableValue = new Object();
        when(context.findInjectableValue(INJECTABLE_VALUE_ID, creatorProperty, null)).thenReturn(injectableValue);
        assertEquals(injectableValue, creatorProperty.findInjectableValue(deserializationContext, null));

        // Test findInjectableValue method with null id
        when(context.findInjectableValue(null, creatorProperty, null)).thenThrow(new JsonMappingException(""));
        assertThrows(JsonMappingException.class, () -> creatorProperty.findInjectableValue(deserializationContext, null));
    }

    @org.junit.Test
    public void testCreatorPropertyInject() throws JsonMappingException, IOException {
        // Create a mock context
        ObjectContext context = mock(ObjectContext.class);

        // Create a mock DeserializationContext
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.getAnnotationInstaller()).thenReturn(context);

        // Create a mock JsonParser
        JsonParser jsonParser = mock(JsonParser.class);

        // Create a mock AnnotatedParameter
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(annotatedParameter.getName()).thenReturn(PropertyName.construct("testProperty"));

        // Create a CreatorProperty instance
        JavaType javaType = createSimpleJavaType("java.lang.Object");
        CreatorProperty creatorProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                javaType,
                null,
                null,
                null,
                annotatedParameter,
                1,
                INJECTABLE_VALUE_ID,
                null);

        // Test injection with null context and bean
        assertDoesNotThrow(() -> creatorProperty.inject(deserializationContext, null));

        // Test injection with valid context, id, and bean
        Object injectableValue = new Object();
        when(context.findInjectableValue(INJECTABLE_VALUE_ID, creatorProperty, null)).thenReturn(injectableValue);
        creatorProperty.inject(deserializationContext, null);
        assertNotNull(creatorProperty.getBean());
        assertEquals(injectableValue, creatorProperty.getBean());

        // Test injection with null context and bean, and empty value id
        when(context.findInjectableValue(INJECTABLE_VALUE_ID, creatorProperty, null)).thenThrow(new JsonMappingException(""));
        assertThrows(JsonMappingException.class, () -> creatorProperty.inject(deserializationContext, null));
    }

    @org.junit.Test
    public void testCreatorPropertyToString() {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createSimpleJavaType("java.lang.Object");
        CreatorProperty creatorProperty = new CreatorProperty(
                name,
                type,
                null,
                null,
                null,
                null,
                1,
                null,
                null);
        String expected = "[creator property, name 'testProperty'; inject id 'null']";
        assertEquals(expected, creatorProperty.toString());

        creatorProperty = new CreatorProperty(
                name,
                type,
                null,
                null,
                null,
                null,
                1,
                INJECTABLE_VALUE_ID,
                null);
        expected = "[creator property, name 'testProperty'; inject id '"+INJECTABLE_VALUE_ID+"']";
        assertEquals(expected, creatorProperty.toString());
    }

    @org.junit.Test
    public void testCreatorPropertyMarkAsIgnorable() {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createSimpleJavaType("java.lang.Object");
        CreatorProperty creatorProperty = new CreatorProperty(
                name,
                type,
                null,
                null,
                null,
                null,
                1,
                null,
                null);
        assertFalse(creatorProperty.isIgnorable());

        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }
}