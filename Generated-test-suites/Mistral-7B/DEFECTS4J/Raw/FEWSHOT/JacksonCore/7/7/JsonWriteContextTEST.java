package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext ctx = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, ctx.getType());
        assertNull(ctx.getParent());
        assertNull(ctx.getCurrentName());
        assertNull(ctx.getCurrentValue());
        assertNull(ctx.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dd = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return false;
            }
        };
        JsonWriteContext ctx = JsonWriteContext.createRootContext(dd);
        assertEquals(JsonWriteContext.TYPE_ROOT, ctx.getType());
        assertNull(ctx.getParent());
        assertNull(ctx.getCurrentName());
        assertNull(ctx.getCurrentValue());
        assertSame(dd, ctx.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext ctx = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, ctx.getType());
        assertSame(parent, ctx.getParent());
        assertNull(ctx.getCurrentName());
        assertNull(ctx.getCurrentValue());
        assertNull(ctx.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext ctx = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, ctx.getType());
        assertSame(parent, ctx.getParent());
        assertNull(ctx.getCurrentName());
        assertNull(ctx.getCurrentValue());
        assertNull(ctx.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameTwice() throws JsonProcessingException {
        JsonWriteContext ctx = JsonWriteContext.createRootContext();
        ctx.writeFieldName("name");
        ctx.writeFieldName("name");
    }

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext ctx = JsonWriteContext.createRootContext();
        ctx.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, ctx.getStatus());
        assertEquals(0, ctx.getCurrentIndex());
        assertEquals("/", ctx.toString());

        ctx = JsonWriteContext.createRootContext();
        ctx.writeValue();
        ctx.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, ctx.getStatus());
        assertEquals(1, ctx.getCurrentIndex());
        assertEquals("/ ", ctx.toString());

        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext child = parent.createChildArrayContext();
        child.writeValue();
        child.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, child.getStatus());
        assertEquals(-1, child.getCurrentIndex());
        assertEquals("[,]", child.toString());

        child = parent.createChildObjectContext();
        child.writeValue();
        child.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, child.getStatus());
        assertEquals(-1, child.getCurrentIndex());
        assertEquals("{\"\":}", child.toString());
    }

    @Test
    public void testToString() {
        JsonWriteContext ctx = JsonWriteContext.createRootContext();
        assertTrue(ctx.toString().startsWith("/"));

        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext child = parent.createChildArrayContext();
        child.writeValue();
        child.writeValue();
        assertTrue(child.toString().startsWith("[0,]"));

        child = parent.createChildObjectContext();
        child.writeFieldName("name");
        child.writeValue();
        child.writeValue();
        assertTrue(child.toString().startsWith("{\"name\":[0,1]}"));
    }
}