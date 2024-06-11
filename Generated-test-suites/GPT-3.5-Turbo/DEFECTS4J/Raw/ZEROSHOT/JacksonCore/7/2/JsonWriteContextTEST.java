package com.fasterxml.jackson.core.json;

import org.junit.Test;
import org.junit.Assert;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeFieldName("fieldName");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        Assert.assertEquals("fieldName", context.getCurrentName());
        Assert.assertTrue(context._gotName);
    }

    @Test
    public void testWriteValue_ObjectType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        Assert.assertFalse(context._gotName);
        Assert.assertEquals(0, context.getCurrentIndex());
    }

    @Test
    public void testWriteValue_ArrayType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        Assert.assertEquals(0, context.getCurrentIndex());
    }

    @Test
    public void testWriteValue_RootType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        Assert.assertEquals(0, context.getCurrentIndex());
    }

    @Test
    public void testWriteFieldName_DuplicateName() {
        DupDetector dups = new DupDetector(null);
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        dups.child();
        try {
            context.writeFieldName("fieldName");
            context.writeFieldName("fieldName");
            Assert.fail("Expected JsonGenerationException was not thrown.");
        } catch (JsonGenerationException e) {
            Assert.assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertNull(context.getParent());
    }

    @Test
    public void testCreateRootContext_WithDupDetector() {
        DupDetector dups = new DupDetector(null);
        JsonWriteContext context = JsonWriteContext.createRootContext(dups);
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertNull(context.getParent());
        Assert.assertEquals(dups, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        Assert.assertEquals(parentContext, context.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildObjectContext();
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        Assert.assertEquals(parentContext, context.getParent());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        Assert.assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("fieldName");
        Assert.assertEquals("fieldName", context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        DupDetector dups = new DupDetector(null);
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        Assert.assertEquals(dups, context.getDupDetector());
    }

    @Test
    public void testReset_ObjectType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("fieldName");
        context._gotName = true;
        context._index = 1;
        context._currentValue = "testValue";
        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_OBJECT);
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, resetContext.getType());
        Assert.assertEquals(-1, resetContext.getCurrentIndex());
        Assert.assertNull(resetContext.getCurrentName());
        Assert.assertFalse(resetContext._gotName);
        Assert.assertNull(resetContext._currentValue);
        Assert.assertNull(resetContext.getDupDetector());      
    }

    @Test
    public void testReset_ArrayType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context._index = 1;
        context._currentValue = "testValue";
        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_ARRAY);
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, resetContext.getType());
        Assert.assertEquals(-1, resetContext.getCurrentIndex());
        Assert.assertNull(resetContext.getCurrentName());
        Assert.assertNull(resetContext._currentValue);
        Assert.assertNull(resetContext.getDupDetector());
    }

    @Test
    public void testReset_RootType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context._index = 1;
        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_ROOT);
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, resetContext.getType());
        Assert.assertEquals(-1, resetContext.getCurrentIndex());
        Assert.assertNull(resetContext.getCurrentName());
        Assert.assertNull(resetContext._currentValue);
        Assert.assertNull(resetContext.getDupDetector());
    }
}