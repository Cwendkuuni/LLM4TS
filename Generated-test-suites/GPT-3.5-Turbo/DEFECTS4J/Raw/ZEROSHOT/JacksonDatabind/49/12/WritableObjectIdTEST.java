package com.fasterxml.jackson.databind.ser.impl;

import java.io.IOException;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        writableObjectId = new WritableObjectId(null);
    }

    @Test
    public void testWriteAsId_WithIdAndIdWritten() throws IOException {
        writableObjectId.id = "testId";
        writableObjectId.idWritten = true;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null);
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithIdAndNotIdWritten() throws IOException {
        writableObjectId.id = "testId";
        writableObjectId.idWritten = false;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null);
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithoutId() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null);
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(result);
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        Object result = writableObjectId.generateId(forPojo);
        assertEquals(forPojo, result);
    }

    @Test
    public void testWriteAsField_WithId() throws IOException {
        writableObjectId.id = "testId";
        writableObjectId.idWritten = true;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null);
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField_WithoutId() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null);
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(writableObjectId.idWritten);
    }
}