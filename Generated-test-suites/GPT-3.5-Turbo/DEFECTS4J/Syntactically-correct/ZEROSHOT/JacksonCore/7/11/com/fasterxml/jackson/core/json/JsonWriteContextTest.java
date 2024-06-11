package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
    }

    @Test
    public void testWriteFieldName_EmptyCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name1");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name2"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());

        context = JsonWriteContext.createRootContext();
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());

        context = JsonWriteContext.createRootContext();
        context.writeValue();
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());

        context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());

        context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());

        context = JsonWriteContext.createRootContext();
        context.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentValue());

        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildArrayContext();
        assertEquals(parent, context.getParent());
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

        DupDetector dups = new DupDetector(null);
        context = context.withDupDetector(dups);
        assertEquals(dups, context.getDupDetector());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals("/", context.toString());

        context.writeFieldName("name");
        assertEquals("/{\"name\"}", context.toString());

        context = context.createChildArrayContext();
        assertEquals("/[{\"name\"}]", context.toString());
    }
}