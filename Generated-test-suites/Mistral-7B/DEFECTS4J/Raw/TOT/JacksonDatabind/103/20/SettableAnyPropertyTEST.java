package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        BeanProperty property = mapper.findProperty("testMap", javaType);

        AnnotatedField setter = PropertyOwner.class.getDeclaredField("testMap");
        setter.setAccessible(true);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        Object testInstance = new PropertyOwner();

        // Test setting a simple property
        Object valueToSet = new Object();
        settableAnyProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance, "testProperty");
        assertNotNull(testInstance);
        assertEquals(valueToSet, testInstance.getClass().getDeclaredField("testMap").get(testInstance).get("testProperty"));

        // Test setting a property with a key deserializer
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        SettableAnyProperty settableAnyPropertyWithKeyDeserializer = settableAnyProperty.withKeyDeserializer(keyDeserializer);
        String key = "testKey";
        settableAnyPropertyWithKeyDeserializer.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance, key);
        assertEquals(key, testInstance.getClass().getDeclaredField("testMap").get(testInstance).keySet().iterator().next());

        // Test setting a property with a value deserializer
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);
        Object valueToSetWithDeserializer = new Object();
        settableAnyPropertyWithValueDeserializer.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance, "testProperty");
        assertEquals(valueToSetWithDeserializer, testInstance.getClass().getDeclaredField("testMap").get(testInstance).get("testProperty"));

        // Test setting a property with a value type deserializer
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        SettableAnyProperty settableAnyPropertyWithValueTypeDeserializer = settableAnyProperty.withValueTypeDeserializer(valueTypeDeserializer);
        Object valueToSetWithTypeDeserializer = new Object();
        settableAnyPropertyWithValueTypeDeserializer.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance, "testProperty");
        assertEquals(valueToSetWithTypeDeserializer.getClass(), testInstance.getClass().getDeclaredField("testMap").get(testInstance).get("testProperty").getClass());

        // Test setting a property with a null value
        settableAnyProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance, "testProperty");
        assertNull(testInstance.getClass().getDeclaredField("testMap").get(testInstance).get("testProperty"));

        // Test setting a property with an empty string as a key
        settableAnyPropertyWithKeyDeserializer.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance, "\"\"");
        assertTrue(testInstance.getClass().getDeclaredField("testMap").get(testInstance).isEmpty());

        // Test setting a property with an invalid JSON token
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.nextToken()).thenThrow(new IOException());
        settableAnyProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), testInstance, "testProperty");
        verify(mockJsonParser, times(2)).nextToken();

        // Test setting a property with a non-string key
        JsonParser jsonParser = mapper.getFactory().createParser("{\"testProperty\": 1}");
        settableAnyPropertyWithKeyDeserializer.deserializeAndSet(jsonParser, mockDeserializationContext(), testInstance, "testProperty");
        assertEquals(1, testInstance.getClass().getDeclaredField("testMap").get(testInstance).get("testProperty"));

        // Test setting a property with a non-Object value
        JsonParser jsonParserWithInvalidValue = mapper.getFactory().createParser("{\"testProperty\": []}");
        try {
            settableAnyPropertyWithValueDeserializer.deserializeAndSet(jsonParserWithInvalidValue, mockDeserializationContext(), testInstance, "testProperty");
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test setting a property with an undefined key
        JsonParser jsonParserUndefinedKey = mapper.getFactory().createParser("{testUndefinedProperty: 1}");
        SettableAnyProperty settableAnyPropertyUndefinedKey = new SettableAnyProperty(property, setter, javaType, null, null, null);
        try {
            settableAnyPropertyUndefinedKey.deserializeAndSet(jsonParserUndefinedKey, mockDeserializationContext(), testInstance, "testUndefinedProperty");
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    private JsonParser mockJsonParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext mockDeserializationContext() {
        return mock(DeserializationContext.class);
    }
}