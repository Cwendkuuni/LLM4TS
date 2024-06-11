package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext context;
    private DupDetector dups;

    @Before
    public void setUp() {
        dups = null;
        context = JsonWriteContext.createRootContext(dups);
    }

    @Test
    public void testGetCurrentValue() {
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        context.setCurrentValue("test");
        assertEquals("test", context.getCurrentValue());
    }

    @Test
    public void testGetParent() {
        assertNull(context.getParent());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWriteFieldName() {
        try {
            context.writeFieldName("name");
        } catch (JsonProcessingException e) {
            fail("Unexpected exception");
        }
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testWriteFieldNameExpectValue() {
        try {
            context.writeFieldName("name");
        } catch (JsonProcessingException e) {
            fail("Unexpected exception");
        }
        try {
            context.writeFieldName("name2");
        } catch (JsonProcessingException e) {
            fail("Unexpected exception");
        }
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name3"));
    }

    @Test
    public void testWriteValueInObject() {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteValueInArray() {
        context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValueInRoot() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childContext = context.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(context, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childContext = context.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(context, childContext.getParent());
    }

    @Test
    public void testToString() {
        assertEquals("/", context.toString());
    }
}