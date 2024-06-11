package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        context.findObjectId(123, null);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference ex) {
            String expectedMessage = "Unresolved forward references for: ";
            assertEquals(expectedMessage, ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        KeyDeserializer actual = context.keyDeserializerInstance(null, String.class);
        assertNull(actual);
    }
    
    @Test
    public void testCopy() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext.Impl actual = context.copy();
        assertNotEquals(context, actual);
    }
    
    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext.Impl actual = context.createInstance(null, null, null);
        assertNotEquals(context, actual);
    }
    
    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext.Impl actual = context.with(null);
        assertNotEquals(context, actual);
    }
}