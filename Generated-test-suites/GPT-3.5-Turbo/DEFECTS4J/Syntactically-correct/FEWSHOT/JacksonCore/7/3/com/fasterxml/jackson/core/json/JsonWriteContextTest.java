package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() {
        // Test case 1: field name is null
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName(null));

        // Test case 2: field name is empty string
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName(""));

        // Test case 3: field name is non-null and non-empty
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        // Test case 1: in object context
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        context.writeFieldName("age");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());

        // Test case 2: in array context
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());

        // Test case 3: in root context
        context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext child = parent.createChildArrayContext();

        assertNotNull(child);
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertEquals(parent, child.getParent());
        assertNull(child.getCurrentName());
        assertFalse(child.getDupDetector().isDup(""));
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext child = parent.createChildObjectContext();

        assertNotNull(child);
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertEquals(parent, child.getParent());
        assertNull(child.getCurrentName());
        assertFalse(child.getDupDetector().isDup(""));
    }
}