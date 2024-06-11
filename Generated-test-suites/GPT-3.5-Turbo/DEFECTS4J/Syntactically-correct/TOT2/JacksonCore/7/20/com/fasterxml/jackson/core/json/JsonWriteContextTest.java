package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new OptimizedDupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertEquals(dupDetector, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
    }

    @Test
    public void testWriteFieldNameWithDuplicate() {
        JsonWriteContext context = JsonWriteContext.createRootContext(new OptimizedDupDetector());
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name1"));
        assertThrows(JsonGenerationException.class, () -> context.writeFieldName("name1"));
    }

    @Test
    public void testWriteValueInObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteValueInArray() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }

    @Test
    public void testWriteFieldNameWithoutValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testSetCurrentFieldValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue(10);
        assertEquals(10, context.getCurrentValue());
    }
}