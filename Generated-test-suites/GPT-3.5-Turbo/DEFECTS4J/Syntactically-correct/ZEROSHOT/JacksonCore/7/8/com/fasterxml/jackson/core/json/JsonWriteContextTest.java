package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext context;

    @Before
    public void setup() {
        context = JsonWriteContext.createRootContext();
    }

    @Test
    public void testCreateRootContext() {
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dd = new DupDetector(null);
        context = JsonWriteContext.createRootContext(dd);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertEquals(dd, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext child = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertEquals(context, child.getParent());
        assertEquals(-1, child.getCurrentIndex());
        assertNull(child.getCurrentName());
        assertNull(child.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext child = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertEquals(context, child.getParent());
        assertEquals(-1, child.getCurrentIndex());
        assertNull(child.getCurrentName());
        assertNull(child.getDupDetector());
    }

    @Test
    public void testWriteFieldNameWithStatusOkAsIs() throws JsonProcessingException {
        int status = context.writeFieldName("testField");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertTrue(context.hasName());
        assertEquals("testField", context.getCurrentName());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWriteFieldNameWithStatusOkAfterComma() throws JsonProcessingException {
        context.writeFieldName("testField1");
        int status = context.writeFieldName("testField2");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertTrue(context.hasName());
        assertEquals("testField2", context.getCurrentName());
        assertNull(context.getDupDetector());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldNameWithDuplicateName() throws JsonProcessingException {
        DupDetector dd = new DupDetector(null);
        context = JsonWriteContext.createRootContext(dd);
        context.writeFieldName("testField");
        context.writeFieldName("testField");
    }

    @Test
    public void testWriteValueForObject() {
        context = context.createChildObjectContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testWriteValueForArray() {
        context = context.createChildArrayContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteValueForRoot() {
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testGetCurrentValue() {
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        context.setCurrentValue("testValue");
        assertEquals("testValue", context.getCurrentValue());
    }

    @Test
    public void testToStringForObjectContext() {
        context = context.createChildObjectContext();
        context.writeFieldName("field1");
        String expected = "{\"field1\"}";
        assertEquals(expected, context.toString());
    }

    @Test
    public void testToStringForArrayContext() {
        context = context.createChildArrayContext();
        context.writeValue();
        String expected = "[0]";
        assertEquals(expected, context.toString());
    }

    @Test
    public void testToStringForRootContext() {
        String expected = "/";
        assertEquals(expected, context.toString());
    }
}