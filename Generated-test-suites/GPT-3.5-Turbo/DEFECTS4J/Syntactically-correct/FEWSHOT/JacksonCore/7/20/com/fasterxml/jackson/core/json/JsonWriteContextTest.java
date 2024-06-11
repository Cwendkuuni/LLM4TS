package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name1"));
        assertEquals("name1", context.getCurrentName());
    
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("name2"));
        assertEquals("name2", context.getCurrentName());
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        context = context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        
        context = context.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        
        context = context.getParent();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context = context.createChildArrayContext();
        
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(0, context.getCurrentIndex());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context = context.createChildObjectContext();
        
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(0, context.getCurrentIndex());
    }
}