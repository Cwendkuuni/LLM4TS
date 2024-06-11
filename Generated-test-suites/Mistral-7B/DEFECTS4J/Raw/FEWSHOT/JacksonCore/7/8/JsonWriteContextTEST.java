package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext rootContextWithDupDetector = JsonWriteContext.createRootContext(null);

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertEquals(null, rootContext.getParent());
        assertNull(rootContext.getDupDetector());

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContextWithDupDetector.getType());
        assertEquals(null, rootContextWithDupDetector.getParent());
        assertNull(rootContextWithDupDetector.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childArrayContext = parentContext.createChildArrayContext();

        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(parentContext, childArrayContext.getParent());
        assertNull(childArrayContext.getDupDetector());
        assertNull(childArrayContext.getCurrentName());
        assertNull(childArrayContext.getCurrentValue());
        assertFalse(childArrayContext.isGotName());
        assertEquals(-1, childArrayContext.getIndex());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childObjectContext = parentContext.createChildObjectContext();

        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(parentContext, childObjectContext.getParent());
        assertNull(childObjectContext.getDupDetector());
        assertNull(childObjectContext.getCurrentName());
        assertNull(childObjectContext.getCurrentValue());
        assertFalse(childObjectContext.isGotName());
        assertEquals(-1, childObjectContext.getIndex());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        int status = context.writeFieldName("name1");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertEquals("name1", context.getCurrentName());

        status = context.writeFieldName("name2");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);
        assertNull(context.getCurrentName());

        context.setCurrentValue("value1");

        status = context.writeFieldName("name1");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);
        assertNull(context.getCurrentName());

        status = context.writeFieldName("name2");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals("name2", context.getCurrentName());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return name.equals("name1");
            }
        };

        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);

        context.writeFieldName("name1");
        context.writeFieldName("name1");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);

        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);

        context.setCurrentIndex(0);

        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);

        context.reset(JsonWriteContext.TYPE_OBJECT);

        context.writeFieldName("name1");
        context.setCurrentValue("value1");
        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);

        assertEquals("/", context.toString());

        context.setCurrentIndex(0);

        assertEquals("[0]", context.toString());

        context.reset(JsonWriteContext.TYPE_OBJECT);
        context.writeFieldName("name1");
        context.setCurrentValue("value1");

        assertEquals("{\"name1\"}", context.toString());
    }
}