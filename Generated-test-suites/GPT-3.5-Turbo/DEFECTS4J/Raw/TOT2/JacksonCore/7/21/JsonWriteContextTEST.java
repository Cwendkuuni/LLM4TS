package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("field1"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("field2"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonStreamContext.TYPE_ARRAY, childContext.type);
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonStreamContext.TYPE_OBJECT, childContext.type);
    }

    @Test
    public void testReset() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("field1");
        context.reset(JsonStreamContext.TYPE_ARRAY);
        assertNull(context.currentName);
        assertFalse(context.gotName);
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }

}