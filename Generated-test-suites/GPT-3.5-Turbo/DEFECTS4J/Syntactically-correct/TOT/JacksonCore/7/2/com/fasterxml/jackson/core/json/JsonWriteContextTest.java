package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonWriteContextTest {
    
    // Test case for writeFieldName method
    @Test
    public void testWriteFieldName() {
        // Typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext(null);
        int status = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        
        // Edge case - context is of type OBJECT with a currentName of null
        JsonWriteContext context2 = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int status2 = context2.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status2);
        
        // Edge case - context is of type OBJECT with a currentName not null
        JsonWriteContext context3 = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context3._currentName = "name";
        int status3 = context3.writeFieldName("name2");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status3);
    }
    
    // Test case for writeValue method
    @Test
    public void testWriteValue() {
        // Typical use case - context is of type OBJECT
        JsonWriteContext context1 = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int status1 = context1.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status1);
        
        // Typical use case - context is of type ARRAY
        JsonWriteContext context2 = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        int status2 = context2.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status2);
        
        // Typical use case - context is of type ROOT
        JsonWriteContext context3 = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        int status3 = context3.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status3);
        
        // Edge case - context is of type ROOT with current index 0
        JsonWriteContext context4 = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context4._index = 0;
        int status4 = context4.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status4);
    }
    
    // Test case for createChildArrayContext method
    @Test
    public void testCreateChildArrayContext() {
        // Typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext(null);
        JsonWriteContext childContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext._type);
        
        // Edge case - child context already exists as array
        context._child = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, context, null);
        JsonWriteContext childContext2 = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext2._type);
    }
    
    // Test case for createChildObjectContext method
    @Test
    public void testCreateChildObjectContext() {
        // Typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext(null);
        JsonWriteContext childContext = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext._type);
        
        // Edge case - child context already exists as object
        context._child = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, context, null);
        JsonWriteContext childContext2 = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext2._type);
    }
    
    // Test case for getParent method
    @Test
    public void testGetParent() {
        // Typical use case
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, parentContext, null);
        JsonWriteContext grandChildContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, childContext, null);
        
        JsonWriteContext parent = grandChildContext.getParent();
        
        assertEquals(childContext, parent);
    }
    
    // Test case for getCurrentName method
    @Test
    public void testGetCurrentName() {
        // Typical use case
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context._currentName = "name";
        
        String currentName = context.getCurrentName();
        
        assertEquals("name", currentName);
    }
}