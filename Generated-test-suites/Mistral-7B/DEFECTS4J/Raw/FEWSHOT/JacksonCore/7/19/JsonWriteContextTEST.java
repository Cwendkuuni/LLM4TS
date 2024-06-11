package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertNull(rootContext.getParent());
        assertEquals("", rootContext.toString());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(dupDetector, rootContext.getDupDetector());
    }

    @Test
    public void testReset() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertFalse(context.isGotName());
        assertEquals(-1, context.getIndex());
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, arrayContext.getParent());
        assertEquals("", arrayContext.toString());
        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, objectContext.getParent());
        assertEquals("", objectContext.toString());
        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithGotName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("fieldName");
        context.writeFieldName("fieldName2");
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("fieldName");
        assertTrue(context.isGotName());
        assertEquals("fieldName", context.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertFalse(context.isGotName());
        assertNull(context.getCurrentName());
    }

    @Test
    public void testWriteValueForObjectContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("fieldName");
        context.setCurrentValue("value");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals("value", context.getCurrentValue());
        assertNull(context.getCurrentName());
        assertFalse(context.isGotName());
    }

    @Test
    public void testWriteValueForArrayContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.writeValue();
        assertEquals(0, context.getIndex());
        assertEquals("[]", context.toString());

        context.writeValue();
        assertEquals(1, context.getIndex());
        assertEquals("[,]", context.toString());

        context.writeValue();
        assertEquals(2, context.getIndex());
        assertEquals("[, ,]", context.toString());

        context.writeValue();
        assertEquals(3, context.getIndex());
        assertEquals("[, , ,]", context.toString());

        context.writeValue();
        assertEquals(4, context.getIndex());
        assertEquals("[, , , ,]", context.toString());
    }

    @Test
    public void testWriteValueForRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.writeValue();
        assertEquals("/", context.toString());

        context.writeValue();
        assertEquals("/ /", context.toString());
    }

    @Test
    public void testCheckDup() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("fieldName");
        dupDetector.markDuplicate("fieldName");
        try {
            context.writeFieldName("fieldName");
            fail();
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }
}