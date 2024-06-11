package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonStreamContext.TYPE_ROOT, context.getType());
        assertEquals(null, context.getParent());
        assertEquals(null, context.getCurrentName());
        assertEquals(null, context.getCurrentValue());
        assertEquals(null, context.getDupDetector());
        assertEquals(0, context.writeFieldName("name"));
        assertEquals(1, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildArrayContext();
        
        assertEquals(JsonStreamContext.TYPE_ARRAY, context.getType());
        assertEquals(parent, context.getParent());
        assertEquals(null, context.getCurrentName());
        assertEquals(null, context.getCurrentValue());
        assertEquals(null, context.getDupDetector());
        assertEquals(0, context.writeFieldName("name"));
        assertEquals(1, context.writeValue());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildObjectContext();
        
        assertEquals(JsonStreamContext.TYPE_OBJECT, context.getType());
        assertEquals(parent, context.getParent());
        assertEquals(null, context.getCurrentName());
        assertEquals(null, context.getCurrentValue());
        assertEquals(null, context.getDupDetector());
        assertEquals(0, context.writeFieldName("name"));
        assertEquals(1, context.writeValue());
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }
}