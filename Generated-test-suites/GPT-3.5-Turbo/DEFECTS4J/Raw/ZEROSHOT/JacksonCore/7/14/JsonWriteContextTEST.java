package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    private JsonWriteContext context;
    private DupDetector dups;
    
    @Before
    public void setUp() {
        context = JsonWriteContext.createRootContext();
        dups = null;
    }
    
    @Test
    public void testGetCurrentValue() {
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testSetCurrentValue() {
        Object value = new Object();
        context.setCurrentValue(value);
        assertEquals(value, context.getCurrentValue());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext child = context.createChildArrayContext();
        assertNotNull(child);
        assertEquals(child.getParent(), context);
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext child = context.createChildObjectContext();
        assertNotNull(child);
        assertEquals(child.getParent(), context);
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
    }
    
    @Test
    public void testGetParent() {
        assertNull(context.getParent());
        JsonWriteContext child = context.createChildArrayContext();
        assertEquals(context, child.getParent());
    }
    
    @Test
    public void testGetCurrentName() {
        assertNull(context.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
    }
    
    @Test
    public void testGetDupDetector() {
        assertNull(context.getDupDetector());
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("anotherName"));
        assertEquals("anotherName", context.getCurrentName());
    }
    
    @Test(expected=JsonProcessingException.class)
    public void testWriteFieldNameWithDuplicateName() throws JsonProcessingException {
        dups = DupDetector.rootDetector(null);
        context = JsonWriteContext.createRootContext(dups);
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
        context.writeFieldName("name");
    }
    
    @Test(expected=JsonProcessingException.class)
    public void testWriteFieldNameWithDuplicateNameInChildContext() throws JsonProcessingException {
        dups = DupDetector.rootDetector(null);
        context = JsonWriteContext.createRootContext(dups);
        JsonWriteContext child = context.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, child.writeValue());
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, child.writeFieldName("name"));
    }
    
    @Test
    public void testWriteValue() {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }
    
    @Test
    public void testToString() {
        String expected = "/";
        assertEquals(expected, context.toString());
        
        context.writeFieldName("name");
        expected = "{name}";
        assertEquals(expected, context.toString());
        
        JsonWriteContext child = context.createChildArrayContext();
        expected = "{name}[0]";
        assertEquals(expected, child.toString());
    }
}