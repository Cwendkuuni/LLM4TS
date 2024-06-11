package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertNull(rootContext.getParent());
        assertEquals("", rootContext.toString());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertSame(dupDetector, rootContext.getDupDetector());
        assertNull(rootContext.getParent());
        assertEquals("/", rootContext.toString());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertSame(parentContext, arrayContext.getParent());
        assertEquals("[", arrayContext.toString());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertSame(parentContext, objectContext.getParent());
        assertEquals("{\n", objectContext.toString());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext objContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = objContext.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals("fieldName", objContext.getCurrentName());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameTwice() throws JsonProcessingException {
        JsonWriteContext objContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        objContext.writeFieldName("fieldName");
        objContext.writeFieldName("fieldName");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext objContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        objContext.setCurrentValue("value");
        int result = objContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals("value", objContext.getCurrentValue());
    }

    @Test
    public void testWriteValueForArrayContext() {
        JsonWriteContext arrContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        arrContext.setCurrentValue("value");
        int result = arrContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals("value", arrContext.getCurrentValue());
    }

    @Test
    public void testWriteValueForRootContext() {
        JsonWriteContext rootContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        rootContext.setCurrentValue("value");
        int result = rootContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        assertEquals("value", rootContext.getCurrentValue());
    }
}