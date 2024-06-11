package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getParent());
        assertEquals("", context.toString());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return name.equals("test");
            }
        };

        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertNull(context.getParent());
        assertEquals("/", context.toString());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals("[", childContext.toString());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals("{\"", childContext.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithoutGotNameSet() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("test");
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("test");
        assertTrue(context.getGotName());
        assertEquals("test", context.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeValue());

        context.writeFieldName("test");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());

        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testWriteValueWithParent() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return name.equals("test");
            }
        };

        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        childContext.writeValue();
        childContext.writeValue();
        assertEquals(2, parentContext.getCurrentIndex());
    }
}