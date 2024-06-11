package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {
    JsonWriteContext context;

    @Before
    public void setUp() {
        context = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName() {
        int result = context.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        Assert.assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameWithDuplicate() {
        JsonWriteContext dupContext = JsonWriteContext.createRootContext().withDupDetector(new DupDetector());
        int result = dupContext.writeFieldName("name");
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        try {
            dupContext.writeFieldName("name");
            Assert.fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            Assert.assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObject() {
        context.writeFieldName("name");
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        Assert.assertNull(context.getCurrentName());
    }

    @Test
    public void testWriteValueInArray() {
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        Assert.assertNull(context.getCurrentName());
    }

    @Test
    public void testWriteValueInRootContext() {
        context.writeValue();
        int result = context.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        Assert.assertNull(context.getCurrentName());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = context.createChildArrayContext();
        Assert.assertNotNull(childArrayContext);
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        Assert.assertEquals(context, childArrayContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = context.createChildObjectContext();
        Assert.assertNotNull(childObjectContext);
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        Assert.assertEquals(context, childObjectContext.getParent());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = context.getParent();
        Assert.assertNull(parentContext);
    }

    @Test
    public void testGetCurrentName() {
        String currentName = context.getCurrentName();
        Assert.assertNull(currentName);
    }

    @Test
    public void testGetDupDetector() {
        DupDetector dupDetector = context.getDupDetector();
        Assert.assertNull(dupDetector);
    }
}