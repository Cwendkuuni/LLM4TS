package com.fasterxml.jackson.core.json;

import org.junit.Test;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertNull(context.getParent());
    }
    
    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dd = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dd);
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertNull(context.getParent());
        Assert.assertEquals(dd, context.getDupDetector());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildArrayContext();
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        Assert.assertEquals(parent, context.getParent());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildObjectContext();
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        Assert.assertEquals(parent, context.getParent());
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        Assert.assertTrue(context.entryCount() > 0);
    }
    
    @Test
    public void testWriteFieldNameWithDups() throws JsonProcessingException {
        DupDetector dd = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dd);
        int status = context.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        Assert.assertTrue(context.entryCount() > 0);
        
        try {
            context.writeFieldName("name");
            Assert.fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            // expected
        }
    }
    
    @Test
    public void testWriteValueForObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        int status = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        Assert.assertEquals(1, context.currentValueCount());
    }
    
    @Test
    public void testWriteValueForArray() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        Assert.assertEquals(1, context.entryCount());
    }
}