package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {

    private DupDetector mockDups;

    @Before
    public void setUp() {
        mockDups = mock(DupDetector.class);
    }

    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, mockDups);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("field"));
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("field1"));
    }

    @Test
    public void testWriteFieldNameWithDuplicate() {
        JsonWriteContext context = JsonWriteContext.createRootContext(mockDups);
        try {
            context.writeFieldName("field");
            context.writeFieldName("field");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'field'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext(mockDups);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext(mockDups);
        JsonWriteContext childArrayContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.type);
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext(mockDups);
        JsonWriteContext childObjectContext = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.type);
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext(mockDups);
        JsonWriteContext childArrayContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, childArrayContext.getParent());
    }

    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext(mockDups);
        context.writeFieldName("field");
        assertEquals("field", context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext(mockDups);
        assertEquals(mockDups, context.getDupDetector());
    }
}