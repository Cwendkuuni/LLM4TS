package com.fasterxml.jackson.databind.ser.impl;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
    }

    @After
    public void tearDown() {
        writableObjectId = null;
    }

    @Test
    public void testWriteAsId() throws IOException {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(result);
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        Object result = writableObjectId.generateId(forPojo);
        assertNotNull(result);
        assertEquals(result, writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(writableObjectId.idWritten);
    }
}