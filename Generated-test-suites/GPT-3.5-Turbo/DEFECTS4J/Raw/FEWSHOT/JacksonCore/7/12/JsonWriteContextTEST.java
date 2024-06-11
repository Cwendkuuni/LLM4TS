package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DupDetector;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWithDupDetector() {
        DupDetector detector = new DupDetector(null);
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.withDupDetector(detector);
        assertSame(detector, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parent.createChildArrayContext();
        assertSame(parent, context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parent.createChildObjectContext();
        assertSame(parent, context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertNull(context.getCurrentValue());
        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals("{}", context.toString());
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentName("name");
        assertEquals("{\"name\"}", context.toString());
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.setCurrentName("name");
        assertEquals("[0]", context.toString());
    }
}