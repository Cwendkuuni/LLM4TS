package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {
    
    private DupDetector dupDetector;

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, dupDetector);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
        
        JsonWriteContext contextWithDup = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, new DupDetector(null));
        Mockito.when(contextWithDup.isDup("name")).thenReturn(true);
        try {
            contextWithDup.writeFieldName("name");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
        
        context = new JsonWriteContext(JsonStreamContext.TYPE_ARRAY, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
        
        context = new JsonWriteContext(JsonStreamContext.TYPE_ROOT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
    }
}