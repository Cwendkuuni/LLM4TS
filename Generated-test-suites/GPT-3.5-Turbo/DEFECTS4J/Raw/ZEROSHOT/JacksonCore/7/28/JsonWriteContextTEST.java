package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {

    private JsonWriteContext jsonContext;

    @Before
    public void setup() {
        jsonContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonContext.writeFieldName("testField"));
    }

    @Test
    public void testWriteValueForTypeObject() {
        JsonWriteContext objectContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());
    }

    @Test
    public void testWriteValueForTypeArray() {
        JsonWriteContext arrayContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
    }

    @Test
    public void testWriteValueForTypeRoot() {
        JsonWriteContext rootContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = jsonContext.createChildArrayContext();
        Assert.assertNotNull(childArrayContext);
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, childArrayContext.writeValue());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = jsonContext.createChildObjectContext();
        Assert.assertNotNull(childObjectContext);
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, childObjectContext.writeValue());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = jsonContext.getParent();
        Assert.assertEquals(null, parentContext);
    }

    @Test
    public void testGetCurrentName() {
        Assert.assertEquals(null, jsonContext.getCurrentName());
    }
}