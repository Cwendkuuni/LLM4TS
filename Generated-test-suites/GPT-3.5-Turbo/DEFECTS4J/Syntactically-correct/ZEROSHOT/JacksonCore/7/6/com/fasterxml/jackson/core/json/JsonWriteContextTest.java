package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {
    
    private JsonWriteContext context;
    
    @Before
    public void setUp() {
        context = JsonWriteContext.createRootContext();
    }
    
    @Test
    public void testGetCurrentValue() {
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testSetCurrentValue() {
        String value = "test";
        context.setCurrentValue(value);
        assertEquals(value, context.getCurrentValue());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(context, childContext.getParent());
        assertNull(childContext.getCurrentName());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childContext = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(context, childContext.getParent());
        assertNull(childContext.getCurrentName());
    }
    
    @Test
    public void testGetType() {
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
    }
    
    @Test
    public void testGetParent() {
        assertNull(context.getParent());
    }
    
    @Test
    public void testGetCurrentIndex() {
        assertEquals(-1, context.getCurrentIndex());
    }
    
    @Test
    public void testGetDupDetector() {
        assertNull(context.getDupDetector());
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        String fieldName = "name";
        int status = context.writeFieldName(fieldName);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(fieldName, context.getCurrentName());
    }
    
    @Test(expected=JsonGenerationException.class)
    public void testWriteFieldName_DuplicateField() throws JsonProcessingException {
        String fieldName = "duplicateField";
        context.getDupDetector().addDup(fieldName);
        context.writeFieldName(fieldName);
    }
    
    @Test
    public void testWriteValue_ObjectContext() {
        context = context.createChildObjectContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertEquals(0, context.getCurrentIndex());
    }
    
    @Test
    public void testWriteValue_ArrayContext() {
        context = context.createChildArrayContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals(0, context.getCurrentIndex());
    }
    
    @Test
    public void testWriteValue_RootContext() {
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(0, context.getCurrentIndex());
    }
    
    @Test
    public void testAppendDesc_ObjectContext() {
        StringBuilder sb = new StringBuilder();
        context = context.createChildObjectContext();
        context.writeFieldName("fieldName");
        context.appendDesc(sb);
        assertEquals("{\"fieldName\"}", sb.toString());
    }
    
    @Test
    public void testAppendDesc_ArrayContext() {
        StringBuilder sb = new StringBuilder();
        context = context.createChildArrayContext();
        context.writeValue();
        context.appendDesc(sb);
        assertEquals("[0]", sb.toString());
    }
    
    @Test
    public void testAppendDesc_RootContext() {
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
    }
    
    @Test
    public void testToString_ObjectContext() {
        context = context.createChildObjectContext();
        context.writeFieldName("fieldName");
        assertEquals("{\"fieldName\"}", context.toString());
    }
    
    @Test
    public void testToString_ArrayContext() {
        context = context.createChildArrayContext();
        context.writeValue();
        assertEquals("[0]", context.toString());
    }
    
    @Test
    public void testToString_RootContext() {
        assertEquals("/", context.toString());
    }
}