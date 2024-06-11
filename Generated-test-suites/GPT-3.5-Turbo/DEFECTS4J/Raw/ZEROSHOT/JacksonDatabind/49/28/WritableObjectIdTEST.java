package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;

import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectIdGenerator<?> generator;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        generator = new ObjectIdGenerator<>();
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_WithIdAndIdWritten_AlwaysAsId_True_GenCanWriteObjectId_True()
            throws IOException {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsId_NullId_IdWritten_AlwaysAsId_True_GenCanWriteObjectId_True()
            throws IOException {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsId_WithId_AndIdNotWritten_OrAlwaysAsId_False_GenCanWriteObjectId_True()
            throws IOException {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        w.alwaysAsId = false;

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsId_WithId_AndIdWritten_OrAlwaysAsId_False_GenCanWriteObjectId_True()
            throws IOException {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        w.alwaysAsId = false;

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsId_WithId_AlwaysAsId_True_GenCanWriteObjectId_False()
            throws IOException {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();

        Object id = writableObjectId.generateId(forPojo);

        assertNotNull(id);
        assertEquals(id, writableObjectId.id);
    }

    @Test
    public void testWriteAsField_GenCanWriteObjectId_True() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.idWritten = true;

        writableObjectId.writeAsField(gen, provider, w);

        assertTrue(gen.canWriteObjectId());
    }

    @Test
    public void testWriteAsField_GenCanWriteObjectId_False_NameNotNull() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.idWritten = true;
        w.propertyName = new SerializableString();

        writableObjectId.writeAsField(gen, provider, w);

        assertFalse(gen.canWriteObjectId());
    }

    @Test
    public void testWriteAsField_GenCanWriteObjectId_False_NameNull() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.idWritten = true;
        w.propertyName = null;

        writableObjectId.writeAsField(gen, provider, w);

        assertFalse(gen.canWriteObjectId());
    }
}