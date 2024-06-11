package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        int result = context.writeFieldName("name");
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("name", context.getCurrentName());
    }
    
    @Test
    public void testWriteFieldNameWhenExpectingValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        
        int result = context.writeFieldName("age");
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals("name", context.getCurrentName());
    }
    
    @Test
    public void testWriteValueInObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        
        int result = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals(null, context.getCurrentName());
    }
    
    @Test
    public void testWriteValueInArray() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeValue();
        
        int result = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(null, context.getCurrentName());
    }
    
    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        int result = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(null, context.getCurrentName());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        JsonWriteContext context = parentContext.createChildArrayContext();
        
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertSame(parentContext, context.getParent());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        JsonWriteContext context = parentContext.createChildObjectContext();
        
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertSame(parentContext, context.getParent());
    }
    
}