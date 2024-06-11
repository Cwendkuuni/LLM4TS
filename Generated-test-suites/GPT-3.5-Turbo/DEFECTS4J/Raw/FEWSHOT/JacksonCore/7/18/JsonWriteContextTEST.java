package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Test;

public class JsonWriteContextTest {
    
    @Test
    public void testCreateRootContext() {
        JsonWriteContext root = JsonWriteContext.createRootContext();
        assertNull(root.getParent());
        assertNull(root.getCurrentName());
        assertEquals("", root.toString());
    }

    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        JsonWriteContext root = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, root.writeFieldName("name"));
        assertNull(root.getParent());
        assertEquals("name", root.getCurrentName());
        assertEquals("\"name\":", root.toString());
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, root.writeFieldName("field"));
        assertTrue(root.getDupDetector() instanceof DupDetector);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, root.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, root.writeValue());
        assertEquals(0, root.getCurrentValue());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext root = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, root.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, root.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, root.writeValue());
        root.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, root.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, root.writeValue());
        root.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, root.writeValue());
        assertEquals(2, root.getCurrentValue());
    }

}