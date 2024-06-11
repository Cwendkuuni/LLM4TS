package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriteContextTest {

    private JsonWriteContext context;

    @BeforeEach
    public void setup() {
        context = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        int status = context.writeFieldName("testField");
        Assertions.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        Assertions.assertEquals("testField", context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameWithSameName() {
        context.writeFieldName("testField1");
        Assertions.assertThrows(JsonProcessingException.class, () -> {
            context.writeFieldName("testField1");
        });
    }

    @Test
    public void testWriteValueForObject() {
        int status = context.writeValue();
        Assertions.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testWriteValueForArray() {
        context.createChildArrayContext();
        int status = context.writeValue();
        Assertions.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteValueForRoot() {
        int status = context.writeValue();
        Assertions.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childContext = context.createChildArrayContext();
        Assertions.assertNotNull(childContext);
        Assertions.assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        Assertions.assertEquals(context, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childContext = context.createChildObjectContext();
        Assertions.assertNotNull(childContext);
        Assertions.assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        Assertions.assertEquals(context, childContext.getParent());
    }

    @Test
    public void testGetCurrentValue() {
        Object value = new Object();
        context.setCurrentValue(value);
        Assertions.assertEquals(value, context.getCurrentValue());
    }

    @Test
    public void testReset() {
        context.writeFieldName("testField");
        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_OBJECT);
        Assertions.assertEquals(JsonWriteContext.TYPE_OBJECT, resetContext.getType());
        Assertions.assertEquals(-1, resetContext.getIndex());
        Assertions.assertNull(resetContext.getCurrentName());
        Assertions.assertFalse(resetContext.hasGotName());
        Assertions.assertNull(resetContext.getCurrentValue());
    }

    @Test
    public void testWithDupDetector() {
        DupDetector dups = new DupDetector(null);
        JsonWriteContext contextWithDup = context.withDupDetector(dups);
        Assertions.assertEquals(dups, contextWithDup.getDupDetector());
        Assertions.assertNotEquals(context, contextWithDup);
    }

    @Test
    public void testAppendDescWithTypeObject() {
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        Assertions.assertEquals("{?}", sb.toString());
    }

    @Test
    public void testAppendDescWithTypeArray() {
        JsonWriteContext childContext = context.createChildArrayContext();
        StringBuilder sb = new StringBuilder();
        childContext.appendDesc(sb);
        Assertions.assertEquals("[0]", sb.toString());
    }

    @Test
    public void testAppendDescWithTypeRoot() {
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        Assertions.assertEquals("/", sb.toString());
    }

    @Test
    public void testToStringWithTypeObject() {
        String expected = "{?}";
        Assertions.assertEquals(expected, context.toString());
    }

    @Test
    public void testToStringWithTypeArray() {
        JsonWriteContext childContext = context.createChildArrayContext();
        String expected = "[0]";
        Assertions.assertEquals(expected, childContext.toString());
    }

    @Test
    public void testToStringWithTypeRoot() {
        String expected = "/";
        Assertions.assertEquals(expected, context.toString());
    }
}