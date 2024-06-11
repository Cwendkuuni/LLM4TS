package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        
        // Test case 1: Starting token is not START_ARRAY
        // Test when current token is not START_ARRAY, method should throw an exception
        JsonParser p1 = new JsonParser();
        DeserializationContext ctxt1 = new DeserializationContext();
        Collection<String> result1 = new ArrayList<String>();
        try {
            deserializer.deserialize(p1, ctxt1, result1);
            fail("Exception not thrown for non-START_ARRAY token");
        } catch (IOException e) {
            // Expected exception
        }
        
        // Test case 2: Value deserializer is null
        // Test when value deserializer is null, method should call handleNonArray() method
        // and add the value to the result collection
        JsonParser p2 = new JsonParser();
        DeserializationContext ctxt2 = new DeserializationContext();
        Collection<String> result2 = new ArrayList<String>();
        
        String value2 = "testValue2";
        p2.setCurrentToken(JsonToken.VALUE_STRING);
        p2.setText(value2);
        
        try {
            deserializer.deserialize(p2, ctxt2, result2);
            assertEquals("Value not added to result", value2, result2.get(0));
        } catch (IOException e) {
            fail("Exception thrown for null value deserializer");
        }
        
        // Test case 3: Value deserializer is not null
        // Test when value deserializer is not null, method should call deserializeUsingCustom()
        // method and add the value to the result collection
        JsonParser p3 = new JsonParser();
        DeserializationContext ctxt3 = new DeserializationContext();
        Collection<String> result3 = new ArrayList<String>();
        
        String value3 = "testValue3";
        p3.setCurrentToken(JsonToken.VALUE_STRING);
        p3.setText(value3);
        
        JsonDeserializer<String> valueDes3 = new JsonDeserializer<String>() {
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
        };
        deserializer._valueDeserializer = valueDes3;
        
        try {
            deserializer.deserialize(p3, ctxt3, result3);
            assertEquals("Value not added to result", value3, result3.get(0));
        } catch (IOException e) {
            fail("Exception thrown for null value deserializer");
        }
        
    }

    @Test
    public void testHandleNonArray() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        
        // Test case 1: cannot wrap single value
        // Test when cannot wrap single value, method should throw an exception
        JsonParser p1 = new JsonParser();
        DeserializationContext ctxt1 = new DeserializationContext();
        Collection<String> result1 = new ArrayList<String>();
        
        try {
            deserializer.handleNonArray(p1, ctxt1, result1);
            fail("Exception not thrown for non-wrappable single value");
        } catch (IOException e) {
            // Expected exception
        }
        
        // Test case 2: null value
        // Test when value is null, it should be added to the result collection
        JsonParser p2 = new JsonParser();
        DeserializationContext ctxt2 = new DeserializationContext();
        Collection<String> result2 = new ArrayList<String>();
        
        String value2 = null;
        p2.setCurrentToken(JsonToken.VALUE_NULL);
        
        try {
            deserializer.handleNonArray(p2, ctxt2, result2);
            assertEquals("Null value not added to result", value2, result2.get(0));
        } catch (IOException e) {
            fail("Exception thrown for null value");
        }
        
        // Test case 3: value deserializer is null
        // Test when value deserializer is null, value should be parsed as string and added to result
        JsonParser p3 = new JsonParser();
        DeserializationContext ctxt3 = new DeserializationContext();
        Collection<String> result3 = new ArrayList<String>();
        
        String value3 = "testValue";
        p3.setCurrentToken(JsonToken.VALUE_STRING);
        p3.setText(value3);
        
        try {
            deserializer.handleNonArray(p3, ctxt3, result3);
            assertEquals("Value not added to result", value3, result3.get(0));
        } catch (IOException e) {
            fail("Exception thrown for null value deserializer");
        }
        
        // Test case 4: value deserializer is not null
        // Test when value deserializer is not null, value should be deserialized and added to result
        JsonParser p4 = new JsonParser();
        DeserializationContext ctxt4 = new DeserializationContext();
        Collection<String> result4 = new ArrayList<String>();
        
        String value4 = "testValue";
        p4.setCurrentToken(JsonToken.VALUE_STRING);
        p4.setText(value4);
        
        JsonDeserializer<String> valueDes4 = new JsonDeserializer<String>() {
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
        };
        deserializer._valueDeserializer = valueDes4;
        
        try {
            deserializer.handleNonArray(p4, ctxt4, result4);
            assertEquals("Value not added to result", value4, result4.get(0));
        } catch (IOException e) {
            fail("Exception thrown for null value deserializer");
        }
    }

    @Test
    public void testDeserializeUsingCustom() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Collection<String> result = new ArrayList<String>();
        
        String value = "testValue";
        p.setCurrentToken(JsonToken.VALUE_STRING);
        p.setText(value);
        
        JsonDeserializer<String> valueDes = new JsonDeserializer<String>() {
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
        };
        
        try {
            deserializer.deserializeUsingCustom(p, ctxt, result, valueDes);
            assertEquals("Value not added to result", value, result.get(0));
        } catch (IOException e) {
            fail("Exception thrown for deserializeUsingCustom()");
        }
    }

    @Test
    public void testDeserializeWithType() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        try {
            deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        } catch (IOException e) {
            fail("Exception thrown for deserializeWithType()");
        }
    }

}