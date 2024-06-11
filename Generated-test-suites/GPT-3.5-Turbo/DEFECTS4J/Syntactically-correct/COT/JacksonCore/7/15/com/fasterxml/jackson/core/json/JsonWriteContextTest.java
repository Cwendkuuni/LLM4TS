
package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertEquals(0, context.writeFieldName("name"));
        Assert.assertEquals(1, context.writeFieldName("age"));
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldNameDuplicate() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.withDupDetector(new DupDetector());
        context.writeFieldName("name");
        context.writeFieldName("name");
    }

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        Assert.assertEquals(parentContext, childContext.getParent());
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        Assert.assertEquals(parentContext, childContext.getParent());
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
    }
}
