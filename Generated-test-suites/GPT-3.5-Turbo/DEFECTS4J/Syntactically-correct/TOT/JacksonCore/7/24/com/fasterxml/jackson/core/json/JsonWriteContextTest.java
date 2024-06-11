package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        // Typical use case: writing a field name in an object context
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals("fieldName", context.getCurrentName());
        
        // Error scenario: trying to write a field name when a value is expected
        result = context.writeFieldName("fieldName2");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
    }

    @Test
    public void testWriteValue() {
        // Typical use case: writing a value in an object context
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        
        // Scenario: writing multiple values in an object context
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        // Typical use case: writing a value in an array context
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        
        // Scenario: writing multiple values in an array context
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        // Typical use case: writing the first value in the root context
        context = JsonWriteContext.createRootContext();
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        
        // Scenario: writing multiple values in the root context
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        
        // Typical use case: creating a child array context
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getEntryCount());
        
        // Error scenario: creating a child array context when a child array context already exists
        JsonWriteContext secondChildContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, secondChildContext.getType());
        assertEquals(2, parentContext.getEntryCount());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        
        // Typical use case: creating a child object context
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        
        // Error scenario: creating a child object context when a child object context already exists
        JsonWriteContext secondChildContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, secondChildContext.getType());
    }

    @Test
    public void testGetParent() {
        // Typical use case: getting the parent context
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testGetCurrentName() {
        // Typical use case: getting the current field name
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals("fieldName", context.getCurrentName());
        
        // Scenario: getting the current field name when there is no field name
        context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        // Typical use case: getting the duplicate detector
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertNull(context.getDupDetector());
        
        // Scenario: getting the duplicate detector when it is not null
        DupDetector dupDetector = new DupDetector(null);
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        assertEquals(dupDetector, context.getDupDetector());
    }

    @Test
    public void testReset() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case: resetting the context
        context.writeFieldName("fieldName");
        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.TYPE_ARRAY, resetContext.getType());
        assertNull(resetContext.getCurrentName());
        assertFalse(resetContext._gotName);
        
        // Error scenario: resetting the context when the duplicate detector is not null
        DupDetector dupDetector = new DupDetector(null);
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("fieldName");
        try {
            context.reset(JsonWriteContext.TYPE_ARRAY);
            fail("JsonProcessingException should be thrown");
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }

    @Test
    public void testGetCurrentValue() {
        // Typical use case: getting the current value
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentValue());
        
        // Scenario: setting and getting the current value
        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        // Typical use case: setting the current value
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }
    
    @Test
    public void testCreateRootContext() {
        // Typical use case: creating the root context without a duplicate detector
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        
        // Scenario: creating the root context with a duplicate detector
        DupDetector dupDetector = new DupDetector(null);
        context = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        
        // Typical use case: getting the string representation of the context
        context.writeFieldName("fieldName");
        assertEquals("{\"fieldName\"}", context.toString());
        
        // Scenario: getting the string representation of the context when the field name is null
        context = JsonWriteContext.createRootContext();
        assertEquals("/", context.toString());
    }
}