
package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {
    
    private JsonWriteContext jsonWriteContext;
    private DupDetector dups;
    
    @Before
    public void setUp() {
        jsonWriteContext = new JsonWriteContext(1, null, null);
        dups = mock(DupDetector.class);
        jsonWriteContext.withDupDetector(dups);
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        String name = "fieldName";
        
        when(dups.isDup(name)).thenReturn(false);
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeFieldName(name));
    }
    
    @Test
    public void testWriteFieldNameWithDup() throws JsonProcessingException {
        String name = "fieldName";
        
        when(dups.isDup(name)).thenReturn(true);
        
        try {
            jsonWriteContext.writeFieldName(name);
        } catch(JsonGenerationException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteFieldNameAfterName() throws JsonProcessingException {
        String name = "fieldName";
        
        when(dups.isDup(name)).thenReturn(false);
        
        jsonWriteContext.writeFieldName(name);
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, jsonWriteContext.writeFieldName(name));
    }
    
    @Test
    public void testWriteValueForObject() throws JsonProcessingException {
        jsonWriteContext.reset(JsonWriteContext.TYPE_OBJECT);
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonWriteContext.writeValue());
    }
    
    @Test
    public void testWriteValueForArray() throws JsonProcessingException {
        jsonWriteContext.reset(JsonWriteContext.TYPE_ARRAY);
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeValue());
    }
    
    @Test
    public void testWriteValueForRoot() throws JsonProcessingException {
        jsonWriteContext.reset(JsonWriteContext.TYPE_ROOT);
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, jsonWriteContext.writeValue());
    }
    
    @Test
    public void testGetCurrentValue() {
        assertNull(jsonWriteContext.getCurrentValue());
    }
    
    @Test
    public void testSetCurrentValue() {
        Object value = new Object();
        jsonWriteContext.setCurrentValue(value);
        
        assertEquals(value, jsonWriteContext.getCurrentValue());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = jsonWriteContext.createChildArrayContext();
        
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.type());
        assertEquals(jsonWriteContext, childArrayContext.parent());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = jsonWriteContext.createChildObjectContext();
        
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.type());
        assertEquals(jsonWriteContext, childObjectContext.parent());
    }
    
    @Test
    public void testGetParent() {
        assertNull(jsonWriteContext.getParent());
    }
    
    @Test
    public void testGetCurrentName() {
        assertNull(jsonWriteContext.getCurrentName());
    }
    
    @Test
    public void testGetDupDetector() {
        assertEquals(dups, jsonWriteContext.getDupDetector());
    }
    
    @Test
    public void testAppendDescForObject() {
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);
        
        assertEquals("{\"?\"}", sb.toString());
    }
    
    @Test
    public void testAppendDescForArray() {
        jsonWriteContext.reset(JsonWriteContext.TYPE_ARRAY);
        jsonWriteContext.writeFieldName("fieldName");
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);
        
        assertEquals("[1]", sb.toString());
    }
    
    @Test
    public void testAppendDescForRoot() {
        jsonWriteContext.reset(JsonWriteContext.TYPE_ROOT);
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);
        
        assertEquals("/", sb.toString());
    }
    
    @Test
    public void testToString() {
        jsonWriteContext.reset(JsonWriteContext.TYPE_OBJECT);
        jsonWriteContext.writeFieldName("fieldName");
        
        assertEquals("{\"fieldName\"}", jsonWriteContext.toString());
    }
}
