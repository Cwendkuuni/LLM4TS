package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        WritableObjectId objectId = new WritableObjectId(null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        boolean result = objectId.writeAsId(gen, provider, w);
        assertFalse(result);
    }

    @Test
    public void testGenerateId() {
        WritableObjectId objectId = new WritableObjectId(null);
        Object forPojo = null;
        Object result = objectId.generateId(forPojo);
        assertNull(result);
    }

    @Test
    public void testWriteAsField() {
        WritableObjectId objectId = new WritableObjectId(null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        try {
            objectId.writeAsField(gen, provider, w);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}