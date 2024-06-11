package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for WritableObjectId class
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter idWriter;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        idGenerator = mock(ObjectIdGenerator.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        idWriter = new ObjectIdWriter(null, null, null);
        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testWriteAsIdFalse() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.alwaysAsId).thenReturn(false);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
    }

    @Test
    public void testWriteAsIdTrueWithGeneratedId() throws IOException {
        Object testObject = new Object();
        idGenerator.generateId(testObject);

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(idWriter.alwaysAsId).thenReturn(false);

        SerializableString name = mock(SerializableString.class);
        when(idWriter.propertyName).thenReturn(name);

        writableObjectId.id = testObject;

        // test write as id when id is generated and not written before
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

        when(idWriter.serializer).thenReturn(mock(JsonSerializer.class));
        idWriter.serializer.serialize(testObject, jsonGenerator, serializerProvider);

        // check that idWritten flag is set
        assertSame(writableObjectId, writableObjectId);
        assertSame(writableObjectId.idWritten, true);
    }

    @Test
    public void testWriteAsIdTrueWithAlwaysAsId() throws IOException {
        Object testObject = new Object();

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.alwaysAsId).thenReturn(true);

        SerializableString name = mock(SerializableString.class);
        when(idWriter.propertyName).thenReturn(name);

        writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

        when(idWriter.serializer).thenReturn(mock(JsonSerializer.class));
        idWriter.serializer.serialize(testObject, jsonGenerator, serializerProvider);

        // check that idWritten flag is set
        assertSame(writableObjectId, writableObjectId);
        assertSame(writableObjectId.idWritten, true);
    }

    @Test
    public void testGenerateId() throws IOException {
        Object testObject = new Object();
        writableObjectId.generateId(testObject);

        assertNotNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        Object testObject = new Object();
        idGenerator.generateId(testObject);

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        SerializableString name = mock(SerializableString.class);
        when(idWriter.propertyName).thenReturn(name);

        writableObjectId.id = testObject;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);

        when(idWriter.serializer).thenReturn(mock(JsonSerializer.class));
        idWriter.serializer.serialize(testObject, jsonGenerator, serializerProvider);

        // check that idWritten flag is set
        assertSame(writableObjectId, writableObjectId);
        assertSame(writableObjectId.idWritten, true);
    }

    @Test
    public void testWriteAsFieldNullObjectId() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        SerializableString name = mock(SerializableString.class);
        when(idWriter.propertyName).thenReturn(name);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);

        // check that nothing is written to JsonGenerator
        // and idWritten flag is still false
        assertNull(jsonGenerator.getOutputBuffer());
        assertSame(writableObjectId.idWritten, false);
    }
}