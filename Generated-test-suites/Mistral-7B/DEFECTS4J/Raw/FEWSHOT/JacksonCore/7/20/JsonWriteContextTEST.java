package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertNull(rootContext.getParent());
        assertEquals("/", rootContext.toString());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = Mockito.mock(DupDetector.class);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(dupDetector, rootContext.getDupDetector());
        assertNull(rootContext.getParent());
        assertEquals("/", rootContext.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDuplicateName() throws JsonProcessingException {
        DupDetector dupDetector = Mockito.mock(DupDetector.class);
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        jsonWriteContext.writeFieldName("field");
        jsonWriteContext.writeFieldName("field");
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = Mockito.mock(JsonWriteContext.class);
        JsonWriteContext childContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, parentContext, null).createChildArrayContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals("[", childContext.toString());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = Mockito.mock(JsonWriteContext.class);
        JsonWriteContext childContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parentContext, null).createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals("{\"", childContext.toString());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        DupDetector dupDetector = Mockito.mock(DupDetector.class);
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        int result = jsonWriteContext.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null).reset(JsonWriteContext.TYPE_OBJECT);
        result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);

        jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }
}