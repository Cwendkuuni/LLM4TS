package com.fasterxml.jackson.databind.deser;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test constructor with all arguments
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "set", new Class[]{}, null);
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = new StdKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new StdValueDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        // Test getters
        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());

        // Test deserializeAndSet method
        Object pojo = new HashMap<>();
        String propName = "test";
        Object key = "testKey";
        Object value = new Integer(42);
        JsonParser jsonParser = jsonFactory.createParser("{\"testKey\":42}");
        settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(objectMapper, null), pojo, propName);
        assertEquals(value, pojo.get(key));

        // Test deserialize method
        JsonParser jsonParser1 = jsonFactory.createParser("{\"test\":42}");
        Object deserializedValue = settableAnyProperty.deserialize(jsonParser1, new DeserializationContext(objectMapper, null));
        assertEquals(value, deserializedValue);

        // Test set method
        Object pojo1 = new HashMap<>();
        settableAnyProperty.set(pojo1, propName, value);
        assertEquals(value, pojo1.get(propName));

        // Test constructor without keyDeserializer and valueTypeDeserializer
        SettableAnyProperty settableAnyProperty1 = new SettableAnyProperty(property, setter, type, null, valueDeserializer, null);
        assertEquals(settableAnyProperty.getProperty(), settableAnyProperty1.getProperty());
        assertFalse(settableAnyProperty1.hasKeyDeserializer());
        assertNull(settableAnyProperty1.getKeyDeserializer());
        assertFalse(settableAnyProperty1.hasValueTypeDeserializer());
        assertNull(settableAnyProperty1.getValueTypeDeserializer());

        // Test withValueDeserializer method
        SettableAnyProperty settableAnyProperty2 = settableAnyProperty.withValueDeserializer(new StdValueDeserializer());
        assertEquals(settableAnyProperty.getProperty(), settableAnyProperty2.getProperty());
        assertEquals(settableAnyProperty2.getValueDeserializer(), new StdValueDeserializer());
        assertEquals(settableAnyProperty.getValueDeserializer(), valueDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test constructor with null setter
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null);
        AnnotatedMember setter = null;
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = new StdKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new StdValueDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testFixAccess() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test fixAccess method
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        DeserializationConfig config = new DeserializationConfig(objectMapper, null);
        settableAnyProperty.fixAccess(config);
    }

    @Test
    public void testReadResolve() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test readResolve method
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        Object obj = settableAnyProperty.readResolve();
        assertSame(settableAnyProperty, obj);
    }

    @Test
    public void testToString() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test toString method
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        String expected = "[any property on class null]";
        String actual = settableAnyProperty.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetWithMap() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test set method with Map
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedField(Map.class, "myMap", new Class[0], null);
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = new StdKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new StdValueDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object pojo = new HashMap<>();
        String propName = "test";
        Object key = "testKey";
        Object value = new Integer(42);
        JsonParser jsonParser = jsonFactory.createParser("{\"testKey\":42}");
        settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(objectMapper, null), pojo, propName);
        Map<Object, Object> map = (Map<Object, Object>) pojo;
        assertEquals(value, map.get(key));

        // Test set method with null Map
        SettableAnyProperty settableAnyProperty1 = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        Object pojo1 = null;
        try {
            settableAnyProperty1.deserializeAndSet(jsonParser, new DeserializationContext(objectMapper, null), pojo1, propName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test _throwAsIOE method
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        IllegalAccessException e = new IllegalAccessException("Illegal access");
        settableAnyProperty._throwAsIOE(e, "test", new Object());
        fail("Expected JsonMappingException");
    }

    private static class StdKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return key;
        }
    }

    private static class StdValueDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }
}