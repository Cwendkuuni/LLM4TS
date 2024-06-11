
package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    // Test STATUS_OK_AS_IS
    @Test
    public void testWriteValue_OkAsIs() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(0, null, null);
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    // Test STATUS_OK_AFTER_COMMA
    @Test
    public void testWriteValue_OkAfterComma() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(1, null, null);
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    // Test STATUS_OK_AFTER_COLON
    @Test
    public void testWriteValue_OkAfterColon() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(2, null, null);
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    // Test STATUS_OK_AFTER_SPACE
    @Test
    public void testWriteValue_OkAfterSpace() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(3, null, null);
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

    // Test STATUS_EXPECT_VALUE
    @Test
    public void testWriteValue_ExpectValue() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(4, null, null);
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
    }

    // Test STATUS_EXPECT_NAME
    @Test
    public void testWriteValue_ExpectName() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(5, null, null);
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, result);
    }

    // Test getCurrentValue
    @Test
    public void testGetCurrentValue() {
        Object value = new Object();
        JsonWriteContext jsonWriteContext = new JsonWriteContext(0, null, null);
        jsonWriteContext.setCurrentValue(value);
        assertEquals(value, jsonWriteContext.getCurrentValue());
    }

    // Test setCurrentValue
    @Test
    public void testSetCurrentValue() {
        Object value = new Object();
        JsonWriteContext jsonWriteContext = new JsonWriteContext(0, null, null);
        jsonWriteContext.setCurrentValue(value);
        assertEquals(value, jsonWriteContext.getCurrentValue());
    }

    // Test createRootContext
    @Test
    @Deprecated
    public void testCreateRootContext() {
        DupDetector dd = new DupDetector(null);
        JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext(dd);
        assertEquals(JsonWriteContext.TYPE_ROOT, jsonWriteContext._type);
        assertNull(jsonWriteContext.getParent());
        assertEquals(dd, jsonWriteContext.getDupDetector());
    }

    // Test createChildArrayContext
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = new JsonWriteContext(0, null, null);
        DupDetector dd = new DupDetector(null);
        JsonWriteContext jsonWriteContext = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, jsonWriteContext._type);
        assertEquals(parent, jsonWriteContext.getParent());
        assertEquals(dd, jsonWriteContext.getDupDetector());
    }

    // Test createChildObjectContext
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = new JsonWriteContext(0, null, null);
        DupDetector dd = new DupDetector(null);
        JsonWriteContext jsonWriteContext = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, jsonWriteContext._type);
        assertEquals(parent, jsonWriteContext.getParent());
        assertEquals(dd, jsonWriteContext.getDupDetector());
    }

    // Test getParent
    @Test
    public void testGetParent() {
        JsonWriteContext parent = new JsonWriteContext(0, null, null);
        JsonWriteContext jsonWriteContext = new JsonWriteContext(1, parent, null);
        assertEquals(parent, jsonWriteContext.getParent());
    }

    // Test getCurrentName
    @Test
    public void testGetCurrentName() {
        String name = "test";
        JsonWriteContext jsonWriteContext = new JsonWriteContext(0, null, null);
        jsonWriteContext._currentName = name;
        assertEquals(name, jsonWriteContext.getCurrentName());
    }

    // Test getDupDetector
    @Test
    public void testGetDupDetector() {
        DupDetector dd = new DupDetector(null);
        JsonWriteContext jsonWriteContext = new JsonWriteContext(0, null, dd);
        assertEquals(dd, jsonWriteContext.getDupDetector());
    }

    // Test writeFieldName
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        String name = "test";
        JsonWriteContext jsonWriteContext = new JsonWriteContext(0, null, null);
        int result = jsonWriteContext.writeFieldName(name);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(name, jsonWriteContext._currentName);
    }

    // Test _checkDup
    @Test
    public void test_checkDup() throws JsonProcessingException {
        String name = "test";
        DupDetector dd = new DupDetector(null);
        JsonWriteContext jsonWriteContext = new JsonWriteContext(0, null, dd);
        jsonWriteContext._checkDup(dd, name);
        assertNotNull(dd.isDup(name));
    }

    // Test reset
    @Test
    public void testReset() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(0, null, null);
        jsonWriteContext._currentName = "test";
        jsonWriteContext._gotName = true;
        jsonWriteContext._currentValue = new Object();
        JsonWriteContext result = jsonWriteContext.reset(0);
        assertEquals(0, result._type);
        assertEquals(-1, result._index);
        assertNull(result._currentName);
        assertFalse(result._gotName);
        assertNull(result._currentValue);
    }
}
