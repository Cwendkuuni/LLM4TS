package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
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
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertNull(context.getParent());
        assertEquals("/", context.toString());
        assertEquals(dupDetector, context.getDupDetector());
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
    public void testWriteFieldNameWithGotName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        context.writeFieldName("age");
        context.writeValue(); // reset _gotName
        context.writeFieldName("name");
    }

    @Test
    public void testWriteValueForRootContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.writeValue();
        assertEquals(1, context.getCurrentIndex());
        assertEquals(" ", context.toString());
    }

    @Test
    public void testWriteValueForObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        childContext.writeFieldName("name");
        childContext.writeValue();
        childContext.writeValue();
        assertEquals(1, childContext.getCurrentIndex());
        assertEquals("{\"name\":}", childContext.toString());
    }

    @Test
    public void testWriteValueForArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        childContext.writeValue();
        childContext.writeValue();
        assertEquals(2, childContext.getCurrentIndex());
        assertEquals("[,]", childContext.toString());
    }

    @Test
    public void testWriteValueWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("name");
        context.writeValue();
        context.writeFieldName("name");
        try {
            context.writeValue();
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
        assertEquals(1, context.getCurrentIndex());
        assertEquals("{\"name\":}", context.toString());
    }
}