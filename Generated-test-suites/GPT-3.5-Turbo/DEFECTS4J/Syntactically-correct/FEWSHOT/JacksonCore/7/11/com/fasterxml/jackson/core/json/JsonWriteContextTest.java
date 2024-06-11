package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("field1"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("field2"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeFieldName("field3"));
        
        JsonWriteContext arrayContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeFieldName("value1"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeFieldName("value2"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeFieldName("value3"));
        
        JsonWriteContext objectContext = arrayContext.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, objectContext.writeFieldName("property1"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, objectContext.writeFieldName("property2"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, objectContext.writeFieldName("property3"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        JsonWriteContext arrayContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, arrayContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
        
        JsonWriteContext objectContext = arrayContext.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, objectContext.writeValue());
    }
}