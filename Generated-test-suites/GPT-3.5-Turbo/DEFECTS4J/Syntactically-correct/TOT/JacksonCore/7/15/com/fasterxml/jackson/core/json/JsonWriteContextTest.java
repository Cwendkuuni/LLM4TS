package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        // Test typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        
        // Test edge case where field name is null
        try {
            context.writeFieldName(null);
            Assert.fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            Assert.assertEquals("Duplicate field '"+name+"'", e.getMessage());
        }
        
        // Test error scenario where duplicate field name is given
        context.writeFieldName("name");
        try {
            context.writeFieldName("name");
            Assert.fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            Assert.assertEquals("Duplicate field '"+name+"'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        // Test typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        
        // Test edge case where value is written in object context without field name
        context.createChildObjectContext();
        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, result);
        
        // Test edge case where value is written in object context with field name
        context.writeFieldName("name");
        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        
        // Test edge case where value is written in array context without comma separator
        context = context.createChildArrayContext();
        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        
        // Test edge case where value is written in array context with comma separator
        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        
        // Test edge case where value is written in root context
        result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

    @Test
    public void testParent() {
        // Test typical use case
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildObjectContext();
        JsonWriteContext grandchild = child.createChildArrayContext();
        
        Assert.assertEquals(child, grandchild.getParent());
        Assert.assertEquals(parent, child.getParent());
        Assert.assertNull(parent.getParent());
    }

    @Test
    public void testGetCurrentName() {
        // Test typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        Assert.assertEquals("name", context.getCurrentName());
        
        // Test edge case where no field name is set
        Assert.assertNull(context.getCurrentName());
    }

    @Test
    public void testDupDetector() {
        // Test typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertNull(context.getDupDetector());
        
        // Test edge case where dup detector is set
        DupDetector dupDetector = new DupDetector();
        context = context.withDupDetector(dupDetector);
        Assert.assertEquals(dupDetector, context.getDupDetector());
    }

    @Test
    public void testGetCurrentValue() {
        // Test typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertNull(context.getCurrentValue());
        
        // Test edge case where current value is set
        Object value = new Object();
        context.setCurrentValue(value);
        Assert.assertEquals(value, context.getCurrentValue());
    }

    @Test
    public void testToString() {
        // Test typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildObjectContext();
        context.writeFieldName("name");
        
        Assert.assertEquals("/0/name", context.toString());
        
        // Test edge case where root context
        context = JsonWriteContext.createRootContext();
        Assert.assertEquals("/", context.toString());
    }

}