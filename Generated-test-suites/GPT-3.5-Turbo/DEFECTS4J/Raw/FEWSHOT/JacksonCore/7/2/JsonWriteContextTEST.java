package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("name"));
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("age"));

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeFieldName("email"));
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();

        JsonWriteContext childContext = context.createChildArrayContext();

        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.writeValue());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();

        JsonWriteContext childContext = context.createChildObjectContext();

        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childContext.writeFieldName("name"));
    }
}