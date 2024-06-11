package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getCurrentName());
        assertNull(context.getParent());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(parent, context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(parent, context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("name", context.getCurrentName());
        status = context.writeFieldName("email");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals("email", context.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);

        context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);

        context = JsonWriteContext.createRootContext();
        context.createChildObjectContext();
        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentValue());
        Object value = new Object();
        context.setCurrentValue(value);
        assertEquals(value, context.getCurrentValue());
    }
    
}