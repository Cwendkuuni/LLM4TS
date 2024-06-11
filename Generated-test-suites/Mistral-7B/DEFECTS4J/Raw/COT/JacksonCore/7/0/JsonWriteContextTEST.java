package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

/**
 * JUnit 4 test file for JsonWriteContext class.
 */
public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(dupDetector, context.getDupDetector());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithoutGotName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentValue("value");
        context.writeFieldName("name");
        assertEquals("name", context.getCurrentName());
        assertEquals("value", context.getCurrentValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
        String name = "dupName";
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName(name);
        context.writeFieldName(name);
        fail("Expected JsonGenerationException for duplicate field");
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, childContext.getParent());
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getDupDetector());
        assertNull(childContext.getCurrentValue());
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getDupDetector());
        assertNull(childContext.getCurrentValue());
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
    }

    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testWriteValueInArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        childContext.setCurrentValue("value");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.writeValue());
        assertEquals("value", parentContext.getCurrentValue());
    }

    @Test
    public void testWriteValueInObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        childContext.writeFieldName("name");
        childContext.setCurrentValue("value");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childContext.writeValue());
        assertEquals("{\"name\":\"value\"}", parentContext.toString());
    }
}