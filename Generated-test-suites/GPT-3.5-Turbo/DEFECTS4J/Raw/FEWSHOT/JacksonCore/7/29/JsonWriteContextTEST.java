package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(0, context.writeFieldName("name"));
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        context = context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        
        context = context.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }
    
    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentValue());
        
        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }
    
    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentName());
        
        context.writeFieldName("name");
        assertEquals("name", context.getCurrentName());
    }
    
    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getDupDetector());
    }
    
}