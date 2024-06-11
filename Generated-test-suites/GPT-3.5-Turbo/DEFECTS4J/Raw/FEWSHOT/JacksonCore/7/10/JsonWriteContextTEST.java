package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class JsonWriteContextTest {
    
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("field1"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("field2"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("field3"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        
        context = context.createChildObjectContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        context = context.createChildObjectContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        context = context.getParent();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        
        context = context.getParent();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        
        context = context.getParent();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }
    
    @Test
    public void testWriteValueWithRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }
    
    @Test
    public void testWriteValueWithObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildObjectContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }
    
    @Test
    public void testWriteValueWithArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildArrayContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }
    
    @Test
    public void testGetCurrentIndex() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildArrayContext();
        
        assertEquals(-1, context.getCurrentIndex());
        
        context.writeValue();
        assertEquals(0, context.getCurrentIndex());
        
        context.writeValue();
        assertEquals(1, context.getCurrentIndex());
    }
    
    @Test
    public void testToString() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        assertEquals("/", parentContext.toString());
        
        JsonWriteContext context = parentContext.createChildObjectContext();
        
        assertEquals("/{0}", context.toString());
        
        context = context.createChildObjectContext();
        
        assertEquals("/{0}/{0}", context.toString());
        
        context = context.getParent();
        
        assertEquals("/{0}/{0}/1", context.toString());
        
        context = context.getParent();
        
        assertEquals("/{0}/{0}/1/2", context.toString());
    }
    
    @Test
    public void testWithDupDetector() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        DupDetector dups = new DupDetector(null);
        
        parentContext.withDupDetector(dups);
        
        assertEquals(dups, parentContext.getDupDetector());
    }
    
    @Test
    public void testGetCurrentValueAndSetCurrentValue() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        assertNull(parentContext.getCurrentValue());
        
        parentContext.setCurrentValue("value");
        
        assertEquals("value", parentContext.getCurrentValue());
    }
    
    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        JsonWriteContext context = parentContext.createChildArrayContext();
        
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(parentContext, context.getParent());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        JsonWriteContext context = parentContext.createChildObjectContext();
        
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(parentContext, context.getParent());
    }
    
    @Test
    public void testReset() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        parentContext.writeFieldName("field1");
        parentContext.writeValue();
        
        JsonWriteContext context = parentContext.createChildArrayContext();
        context.writeFieldName("field2");
        context.writeValue();
        
        context = context.createChildObjectContext();
        context.writeFieldName("field3");
        context.writeValue();
        
        context.reset(JsonWriteContext.TYPE_ARRAY);
        
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(-1, context.getIndex());
        assertNull(context.getCurrentName());
        assertFalse(context.isGotName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());
    }
    
    @Test
    public void testAppendDesc() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        StringBuilder sb = new StringBuilder();
        parentContext.appendDesc(sb);
        assertEquals("/", sb.toString());
        
        JsonWriteContext context = parentContext.createChildObjectContext();
        
        sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/{0}", sb.toString());
        
        context = context.createChildObjectContext();
        
        sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/{0}/{0}", sb.toString());
        
        context = context.getParent();
        
        sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/{0}/{0}/1", sb.toString());
        
        context = context.getParent();
        
        sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/{0}/{0}/1/2", sb.toString());
    }
    
    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        
        assertEquals(parentContext, childContext.getParent());
    }
    
    @Test
    public void testGetCurrentName() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        assertNull(parentContext.getCurrentName());
        
        parentContext.writeFieldName("name");
        
        assertEquals("name", parentContext.getCurrentName());
    }
    
    @Test
    public void testGetDupDetector() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        assertNull(parentContext.getDupDetector());
        
        parentContext.withDupDetector(new DupDetector(null));
        
        assertNotNull(parentContext.getDupDetector());
    }
    
    @Test
    public void testGetIndex() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        assertEquals(-1, parentContext.getIndex());
        
        parentContext.writeValue();
        
        assertEquals(0, parentContext.getIndex());
    }
    
    @Test
    public void testIsGotName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertFalse(context.isGotName());
        
        context.writeFieldName("name");
        
        assertTrue(context.isGotName());
    }
}