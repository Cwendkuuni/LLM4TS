package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DupDetector;

public class JsonWriteContextTest {
    // Test starts here
    

    @Test
    public void testCreateRootContext() {
        JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext();
        assertNull(jsonWriteContext.getParent());
        assertEquals("", jsonWriteContext.getCurrentName());
        assertNull(jsonWriteContext.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext(dupDetector);
        assertSame(dupDetector, jsonWriteContext.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithGotName() throws JsonProcessingException {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        jsonWriteContext.writeFieldName("name");
        jsonWriteContext.writeFieldName("age");
        jsonWriteContext.writeValue(); // reset _gotName to false
        jsonWriteContext.writeFieldName("name"); // should throw an exception
    }

    @Test
    public void testWriteValueForRootContext() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, jsonWriteContext.getStatus());
    }

    @Test
    public void testWriteValueForArrayContext() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.getStatus());
    }

    @Test
    public void testWriteValueForObjectContext() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        jsonWriteContext.writeFieldName("name");
        jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonWriteContext.getStatus());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueAfterWriteFieldNameWithGotName() throws JsonProcessingException {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        jsonWriteContext.writeFieldName("name");
        jsonWriteContext.writeValue(); // reset _gotName to false
        jsonWriteContext.writeValue(); // should throw an exception
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext child = parent.createChildArrayContext();
        assertNotNull(child);
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertSame(parent, child.getParent());
        assertNull(child.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext child = parent.createChildObjectContext();
        assertNotNull(child);
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertSame(parent, child.getParent());
        assertNull(child.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithNullName() throws JsonProcessingException {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        jsonWriteContext.writeFieldName(null);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithEmptyName() throws JsonProcessingException {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        jsonWriteContext.writeFieldName("");
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDuplicateName() throws JsonProcessingException {
        String name = "name";
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        jsonWriteContext.writeFieldName(name);
        jsonWriteContext.writeValue();
        jsonWriteContext.writeFieldName(name); // should throw an exception
    }

    @Test
    public void testAppendDescForObjectContext() {
        StringBuilder sb = new StringBuilder(64);
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        jsonWriteContext.appendDesc(sb);
        String expected = "{name}";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testAppendDescForArrayContext() {
        StringBuilder sb = new StringBuilder(64);
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        jsonWriteContext.appendDesc(sb);
        String expected = "[0]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testAppendDescForRootContext() {
        StringBuilder sb = new StringBuilder(64);
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        jsonWriteContext.appendDesc(sb);
        String expected = "/";
        assertEquals(expected, sb.toString());
    }

    
}