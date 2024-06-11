package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext root = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, root.getType());
        assertNull(root.getParent());
        assertNull(root.getCurrentName());
        assertNull(root.getCurrentValue());
        assertNull(root.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext root = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, root.getType());
        assertNull(root.getParent());
        assertNull(root.getCurrentName());
        assertNull(root.getCurrentValue());
        assertEquals(dupDetector, root.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childArray = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArray.getType());
        assertEquals(parent, childArray.getParent());
        assertNull(childArray.getCurrentName());
        assertNull(childArray.getCurrentValue());
        assertNull(childArray.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childObject = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObject.getType());
        assertEquals(parent, childObject.getParent());
        assertNull(childObject.getCurrentName());
        assertNull(childObject.getCurrentValue());
        assertNull(childObject.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        context.writeFieldName("name1");
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(0, context.getCurrentIndex());
        assertEquals("name1", context.getCurrentName());
        assertTrue(context.isGotName());
        assertNull(context.getCurrentValue());
        assertEquals(dupDetector, context.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameDupDetectorThrows() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        context.writeFieldName("name1");
        context.writeFieldName("name1");
    }

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeValue();
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(0, context.getCurrentIndex());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWriteValueArray() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.writeValue();
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(0, context.getCurrentIndex());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWriteValueRootContext() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.writeValue();
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertEquals(0, context.getCurrentIndex());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());
    }
}