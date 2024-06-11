
package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        ctxt.writeFieldName("name");
        assertEquals("name", ctxt.getCurrentName());
    }

    @Test
    public void testWriteFieldName_Duplicate() throws JsonProcessingException {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        ctxt.writeFieldName("name");
        try {
            ctxt.writeFieldName("name");
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue_ObjectContext() {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        ctxt.createChildObjectContext();
        int status = ctxt.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testWriteValue_ObjectContext_NoComma() {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        int status = ctxt.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteValue_ArrayContext() {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        ctxt.createChildArrayContext();
        int status = ctxt.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteValue_ArrayContext_NoComma() {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        int status = ctxt.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteValue_RootContext() {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        int status = ctxt.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
    }

    @Test
    public void testWriteValue_RootContext_WithSpace() {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        ctxt.writeValue();
        int status = ctxt.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
    }

}
