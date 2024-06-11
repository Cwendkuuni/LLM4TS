package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case
        int result = context.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        
        // Edge case: empty field name
        result = context.writeFieldName("");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        
        // Error scenario: duplicate field name
        try {
            context.writeFieldName("name");
            Assert.fail("Expected JsonGenerationException to be thrown");
        } catch (JsonGenerationException e) {
            Assert.assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case: writing an object
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        
        // Typical use case: writing an array
        result = context.createChildArrayContext().writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        // Typical use case: writing root context
        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }
    
    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case: getting current value
        Object currentValue = context.getCurrentValue();
        Assert.assertNull(currentValue);
        
        // Setting current value
        String value = "Test";
        context.setCurrentValue(value);
        currentValue = context.getCurrentValue();
        Assert.assertEquals(value, currentValue);
    }
    
    @Test
    public void testCreateChildContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case: creating child array context
        JsonWriteContext childArrayContext = context.createChildArrayContext();
        Assert.assertNotNull(childArrayContext);
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        
        // Typical use case: creating child object context
        JsonWriteContext childObjectContext = context.createChildObjectContext();
        Assert.assertNotNull(childObjectContext);
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
    }
    
    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        // Typical use case: getting parent context
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        Assert.assertEquals(parentContext, childContext.getParent());
    }
    
    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case: getting current name
        String name = "name";
        context.writeFieldName(name);
        Assert.assertEquals(name, context.getCurrentName());
    }
    
    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case: getting duplication detector
        DupDetector dupDetector = new DupDetector();
        context.withDupDetector(dupDetector);
        Assert.assertEquals(dupDetector, context.getDupDetector());
    }

}