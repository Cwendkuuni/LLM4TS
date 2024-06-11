package com.fasterxml.jackson.core.json;

import org.junit.*;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testSetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }
    
    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertSame(dupDetector, context.getDupDetector());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        parent.setCurrentValue(new int[] {1});
        
        JsonWriteContext context = parent.createChildArrayContext();
        assertSame(parent, context.getParent());
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(0, context.getIndex());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        parent.setCurrentValue(new Object());
        
        JsonWriteContext context = parent.createChildObjectContext();
        assertSame(parent, context.getParent());
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(0, context.getIndex());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        String fieldName = "name";
        
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        
        int status = context.writeFieldName(fieldName);
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertTrue(context._gotName);
        assertEquals(fieldName, context._currentName);
    }
    
    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldNameWithDuplicateField() throws JsonProcessingException {
        String fieldName = "name";
        DupDetector dupDetector = new DupDetector();
        dupDetector.registerDup(fieldName);
        
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        
        context.writeFieldName(fieldName);
    }
    
    @Test
    public void testWriteValueForObject() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        
        int status = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertFalse(context._gotName);
        assertEquals(0, context._index);
    }
    
    @Test
    public void testWriteValueForArrayAtRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        
        int status = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(0, context._index);
    }
    
    @Test
    public void testWriteValueForArrayAtNonRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        
        int status = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals(0, context._index);
    }
    
    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        
        String result = context.toString();
        
        assertEquals("{?}", result);
    }
    
}