package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    @Test
    public void testReset() {
        DupDetector dups = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(-1, context.getCurrentIndex());
        assertNull(context.getCurrentName());
        assertFalse(context.isGotName());
        assertNull(context.getCurrentValue());
        assertEquals(dups, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        DupDetector dups = new DupDetector();
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        JsonWriteContext child = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertEquals(parent, child.getParent());
        assertEquals(dups.child(), child.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        DupDetector dups = new DupDetector();
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        JsonWriteContext child = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertEquals(parent, child.getParent());
        assertEquals(dups.child(), child.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        DupDetector dups = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        assertEquals("fieldName", context.getCurrentName());
        assertTrue(context.isGotName());
        assertEquals(-1, context.getCurrentIndex());
        assertNull(context.getCurrentValue());
        assertEquals(dups, context.getDupDetector());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldNameWithDuplicate() throws JsonProcessingException {
        DupDetector dups = new DupDetector();
        dups.findDup("duplicateField");
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        context.writeFieldName("duplicateField");
    }

    @Test
    public void testWriteValueForObject() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertFalse(context.isGotName());
        assertEquals(0, context.getCurrentIndex());
    }

    @Test
    public void testWriteValueForArray() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
    }

    @Test
    public void testWriteValueForRoot() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
    }
}