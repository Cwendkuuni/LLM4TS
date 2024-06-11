package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getParent());
        assertEquals("/", context.toString());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(dupDetector, context.getDupDetector());
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
    public void testCreateChildArrayContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals("[", childContext.toString());
        assertEquals(dupDetector, childContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals("{\n", childContext.toString());
    }

    @Test
    public void testCreateChildObjectContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals("{\n", childContext.toString());
        assertEquals(dupDetector, childContext.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("test");
        context.writeFieldName("test");
    }

    @Test
    public void testWriteValue_ArrayContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals("[", context.toString());
    }

    @Test
    public void testWriteValue_ObjectContext_NoName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.reset(JsonWriteContext.TYPE_OBJECT);
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeValue());
        assertEquals("{\n", context.toString());
    }

    @Test
    public void testWriteValue_ObjectContext_WithName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.reset(JsonWriteContext.TYPE_OBJECT);
        context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals("{\"name\":}", context.toString());
    }

    @Test
    public void testWriteValue_RootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.reset(JsonWriteContext.TYPE_ROOT);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals("/", context.toString());
    }
}