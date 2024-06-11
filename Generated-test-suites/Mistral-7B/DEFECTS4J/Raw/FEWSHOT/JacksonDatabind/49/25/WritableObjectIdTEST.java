package com.fasterxml.jackson.databind.ser.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {
    private ObjectIdGenerator<Object> idGenerator;
    private ObjectIdWriter idWriter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        idGenerator = mock(IdIdGenerator.class);
        when(idGenerator.generateId(null)).thenReturn("generatedId");

        StdSerializer<Object> idSerializer = mock(StdSerializer.class);
        idWriter = new ObjectIdWriter(idSerializer, null);

        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();
        Object result = writableObjectId.generateId(forPojo);
        assertSame(result, "generatedId");
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsId_IdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = "id";

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(String.valueOf(writableObjectId.id))).thenAnswer(i -> i.getArguments()[0]);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
    }

    @Test
    public void testWriteAsId_NoIdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.serializer).thenReturn(idSerializer);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
    }

    @Test
    public void testWriteAsId_AlwaysAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        when(idGenerator.generateId(null)).thenThrow(new JsonMappingException("Test Error"));

        idWriter.alwaysAsId = true;

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(new SerializableString("name"));

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();
        writableObjectId.id = "id";

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(new SerializableString("name"));

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField_NullId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(new SerializableString("name"));

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertNull(writableObjectId.id);
    }
}