package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext writeContext = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, writeContext.writeFieldName("name"));
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, writeContext.writeFieldName("name"));
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext writeContext = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, writeContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, writeContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, writeContext.writeValue());
    }
}