package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Mock
    private DupDetector dups;

    private JsonStreamContext parentContext;

    private JsonWriteContext jsonWriteContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parentContext = new JsonStreamContext();
        jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, parentContext, dups);
    }

    @Test
    public void testReset() {
        jsonWriteContext.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.TYPE_ARRAY, jsonWriteContext.getType());
        assertEquals(-1, jsonWriteContext.getIndex());
        assertNull(jsonWriteContext.getCurrentName());
        assertFalse(jsonWriteContext.getGotName());
        assertNull(jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testWithDupDetector() {
        jsonWriteContext.withDupDetector(dups);
        assertEquals(dups, jsonWriteContext.getDupDetector());
    }

    @Test
    public void testGetCurrentValue() {
        Object value = new Object();
        jsonWriteContext.setCurrentValue(value);
        assertEquals(value, jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dups);
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertNull(rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertFalse(rootContext.getGotName());
        assertNull(rootContext.getCurrentValue());
        assertEquals(dups, rootContext.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childContext1 = jsonWriteContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext1.getType());
        assertEquals(jsonWriteContext, childContext1.getParent());
        assertNull(childContext1.getCurrentName());
        assertFalse(childContext1.getGotName());
        assertNull(childContext1.getCurrentValue());
        assertEquals(dups, childContext1.getDupDetector());

        JsonWriteContext childContext2 = jsonWriteContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext2.getType());
        assertEquals(jsonWriteContext, childContext2.getParent());
        assertNull(childContext2.getCurrentName());
        assertFalse(childContext2.getGotName());
        assertNull(childContext2.getCurrentValue());
        assertEquals(dups, childContext2.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childContext1 = jsonWriteContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext1.getType());
        assertEquals(jsonWriteContext, childContext1.getParent());
        assertNull(childContext1.getCurrentName());
        assertFalse(childContext1.getGotName());
        assertNull(childContext1.getCurrentValue());
        assertEquals(dups, childContext1.getDupDetector());

        JsonWriteContext childContext2 = jsonWriteContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext2.getType());
        assertEquals(jsonWriteContext, childContext2.getParent());
        assertNull(childContext2.getCurrentName());
        assertFalse(childContext2.getGotName());
        assertNull(childContext2.getCurrentValue());
        assertEquals(dups, childContext2.getDupDetector());
    }

    @Test
    public void testGetCurrentName() {
        String name = "test";
        jsonWriteContext.setCurrentName(name);
        assertEquals(name, jsonWriteContext.getCurrentName());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        String name = "test";
        int result = jsonWriteContext.writeFieldName(name);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(name, jsonWriteContext.getCurrentName());
        assertTrue(jsonWriteContext.getGotName());
        assertNull(jsonWriteContext.getCurrentValue());

        jsonWriteContext.writeFieldName(name);
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals(name, jsonWriteContext.getCurrentName());
        assertTrue(jsonWriteContext.getGotName());
        assertNull(jsonWriteContext.getCurrentValue());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldName_duplicateFieldName_shouldThrowJsonGenerationException() throws JsonProcessingException {
        String name = "test";
        jsonWriteContext.withDupDetector(dups);
        jsonWriteContext.setCurrentName(name);
        jsonWriteContext.writeFieldName(name);
    }

    @Test
    public void testWriteValue_objectType() {
        jsonWriteContext.setType(JsonWriteContext.TYPE_OBJECT);
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertFalse(jsonWriteContext.getGotName());
        assertNull(jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testWriteValue_arrayType() {
        jsonWriteContext.setType(JsonWriteContext.TYPE_ARRAY);
        jsonWriteContext.setIndex(-1);

        int result1 = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result1);
        assertFalse(jsonWriteContext.getGotName());
        assertNull(jsonWriteContext.getCurrentValue());

        jsonWriteContext.setIndex(0);
        int result2 = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result2);
        assertFalse(jsonWriteContext.getGotName());
        assertNull(jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testWriteValue_rootType() {
        jsonWriteContext.setIndex(0);

        int result1 = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result1);
        assertFalse(jsonWriteContext.getGotName());
        assertNull(jsonWriteContext.getCurrentValue());

        jsonWriteContext.setIndex(1);
        int result2 = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result2);
        assertFalse(jsonWriteContext.getGotName());
        assertNull(jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testAppendDesc_objectType() {
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.setType(JsonWriteContext.TYPE_OBJECT);
        jsonWriteContext.appendDesc(sb);
        assertEquals("{\"", sb.toString());
    }

    @Test
    public void testAppendDesc_arrayType() {
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.setType(JsonWriteContext.TYPE_ARRAY);
        jsonWriteContext.appendDesc(sb);
        assertEquals("[0]", sb.toString());
    }

    @Test
    public void testAppendDesc_rootType() {
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);
        assertEquals("/", sb.toString());
    }

    @Test
    public void testToString() {
        jsonWriteContext.setCurrentName("test");
        String result = jsonWriteContext.toString();
        assertEquals("/test", result);
    }

}