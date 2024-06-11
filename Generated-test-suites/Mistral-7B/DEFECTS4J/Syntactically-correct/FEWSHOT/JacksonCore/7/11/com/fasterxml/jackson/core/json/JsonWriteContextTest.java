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
        JsonWriteContext root = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, root.getType());
        assertNull(root.getParent());
        assertNull(root.getCurrentName());
        assertNull(root.getCurrentValue());
        assertNull(root.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext root = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, root.getType());
        assertNull(root.getParent());
        assertNull(root.getCurrentName());
        assertNull(root.getCurrentValue());
        assertSame(dupDetector, root.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext child = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertSame(parent, child.getParent());
        assertNull(child.getCurrentName());
        assertNull(child.getCurrentValue());
        assertNull(child.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext child = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertSame(parent, child.getParent());
        assertNull(child.getCurrentName());
        assertNull(child.getCurrentValue());
        assertSame(dupDetector, child.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext child = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertSame(parent, child.getParent());
        assertNull(child.getCurrentName());
        assertNull(child.getCurrentValue());
        assertNull(child.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext child = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertSame(parent, child.getParent());
        assertNull(child.getCurrentName());
        assertNull(child.getCurrentValue());
        assertSame(dupDetector, child.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name1");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
        assertEquals("name1", context.getCurrentName());

        context.writeFieldName("name2");
        assertTrue(context.getStatus() > JsonWriteContext.STATUS_OK_AFTER_COMMA);
        assertEquals("name2", context.getCurrentName());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDuplicateName() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("name1");
        context.writeFieldName("name1");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());

        Object value = new Object();
        context.setCurrentValue(value);
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
        assertSame(value, context.getCurrentValue());
        assertNull(context.getCurrentName());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals("{}", context.toString());

        context.writeFieldName("name1");
        context.setCurrentValue("value1");
        context.writeValue();
        assertEquals("{\"name1\":\"value1\"}", context.toString());

        context.writeFieldName("name2");
        context.setCurrentValue(123);
        context.writeValue();
        assertEquals("{\"name1\":\"value1\",\"name2\":123}", context.toString());
    }
}