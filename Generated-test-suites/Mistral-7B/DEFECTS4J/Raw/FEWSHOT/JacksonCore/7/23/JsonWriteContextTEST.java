package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertNull(rootContext.getParent());
        assertNull(rootContext.getDupDetector());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertFalse(rootContext.isGotName());
        assertEquals(-1, rootContext.getIndex());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(parentContext, childContext.getParent());
        assertNull(childContext.getDupDetector());
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getCurrentValue());
        assertFalse(childContext.isGotName());
        assertEquals(-1, childContext.getIndex());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(parentContext, childContext.getParent());
        assertNull(childContext.getDupDetector());
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getCurrentValue());
        assertFalse(childContext.isGotName());
        assertEquals(-1, childContext.getIndex());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals("fieldName", context.getCurrentName());
        assertTrue(context.isGotName());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector() {
            @Override public boolean isDup(String name) {
                return "fieldName".equals(name);
            }
        };
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        context.writeFieldName("fieldName");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
    }

    @Test
    public void testWriteValueForObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        int status = childContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testWriteValueForArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        int status = childContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testToString() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals("/", context.toString());
    }

    @Test
    public void testToStringForObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        childContext.writeFieldName("fieldName");
        assertEquals("{\"fieldName\"}", childContext.toString());
    }

    @Test
    public void testToStringForArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        childContext.writeValue();
        assertEquals("[0]", childContext.toString());
    }
}