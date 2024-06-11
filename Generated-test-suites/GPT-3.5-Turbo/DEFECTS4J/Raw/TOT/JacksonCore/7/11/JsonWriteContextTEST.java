package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {

    // Test case for writeFieldName() method
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeFieldName("field1");

        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        Assert.assertEquals("field1", context.getCurrentName());
    }

    // Test case for writeValue() method when type is TYPE_OBJECT
    @Test
    public void testWriteValueWithObjectType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeValue();

        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    // Test case for writeValue() method when type is TYPE_ARRAY and _index is -1
    @Test
    public void testWriteValueWithArrayTypeAndNegativeIndex() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        int result = context.writeValue();

        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    // Test case for writeValue() method when type is TYPE_ARRAY and _index is positive
    @Test
    public void testWriteValueWithArrayTypeAndPositiveIndex() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.setCurrentValue("value1");
        context.writeValue();

        int result = context.writeValue();

        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    // Test case for writeValue() method when type is neither TYPE_OBJECT nor TYPE_ARRAY
    @Test
    public void testWriteValueWithRootType() {
        JsonWriteContext context = new JsonWriteContext(-1, null, null);
        context.writeValue();

        int result = context.writeValue();

        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

    // Test case for createChildArrayContext() method
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = context.createChildArrayContext();

        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        Assert.assertEquals(context, childContext.getParent());
    }

    // Test case for createChildObjectContext() method
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = context.createChildObjectContext();

        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        Assert.assertEquals(context, childContext.getParent());
    }

    // Test case for getCurrentValue() and setCurrentValue() methods
    @Test
    public void testGetCurrentValueAndSetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentValue("value1");
        Object result = context.getCurrentValue();

        Assert.assertEquals("value1", result);
    }

    // Test case for reset() method
    @Test
    public void testReset() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentValue("value1");
        context.writeFieldName("field1");
        context.reset(JsonWriteContext.TYPE_ROOT);

        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertEquals(-1, context.getIndex());
        Assert.assertNull(context.getCurrentName());
        Assert.assertFalse(context.isGotName());
        Assert.assertNull(context.getCurrentValue());
    }

    // Test case for getDupDetector() method
    @Test
    public void testGetDupDetector() {
        DupDetector dupDetector = new DupDetector(null);
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        DupDetector result = context.getDupDetector();

        Assert.assertEquals(dupDetector, result);
    }

    // Test case for getParent() method
    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, parentContext, null);
        JsonWriteContext result = context.getParent();

        Assert.assertEquals(parentContext, result);
    }

    // Test case for getCurrentName() method
    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("field1");
        String result = context.getCurrentName();

        Assert.assertEquals("field1", result);
    }

    @Test
    public void testToStringWithObjectType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("field1");
        String result = context.toString();

        Assert.assertEquals("{\"field1\"}", result);
    }

    @Test
    public void testToStringWithArrayType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.writeValue();
        String result = context.toString();

        Assert.assertEquals("[0]", result);
    }

    @Test
    public void testToStringWithRootType() {
        JsonWriteContext context = new JsonWriteContext(-1, null, null);
        context.writeValue();
        String result = context.toString();

        Assert.assertEquals("/", result);
    }

}