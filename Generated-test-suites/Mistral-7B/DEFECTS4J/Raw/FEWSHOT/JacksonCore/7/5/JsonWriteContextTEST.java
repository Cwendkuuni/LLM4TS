package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertSame(dupDetector, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertSame(parentContext, childContext.getParent());
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getCurrentValue());
        assertNull(childContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertSame(parentContext, childContext.getParent());
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getCurrentValue());
        assertNull(childContext.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWhenGotName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        context.writeFieldName("name2");
    }

    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.writeValue();
        assertTrue(context.getIndex() > 0);
    }

    @Test
    public void testWriteValueInArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        childContext.writeValue();
        assertTrue(childContext.getIndex() > 0);
    }

    @Test
    public void testWriteValueInObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        childContext.writeValue();
        assertTrue(childContext.getIndex() > 0);
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals("{}", context.toString());

        context.writeFieldName("name");
        context.setCurrentValue("value");
        assertEquals("{\"name\":\"value\"}", context.toString());
    }
}