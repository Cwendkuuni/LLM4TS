package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("fieldName"));
        assertEquals("fieldName", context.getCurrentName());
        
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        assertNull(context.getCurrentName());
    }

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());

        context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }
}