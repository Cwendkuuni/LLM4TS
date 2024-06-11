package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        WritableObjectId objectId = new WritableObjectId(null);
        assertNull(objectId.generateId(null));
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        assertFalse(objectId.writeAsId(gen, provider, w));
        objectId.id = new Object();
        assertFalse(objectId.writeAsId(gen, provider, w));
        objectId.idWritten = true;
        assertFalse(objectId.writeAsId(gen, provider, w));

        provider = new SerializerProvider();
        assertFalse(objectId.writeAsId(gen, provider, w));
        w = new ObjectIdWriter(provider);
        assertFalse(objectId.writeAsId(gen, provider, w));

        w.alwaysAsId = true;
        assertFalse(objectId.writeAsId(gen, provider, w));

        gen = new JsonGenerator();
        assertFalse(objectId.writeAsId(gen, provider, w));
        gen.canWriteObjectId = true;
        assertFalse(objectId.writeAsId(gen, provider, w));

        gen.writeObjectRef("Test");
        assertTrue(objectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId objectId = new WritableObjectId(null);
        assertNull(objectId.generateId(null));

        Object forPojo = new Object();
        Object id = new Object();
        objectId.generator = new ObjectIdGenerator();
        assertNull(objectId.generateId(forPojo));

        objectId.id = id;
        assertEquals(id, objectId.generateId(forPojo));
    }

    @Test
    public void testWriteAsField() {
        WritableObjectId objectId = new WritableObjectId(null);
        assertNull(objectId.generateId(null));

        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = null;

        objectId.writeAsField(gen, provider, w);
        assertFalse(objectId.idWritten);
        assertFalse(gen.canWriteObjectId());

        objectId.id = new Object();
        objectId.writeAsField(gen, provider, w);
        assertTrue(objectId.idWritten);
        assertFalse(gen.canWriteObjectId());

        gen = new JsonGenerator();
        gen.canWriteObjectId = true;
        objectId.writeAsField(gen, provider, w);
        assertTrue(objectId.idWritten);
        assertTrue(gen.canWriteObjectId());
    }
}