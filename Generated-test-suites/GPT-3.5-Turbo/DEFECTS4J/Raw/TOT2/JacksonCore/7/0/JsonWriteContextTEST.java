package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("test"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testWriteValueInArray() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, childContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.writeValue());
    }

    @Test
    public void testWriteValueInObject() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childContext.writeValue());
    }

    @Test
    public void testAppendDescForObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("{}", sb.toString());
    }

    @Test
    public void testAppendDescForArray() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        StringBuilder sb = new StringBuilder();
        childContext.appendDesc(sb);
        assertEquals("[0]", sb.toString());
    }

    @Test
    public void testAppendDescForRoot() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
    }

}