package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        // Typical use case
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey("objectId", null, null);
        ReadableObjectId objectId = context.findObjectId("objectId", null, null);
        assertNull(objectId);
        
        // Edge case: already seen object id
        objectId = context.findObjectId("objectId", null, null);
        assertNotNull(objectId);
        
        // Error scenario: null generator
        try {
            context.findObjectId("objectId", null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        
        // Typical use case: no unresolved forward references
        context.checkUnresolvedObjectId();
        
        // Error scenario: enabled FAIL_ON_UNRESOLVED_OBJECT_IDS
        context.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        context.checkUnresolvedObjectId();
        
        // Error scenario: unresolved forward references
        UnresolvedForwardReference exception = new UnresolvedForwardReference("Unresolved forward references for: ");
        exception.addUnresolvedId("id", Object.class, null);
        context._objectIds.put(new ObjectIdGenerator.IdKey("id", null, null), new ReadableObjectId(null));
        context.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            assertEquals(exception.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated ann = null;
        Object deserDef = null;
        
        // Typical use case: deserDef is JsonDeserializer
        JsonDeserializer<Object> expected = context.deserializerInstance(ann, new TestDeserializer());
        assertNotNull(expected);
        assertTrue(expected instanceof TestDeserializer);
        
        // Error scenario: deserDef is not JsonDeserializer or Class
        try {
            context.deserializerInstance(ann, new Object());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
        
        // Error scenario: deserDef is Class, but not JsonDeserializer
        try {
            context.deserializerInstance(ann, Object.class);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
        
        // Error scenario: deserDef is bogus class
        JsonDeserializer<Object> result = context.deserializerInstance(ann, BogusClass.class);
        assertNull(result);
        
        // Error scenario: deserDef is null
        result = context.deserializerInstance(ann, null);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated ann = null;
        Object deserDef = null;
        
        // Typical use case: deserDef is KeyDeserializer
        KeyDeserializer expected = context.keyDeserializerInstance(ann, new TestKeyDeserializer());
        assertNotNull(expected);
        assertTrue(expected instanceof TestKeyDeserializer);
        
        // Error scenario: deserDef is not KeyDeserializer or Class
        try {
            context.keyDeserializerInstance(ann, new Object());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
        
        // Error scenario: deserDef is Class, but not KeyDeserializer
        try {
            context.keyDeserializerInstance(ann, Object.class);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
        
        // Error scenario: deserDef is bogus class
        KeyDeserializer result = context.keyDeserializerInstance(ann, BogusClass.class);
        assertNull(result);
        
        // Error scenario: deserDef is null
        result = context.keyDeserializerInstance(ann, null);
        assertNull(result);
    }
    
    private static class TestDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }
    
    private static class TestKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }
    
    private static class BogusClass {}

}