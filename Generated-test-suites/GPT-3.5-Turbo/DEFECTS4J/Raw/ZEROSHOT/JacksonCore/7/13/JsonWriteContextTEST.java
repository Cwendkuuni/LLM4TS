package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        int result = context.writeFieldName("testField");
        assertEquals(0, result);
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        int result = context.writeValue();
        assertEquals(0, result);
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildArrayContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertSame(parentContext, context.getParent());
        assertNull(context.getCurrentName());
        assertNotNull(context.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildObjectContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertSame(parentContext, context.getParent());
        assertNull(context.getCurrentName());
        assertNotNull(context.getDupDetector());
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        assertNull(context.getCurrentValue());
        Object value = new Object();
        context.setCurrentValue(value);
        assertSame(value, context.getCurrentValue());
    }
}