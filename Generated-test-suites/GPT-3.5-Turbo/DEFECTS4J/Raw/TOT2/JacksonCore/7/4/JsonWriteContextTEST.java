package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("test"));
            assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("test"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext childArrayContext = parent.createChildArrayContext();
        assertNotNull(childArrayContext);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, childArrayContext.writeValue());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext childObjectContext = parent.createChildObjectContext();
        assertNotNull(childObjectContext);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, childObjectContext.writeValue());
    }

    @Test
    public void testReset() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("test");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeValue());
        context.reset(JsonWriteContext.TYPE_OBJECT);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals("/{?}", context.toString());
    }
}