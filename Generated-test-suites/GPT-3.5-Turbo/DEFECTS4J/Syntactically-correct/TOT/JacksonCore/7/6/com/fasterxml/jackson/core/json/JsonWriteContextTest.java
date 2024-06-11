package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;

public class JsonWriteContextTest {

    private JsonWriteContext rootContext;
    private JsonWriteContext arrayContext;
    private JsonWriteContext objectContext;
    
    @Before
    public void setUp() {
        rootContext = JsonWriteContext.createRootContext();
        arrayContext = rootContext.createChildArrayContext();
        objectContext = rootContext.createChildObjectContext();
    }
    
    // Test for constructor
    
    @Test
    public void testJsonWriteContextConstructor() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_ARRAY, rootContext,
                new DupDetector());
        
        assertEquals(JsonStreamContext.TYPE_ARRAY, context.getType());
        assertEquals(rootContext, context.getParent());
        assertNull(context.getCurrentName());
        assertEquals(false, context.getDupDetector().isDup("test"));
    }
    
    // Test for reset method
    
    @Test
    public void testJsonWriteContextReset() {
        JsonWriteContext context = arrayContext.reset(JsonStreamContext.TYPE_OBJECT);
        
        assertEquals(JsonStreamContext.TYPE_OBJECT, context.getType());
        assertEquals(-1, context.getCurrentIndex());
        assertNull(context.getCurrentName());
        assertEquals(false, context.getCurrentValue());
    }
    
    // Test for withDupDetector method
    
    @Test
    public void testJsonWriteContextWithDupDetector() {
        DupDetector dup = new DupDetector();
        JsonWriteContext context = rootContext.withDupDetector(dup);
        
        assertEquals(dup, context.getDupDetector());
    }
    
    // Test for getCurrentValue method
    
    @Test
    public void testJsonWriteContextGetCurrentValue() {
        assertNull(rootContext.getCurrentValue());
    }
    
    // Test for setCurrentValue method
    
    @Test
    public void testJsonWriteContextSetCurrentValue() {
        String value = "test";
        objectContext.setCurrentValue(value);
        
        assertEquals(value, objectContext.getCurrentValue());
    }
    
    // Test for createRootContext method
    
    @Test
    public void testJsonWriteContextCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonStreamContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
    }
    
    // Test for createChildArrayContext method
    
    @Test
    public void testJsonWriteContextCreateChildArrayContext() {
        JsonWriteContext context = rootContext.createChildArrayContext();
        
        assertEquals(JsonStreamContext.TYPE_ARRAY, context.getType());
        assertEquals(rootContext, context.getParent());
        assertNull(context.getCurrentName());
        assertEquals(-1, context.getCurrentIndex());
    }
    
    // Test for createChildObjectContext method
    
    @Test
    public void testJsonWriteContextCreateChildObjectContext() {
        JsonWriteContext context = rootContext.createChildObjectContext();
        
        assertEquals(JsonStreamContext.TYPE_OBJECT, context.getType());
        assertEquals(rootContext, context.getParent());
        assertNull(context.getCurrentName());
        assertEquals(-1, context.getCurrentIndex());
    }
    
    // Test for writeFieldName method
    
    @Test
    public void testJsonWriteContextWriteFieldName() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, objectContext.writeFieldName("name"));
    }
    
    @Test
    public void testJsonWriteContextWriteFieldNameWithDuplicate() {
        objectContext.withDupDetector(new DupDetector());
        try {
            objectContext.writeFieldName("name");
            objectContext.writeFieldName("name");
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }
    
    // Test for writeValue method
    
    @Test
    public void testJsonWriteContextWriteValue() {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, objectContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, arrayContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.writeValue());
    }
    
    // Test for getParent method
    
    @Test
    public void testJsonWriteContextGetParent() {
        assertEquals(rootContext, objectContext.getParent());
    }
    
    // Test for getCurrentName method
    
    @Test
    public void testJsonWriteContextGetCurrentName() {
        assertNull(rootContext.getCurrentName());
    }
    
    // Test for getDupDetector method
    
    @Test
    public void testJsonWriteContextGetDupDetector() {
        assertNull(rootContext.getDupDetector());
    }
    
    // Test for appendDesc method
    
    @Test
    public void testJsonWriteContextAppendDescArray() {
        StringBuilder sb = new StringBuilder();
        arrayContext.appendDesc(sb);
        
        assertEquals("[0]", sb.toString());
    }
    
    @Test
    public void testJsonWriteContextAppendDescObject() {
        StringBuilder sb = new StringBuilder();
        objectContext.appendDesc(sb);
        
        assertEquals("{\"?\"}", sb.toString());
    }
    
    @Test
    public void testJsonWriteContextAppendDescRoot() {
        StringBuilder sb = new StringBuilder();
        rootContext.appendDesc(sb);
        
        assertEquals("/", sb.toString());
    }
    
    // Test for toString method
    
    @Test
    public void testJsonWriteContextToString() {
        assertEquals("/0", arrayContext.toString());
    }
}