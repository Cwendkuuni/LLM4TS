package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldNameWithValidName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            int status = context.writeFieldName("testField");
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
            assertEquals("testField", context.getCurrentName());
        } catch (JsonProcessingException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWriteFieldNameWithDuplicateName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("duplicateField");
        try {
            context.writeFieldName("duplicateField");
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'duplicateField'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildObjectContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testWriteValueInArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
    }

}