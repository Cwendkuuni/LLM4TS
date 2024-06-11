package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter w;

    @Before
    public void setup() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        writableObjectId = new WritableObjectId(generator);
        gen = new JsonGenerator();
        provider = new SerializerProvider();
        w = new ObjectIdWriter();
    }
    
    @Test
    public void testWriteAsId() throws IOException {
        assertFalse(writableObjectId.writeAsId(gen, provider, w));
        
        writableObjectId.id = "123";
        assertFalse(writableObjectId.writeAsId(gen, provider, w));
        
        w.alwaysAsId = true;
        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }
    
    @Test
    public void testGenerateId() {
        Object obj = new Object();
        
        assertNull(writableObjectId.generateId(obj));
        
        Object id = new Object();
        writableObjectId.id = id;
        
        assertEquals(id, writableObjectId.generateId(obj));
    }

    @Test
    public void testWriteAsField() throws IOException {
        writableObjectId.writeAsField(gen, provider, w);
        assertFalse(writableObjectId.idWritten);
        
        writableObjectId.id = "123";
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(writableObjectId.idWritten);
    }
}