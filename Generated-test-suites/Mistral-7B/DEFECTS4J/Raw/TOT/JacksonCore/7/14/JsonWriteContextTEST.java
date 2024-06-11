package com.fasterxml.jackson.core.json.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.throwException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.TestTools;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.Types;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        assertNotNull(ctxt);
        assertEquals("", ctxt.toString());
        assertEquals(JsonWriteContext.TYPE_ROOT, ctxt.getType());
        assertNull(ctxt.getParent());
    }

    @Test
    public void testCreateChildContextObject() throws JsonProcessingException {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildObjectContext();

        assertEquals("{\"}", child.toString());
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testCreateChildContextArray() throws JsonProcessingException {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildArrayContext();

        assertEquals("[]", child.toString());
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertEquals(parent, child.getParent());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameNullName() throws JsonProcessingException {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        int result = ctxt.writeFieldName(null);
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameDuplicateName() throws JsonProcessingException {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext ctxt = parent.createChildObjectContext();

        DupDetector dups = TestTools.newMinimalDupDetector();

        int status = ctxt.writeFieldName("test");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);

        // Test "duplicate" name
        status = ctxt.writeFieldName("test");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);

        dups.assertNoDuplicates();
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameInvalidName() throws JsonProcessingException {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        int result = ctxt.writeFieldName("\u0000test");
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, result);
    }

    @Test
    public void testWriteValueNullParentObject() throws JsonProcessingException {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        int result = ctxt.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testWriteValueNullParentArray() throws JsonProcessingException {
        JsonWriteContext ctxt = JsonWriteContext.createRootContext();
        JsonWriteContext arrayContext = ctxt.createChildArrayContext();
        int result = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValueObjectContext() throws JsonProcessingException {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildObjectContext();
        child.writeFieldName("test");
        child.writeValue("testValue");

        assertEquals("{\"test\":\"testValue\"}", child.toString());
    }

    @Test
    public void testWriteValueArrayContext() throws JsonProcessingException {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext arrayContext = parent.createChildArrayContext();
        int result = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

        JsonWriteContext childContext = arrayContext.createChildArrayContext();
        childContext.writeValue("test1");
        childContext.writeValue("test2");

        assertEquals("[\"test1\",\"test2\"]", arrayContext.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueSameKeyObjectContext() throws JsonProcessingException {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = parent.createChildObjectContext();
        objectContext.writeFieldName("test");
        objectContext.writeValue("testValue1");

        int status = objectContext.writeFieldName("test");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);

        try {
            objectContext.writeValue("testValue2");
        } finally {
            assertNull(objectContext.getCurrentValue());
        }

        assertEquals("{\"test\":null}", objectContext.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueSameKeyArrayContext() throws JsonProcessingException {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext arrayContext = parent.createChildArrayContext();

        int index = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, index);

        JsonWriteContext childArrayContext = arrayContext.createChildArrayContext();
        childArrayContext.writeValue("test1");

        int status = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);

        try {
            arrayContext.writeValue("test2");
        } finally {
            assertNull(arrayContext.getCurrentValue());
        }

        assertEquals("[\"test1\",null]", arrayContext.toString());
    }

    @Test(expected = JsonGenerationException.class)
    public void testCheckDupException() throws JsonProcessingException {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext childObjectContext = parent.createChildObjectContext();
        DupDetector dups = TestTools.newMinimalDupDetector();

        childObjectContext.writeFieldName("test");
        childObjectContext.writeValue("testValue");

        childObjectContext._dups = dups;
        childObjectContext.writeFieldName("test");
    }
}