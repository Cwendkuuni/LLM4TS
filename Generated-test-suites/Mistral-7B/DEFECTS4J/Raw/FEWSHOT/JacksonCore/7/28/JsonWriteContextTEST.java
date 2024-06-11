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
        assertFalse(rootContext.isIndexSet());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return false;
            }
        };
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertEquals(dupDetector, rootContext.getDupDetector());
        assertNull(rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertFalse(rootContext.isGotName());
        assertFalse(rootContext.isIndexSet());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(parentContext, arrayContext.getParent());
        assertNull(arrayContext.getDupDetector());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertFalse(arrayContext.isGotName());
        assertFalse(arrayContext.isIndexSet());
        assertEquals(-1, arrayContext.getCurrentIndex());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(parentContext, objectContext.getParent());
        assertNull(objectContext.getDupDetector());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertFalse(objectContext.isGotName());
        assertFalse(objectContext.isIndexSet());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithGotName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        context.writeFieldName("name");
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertTrue(context.isGotName());
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testWriteValueArrayContext() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
        assertEquals(0, arrayContext.getCurrentIndex());
        assertTrue(arrayContext.isIndexSet());
    }

    @Test
    public void testWriteValueObjectContext() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());
        assertTrue(objectContext.isIndexSet());
        assertEquals(0, objectContext.getCurrentIndex());
    }

    @Test
    public void testWriteValueRootContext() throws JsonProcessingException {
        JsonWriteContext rootContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.writeValue());
        assertFalse(rootContext.isIndexSet());
        assertEquals(-1, rootContext.getCurrentIndex());
    }
}