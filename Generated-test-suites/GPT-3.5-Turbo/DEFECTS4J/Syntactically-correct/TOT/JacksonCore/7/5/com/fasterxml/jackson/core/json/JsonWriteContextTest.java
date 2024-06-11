package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        // Typical use case
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);

        // Edge case: writing empty field name
        status = context.writeFieldName("");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);

        // Error scenario: Duplicate field name
        try {
            status = context.writeFieldName("name");
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        // Typical use case in object context
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);

        // Typical use case in array context
        context.createChildArrayContext();
        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);

        // Typical use case in root context
        context = JsonWriteContext.createRootContext();
        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testGetCurrentName() {
        // Typical use case - getting current name
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        String name = context.getCurrentName();
        assertEquals("name", name);

        // Edge case - getting current name when the name is null
        context.reset(JsonWriteContext.TYPE_ARRAY);
        name = context.getCurrentName();
        assertNull(name);
    }

    @Test
    public void testGetDupDetector() {
        // Typical use case - getting dup detector
        JsonWriteContext context = JsonWriteContext.createRootContext();
        DupDetector dupDetector = context.getDupDetector();
        assertNull(dupDetector);

        // Edge case - getting dup detector after setting it
        dupDetector = new DupDetector(null);
        context.withDupDetector(dupDetector);
        DupDetector actualDupDetector = context.getDupDetector();
        assertEquals(dupDetector, actualDupDetector);
    }
}