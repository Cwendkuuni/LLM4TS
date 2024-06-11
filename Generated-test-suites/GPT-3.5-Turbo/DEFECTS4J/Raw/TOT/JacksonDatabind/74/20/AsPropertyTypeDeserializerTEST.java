package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() {
        // Typical use case
        // When the current token is START_OBJECT and there is a type id property
        // The method should deserialize the object with the property value as the type id
        // and return the deserialized object
        String json = "{\"type\":\"test\",\"name\":\"John\"}";
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer();
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        Object result = deserializer.deserializeTypedFromObject(parser, context);
        assertNotNull(result);
        assertTrue(result instanceof TestObject);
        TestObject testObject = (TestObject) result;
        assertEquals("test", testObject.getType());
        assertEquals("John", testObject.getName());
        
        // Edge case
        // When the current token is not START_OBJECT or FIELD_NAME
        // The method should deserialize the object using the default implementation
        json = "\"test\"";
        parser = new JsonParser(json);
        result = deserializer.deserializeTypedFromObject(parser, context);
        assertNotNull(result);
        assertTrue(result instanceof DefaultObject);
        DefaultObject defaultObject = (DefaultObject) result;
        assertEquals("test", defaultObject.getValue());
        
        // Error scenario
        // When there is no type id property
        // The method should throw JsonMappingException
        json = "{\"name\":\"John\"}";
        parser = new JsonParser(json);
        try {
            deserializer.deserializeTypedFromObject(parser, context);
        } catch (JsonMappingException e) {
            assertEquals("missing property 'type' that is to contain type id  (for class TestObject)", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedForId() {
        // Typical use case
        // When the type id is visible and token buffer is null
        // The method should write the type id to a new token buffer
        // and return the deserialized object
        String json = "\"test\"";
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer();
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        TokenBuffer tb = null;
        Object result = deserializer.deserializeTypedForId(parser, context, tb);
        assertNotNull(result);
        assertTrue(result instanceof TestObject);
        TestObject testObject = (TestObject) result;
        assertEquals("test", testObject.getType());
        assertEquals("test", testObject.getValue());
        
        // Edge case
        // When the type id is not visible and token buffer is not null
        // The method should not modify the token buffer and return the deserialized object
        json = "\"test\"";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        tb = new TokenBuffer(parser, context);
        result = deserializer.deserializeTypedForId(parser, context, tb);
        assertNotNull(result);
        assertTrue(result instanceof TestObject);
        testObject = (TestObject) result;
        assertEquals("test", testObject.getType());
        assertEquals("test", testObject.getValue());
        
        // Error scenario
        // When the type id is not visible and token buffer is null
        // The method should throw JsonMappingException
        json = "\"test\"";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        try {
            deserializer.deserializeTypedForId(parser, context, tb);
        } catch (JsonMappingException e) {
            assertEquals("missing property 'type' that is to contain type id  (for class TestObject)", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        // Typical use case
        // When there is a default implementation deserializer available
        // The method should deserialize using the default implementation
        String json = "\"test\"";
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer();
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        TokenBuffer tb = null;
        Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, context, tb);
        assertNotNull(result);
        assertTrue(result instanceof DefaultObject);
        DefaultObject defaultObject = (DefaultObject) result;
        assertEquals("test", defaultObject.getValue());
        
        // Edge case
        // When the token is a natural value
        // The method should return the natural value
        json = "true";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        tb = null;
        result = deserializer.deserializeTypedUsingDefaultImpl(parser, context, tb);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);
        
        // Error scenario
        // When the token is neither START_ARRAY nor FIELD_NAME
        // The method should throw JsonMappingException
        json = "\"test\"";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        tb = null;
        try {
            deserializer.deserializeTypedUsingDefaultImpl(parser, context, tb);
        } catch (JsonMappingException e) {
            assertEquals("missing property 'type' that is to contain type id  (for class TestObject)", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromAny() {
        // Typical use case
        // When the token is START_ARRAY
        // The method should call super.deserializeTypedFromArray
        String json = "[{\"type\":\"test\",\"name\":\"John\"}]";
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer();
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        Object result = deserializer.deserializeTypedFromAny(parser, context);
        assertNotNull(result);
        assertTrue(result instanceof TestObject[]);
        TestObject[] testObjects = (TestObject[]) result;
        assertEquals(1, testObjects.length);
        assertEquals("test", testObjects[0].getType());
        assertEquals("John", testObjects[0].getName());
        
        // Typical use case
        // When the token is not START_ARRAY
        // The method should call deserializeTypedFromObject
        json = "{\"type\":\"test\",\"name\":\"John\"}";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        result = deserializer.deserializeTypedFromAny(parser, context);
        assertNotNull(result);
        assertTrue(result instanceof TestObject);
        TestObject testObject = (TestObject) result;
        assertEquals("test", testObject.getType());
        assertEquals("John", testObject.getName());
    }

}