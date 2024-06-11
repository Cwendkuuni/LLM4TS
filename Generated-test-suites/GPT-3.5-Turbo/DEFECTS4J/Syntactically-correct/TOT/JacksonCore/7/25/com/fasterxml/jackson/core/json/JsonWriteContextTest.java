package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext context;

    @Before
    public void setUp() {
        context = JsonWriteContext.createRootContext();
    }

    @Test
    public void testGetCurrentValue() {
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        Object value = new Object();
        context.setCurrentValue(value);
        assertEquals(value, context.getCurrentValue());
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertNull(rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getDupDetector());
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.TYPE_ROOT);
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = context.createChildArrayContext();
        assertEquals(context, childArrayContext.getParent());
        assertNull(childArrayContext.getCurrentName());
        assertNotNull(childArrayContext.getDupDetector());
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.TYPE_ARRAY);
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = context.createChildObjectContext();
        assertEquals(context, childObjectContext.getParent());
        assertNull(childObjectContext.getCurrentName());
        assertNotNull(childObjectContext.getDupDetector());
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.TYPE_OBJECT);
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteFieldNameWithDuplicate() {
        context = JsonWriteContext.createRootContext(new DupDetector());
        try {
            context.writeFieldName("name");
            context.writeFieldName("name");
            fail("JsonProcessingException not thrown.");
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObject() {
        context = context.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValueInArray() {
        context = context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValueInRoot() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }

    @Test
    public void testToString() {
        assertEquals("/", context.toString());
        context = context.createChildArrayContext();
        assertEquals("/[0]", context.toString());
    }

}