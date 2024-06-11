package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    // Test for deserialize() method
    @Test
    public void testDeserialize() {
        // Test with empty string array
        JsonParser jp1 = new JsonParser();
        DeserializationContext ctxt1 = new DeserializationContext();
        StringArrayDeserializer deserializer1 = new StringArrayDeserializer();
        String[] result1 = deserializer1.deserialize(jp1, ctxt1);
        assertNull(result1);
        
        // Test with single value string array
        JsonParser jp2 = new JsonParser();
        DeserializationContext ctxt2 = new DeserializationContext();
        StringArrayDeserializer deserializer2 = new StringArrayDeserializer();
        jp2.setCurrentToken(JsonToken.VALUE_STRING);
        jp2.setText("test");
        String[] result2 = deserializer2.deserialize(jp2, ctxt2);
        assertEquals(1, result2.length);
        assertEquals("test", result2[0]);
        
        // Test with null string array
        JsonParser jp3 = new JsonParser();
        DeserializationContext ctxt3 = new DeserializationContext();
        StringArrayDeserializer deserializer3 = new StringArrayDeserializer();
        jp3.setCurrentToken(JsonToken.VALUE_NULL);
        String[] result3 = deserializer3.deserialize(jp3, ctxt3);
        assertEquals(1, result3.length);
        assertNull(result3[0]);
        
        // Test with multiple value string array
        JsonParser jp4 = new JsonParser();
        DeserializationContext ctxt4 = new DeserializationContext();
        StringArrayDeserializer deserializer4 = new StringArrayDeserializer();
        jp4.setCurrentToken(JsonToken.START_ARRAY);
        jp4.nextToken(); // Move to the first value token
        jp4.setValue("test1");
        jp4.nextToken(); // Move to the second value token
        jp4.setValue("test2");
        jp4.nextToken(); // Move to the end array token
        String[] result4 = deserializer4.deserialize(jp4, ctxt4);
        assertEquals(2, result4.length);
        assertEquals("test1", result4[0]);
        assertEquals("test2", result4[1]);
    }

    // Test for _deserializeCustom() method
    @Test
    public void testDeserializeCustom() {
        // Test with empty string array
        JsonParser jp1 = new JsonParser();
        DeserializationContext ctxt1 = new DeserializationContext();
        StringArrayDeserializer deserializer1 = new StringArrayDeserializer();
        jp1.setCurrentToken(JsonToken.START_ARRAY);
        jp1.nextToken(); // Move to the end array token
        String[] result1 = deserializer1._deserializeCustom(jp1, ctxt1);
        assertNull(result1);
        
        // Test with single value string array
        JsonParser jp2 = new JsonParser();
        DeserializationContext ctxt2 = new DeserializationContext();
        StringArrayDeserializer deserializer2 = new StringArrayDeserializer();
        JsonDeserializer<String> elementDeserializer2 = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return p.getValueAsString().toUpperCase();
            }
        };
        deserializer2._elementDeserializer = elementDeserializer2;
        jp2.setCurrentToken(JsonToken.START_ARRAY);
        jp2.nextToken(); // Move to the first value token
        jp2.setValue("test");
        jp2.nextToken(); // Move to the end array token
        String[] result2 = deserializer2._deserializeCustom(jp2, ctxt2);
        assertEquals(1, result2.length);
        assertEquals("TEST", result2[0]);
        
        // Test with null string array
        JsonParser jp3 = new JsonParser();
        DeserializationContext ctxt3 = new DeserializationContext();
        StringArrayDeserializer deserializer3 = new StringArrayDeserializer();
        JsonDeserializer<String> elementDeserializer3 = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return p.getValueAsString().toLowerCase();
            }
        };
        deserializer3._elementDeserializer = elementDeserializer3;
        jp3.setCurrentToken(JsonToken.START_ARRAY);
        jp3.nextToken(); // Move to the first value token
        jp3.setValue(null);
        jp3.nextToken(); // Move to the end array token
        String[] result3 = deserializer3._deserializeCustom(jp3, ctxt3);
        assertEquals(1, result3.length);
        assertNull(result3[0]);
        
        // Test with multiple value string array
        JsonParser jp4 = new JsonParser();
        DeserializationContext ctxt4 = new DeserializationContext();
        StringArrayDeserializer deserializer4 = new StringArrayDeserializer();
        JsonDeserializer<String> elementDeserializer4 = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return p.getValueAsString().toUpperCase();
            }
        };
        deserializer4._elementDeserializer = elementDeserializer4;
        jp4.setCurrentToken(JsonToken.START_ARRAY);
        jp4.nextToken(); // Move to the first value token
        jp4.setValue("test1");
        jp4.nextToken(); // Move to the second value token
        jp4.setValue("test2");
        jp4.nextToken(); // Move to the end array token
        String[] result4 = deserializer4._deserializeCustom(jp4, ctxt4);
        assertEquals(2, result4.length);
        assertEquals("TEST1", result4[0]);
        assertEquals("TEST2", result4[1]);
    }

    // Test for deserializeWithType() method
    @Test
    public void testDeserializeWithType() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertNull(result);
    }

    // Test for handleNonArray() method
    @Test(expected = IOException.class)
    public void testHandleNonArray() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        jp.nextToken(); // Move to a non-array token
        deserializer.handleNonArray(jp, ctxt);
    }

    // Test for createContextual() method
    @Test
    public void testCreateContextual() {
        DeserializationContext ctxt = new DeserializationContext();
        BeanProperty property = new BeanProperty();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertNull(result);
    }

}