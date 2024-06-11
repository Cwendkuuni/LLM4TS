package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteFieldName_duplicateField() {
        JsonWriteContext context = JsonWriteContext.createRootContext()
                .withDupDetector(new DupDetector());
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        try {
            context.writeFieldName("name");
            fail("JsonGenerationException expected");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue_objectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteValue_arrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValue_rootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }
}