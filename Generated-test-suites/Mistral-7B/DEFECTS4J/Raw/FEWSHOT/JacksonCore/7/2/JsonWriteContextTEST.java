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
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertNull(rootContext.getDupDetector());

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContextWithDupDetector.getType());
        assertEquals(null, rootContextWithDupDetector.getParent());
        assertNull(rootContextWithDupDetector.getCurrentName());
        assertNull(rootContextWithDupDetector.getCurrentValue());
        assertNull(rootContextWithDupDetector.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();

        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(parentContext, arrayContext.getParent());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertNull(arrayContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();

        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(parentContext, objectContext.getParent());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertNull(objectContext.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        int result = context.writeFieldName("name1");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals("name1", context.getCurrentName());

        result = context.writeFieldName("name2");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals(null, context.getCurrentName());

        context.setCurrentValue("value1");
        result = context.writeFieldName("name1"); // previous name
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals("name1", context.getCurrentName());
        assertEquals("value1", context.getCurrentValue());
    }

    @Test(expected = JsonGenerationException.class)
    public void testDupDetector() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector() {
            private final String[] names = {"name1", "name2", "name1"};

            @Override
            public boolean isDup(String name) {
                for (String nameInArray : names) {
                    if (name.equals(nameInArray)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void reset() {
                // do nothing
            }

            @Override
            public DupDetector child() {
                return this;
            }
        };

        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("name1");
        context.writeFieldName("name2");
        context.writeFieldName("name1");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);

        context.writeFieldName("name1");
        context.setCurrentValue("value1");
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        context.writeFieldName("name2");
        context.setCurrentValue("value2");
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name1");
        context.setCurrentValue("value1");

        assertEquals("{name1}", context.toString());
    }
}