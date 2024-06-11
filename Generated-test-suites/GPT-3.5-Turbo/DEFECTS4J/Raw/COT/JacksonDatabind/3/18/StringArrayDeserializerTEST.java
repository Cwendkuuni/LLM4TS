
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test deserialization of empty array
        String[] result1 = deserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(0, result1.length);
        
        // Test deserialization of array with 1 element
        JsonParser jsonParser = new JsonParser();
        jsonParser.setExpectedStartArrayToken();
        jsonParser.nextToken();
        jsonParser.setText("value1");
        jsonParser.nextToken();
        jsonParser.markAsEndArray();
        String[] result2 = deserializer.deserialize(jsonParser, new DeserializationContext());
        assertEquals(1, result2.length);
        assertEquals("value1", result2[0]);
        
        // Test deserialization of array with multiple elements
        JsonParser jsonParser2 = new JsonParser();
        jsonParser2.setExpectedStartArrayToken();
        jsonParser2.nextToken();
        jsonParser2.setText("value1");
        jsonParser2.nextToken();
        jsonParser2.setText("value2");
        jsonParser2.nextToken();
        jsonParser2.markAsEndArray();
        String[] result3 = deserializer.deserialize(jsonParser2, new DeserializationContext());
        assertEquals(2, result3.length);
        assertEquals("value1", result3[0]);
        assertEquals("value2", result3[1]);
    }
    
    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test deserialization with type
        JsonParser jsonParser = new JsonParser();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(), typeDeserializer);
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test handleNonArray with ACCEPT_SINGLE_VALUE_AS_ARRAY disabled
        JsonParser jsonParser1 = new JsonParser();
        jsonParser1.setExpectedStartArrayToken();
        try {
            deserializer.handleNonArray(jsonParser1, new DeserializationContext());
            fail("Expected MappingException to be thrown");
        } catch (MappingException e) {
            // Expected exception
        }
        
        // Test handleNonArray with ACCEPT_SINGLE_VALUE_AS_ARRAY enabled
        DeserializationContext ctxt = new DeserializationContext();
        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonParser jsonParser2 = new JsonParser();
        jsonParser2.setExpectedStartArrayToken();
        jsonParser2.nextToken();
        jsonParser2.setText("value1");
        jsonParser2.nextToken();
        jsonParser2.markAsEndArray();
        String[] result = deserializer.handleNonArray(jsonParser2, ctxt);
        assertEquals(1, result.length);
        assertEquals("value1", result[0]);
    }
    
    @Test
    public void testCreateContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test createContextual with context, property and element deserializer
        DeserializationContext ctxt = new DeserializationContext();
        BeanProperty property = new BeanProperty();
        JsonDeserializer<String> elementDeserializer = new JsonDeserializer<String>();
        JsonDeserializer<?> result1 = deserializer.createContextual(ctxt, property, elementDeserializer);
        assertTrue(result1 instanceof StringArrayDeserializer);
        assertNotEquals(deserializer, result1);
        assertEquals(elementDeserializer, ((StringArrayDeserializer) result1)._elementDeserializer);
        
        // Test createContextual with context, property and null element deserializer
        JsonDeserializer<?> result2 = deserializer.createContextual(ctxt, property, null);
        assertTrue(result2 instanceof StringArrayDeserializer);
        assertNotEquals(deserializer, result2);
        assertNull(((StringArrayDeserializer) result2)._elementDeserializer);
        
        // Test createContextual with context, property and default element deserializer
        JsonDeserializer<?> result3 = deserializer.createContextual(ctxt, property, StringDeserializer.instance);
        assertTrue(result3 instanceof StringArrayDeserializer);
        assertEquals(deserializer, result3);
    }

}
