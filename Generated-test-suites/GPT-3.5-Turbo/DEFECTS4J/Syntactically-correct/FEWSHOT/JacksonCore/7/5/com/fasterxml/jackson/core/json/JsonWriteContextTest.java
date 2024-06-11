package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {
    private JsonWriteContext context;
    private DupDetector dups;
    
    @Before
    public void setUp() {
        dups = Mockito.mock(DupDetector.class);
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
    }
    
    @Test
    public void testWithDupDetector() {
        JsonWriteContext result = context.withDupDetector(dups);
        
        assertEquals(dups, result.getDupDetector());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext result = context.createChildArrayContext();
        
        assertEquals(JsonWriteContext.TYPE_ARRAY, result._type);
        assertEquals(context, result.getParent());
        assertEquals(context._dups, result._dups);
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext result = context.createChildObjectContext();
        
        assertEquals(JsonWriteContext.TYPE_OBJECT, result._type);
        assertEquals(context, result.getParent());
        assertEquals(context._dups, result._dups);
    }
    
    @Test
    public void testWriteFieldNameWhenGotNameIsTrue() throws JsonProcessingException {
        context._gotName = true;
        
        int result = context.writeFieldName("name");
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals("name", context._currentName);
    }
    
    @Test
    public void testWriteFieldNameWhenDupDetectorIsNotNull() throws JsonProcessingException {
        Mockito.when(dups.isDup("name")).thenReturn(true);
        
        String name = "name";
        try {
            context.writeFieldName(name);
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
            Mockito.verify(dups).isDup(name);
            return;
        }
        
        assertTrue(false);
    }
    
    @Test
    public void testWriteFieldNameWhenDupDetectorIsNull() throws JsonProcessingException {
        context._dups = null;
        
        int result = context.writeFieldName("name");
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals("name", context._currentName);
        assertTrue(context._gotName);
    }
    
    @Test
    public void testWriteFieldNameWhenIndexIsNegative() throws JsonProcessingException {
        int result = context.writeFieldName("name");
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("name", context._currentName);
        assertTrue(context._gotName);
    }
    
    @Test
    public void testWriteFieldNameWhenIndexIsPositive() throws JsonProcessingException {
        context._index = 2;
        
        int result = context.writeFieldName("name");
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals("name", context._currentName);
        assertTrue(context._gotName);
    }
    
    @Test
    public void testWriteValueWhenTypeIsObject() {
        int result = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertFalse(context._gotName);
        assertEquals(0, context._index);
    }
    
    @Test
    public void testWriteValueWhenTypeIsArrayAndIndexIsZero() {
        int result = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertFalse(context._gotName);
        assertEquals(0, context._index);
    }
    
    @Test
    public void testWriteValueWhenTypeIsArrayAndIndexIsNotZero() {
        context._index = 1;
        
        int result = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertFalse(context._gotName);
        assertEquals(2, context._index);
    }
    
    @Test
    public void testWriteValueWhenTypeIsRootAndIndexIsZero() {
        int result = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertFalse(context._gotName);
        assertEquals(0, context._index);
    }
    
    @Test
    public void testWriteValueWhenTypeIsRootAndIndexIsNotZero() {
        context._index = 1;
        
        int result = context.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        assertFalse(context._gotName);
        assertEquals(2, context._index);
    }
    
    @Test
    public void testGetCurrentValue() {
        Object value = new Object();
        context._currentValue = value;
        
        Object result = context.getCurrentValue();
        
        assertEquals(value, result);
    }
    
    @Test
    public void testSetCurrentValue() {
        Object value = new Object();
        
        context.setCurrentValue(value);
        
        assertEquals(value, context._currentValue);
    }
    
    @Test
    public void testToStringWhenTypeIsObjectAndCurrentNameIsNull() {
        String result = context.toString();
        
        assertEquals("{\"?\"}", result);
    }
    
    @Test
    public void testToStringWhenTypeIsObjectAndCurrentNameIsNotNull() {
        context._currentName = "name";
        
        String result = context.toString();
        
        assertEquals("{\"name\"}", result);
    }
    
    @Test
    public void testToStringWhenTypeIsArray() {
        String result = context.toString();
        
        assertEquals("[0]", result);
    }
    
    @Test
    public void testToStringWhenTypeIsRoot() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, dups);
        
        String result = context.toString();
        
        assertEquals("/", result);
    }
    
    @Test
    public void testReset() {
        JsonWriteContext result = context.reset(JsonWriteContext.TYPE_ROOT);
        
        assertNull(result._currentName);
        assertFalse(result._gotName);
        assertNull(result._currentValue);
        assertNull(result.getDupDetector());
        assertEquals(JsonWriteContext.TYPE_ROOT, result._type);
    }
}