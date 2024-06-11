package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertNull(rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertSame(dupDetector, rootContext.getDupDetector());
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
    public void testCreateChildArrayContextWithDupDetector() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();

        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(parentContext, arrayContext.getParent());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertSame(dupDetector, arrayContext.getDupDetector());
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
    public void testCreateChildObjectContextWithDupDetector() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext objectContext = parentContext.createChildObjectContext();

        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(parentContext, objectContext.getParent());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertSame(dupDetector, objectContext.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithParentAndCurrentName() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        String name = "name";
        parentContext.writeFieldName(name);
        parentContext.writeFieldName(name);
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        String name = "name";
        int result = context.writeFieldName(name);

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals(name, context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());

        context.writeValue();
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeValue();

        assertEquals("{}", context.toString());
    }

    @Test
    public void testWriteValueWithParentAndCurrentValue() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        String value = "value";
        parentContext.setCurrentValue(value);
        int result = parentContext.writeValue();

        assertEquals(JsonWriteContext.TYPE_OBJECT, parentContext.getType());
        assertEquals("{value}", parentContext.toString());
        assertEquals(value, parentContext.getCurrentValue());
        assertNull(parentContext.getParent());
        assertNull(parentContext.getCurrentName());
        assertNull(parentContext.getDupDetector());
    }

    @Test
    public void testWriteValueWithParentAndCurrentValueAndDupDetector() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        String value = "value";
        parentContext.setCurrentValue(value);
        JsonWriteContext.DupDetector dupDetector = new JsonWriteContext.DupDetector();
        parentContext.withDupDetector(dupDetector);
        int result = parentContext.writeValue();

        assertEquals(JsonWriteContext.TYPE_OBJECT, parentContext.getType());
        assertEquals("{value}", parentContext.toString());
        assertEquals(value, parentContext.getCurrentValue());
        assertSame(dupDetector, parentContext.getDupDetector());
    }

    @Test
    public void testWriteValueWithChildArrayContext() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        int index = 0;
        arrayContext.writeValue();

        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(parentContext, arrayContext.getParent());
        assertEquals("[]", arrayContext.toString());
        assertEquals(null, arrayContext.getCurrentName());
        assertEquals(null, arrayContext.getCurrentValue());
        assertNull(arrayContext.getDupDetector());

        parentContext.writeValue();
        assertEquals("{[]}", parentContext.toString());
    }

    @Test
    public void testWriteValueWithChildObjectContext() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        String name = "name";
        String value = "value";
        objectContext.writeFieldName(name);
        objectContext.writeValue(value);

        assertEquals(JsonWriteContext.TYPE_OBJECT, parentContext.getType());
        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals("{\"name\":\"" + value + "\"}", parentContext.toString());
        assertEquals(name, objectContext.getCurrentName());
        assertEquals(value, objectContext.getCurrentValue());
        assertNull(objectContext.getParent());
        assertNull(objectContext.getDupDetector());

        parentContext.writeValue();
        assertEquals("{\"name\":\"" + value + "\"}", parentContext.toString());
    }

    @Test
    public void testWriteValueWithChildObjectContextWithDupDetector() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        String name = "name";
        String value = "value";
        JsonWriteContext.DupDetector dupDetector = new JsonWriteContext.DupDetector();
        objectContext.withDupDetector(dupDetector);
        objectContext.writeFieldName(name);
        objectContext.writeValue(value);

        assertEquals(JsonWriteContext.TYPE_OBJECT, parentContext.getType());
        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals("{\"name\":\"" + value + "\"}", parentContext.toString());
        assertEquals(name, objectContext.getCurrentName());
        assertEquals(value, objectContext.getCurrentValue());
        assertSame(dupDetector, objectContext.getDupDetector());

        parentContext.writeValue();
        assertEquals("{\"name\":\"" + value + "\"}", parentContext.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueWithTwoChildObjectsWithSameName() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext1 = parentContext.createChildObjectContext();
        JsonWriteContext objectContext2 = parentContext.createChildObjectContext();
        String name = "name";
        String value1 = "value1";
        String value2 = "value2";

        objectContext1.writeFieldName(name);
        objectContext1.writeValue(value1);
        objectContext2.writeFieldName(name);
        objectContext2.writeValue(value2);
    }

    @Test
    public void testGetCurrentIndex() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.writeValue();
        context.writeValue();
        context.writeValue();

        assertEquals(2, context.getCurrentIndex());
    }
}