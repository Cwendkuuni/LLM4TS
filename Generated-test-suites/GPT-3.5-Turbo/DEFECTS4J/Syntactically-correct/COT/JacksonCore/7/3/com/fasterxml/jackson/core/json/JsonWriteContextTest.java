
package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldNameExpectValue() throws JsonGenerationException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeFieldName("fieldName");
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals("fieldName", context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameOkAsIs() throws JsonGenerationException {
        JsonWriteContext context = JsonWriteContext.createChildObjectContext();
        int result = context.writeFieldName("fieldName");
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("fieldName", context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameOkAfterComma() throws JsonGenerationException {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        
        parentContext.writeFieldName("fieldName1");
        int result = childContext.writeFieldName("fieldName2");
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals("fieldName2", childContext.getCurrentName());
    }

    @Test
    public void testWriteFieldNameOkAfterColon() throws JsonGenerationException {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        
        parentContext.writeFieldName("fieldName1");
        int result = childContext.writeFieldName("fieldName2");
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals("fieldName2", childContext.getCurrentName());
    }

    @Test
    public void testWriteFieldNameOkAfterSpace() throws JsonGenerationException {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        
        int result = childContext.writeFieldName("fieldName");
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        assertEquals("fieldName", childContext.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childObjectContext = parentContext.createChildObjectContext();
        JsonWriteContext childArrayContext = parentContext.createChildArrayContext();
        
        int objectResult = childObjectContext.writeValue();
        int arrayResult = childArrayContext.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectResult);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayResult);
    }
}
