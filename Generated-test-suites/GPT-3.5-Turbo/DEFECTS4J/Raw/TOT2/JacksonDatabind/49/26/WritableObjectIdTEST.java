package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        
        JsonGenerator gen = null; // mock JsonGenerator
        SerializerProvider provider = null; // mock SerializerProvider
        ObjectIdWriter w = null; // mock ObjectIdWriter
        
        assertFalse(writableObjectId.writeAsId(gen, provider, w));
        
        writableObjectId.generateId(new Object());
        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        
        Object id = writableObjectId.generateId(new Object());
        assertNotNull(id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        
        JsonGenerator gen = null; // mock JsonGenerator
        SerializerProvider provider = null; // mock SerializerProvider
        ObjectIdWriter w = null; // mock ObjectIdWriter
        
        writableObjectId.generateId(new Object());
        writableObjectId.writeAsField(gen, provider, w);

        assertTrue(writableObjectId.idWritten);
    }
}