package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> generator;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private ObjectIdWriter writer;

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "1";
        writableObjectId.idWritten = true;

        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;

        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithIdNullAndAlwaysAsIdFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        writer.alwaysAsId = false;

        assertFalse(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithIdNullAndAlwaysAsIdTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        writer.alwaysAsId = true;

        assertFalse(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenTrueAndCanWriteObjectIdTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "1";
        writableObjectId.idWritten = true;
        gen.canWriteObjectId();

        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();
        Object generatedId = writableObjectId.generateId(forPojo);

        assertEquals(generatedId, writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.idWritten = true;
        gen.canWriteObjectId();

        writableObjectId.writeAsField(gen, provider, writer);

        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalseAndPropertyNameNotNull() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.idWritten = false;
        writer.propertyName = new SerializableString("id");

        writableObjectId.writeAsField(gen, provider, writer);

        assertTrue(writableObjectId.idWritten);
        assertEquals(writer.propertyName, gen.currentName());
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalseAndPropertyNameNull() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.idWritten = false;
        writer.propertyName = null;

        writableObjectId.writeAsField(gen, provider, writer);

        assertTrue(writableObjectId.idWritten);
        assertNull(gen.currentName());
    }
}