
package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {

    @Mock
    private DupDetector dupDetector;

    private JsonWriteContext rootContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        rootContext = JsonWriteContext.createRootContext(dupDetector);
    }

    // Test for constructor

    @Test
    public void testConstructorWithTypeRoot() {
        assertEquals(0, rootContext.getType());
        assertNull(rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertNull(rootContext.getDupDetector());
        assertFalse(rootContext.hasCurrentName());
        assertFalse(rootContext.hasCurrentValue());
    }

    @Test
    public void testConstructorWithTypeArray() {
        JsonWriteContext arrayContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, rootContext, dupDetector);

        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(rootContext, arrayContext.getParent());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertEquals(dupDetector, arrayContext.getDupDetector());
        assertFalse(arrayContext.hasCurrentName());
        assertFalse(arrayContext.hasCurrentValue());
    }

    @Test
    public void testConstructorWithTypeObject() {
        JsonWriteContext objectContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, rootContext, dupDetector);

        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(rootContext, objectContext.getParent());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertEquals(dupDetector, objectContext.getDupDetector());
        assertFalse(objectContext.hasCurrentName());
        assertFalse(objectContext.hasCurrentValue());
    }

    // Test for reset method

    @Test
    public void testResetWithTypeRoot() {
        rootContext.setCurrentValue("testValue");
        rootContext.setCurrentName("testName");
        rootContext.reset(JsonWriteContext.TYPE_ROOT);

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertNull(rootContext.getCurrentName());
        assertEquals("testValue", rootContext.getCurrentValue());
        assertNull(rootContext.getDupDetector());
        assertFalse(rootContext.hasCurrentName());
        assertTrue(rootContext.hasCurrentValue());
    }

    @Test
    public void testResetWithTypeArray() {
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        arrayContext.setCurrentValue("testValue");
        arrayContext.reset(JsonWriteContext.TYPE_ARRAY);

        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertNull(arrayContext.getCurrentName());
        assertEquals("testValue", arrayContext.getCurrentValue());
        assertNull(arrayContext.getDupDetector());
        assertFalse(arrayContext.hasCurrentName());
        assertTrue(arrayContext.hasCurrentValue());
    }

    @Test
    public void testResetWithTypeObject() {
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.setCurrentValue("testValue");
        objectContext.reset(JsonWriteContext.TYPE_OBJECT);

        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertNull(objectContext.getCurrentName());
        assertEquals("testValue", objectContext.getCurrentValue());
        assertNull(objectContext.getDupDetector());
        assertFalse(objectContext.hasCurrentName());
        assertTrue(objectContext.hasCurrentValue());
    }

    // Test for writeFieldName method

    @Test
    public void testWriteFieldNameWhenNoDupDetector() throws JsonProcessingException {
        int result = rootContext.writeFieldName("fieldName");

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertFalse(rootContext.hasCurrentName());
        assertEquals("fieldName", rootContext.getCurrentName());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldNameWhenDupDetected() throws JsonProcessingException {
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.writeFieldName("fieldName");

        // Call again with the same field name, it should throw JsonGenerationException
        objectContext.writeFieldName("fieldName");
    }

    // Test for writeValue method

    @Test
    public void testWriteValueWhenTypeIsObject() {
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.setCurrentValue("value");

        int result = objectContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertFalse(objectContext.hasCurrentName());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
    }

    @Test
    public void testWriteValueWhenTypeIsArray() {
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        arrayContext.setCurrentIndex(0);

        int result = arrayContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertFalse(arrayContext.hasCurrentName());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
    }

    @Test
    public void testWriteValueWhenTypeIsRoot() {
        int result = rootContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertFalse(rootContext.hasCurrentName());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
    }

    // Test for other methods

    @Test
    public void testGetCurrentValue() {
        rootContext.setCurrentValue("testValue");

        assertEquals("testValue", rootContext.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        rootContext.setCurrentValue("testValue");

        assertEquals("testValue", rootContext.getCurrentValue());
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);

        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertEquals(dupDetector, context.getDupDetector());
        assertFalse(context.hasCurrentName());
        assertFalse(context.hasCurrentValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();

        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(rootContext, arrayContext.getParent());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertEquals(dupDetector, arrayContext.getDupDetector());
        assertFalse(arrayContext.hasCurrentName());
        assertFalse(arrayContext.hasCurrentValue());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext objectContext = rootContext.createChildObjectContext();

        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(rootContext, objectContext.getParent());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertEquals(dupDetector, objectContext.getDupDetector());
        assertFalse(objectContext.hasCurrentName());
        assertFalse(objectContext.hasCurrentValue());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();

        assertEquals(rootContext, arrayContext.getParent());
    }

    @Test
    public void testGetCurrentName() {
        rootContext.writeFieldName("fieldName");

        assertEquals("fieldName", rootContext.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        assertEquals(dupDetector, rootContext.getDupDetector());
    }

    @Test
    public void testAppendDesc() {
        StringBuilder sb = new StringBuilder();
        rootContext.appendDesc(sb);

        assertEquals("/root", sb.toString());
    }

    @Test
    public void testToStringWhenTypeRoot() {
        String result = rootContext.toString();

        assertEquals("/root", result);
    }

    @Test
    public void testToStringWhenTypeArray() {
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        arrayContext.setCurrentIndex(0);

        String result = arrayContext.toString();

        assertEquals("[0]", result);
    }

    @Test
    public void testToStringWhenTypeObject() {
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.setCurrentName("fieldName");

        String result = objectContext.toString();

        assertEquals("{\"fieldName\"}", result);
    }
}

