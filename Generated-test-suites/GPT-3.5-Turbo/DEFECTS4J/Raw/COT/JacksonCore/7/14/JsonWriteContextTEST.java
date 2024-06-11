
package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("name"));
        Assert.assertEquals("name", context.getCurrentName());
        
        // Test for duplicate field name
        DupDetector dups = new DupDetector(null);
        context.withDupDetector(dups);
        try {
            context.writeFieldName("name");
            Assert.fail("Expected JsonGenerationException to be thrown");
        } catch (JsonGenerationException e) {
            Assert.assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertNull(context.getCurrentName());

        DupDetector dups = new DupDetector(null);
        context = JsonWriteContext.createRootContext(dups);
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertNull(context.getCurrentName());
        Assert.assertEquals(dups, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parent.createChildArrayContext();
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        Assert.assertNull(context.getCurrentName());
        
        // Test resetting the context to array type
        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_ARRAY);
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, resetContext.getType());
        Assert.assertEquals(parent, resetContext.getParent());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parent.createChildObjectContext();
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        Assert.assertNull(context.getCurrentName());
        
        // Test resetting the context to object type
        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_OBJECT);
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, resetContext.getType());
        Assert.assertEquals(parent, resetContext.getParent());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext objectContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());
        
        JsonWriteContext arrayContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
        
        JsonWriteContext rootContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, rootContext.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.writeValue());
    }

    @Test
    public void testGetCurrentValueAndSetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.setCurrentValue("testValue");
        Assert.assertEquals("testValue", context.getCurrentValue());
    }
}
