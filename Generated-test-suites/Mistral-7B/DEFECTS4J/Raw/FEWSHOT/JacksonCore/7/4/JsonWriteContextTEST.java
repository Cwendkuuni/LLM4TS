package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

public class JsonWriteContextTest {
    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals("/", rootContext.toString());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertEquals("/", rootContext.toString());
        assertEquals(dupDetector, rootContext.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertEquals("[]", arrayContext.toString());
        assertEquals(parentContext, arrayContext.getParent());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertEquals("{", objectContext.toString());
        assertEquals(parentContext, objectContext.getParent());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        int status = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertEquals("{\"name\":}", context.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        context.writeFieldName("name");
        context.writeFieldName("name");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertEquals("{\"\":}", context.toString());

        context.writeValue();
        assertEquals("{\"\":}", context.toString());

        context.setCurrentValue("value");
        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals("{\"\":,\"value\"}", context.toString());
    }

    @Test
    public void testWriteValueInArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        int status;

        status = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals("[,]", arrayContext.toString());

        arrayContext.setCurrentValue("value1");
        status = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("[\"value1\"]", arrayContext.toString());

        arrayContext.setCurrentValue("value2");
        status = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals("[\"value1\",]", arrayContext.toString());

        arrayContext.setCurrentValue("value3");
        status = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("[\"value1\",\"value3\"]", arrayContext.toString());
    }

    @Test
    public void testWriteValueInObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        int status;

        status = objectContext.writeFieldName("field1");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertEquals("{\"field1\":}", objectContext.toString());

        objectContext.setCurrentValue("value1");
        status = objectContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("{\"field1\":\"value1\"}", objectContext.toString());

        status = objectContext.writeFieldName("field2");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertEquals("{\"field1\":\"value1\",\"field2\":}", objectContext.toString());

        objectContext.setCurrentValue("value2");
        status = objectContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals("{\"field1\":\"value1\",\"field2\":,\"value2\"}", objectContext.toString());
    }
}