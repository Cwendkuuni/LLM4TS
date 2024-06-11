package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext rootContextWithDupDetector = JsonWriteContext.createRootContext(null);

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertEquals(null, rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertNull(rootContext.getDupDetector());

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContextWithDupDetector.getType());
        assertEquals(null, rootContextWithDupDetector.getParent());
        assertNull(rootContextWithDupDetector.getCurrentName());
        assertNull(rootContextWithDupDetector.getCurrentValue());
        assertNull(rootContextWithDupDetector.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();

        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(parentContext, arrayContext.getParent());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertNull(arrayContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();

        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(parentContext, objectContext.getParent());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertNull(objectContext.getDupDetector());
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");

        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(null, context.getParent());
        assertEquals("name", context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameTwice() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        context.writeFieldName("name");
    }

    @Test
    public void testWriteValue_ArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();

        arrayContext.writeValue();

        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(parentContext, arrayContext.getParent());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertNull(arrayContext.getDupDetector());
        assertEquals(1, arrayContext.getIndex());
    }

    @Test
    public void testWriteValue_ObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();

        objectContext.writeValue();

        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(parentContext, objectContext.getParent());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertNull(objectContext.getDupDetector());
        assertEquals(0, objectContext.getIndex());
    }

    @Test
    public void testWriteValue_RootContext() {
        JsonWriteContext rootContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);

        rootContext.writeValue();

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertEquals(null, rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertNull(rootContext.getDupDetector());
        assertEquals(0, rootContext.getIndex());
    }
}