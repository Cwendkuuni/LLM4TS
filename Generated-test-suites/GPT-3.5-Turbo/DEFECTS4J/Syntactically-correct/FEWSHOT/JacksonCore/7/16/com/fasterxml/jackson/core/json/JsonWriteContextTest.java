package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext jsonWriteContext;

    @Before
    public void setUp() {
        jsonWriteContext = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeFieldName("field1"));
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeFieldName("field2"));
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, jsonWriteContext.writeFieldName("field2"));
    }

    @Test
    public void testWriteValue() {
        jsonWriteContext = jsonWriteContext.createChildObjectContext();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonWriteContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeValue());

        jsonWriteContext = jsonWriteContext.createChildArrayContext();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = jsonWriteContext.createChildArrayContext();

        assertNotNull(childArrayContext.getParent());
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertNull(childArrayContext.getCurrentName());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = jsonWriteContext.createChildObjectContext();

        assertNotNull(childObjectContext.getParent());
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertNull(childObjectContext.getCurrentName());
    }

    @Test
    public void testReset() {
        jsonWriteContext.writeValue();
        jsonWriteContext.writeFieldName("field1");
        jsonWriteContext.writeValue();
        jsonWriteContext.writeFieldName("field2");
        jsonWriteContext.writeValue();

        jsonWriteContext.reset(JsonWriteContext.TYPE_ARRAY);

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeValue());
        assertNull(jsonWriteContext.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        assertNull(jsonWriteContext.getDupDetector());
    }

    @Test
    public void testAppendDesc() {
        StringBuilder sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);
        assertEquals("/", sb.toString());

        jsonWriteContext.writeValue();
        sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);
        assertEquals("/0", sb.toString());

        jsonWriteContext = jsonWriteContext.createChildObjectContext();
        sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);
        assertEquals("/{?}", sb.toString());

        jsonWriteContext = jsonWriteContext.createChildArrayContext();
        sb = new StringBuilder();
        jsonWriteContext.appendDesc(sb);
        assertEquals("/{}[0]", sb.toString());
    }
}