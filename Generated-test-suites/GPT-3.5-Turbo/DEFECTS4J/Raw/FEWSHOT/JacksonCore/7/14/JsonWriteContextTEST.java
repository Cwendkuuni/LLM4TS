package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    // Test constructor
    @Test
    public void testConstructor() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        assertNotNull(context);
    }
    
    // Test reset
    @Test
    public void testReset() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        context.reset(1);
        assertNotNull(context);
    }
    
    // Test createChildArrayContext
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(0, null, null);
        JsonWriteContext context = parent.createChildArrayContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(parent, context.getParent());
        assertEquals(-1, context.getIndex());
        assertNull(context.getCurrentName());
    }
    
    // Test createChildObjectContext
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(0, null, null);
        JsonWriteContext context = parent.createChildObjectContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(parent, context.getParent());
        assertEquals(-1, context.getIndex());
        assertNull(context.getCurrentName());
    }
    
    // Test writeFieldName when current name is null
    @Test
    public void testWriteFieldNameWhenCurrentNameIsNull() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeFieldName("test");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("test", context.getCurrentName());
    }
    
    // Test writeFieldName when current name is not null
    @Test
    public void testWriteFieldNameWhenCurrentNameIsNotNull() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentName("previous");
        int result = context.writeFieldName("test");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals("previous", context.getCurrentName());
    }
    
    // Test setCurrentValue
    @Test
    public void testSetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        context.setCurrentValue("test");
        assertEquals("test", context.getCurrentValue());
    }
    
    // Test toString for root context
    @Test
    public void testToStringForRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        String result = context.toString();
        assertEquals("/", result);
    }
    
    // Test toString for object context with current name
    @Test
    public void testToStringForObjectContextWithCurrentName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentName("test");
        String result = context.toString();
        assertEquals("{\"test\"}", result);
    }
    
    // Test toString for object context without current name
    @Test
    public void testToStringForObjectContextWithoutCurrentName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        String result = context.toString();
        assertEquals("{\"?\"}", result);
    }
    
    // Test toString for array context
    @Test
    public void testToStringForArrayContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        String result = context.toString();
        assertEquals("[0]", result);
    }
    
    // Test writeValue for object context
    @Test
    public void testWriteValueForObjectContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals(0, context.getIndex());
    }
    
    // Test writeValue for array context
    @Test
    public void testWriteValueForArrayContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(0, context.getIndex());
        
        context.setIndex(1);
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(2, context.getIndex());
    }
    
    // Test writeValue for root context
    @Test
    public void testWriteValueForRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        assertEquals(0, context.getIndex());
        
        context.setIndex(1);
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(2, context.getIndex());
    }
    
    // Test appendDesc for root context
    @Test
    public void testAppendDescForRootContext() {
        StringBuilder sb = new StringBuilder();
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
    }
    
    // Test appendDesc for object context with current name
    @Test
    public void testAppendDescForObjectContextWithCurrentName() {
        StringBuilder sb = new StringBuilder();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentName("test");
        context.appendDesc(sb);
        assertEquals("{\"test\"}", sb.toString());
    }
    
    // Test appendDesc for object context without current name
    @Test
    public void testAppendDescForObjectContextWithoutCurrentName() {
        StringBuilder sb = new StringBuilder();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.appendDesc(sb);
        assertEquals("{\"?\"}", sb.toString());
    }
    
    // Test appendDesc for array context
    @Test
    public void testAppendDescForArrayContext() {
        StringBuilder sb = new StringBuilder();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.setIndex(3);
        context.appendDesc(sb);
        assertEquals("[3]", sb.toString());
    }
    
    // Test getDupDetector
    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null);
        assertNull(context.getDupDetector());
    }
    
    // Test withDupDetector
    @Test
    public void testWithDupDetector() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null);
        DupDetector dd = new DupDetector(null);
        JsonWriteContext result = context.withDupDetector(dd);
        assertEquals(dd, result.getDupDetector());
    }
    
    // Test getType
    @Test
    public void testGetType() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
    }
    
    // Test getParent
    @Test
    public void testGetParent() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parent, null);
        assertEquals(parent, context.getParent());
    }
    
    // Test getCurrentName
    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentName("test");
        assertEquals("test", context.getCurrentName());
    }
    
    // Test getCurrentValue
    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentValue("test");
        assertEquals("test", context.getCurrentValue());
    }
}