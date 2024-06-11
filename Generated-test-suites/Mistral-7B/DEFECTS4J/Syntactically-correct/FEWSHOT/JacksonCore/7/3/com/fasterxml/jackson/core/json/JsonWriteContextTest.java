package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;

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
        assertNull(rootContext.getIndex());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertNull(rootContext.getParent());
        assertSame(dupDetector, rootContext.getDupDetector());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertFalse(rootContext.isGotName());
        assertNull(rootContext.getIndex());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null) {
            @Override
            public void writeFieldName(String name) throws JsonProcessingException {
                // Do nothing
            }
        };
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertSame(parentContext, arrayContext.getParent());
        assertNull(arrayContext.getDupDetector());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertFalse(arrayContext.isGotName());
        assertNull(arrayContext.getIndex());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null) {
            @Override
            public void writeFieldName(String name) throws JsonProcessingException {
                // Do nothing
            }
        };
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertSame(parentContext, objectContext.getParent());
        assertNull(objectContext.getDupDetector());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertFalse(objectContext.isGotName());
        assertNull(objectContext.getIndex());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameNullName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName(null);
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        assertEquals("name", context.getCurrentName());
        assertTrue(context.isGotName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertNull(context.getCurrentName());
        assertFalse(context.isGotName());
    }

    @Test
    public void testCheckDup() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("name");
        context.writeFieldName("name");
    }

    @Test(expected = JsonGenerationException.class)
    public void testCheckDupDuplicate() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        dupDetector.addDuplicate("name");
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("name");
    }
}