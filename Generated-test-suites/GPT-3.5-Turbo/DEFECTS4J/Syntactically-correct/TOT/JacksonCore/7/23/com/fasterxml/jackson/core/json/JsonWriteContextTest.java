package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        result = context.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
    }

    @Test
    public void testWriteValue_objectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);

        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValue_arrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValue_rootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testWriteValue_rootContextWithSpace() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeValue();
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertNull(context.getCurrentValue());

        Object value = "test";
        context.setCurrentValue(value);
        Assert.assertEquals(value, context.getCurrentValue());
    }

    @Test
    public void testWithDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        DupDetector detector = new DupDetector(null);
        context.withDupDetector(detector);
        DupDetector returnedDetector = context.getDupDetector();
        Assert.assertEquals(detector, returnedDetector);
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildArrayContext();
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildObjectContext();
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildArrayContext();
        Assert.assertEquals(parentContext, context.getParent());
    }

    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertNull(context.getCurrentName());

        String name = "name";
        context.writeFieldName(name);
        Assert.assertEquals(name, context.getCurrentName());
    }

    @Test(expected = JsonGenerationException.class)
    public void test_checkDup() throws JsonGenerationException {
        DupDetector detector = new DupDetector(null);
        JsonWriteContext context = JsonWriteContext.createRootContext().withDupDetector(detector);
        String name = "name";
        context.writeFieldName(name);
        context._checkDup(detector, name);
    }
}