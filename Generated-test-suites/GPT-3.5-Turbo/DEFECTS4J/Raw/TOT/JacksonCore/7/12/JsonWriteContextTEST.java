package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class JsonWriteContextTest {

    private JsonWriteContext jsonWriteContext;

    @Before
    public void setUp() {
        jsonWriteContext = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWithDupDetector() {
        DupDetector detector = new DupDetector();
        JsonWriteContext context = jsonWriteContext.withDupDetector(detector);

        assertEquals(context, jsonWriteContext);
        assertEquals(detector, jsonWriteContext.getDupDetector());
    }

    @Test
    public void testGetCurrentValue() {
        assertNull(jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        String value = "testValue";
        jsonWriteContext.setCurrentValue(value);

        assertEquals(value, jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childContext = jsonWriteContext.createChildArrayContext();

        assertNull(jsonWriteContext.getCurrentValue());
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(jsonWriteContext, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childContext = jsonWriteContext.createChildObjectContext();

        assertNull(jsonWriteContext.getCurrentValue());
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(jsonWriteContext, childContext.getParent());
    }

    @Test
    public void testWriteFieldNameWithValidName() throws JsonProcessingException {
        int result = jsonWriteContext.writeFieldName("field");

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("field", jsonWriteContext.getCurrentName());
    }

    @Test
    public void testWriteFieldNameWithoutValue() throws JsonProcessingException {
        jsonWriteContext.writeFieldName("field");
        int result = jsonWriteContext.writeFieldName("field2");

        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals("field2", jsonWriteContext.getCurrentName());
    }

    @Test
    public void testWriteFieldNameWithDuplicateName() {
        DupDetector detector = new DupDetector();
        JsonWriteContext context = jsonWriteContext.withDupDetector(detector);

        try {
            context.writeFieldName("field");
            context.writeFieldName("field");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'field'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObject() {
        jsonWriteContext.setType(JsonWriteContext.TYPE_OBJECT);
        int result = jsonWriteContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertFalse(jsonWriteContext.isGotName());
        assertEquals(0, jsonWriteContext.getIndex());
    }

    @Test
    public void testWriteValueInArray() {
        jsonWriteContext.setType(JsonWriteContext.TYPE_ARRAY);
        int result = jsonWriteContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(0, jsonWriteContext.getIndex());
    }

    @Test
    public void testWriteValueInRootContext() {
        int result = jsonWriteContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(0, jsonWriteContext.getIndex());
    }

    @Test
    public void testAppendDescForObject() {
        jsonWriteContext.setCurrentName("field");
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);

        assertEquals("{\"field\"}", sb.toString());
    }

    @Test
    public void testAppendDescForArray() {
        jsonWriteContext.setIndex(1);
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);

        assertEquals("[1]", sb.toString());
    }

    @Test
    public void testAppendDescForRootContext() {
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);

        assertEquals("/", sb.toString());
    }

    @Test
    public void testToString() {
        jsonWriteContext.setCurrentName("field");
        jsonWriteContext.setIndex(1);

        assertEquals("{\"field\"}[1]", jsonWriteContext.toString());
    }

}