package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

/**
 * Test class for JsonWriteContext.
 */
public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(dupDetector, context.getDupDetector());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(0, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, childContext.getParent());
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getDupDetector());
        assertEquals(TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(0, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getDupDetector());
        assertEquals(TYPE_OBJECT, childContext.getType());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithGotName() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        context.writeFieldName("name");
        context.writeFieldName("value");
        context.writeValue();
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        context.writeFieldName("name");
        assertTrue(context.getGotName());
        assertNull(context.getCurrentValue());
        assertEquals(STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueAfterWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        context.writeFieldName("name");
        context.writeValue();
    }

    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        assertEquals(STATUS_OK_AFTER_SPACE, context.writeValue());
    }

    @Test
    public void testWriteValueInArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(TYPE_ARRAY, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(STATUS_OK_AFTER_COMMA, childContext.writeValue());
    }

    @Test
    public void testWriteValueInObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(STATUS_OK_AFTER_COLON, childContext.writeValue());
    }

    @Test
    public void testWriteValueWithCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(0, null, null);
        context.setCurrentValue("value");
        assertEquals(STATUS_OK_AFTER_SPACE, context.writeValue());
        assertEquals("value", context.getCurrentValue());
    }
}