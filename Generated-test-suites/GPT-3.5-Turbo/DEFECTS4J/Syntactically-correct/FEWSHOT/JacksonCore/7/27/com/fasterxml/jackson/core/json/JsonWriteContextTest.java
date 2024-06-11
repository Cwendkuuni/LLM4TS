package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {

    private JsonWriteContext jsonWriteContext;

    @Before
    public void setUp() {
        jsonWriteContext = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeFieldName("name"));
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, jsonWriteContext.writeFieldName("name"));
        assertNull(jsonWriteContext.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeValue());
        assertEquals("name", jsonWriteContext.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, jsonWriteContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonWriteContext.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = jsonWriteContext.createChildArrayContext();
        assertNotNull(childArrayContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(JsonWriteContext.TYPE_OBJECT, jsonWriteContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = jsonWriteContext.createChildObjectContext();
        assertNotNull(childObjectContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(JsonWriteContext.TYPE_OBJECT, jsonWriteContext.getType());
    }

    @Test
    public void testGettersAndSetters() {
        assertNull(jsonWriteContext.getCurrentValue());
        assertNull(jsonWriteContext.getParent());
        assertNull(jsonWriteContext.getDupDetector());

        Object value = new Object();
        jsonWriteContext.setCurrentValue(value);
        assertTrue(value.equals(jsonWriteContext.getCurrentValue()));

        DupDetector dupDetector = new DupDetector(null);
        jsonWriteContext.withDupDetector(dupDetector);
        assertTrue(dupDetector.equals(jsonWriteContext.getDupDetector()));
    }

    @Test
    public void testToString() throws JsonProcessingException {
        String expected = "/root";
        assertEquals(expected, jsonWriteContext.toString());

        jsonWriteContext.writeFieldName("name");
        expected = "/root/{\"name\"}";
        assertEquals(expected, jsonWriteContext.toString());

        jsonWriteContext.createChildArrayContext();
        expected = "/root/{\"name\"}/[0]";
        assertEquals(expected, jsonWriteContext.toString());
    }
}