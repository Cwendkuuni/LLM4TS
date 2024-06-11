package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName1"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("fieldName2"));
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }
    
    @Test
    public void testWithDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getDupDetector());
        context.withDupDetector(new DupDetector());
        assertNotNull(context.getDupDetector());
    }
}