package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertNull(rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertNull(rootContext.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(parentContext, arrayContext.getParent());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertNull(arrayContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(parentContext, objectContext.getParent());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertNull(objectContext.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        String fieldName = "testField";
        int result = objectContext.writeFieldName(fieldName);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals(fieldName, objectContext.getCurrentName());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameDuplicateName() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        String fieldName = "testField";
        objectContext.writeFieldName(fieldName); // first write
        objectContext.writeFieldName(fieldName); // second write
    }

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        String value = "testValue";
        int result = objectContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals(value, objectContext.getCurrentValue());
    }

    @Test
    public void testWriteValueArray() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        String value = "testValue";
        int result = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(value, arrayContext.getCurrentValue());
    }

    @Test
    public void testToString() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        objectContext.writeFieldName("testField");
        String expectedResult = "{testField}";
        assertEquals(expectedResult, objectContext.toString());
    }
}