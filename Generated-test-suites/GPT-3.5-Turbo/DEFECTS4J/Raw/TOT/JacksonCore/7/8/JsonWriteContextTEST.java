
package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class JsonWriteContextTest {

    JsonWriteContext jsonWriteContext;

    @BeforeEach
    public void setup() {
        jsonWriteContext = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName_typicalCase_shouldWriteNameAndReturnStatusOkAfterComma() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeFieldName("fieldName"));
        assertEquals("fieldName", jsonWriteContext.getCurrentName());
        assertFalse(jsonWriteContext._gotName);
    }

    @Test
    public void testWriteFieldName_emptyName_shouldThrowJsonProcessingException() {
        assertThrows(JsonProcessingException.class, () -> jsonWriteContext.writeFieldName("")); 
    }

    @Test
    public void testWriteFieldName_duplicateName_whenDupDetectorIsNotNull_shouldThrowJsonProcessingException() {
        JsonWriteContext rootContextWithDupDetector = JsonWriteContext.createRootContext(new DupDetector());

        assertThrows(JsonProcessingException.class, () -> rootContextWithDupDetector.writeFieldName("fieldName")); 
    }

    @Test
    public void testWriteFieldName_duplicateName_whenDupDetectorIsNull_shouldNotThrowException() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeFieldName("fieldName")); 
    }

    @Test
    public void testWriteFieldName_afterGotNameIsTrue_shouldReturnStatusExpectValue() throws JsonProcessingException {
        jsonWriteContext._gotName = true;

        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, jsonWriteContext.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteFieldName_afterGotNameIsTrue_shouldResetGotNameToFalse() throws JsonProcessingException {
        jsonWriteContext._gotName = true;
        jsonWriteContext.writeFieldName("fieldName");

        assertFalse(jsonWriteContext._gotName);
    }

    @Test
    public void testWriteFieldName_whenTypeIsArray_shouldReturnStatusOkAfterComma() throws JsonProcessingException {
        jsonWriteContext = jsonWriteContext.createChildArrayContext();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteFieldName_whenTypeIsArray_shouldIncreaseIndex() throws JsonProcessingException {
        jsonWriteContext = jsonWriteContext.createChildArrayContext();
        int beforeIndex = jsonWriteContext._index;

        jsonWriteContext.writeFieldName("fieldName");

        assertEquals(beforeIndex + 1, jsonWriteContext._index);
    }

    @Test
    public void testWriteFieldName_whenTypeIsArray_andIndexIsMinusOne_shouldReturnStatusOkAsIs() throws JsonProcessingException {
        jsonWriteContext = jsonWriteContext.createChildArrayContext();
        jsonWriteContext.reset(JsonWriteContext.TYPE_ARRAY);

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteFieldName_whenTypeIsObject_shouldReturnStatusOkAfterColon() throws JsonProcessingException {
        jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, jsonWriteContext.getParent(), new DupDetector());

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonWriteContext.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteFieldName_whenTypeIsObject_resetParentFields() throws JsonProcessingException {
        jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, jsonWriteContext.getParent(), new DupDetector());
        jsonWriteContext._index = 2;
        jsonWriteContext._currentName = "parentName";
        jsonWriteContext._gotName = true;

        jsonWriteContext.writeFieldName("fieldName");

        assertEquals(1, jsonWriteContext._index);
        assertEquals("fieldName", jsonWriteContext._currentName);
        assertFalse(jsonWriteContext._gotName);
    }

    @Test
    public void testWriteFieldName_whenTypeIsRoot_shouldReturnStatusOkAsIs() throws JsonProcessingException {
        jsonWriteContext = JsonWriteContext.createRootContext();
        jsonWriteContext.reset(JsonWriteContext.TYPE_ROOT);

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteFieldName_whenTypeIsRoot_andIndexIsZero_shouldReturnStatusOkAsIs() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteFieldName_whenTypeIsRoot_andIndexIsNotZero_shouldReturnStatusOkAfterSpace() throws JsonProcessingException {
        jsonWriteContext._index = 1;

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, jsonWriteContext.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteValue_whenTypeIsObject_shouldIncreaseIndex() {
        jsonWriteContext._type = JsonWriteContext.TYPE_OBJECT;
        int beforeIndex = jsonWriteContext._index;

        jsonWriteContext.writeValue();

        assertEquals(beforeIndex + 1, jsonWriteContext._index);
    }

    @Test
    public void testWriteValue_whenTypeIsObjectContext_shouldReturnStatusOkAfterColon() {
        jsonWriteContext._type = JsonWriteContext.TYPE_OBJECT;

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonWriteContext.writeValue());
    }

    @Test
    public void testWriteValue_whenTypeIsArray_shouldReturnStatusOkAfterComma() {
        jsonWriteContext._type = JsonWriteContext.TYPE_ARRAY;

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeValue());
    }

    @Test
    public void testWriteValue_whenTypeIsArray_andIndexIsMinusOne_shouldReturnStatusOkAsIs() {
        jsonWriteContext._type = JsonWriteContext.TYPE_ARRAY;
        jsonWriteContext.reset(JsonWriteContext.TYPE_ARRAY);

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeValue());
    }

    @Test
    public void testWriteValue_whenTypeIsRoot_shouldReturnStatusOkAsIs() {
        jsonWriteContext._type = JsonWriteContext.TYPE_ROOT;

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeValue());
    }

    @Test
    public void testWriteValue_whenTypeIsRoot_andIndexIsZero_shouldReturnStatusOkAsIs() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeValue());
    }

    @Test
    public void testWriteValue_whenTypeIsRoot_andIndexIsNotZero_shouldReturnStatusOkAfterSpace() {
        jsonWriteContext._type = JsonWriteContext.TYPE_ROOT;
        jsonWriteContext._index = 1;

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, jsonWriteContext.writeValue());
    }

    @Test
    public void testGetCurrentValue_shouldReturnNull() {
        assertNull(jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        Object value = new Object();

        jsonWriteContext.setCurrentValue(value);

        assertEquals(value, jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testGetParent_shouldReturnNull() {
        assertNull(jsonWriteContext.getParent());
    }

    @Test
    public void testGetCurrentName_shouldReturnNull() {
        assertNull(jsonWriteContext.getCurrentName());
    }

    @Test
    public void testGetDupDetector_shouldReturnNull() {
        assertNull(jsonWriteContext.getDupDetector());
    }

}
