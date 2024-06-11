package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonWriteContextTest {
    private JsonWriteContext context;

    @Before
    public void setup() {
        context = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());

        context = context.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());

        context = context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testGetParent() {
        assertNull(context.getParent());

        JsonWriteContext childContext = context.createChildArrayContext();
        assertEquals(context, childContext.getParent());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(context.getCurrentName());

        context.writeFieldName("name");
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        assertNull(context.getDupDetector());

        DuplicateDetector duplicateDetector = new DuplicateDetector();
        context = context.withDupDetector(duplicateDetector);
        assertEquals(duplicateDetector, context.getDupDetector());
    }

    private static class DuplicateDetector {
        public boolean isDup(String name) {
            return false;
        }

        public void reset() {

        }
    }
}