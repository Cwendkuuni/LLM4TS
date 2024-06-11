package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    private JsonWriteContext context;
    
    @Before
    public void setUp() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
    }
    
    @Test
    public void testReset() {
        context.reset(JsonWriteContext.TYPE_OBJECT);
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
    }
    
    @Test
    public void testWithValue() {
        context.setCurrentValue("test");
        assertEquals("test", context.getCurrentValue());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childContext = context.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(context, childContext.getParent());
        assertNull(childContext.getDupDetector());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childContext = context.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(context, childContext.getParent());
        assertNull(childContext.getDupDetector());
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("field"));
        assertTrue(context.hasName());
        assertEquals("field", context.getCurrentName());
    }
    
    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldNameWithDuplicate() throws JsonProcessingException {
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, new DupDetector(null));
        context.writeFieldName("field");
        context.writeFieldName("field");
    }
    
    @Test
    public void testWriteValueInObject() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
    }
    
    @Test
    public void testWriteValueInArray() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
    }
    
    @Test
    public void testWriteValueInRootContext() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
    }
    
    @Test
    public void testGetParent() {
        assertNull(context.getParent());
    }
    
    @Test
    public void testGetCurrentName() {
        assertNull(context.getCurrentName());
    }
    
    @Test
    public void testGetDupDetector() {
        assertNull(context.getDupDetector());
    }
    
    @Test
    public void testAppendDescForObject() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        StringBuilder sb = new StringBuilder(64);
        context.appendDesc(sb);
        assertEquals("{}", sb.toString());
    }
    
    @Test
    public void testAppendDescForArray() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        StringBuilder sb = new StringBuilder(64);
        context.appendDesc(sb);
        assertEquals("[0]", sb.toString());
    }
    
    @Test
    public void testAppendDescForRoot() {
        StringBuilder sb = new StringBuilder(64);
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
    }
    
    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder(64);
        context.appendDesc(sb);
        assertEquals(sb.toString(), context.toString());
    }
}