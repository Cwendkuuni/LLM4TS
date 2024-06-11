package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        // Typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeFieldName("fieldName");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        // Edge case: empty field name
        context.reset(JsonWriteContext.TYPE_ARRAY);
        result = context.writeFieldName("");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        // Error scenario: Duplicate field name
        context = JsonWriteContext.createRootContext();
        try {
            context.writeFieldName("fieldName");
            context.writeFieldName("fieldName");
            Assert.fail("Expected JsonGenerationException was not thrown");
        } catch (JsonGenerationException e) {
            Assert.assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        // Typical use case: write value in object context
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("fieldName");
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);

        // Typical use case: write value in array context
        context.reset(JsonWriteContext.TYPE_ARRAY);
        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        // Typical use case: write value in root context
        context.reset(JsonWriteContext.TYPE_ROOT);
        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

}