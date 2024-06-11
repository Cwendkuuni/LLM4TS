package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        int status = context.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        Assert.assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameWithDuplicate() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, new DupDetector(null));
        try {
            context.writeFieldName("name");
            context.writeFieldName("name");
            Assert.fail("Expected JsonGenerationException");
        } catch (JsonProcessingException e) {
            Assert.assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObject() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        int status = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        Assert.assertFalse(context._gotName);
        Assert.assertEquals(0, context._index);
    }

    @Test
    public void testWriteValueInArray() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_ARRAY, null, null);
        int status = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        Assert.assertEquals(0, context._index);
    }

    @Test
    public void testWriteValueInRoot() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_ROOT, null, null);
        int status = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
        Assert.assertEquals(0, context._index);
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertEquals(JsonStreamContext.TYPE_ROOT, context.getType());
        Assert.assertNull(context.getParent());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildArrayContext();
        Assert.assertEquals(JsonStreamContext.TYPE_ARRAY, childContext.getType());
        Assert.assertEquals(context, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildObjectContext();
        Assert.assertEquals(JsonStreamContext.TYPE_OBJECT, childContext.getType());
        Assert.assertEquals(context, childContext.getParent());
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        Assert.assertNull(context.getCurrentValue());
        Object value = new Object();
        context.setCurrentValue(value);
        Assert.assertEquals(value, context.getCurrentValue());
    }

    @Test
    public void testReset() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        context._index = 1;
        context._currentName = "name";
        context._gotName = true;
        context._currentValue = new Object();
        context._dups = new DupDetector(null);

        context.reset(JsonStreamContext.TYPE_ROOT);

        Assert.assertEquals(JsonStreamContext.TYPE_ROOT, context.getType());
        Assert.assertEquals(-1, context._index);
        Assert.assertNull(context._currentName);
        Assert.assertFalse(context._gotName);
        Assert.assertNull(context._currentValue);
        Assert.assertNull(context._dups);
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        Assert.assertEquals("{?}", context.toString());

        context._currentName = "name";
        Assert.assertEquals("{\"name\"}", context.toString());

        context._type = JsonStreamContext.TYPE_ARRAY;
        context._index = 2;
        Assert.assertEquals("[2]", context.toString());

        context._type = JsonStreamContext.TYPE_ROOT;
        Assert.assertEquals("/", context.toString());
    }

}