package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    private WritableObjectId writableObjId;

    @Before
    public void setUp() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator();
        writableObjId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_ObjectIdNotNullIdWrittenTrue() throws IOException {
        writableObjId.id = new Object();
        writableObjId.idWritten = true;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        boolean result = writableObjId.writeAsId(gen, provider, w);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_ObjectIdNotNullIdWrittenFalse() throws IOException {
        writableObjId.id = new Object();
        writableObjId.idWritten = false;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        boolean result = writableObjId.writeAsId(gen, provider, w);
        assertFalse(result);
    }

    @Test
    public void testWriteAsId_ObjectIdNullIdWrittenTrue() throws IOException {
        writableObjId.id = null;
        writableObjId.idWritten = true;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        boolean result = writableObjId.writeAsId(gen, provider, w);
        assertFalse(result);
    }

    @Test
    public void testWriteAsId_ObjectIdNullIdWrittenFalse() throws IOException {
        writableObjId.id = null;
        writableObjId.idWritten = false;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        boolean result = writableObjId.writeAsId(gen, provider, w);
        assertFalse(result);
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        Object result = writableObjId.generateId(forPojo);
        assertEquals(result, writableObjId.id);
    }

    @Test
    public void testWriteAsField_WithId() throws IOException {
        writableObjId.idWritten = true;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        writableObjId.writeAsField(gen, provider, w);
        assertTrue(gen.canWriteObjectId());
    }

    @Test
    public void testWriteAsField_WithoutId() throws IOException {
        writableObjId.idWritten = false;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        writableObjId.writeAsField(gen, provider, w);
        assertFalse(gen.canWriteObjectId());
    }

}