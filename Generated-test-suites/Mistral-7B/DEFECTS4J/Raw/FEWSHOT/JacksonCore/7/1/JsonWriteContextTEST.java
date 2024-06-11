package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext rootContextWithDupDetector = JsonWriteContext.createRootContext(null);

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertEquals(null, rootContext.getParent());
        assertNull(rootContext.getDupDetector());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertFalse(rootContext.isGotName());
        assertNull(rootContext.getIndex());

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContextWithDupDetector.getType());
        assertEquals(null, rootContextWithDupDetector.getParent());
        assertNull(rootContextWithDupDetector.getDupDetector());
        assertNull(rootContextWithDupDetector.getCurrentName());
        assertNull(rootContextWithDupDetector.getCurrentValue());
        assertFalse(rootContextWithDupDetector.isGotName());
        assertNull(rootContextWithDupDetector.getIndex());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        JsonWriteContext childArrayContext = parentContext.createChildArrayContext();
        JsonWriteContext childArrayContext2 = parentContext.createChildArrayContext();

        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(parentContext, childArrayContext.getParent());
        assertNull(childArrayContext.getDupDetector());
        assertNull(childArrayContext.getCurrentName());
        assertNull(childArrayContext.getCurrentValue());
        assertFalse(childArrayContext.isGotName());
        assertNull(childArrayContext.getIndex());

        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext2.getType());
        assertEquals(parentContext, childArrayContext2.getParent());
        assertNull(childArrayContext2.getDupDetector());
        assertNull(childArrayContext2.getCurrentName());
        assertNull(childArrayContext2.getCurrentValue());
        assertFalse(childArrayContext2.isGotName());
        assertNull(childArrayContext2.getIndex());

        childArrayContext.setCurrentValue("value1");
        assertEquals("value1", childArrayContext.getCurrentValue());

        childArrayContext2.setCurrentValue("value2");
        assertEquals("value2", childArrayContext2.getCurrentValue());

        JsonWriteContext childArrayContext3 = parentContext.createChildArrayContext();
        assertNull(childArrayContext3.getCurrentValue());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        JsonWriteContext childObjectContext = parentContext.createChildObjectContext();
        JsonWriteContext childObjectContext2 = parentContext.createChildObjectContext();

        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(parentContext, childObjectContext.getParent());
        assertNull(childObjectContext.getDupDetector());
        assertNull(childObjectContext.getCurrentName());
        assertNull(childObjectContext.getCurrentValue());
        assertFalse(childObjectContext.isGotName());
        assertNull(childObjectContext.getIndex());

        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext2.getType());
        assertEquals(parentContext, childObjectContext2.getParent());
        assertNull(childObjectContext2.getDupDetector());
        assertNull(childObjectContext2.getCurrentName());
        assertNull(childObjectContext2.getCurrentValue());
        assertFalse(childObjectContext2.isGotName());
        assertNull(childObjectContext2.getIndex());

        childObjectContext.writeFieldName("field1");
        childObjectContext.setCurrentValue("value1");
        assertEquals("field1", childObjectContext.getCurrentName());
        assertEquals("value1", childObjectContext.getCurrentValue());
        assertTrue(childObjectContext.isGotName());
        assertEquals(0, childObjectContext.getIndex());

        childObjectContext2.writeFieldName("field2");
        childObjectContext2.setCurrentValue("value2");
        assertEquals("field2", childObjectContext2.getCurrentName());
        assertEquals("value2", childObjectContext2.getCurrentValue());
        assertTrue(childObjectContext2.isGotName());
        assertEquals(0, childObjectContext2.getIndex());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameDupDetector() throws JsonProcessingException {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(new DupDetector() {
            private final String[] _names = new String[]{"field1", "field1"};

            @Override
            public boolean isDup(String name) {
                for (String existingName : _names) {
                    if (existingName.equals(name)) {
                        return true;
                    }
                }
                return false;
            }
        });

        rootContext.writeFieldName("field1");
        rootContext.writeFieldName("field1");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getStatus());

        context = context.createChildArrayContext();

        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());

        context = context.createChildObjectContext();

        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());

        context.reset(JsonWriteContext.TYPE_OBJECT);

        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.getStatus());
    }
}