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
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertNull(rootContext.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector duplicates = new DupDetector();
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(duplicates);
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertNull(rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertEquals(duplicates, rootContext.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childArrayContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(parentContext, childArrayContext.getParent());
        assertNull(childArrayContext.getCurrentName());
        assertNull(childArrayContext.getCurrentValue());
        assertNull(childArrayContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childObjectContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(parentContext, childObjectContext.getParent());
        assertNull(childObjectContext.getCurrentName());
        assertNull(childObjectContext.getCurrentValue());
        assertNull(childObjectContext.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDuplicate() throws JsonProcessingException {
        DupDetector duplicates = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(duplicates);
        context.writeFieldName("field");
        context.writeFieldName("field");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getType());
    }

    @Test
    public void testWriteValueInArray() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getType());
    }

    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getType());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("field");
        context.writeValue();
        String expected = "{field}";
        assertEquals(expected, context.toString());
    }

    @Test
    public void testToStringArray() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        context.writeValue();
        context.writeValue();
        String expected = "[0,1]";
        assertEquals(expected, context.toString());
    }

    @Test
    public void testToStringRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.writeValue();
        context.writeValue();
        String expected = "/";
        assertEquals(expected, context.toString());
    }
}