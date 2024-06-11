package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DupDetector;

import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {

    private JsonWriteContext context;
    private DupDetector dupDetector;

    @Before
    public void setup() {
        dupDetector = new DupDetector();
        context = JsonWriteContext.createRootContext(dupDetector);
    }

    @Test
    public void testCreateRootContext() {
        assertEquals("", context.toString());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext arrayContext = context.createChildArrayContext();
        assertEquals("[", arrayContext.toString());
        assertNull(arrayContext.getCurrentName());
        assertNull(arrayContext.getCurrentValue());
        assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
        assertEquals(context, arrayContext.getParent());
        assertSame(dupDetector, arrayContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext objectContext = context.createChildObjectContext();
        assertEquals("{\\d}", objectContext.toString());
        assertNull(objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());
        assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
        assertEquals(context, objectContext.getParent());
        assertSame(dupDetector, objectContext.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        context.writeFieldName("field1");
        assertEquals("{\"field1\":}", context.toString());
        assertTrue(context._gotName);

        context.reset();
        context.writeFieldName("field2");
        assertEquals("{\"field2\":}", context.toString());

        context.reset();
        context.writeFieldName("field1");
        assertEquals("{\"field1\":}", context.toString());
        assertEquals("field1", context._currentName);
        assertEquals("field1", context.getCurrentName());

        context.reset();
        context.writeFieldName(null);
        assertEquals("{}", context.toString());
        assertNull(context._currentName);
        assertNull(context.getCurrentName());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
        dupDetector.addDup("field1");
        context.writeFieldName("field1");
    }

    @Test
    public void testWriteValue() {
        context.setCurrentValue("value1");
        assertEquals("{\"field1\":\"value1\"}", context.toString());
        assertEquals("value1", context.getCurrentValue());

        context.reset();
        context.writeValue();
        assertEquals("{\"field1\":}", context.toString());
        assertNull(context.getCurrentValue());

        context.reset();
        context.writeValue();
        assertEquals("{\"field1\":}", context.toString());
    }
}