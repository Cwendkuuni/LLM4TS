package com.example.junit4;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DupDetector;

public class JsonWriteContextTest {

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    @Test
    public void testWriteFieldName_GotNameIsTrue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name1");
        context.writeFieldName("name2");
        try {
            context.writeFieldName("name2");
            fail();
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'name2'", e.getMessage());
        }
    }

    @Test
    public void testWriteFieldName_NullName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        try {
            context.writeFieldName(null);
            fail();
        } catch (JsonProcessingException e) {
            assertEquals("Name cannot be null", e.getMessage());
        }
    }

    @Test
    public void testWriteFieldName_EmptyName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        try {
            context.writeFieldName("");
            fail();
        } catch (JsonProcessingException e) {
            assertEquals("Name cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testWriteFieldName_InvalidCharacters() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        try {
            context.writeFieldName("field\"name");
            fail();
        } catch (JsonProcessingException e) {
            assertEquals("Invalid name character '\"'", e.getMessage());
        }
    }

    @Test
    public void testWriteFieldName_NameCollision() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        DupDetector dupDetector = new DupDetector();
        context = context.withDupDetector(dupDetector);
        context.writeFieldName("name1");
        context.writeFieldName("name1");
        try {
            context.writeFieldName("name1");
            fail();
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'name1'", e.getMessage());
        }
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext result = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, result.getType());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext result = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, result.getType());
        assertEquals(dupDetector, result.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childArrayContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(context, childArrayContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childObjectContext = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(context, childObjectContext.getParent());
    }

    @Test
    public void testReset() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(null, context.getCurrentName());
        assertEquals(null, context.getCurrentValue());
    }

    @Test
    public void testWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext result = context.withDupDetector(dupDetector);
        assertEquals(JsonWriteContext.TYPE_OBJECT, result.getType());
        assertEquals(dupDetector, result.getDupDetector());
    }
}