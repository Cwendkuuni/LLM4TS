package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertNull(rootContext.getParent());
        assertEquals("/", rootContext.toString());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return false;
            }
        };
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertNull(rootContext.getParent());
        assertEquals("/", rootContext.toString());
        assertEquals(dupDetector, rootContext.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithGotName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        context.writeFieldName("age"); // should throw an exception
    }

    @Test
    public void testCreateArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, arrayContext.getParent());
        assertEquals("[", arrayContext.toString());
    }

    @Test
    public void testCreateArrayContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return false;
            }
        };
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext arrayContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, arrayContext.getParent());
        assertEquals("[", arrayContext.toString());
        assertEquals(dupDetector, arrayContext.getDupDetector());
    }

    @Test
    public void testCreateObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, objectContext.getParent());
        assertEquals("{", objectContext.toString());
    }

    @Test
    public void testCreateObjectContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return false;
            }
        };
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext objectContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, objectContext.getParent());
        assertEquals("{", objectContext.toString());
        assertEquals(dupDetector, objectContext.getDupDetector());
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        assertTrue(context.isGotName());
        assertEquals("name", context.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteFieldNameWithDupDetector() {
        DupDetector dupDetector = new DupDetector() {
            @Override
            public boolean isDup(String name) {
                return "name".equals(name);
            }
        };
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        context.writeFieldName("name");
        assertFalse(context.isGotName());
        context.writeFieldName("name"); // should throw an exception
    }

    @Test
    public void testWriteValueAsRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue("value");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals("value", context.toString());
    }

    @Test
    public void testWriteValueAsObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildObjectContext();
        context.setCurrentValue("value");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals("{", parentContext.toString());
        assertEquals("value", context.toString());
    }

    @Test
    public void testWriteValueAsArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        context.setCurrentValue("value");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals("[", parentContext.toString());
        assertEquals("value", context.toString());
    }

    @Test
    public void testWriteValueAsArrayContextWithIndex() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        context.setCurrentValue("value");
        context.writeValue();
        context.writeValue();
        context.writeValue();
        assertEquals("[, value, value]", parentContext.toString());
    }
}