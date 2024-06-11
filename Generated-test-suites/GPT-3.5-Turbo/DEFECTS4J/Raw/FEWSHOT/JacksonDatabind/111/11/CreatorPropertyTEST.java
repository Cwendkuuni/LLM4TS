
package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;

    @Before
    public void setUp() throws Exception {
        PropertyName name = new PropertyName("testName");
        JavaType type = new JavaType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations,
                param, index, injectableValueId, metadata);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty property = creatorProperty.withName(newName);
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = null;
        SettableBeanProperty property = creatorProperty.withValueDeserializer(deser);
        assertNull(property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        SettableBeanProperty property = creatorProperty.withNullProvider(nva);
        assertNull(property.getValueDeserializer());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = null;
        creatorProperty.fixAccess(config);
        // To check there's no exception
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = null;
        creatorProperty.setFallbackSetter(fallbackSetter);
        // To check there's no exception
    }

    @Test
    public void testMarkAsIgnorable() {
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testIsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws JsonMappingException {
        DeserializationContext context = null;
        Object beanInstance = null;
        try {
            creatorProperty.findInjectableValue(context, beanInstance);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Property 'testName' (type com.fasterxml.jackson.databind.deser.CreatorProperty) " +
                    "has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testInject() throws Exception {
        DeserializationContext context = null;
        Object beanInstance = null;
        try {
            creatorProperty.inject(context, beanInstance);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testName'", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation() {
        assertNull(creatorProperty.getAnnotation(CreatorPropertyTest.class));
    }

    @Test
    public void testGetMember() {
        assertNull(creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            creatorProperty.deserializeAndSet(p, ctxt, instance);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testName'", e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testName'", e.getMessage());
        }
    }

    @Test
    public void testSet() throws Exception {
        Object instance = null;
        Object value = null;
        try {
            creatorProperty.set(instance, value);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testName'", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = null;
        Object value = null;
        try {
            creatorProperty.setAndReturn(instance, value);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testName'", e.getMessage());
        }
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'testName'; inject id 'null']",
                creatorProperty.toString());
    }
}
