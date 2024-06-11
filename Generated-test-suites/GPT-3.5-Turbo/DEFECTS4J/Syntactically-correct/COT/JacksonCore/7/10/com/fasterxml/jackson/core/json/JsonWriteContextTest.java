
package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName_StatusOkAsIs() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteFieldName_StatusExpectValue() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("fieldName");
        int status = context.writeFieldName("fieldName2");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);
    }

    @Test
    public void testWriteValue_StatusOkAsIs() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteValue_StatusOkAfterSpace() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeValue();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
    }
    
    // Test edge cases and exception handling scenarios

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldName_DuplicateFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext().withDupDetector(new DupDetector());
        context.writeFieldName("fieldName");
        context.writeFieldName("fieldName");
    }

    @Test
    public void testWriteFieldName_NullName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName(null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteValue_RootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteValue_ArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteValue_ObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildObjectContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

}
