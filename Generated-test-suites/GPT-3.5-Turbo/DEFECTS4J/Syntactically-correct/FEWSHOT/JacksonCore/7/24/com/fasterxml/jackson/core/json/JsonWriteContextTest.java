package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JsonWriteContextTest {
    
    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        JsonWriteContext context = parentContext.createChildArrayContext();
        
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(parentContext, context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        JsonWriteContext context = parentContext.createChildObjectContext();
        
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(parentContext, context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testWriteFieldNameWithDuplicate() {
        DupDetector dupDetector = mock(DupDetector.class);
        when(dupDetector.isDup("name")).thenReturn(true);
        
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        
        try {
            context.writeFieldName("name");
            fail("Expected JsonGenerationException from writeFieldName with duplicate name");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteValueInObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertFalse(context.hasCurrentName());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testWriteValueInArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(1, context.getCurrentIndex());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(1, context.getCurrentIndex());
        assertNull(context.getCurrentValue());
    }
    
    @Test
    public void testToString() {
        JsonWriteContext context1 = JsonWriteContext.createRootContext();
        assertEquals("/", context1.toString());
        
        JsonWriteContext context2 = context1.createChildArrayContext();
        assertEquals("[0]", context2.toString());
        
        JsonWriteContext context3 = context2.createChildObjectContext();
        context3.writeFieldName("name");
        assertEquals("[0]{\"name\"}", context3.toString());
    }
}