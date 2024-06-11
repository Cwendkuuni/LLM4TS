package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    private JsonWriteContext context;

    @Before
    public void setUp() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    }

    @Test
    public void testGetType() {
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
    }

    @Test
    public void testGetParent() {
        assertNull(context.getParent());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWriteFieldName() {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("name"));
    }

    @Test
    public void testWriteValue_ObjectContext() {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteValue_ArrayContext() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValue_RootContext() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testAppendDesc_ObjectContext() {
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("{\"?\"}", sb.toString());
    }

    @Test
    public void testAppendDesc_ArrayContext() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("[0]", sb.toString());
    }

    @Test
    public void testAppendDesc_RootContext() {
        context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
    }
}