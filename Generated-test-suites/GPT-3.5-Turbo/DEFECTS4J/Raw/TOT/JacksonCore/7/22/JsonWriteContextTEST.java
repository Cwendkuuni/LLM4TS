package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext context;

    @Before
    public void setUp() {
        context = JsonWriteContext.createRootContext();
    }

    /**
     * Test method for {@link JsonWriteContext#writeFieldName(java.lang.String)}.
     */
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("field1"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("field2"));

        // Writing field name without a value
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("field3"));
    }

    /**
     * Test method for {@link JsonWriteContext#writeValue()}.
     */
    @Test
    public void testWriteValue() {
        // Test for writing value in an object context
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());

        // Test for writing value in an array context
        context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());

        // Test for writing value in the root context
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }

    /**
     * Test method for {@link JsonWriteContext#getParent()}.
     */
    @Test
    public void testGetParent() {
        assertNull(context.getParent());

        JsonWriteContext childContext = context.createChildObjectContext();
        assertEquals(context, childContext.getParent());
    }

    /**
     * Test method for {@link JsonWriteContext#getCurrentName()}.
     */
    @Test
    public void testGetCurrentName() {
        assertNull(context.getCurrentName());

        context.writeFieldName("field1");
        assertEquals("field1", context.getCurrentName());
    }

    /**
     * Test method for {@link JsonWriteContext#getDupDetector()}.
     */
    @Test
    public void testGetDupDetector() {
        assertNull(context.getDupDetector());

        context.withDupDetector(new DupDetector());
        assertNotNull(context.getDupDetector());
    }

    /**
     * Test method for {@link JsonWriteContext#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("/null", context.toString());

        context.writeFieldName("field1");
        assertEquals("/{field1:null}", context.toString());

        context.createChildArrayContext().writeValue();
        assertEquals("/{field1:null}/[0]", context.toString());
    }

}