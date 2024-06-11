package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {
    @Test
    public void testWritableObjectId() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
        assertEquals(generator.toJson(null), writableObjectId.generateId(null));
    }

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = null; // Initialize json generator
        SerializerProvider provider = null; // Initialize serializer provider
        ObjectIdWriter w = null; // Initialize object id writer
      
        assertFalse(writableObjectId.writeAsId(gen, provider, w));
        writableObjectId.id = "123";
        assertFalse(writableObjectId.writeAsId(gen, provider, w));
        writableObjectId.idWritten = true;
        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNull(writableObjectId.id);
        writableObjectId.generateId(null);
        assertNotNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = null; // Initialize json generator
        SerializerProvider provider = null; // Initialize serializer provider
        ObjectIdWriter w = null; // Initialize object id writer
        
        writableObjectId.writeAsField(gen, provider, w);
        writableObjectId.id = "123";
        writableObjectId.writeAsField(gen, provider, w);
        writableObjectId.idWritten = true;
        writableObjectId.writeAsField(gen, provider, w);
        writableObjectId.id = null;
        writableObjectId.writeAsField(gen, provider, w);
        assertNotNull(writableObjectId.id);
    }
}