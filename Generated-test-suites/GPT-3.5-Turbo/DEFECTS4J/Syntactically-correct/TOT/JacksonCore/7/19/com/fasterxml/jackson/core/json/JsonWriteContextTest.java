package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case
        int result = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        
        // Edge case: Empty field name
        result = context.writeFieldName("");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        
        // Error scenario: Duplicate field name
        context.writeFieldName("name");
        try {
            context.writeFieldName("name");
            fail("Expected JsonGenerationException not thrown");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case for object
        context.writeFieldName("name");
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        
        // Typical use case for array
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        
        // Typical use case for root context
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }
    
    @Test
    public void testAppendDesc() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case for object
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("{\"?\"}", sb.toString());
        
        // Typical use case for array
        context.createChildArrayContext();
        sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("[0]", sb.toString());
        
        // Typical use case for root context
        context.createRootContext();
        sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
    }
}