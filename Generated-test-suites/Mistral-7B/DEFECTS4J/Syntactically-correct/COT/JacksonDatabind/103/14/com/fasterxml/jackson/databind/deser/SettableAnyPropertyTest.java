package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class SettableAnyPropertyTest extends TestBase {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSettableAnyProperty() throws IOException, JsonProcessingException {
        // Test constructor
        BeanProperty property = createMockBeanProperty();
        AnnotatedMember setter = createMockAnnotatedMember();
        JavaType type = createMockJavaType();
        KeyDeserializer keyDeserializer = createMockKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = createMockJsonDeserializer();
        TypeDeserializer valueTypeDeserializer = createMockTypeDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);

        // Test getters
        assertNotNull(settableAnyProperty.getProperty());
        assertNotNull(settableAnyProperty.getSetter());
        assertNotNull(settableAnyProperty.getType());
        assertNotNull(settableAnyProperty.getValueDeserializer());
        assertNotNull(settableAnyProperty.getValueTypeDeserializer());
        assertNotNull(settableAnyProperty.getKeyDeserializer());
        assertFalse(settableAnyProperty.isSetterField());

        // Test withValueDeserializer method
        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);
        assertNotNull(settableAnyPropertyWithValueDeserializer);
        assertSame(valueDeserializer, settableAnyPropertyWithValueDeserializer.getValueDeserializer());

        // Test fixAccess method
        settableAnyProperty.fixAccess(getDeserializationConfig());
        assertNotNull(settableAnyProperty.getSetter().getAnnotated());

        // Test deserializeAndSet method (happy path)
        Object instance = createMockObject();
        String propName = "testProperty";
        Object key = "testKey";
        Object value = createMockObject();
        settableAnyProperty.deserializeAndSet(createMockJsonParser(), getDeserializationContext(), instance, propName);
        Map<Object, Object> map = (Map<Object, Object>) settableAnyProperty.getSetter().getValue(instance);
        assertNotNull(map);
        assertNotNull(map.get(key));
        assertEquals(value, map.get(key));

        // Test deserializeAndSet method (null value)
        settableAnyProperty.deserializeAndSet(createMockJsonParser(), getDeserializationContext(), instance, propName);
        map = (Map<Object, Object>) settableAnyProperty.getSetter().getValue(instance);
        assertNull(map.get(propName));

        // Test deserialize method (happy path)
        JsonParser parser = createMockJsonParser();
        Object deserializedValue = settableAnyProperty.deserialize(parser, getDeserializationContext());
        assertNotNull(deserializedValue);

        // Test deserialize method (valueDeserializer is null)
        settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);
        deserializedValue = settableAnyProperty.deserialize(parser, getDeserializationContext());
        assertNull(deserializedValue);

        // Test deserialize method (TypeDeserializer is null)
        settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, null);
        deserializedValue = settableAnyProperty.deserialize(parser, getDeserializationContext());
        assertNotNull(deserializedValue);

        // Test set method (happy path)
        settableAnyProperty.set(instance, propName, value);
        map = (Map<Object, Object>) settableAnyProperty.getSetter().getValue(instance);
        assertNotNull(map);
        assertNotNull(map.get(propName));
        assertEquals(value, map.get(propName));

        // Test set method (instance is null)
        try {
            settableAnyProperty.set(null, propName, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test set method (propName is null)
        try {
            settableAnyProperty.set(instance, null, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test set method (value is null)
        try {
            settableAnyProperty.set(instance, propName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test _throwAsIOE method (IllegalArgumentException)
        String propNameWithInvalidType = "testPropertyWithInvalidType";
        try {
            settableAnyProperty.set(instance, propNameWithInvalidType, value);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        // Test _throwAsIOE method (other exception)
        String propNameWithOtherException = "testPropertyWithOtherException";
        throw new RuntimeException("Error setting property " + propNameWithOtherException);
        try {
            settableAnyProperty.set(instance, propNameWithOtherException, value);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        // Test toString method
        String toString = settableAnyProperty.toString();
        assertTrue(toString.startsWith("[any property on class "));

        // Test AnySetterReferring class
        UnresolvedForwardReference reference = createMockUnresolvedForwardReference();
        SettableAnyProperty parent = settableAnyProperty;
        Object pojo = createMockObject();
        String propNameWithId = "testPropertyWithId";
        Object id = createMockObject();
        Object valueWithId = createMockObject();
        AnySetterReferring anySetterReferring = new AnySetterReferring(parent, reference, type, pojo, propNameWithId);
        anySetterReferring.handleResolvedForwardReference(id, valueWithId);
        assertNotNull(anySetterReferring.getInstance());
        assertEquals(pojo, anySetterReferring.getInstance());
        assertEquals(propNameWithId, anySetterReferring.getPropertyName());
        assertEquals(valueWithId, anySetterReferring.getValue());
    }

    private AnnotatedMember createMockAnnotatedMember() {
        return mock(AnnotatedMember.class);
    }

    private AnnotatedField createMockAnnotatedField() {
        return (AnnotatedField) createMockAnnotatedMember();
    }

    private BeanProperty createMockBeanProperty() {
        return mock(BeanProperty.class);
    }

    private JavaType createMockJavaType() {
        return mock(JavaType.class);
    }

    private KeyDeserializer createMockKeyDeserializer() {
        return mock(KeyDeserializer.class);
    }

    private JsonDeserializer<Object> createMockJsonDeserializer() {
        return mock(JsonDeserializer.class);
    }

    private TypeDeserializer createMockTypeDeserializer() {
        return mock(TypeDeserializer.class);
    }

    private Object createMockObject() {
        try {
            return Map.class.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonParser createMockJsonParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext getDeserializationContext() {
        return createMockContext();
    }

    private DeserializationConfig createMockContext() {
        return mock(DeserializationConfig.class);
    }

    private UnresolvedForwardReference createMockUnresolvedForwardReference() {
        return mock(UnresolvedForwardReference.class);
    }
}