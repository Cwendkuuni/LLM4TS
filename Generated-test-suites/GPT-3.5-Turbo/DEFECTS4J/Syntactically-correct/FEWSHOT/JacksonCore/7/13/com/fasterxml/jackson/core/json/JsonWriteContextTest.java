package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        JsonWriteContext context = JsonWriteContext.createRootContext();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("age"));
        assertEquals("age", context.getCurrentName());

        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals(0, context.getCurrentIndex());

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals(1, context.getCurrentIndex());

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
        assertEquals(2, context.getCurrentIndex());
    }
}