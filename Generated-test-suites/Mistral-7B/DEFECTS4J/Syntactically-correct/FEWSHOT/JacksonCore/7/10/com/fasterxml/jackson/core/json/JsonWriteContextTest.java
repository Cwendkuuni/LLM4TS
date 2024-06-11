package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {

    JsonWriteContext context;
    JsonWriteContext parentContext;
    DupDetector dupDetector;

    @Before
    public void setUp() {
        parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return name.equals("test");
            }
        };
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parentContext, dupDetector);
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertSame(dupDetector, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertSame(context, childContext.getParent());
        assertNull(childContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childContext = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertSame(context, childContext.getParent());
        assertNull(childContext.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        context.writeFieldName("test");
        assertTrue(_gotName);
        assertEquals("test", context.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDupName() throws JsonProcessingException {
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parentContext, dupDetector);
        context.writeFieldName("test");
        context.writeFieldName("test");
    }

    @Test
    public void testWriteValue() {
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
    }

    @Test
    public void testWriteValueForArray() {
        JsonWriteContext arrayContext = context.createChildArrayContext();
        arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.getStatus());
    }

    @Test
    public void testWriteValueForRootContext() {
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getStatus());
    }
}