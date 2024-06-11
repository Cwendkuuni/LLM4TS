package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

/**
 * Test class for WritableObjectId.
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setup() {
        idGenerator = Mockito.mock(ObjectIdGenerator.class);
        objectMapper = new ObjectMapper();
        jsonGenerator = Mockito.mock(JsonGenerator.class);
        serializerProvider = Mockito.mock(SerializerProvider.class);
        objectIdWriter = new ObjectIdWriter(new IdIntGenerator(), new TypeSerializer(), SerializableString.class, null);
    }

    @Test
    public void testConstructor() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertNotNull(writableObjectId);
        assertEquals(idGenerator, writableObjectId.generator);
    }

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = "testId";

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        Mockito.when(jsonGenerator.writeObjectRef(Mockito.anyString())).thenReturn(null);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        assertEquals(id, writableObjectId.id);

        Mockito.verify(jsonGenerator).writeObjectRef(Mockito.eq(String.valueOf(id)));

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        Mockito.when(objectIdWriter.serializer).thenReturn(Mockito.mock(JsonSerializer.class));

        Mockito.doAnswer(invocation -> {
            Object serializableId = invocation.getArguments()[0];
            writableObjectId.id = serializableId;
            return null;
        }).when(objectIdWriter).serialize(Mockito.any(), Mockito.eq(jsonGenerator), Mockito.eq(serializerProvider));

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        assertEquals(id, writableObjectId.id);

        Mockito.verify(objectIdWriter).serializer.serialize(Mockito.eq(id), Mockito.eq(jsonGenerator), Mockito.eq(serializerProvider));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object testPojo = new Object();

        Mockito.when(idGenerator.generateId(Mockito.eq(testPojo))).thenReturn("testId");

        Object generatedId = writableObjectId.generateId(testPojo);
        assertEquals("testId", generatedId);
        assertEquals("testId", writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = "testId";

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        Mockito.doAnswer(invocation -> {
            String name = (String) invocation.getArguments()[0];
            writableObjectId.id = id;
            return null;
        }).when(objectIdWriter).propertyName;

        Mockito.when(jsonGenerator.writeObjectId(Mockito.eq(String.valueOf(id)))).thenReturn(null);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        assertTrue(writableObjectId.idWritten);
        assertEquals(id, writableObjectId.id);

        Mockito.verify(jsonGenerator).writeObjectId(Mockito.eq(String.valueOf(id)));

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        Mockito.when(objectIdWriter.propertyName).thenReturn("testName");
        Mockito.when(jsonGenerator.writeFieldName(Mockito.eq("testName"))).thenReturn(null);
        Mockito.when(objectIdWriter.serializer).thenReturn(Mockito.mock(JsonSerializer.class));

        Mockito.doAnswer(invocation -> {
            Object serializableId = invocation.getArguments()[0];
            writableObjectId.id = serializableId;
            return null;
        }).when(objectIdWriter).serialize(Mockito.any(), Mockito.eq(jsonGenerator), Mockito.eq(serializerProvider));

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        assertTrue(writableObjectId.idWritten);
        assertEquals(id, writableObjectId.id);

        Mockito.verify(objectIdWriter).serializer.serialize(Mockito.eq(id), Mockito.eq(jsonGenerator), Mockito.eq(serializerProvider));
        Mockito.verify(jsonGenerator).writeFieldName(Mockito.eq("testName"));
    }

    @Test(expected = IOException.class)
    public void testWriteAsIdIOException() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        Mockito.when(jsonGenerator.canWriteObjectId()).thenThrow(new IOException("Test IOException"));

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test(expected = IOException.class)
    public void testGenerateIdIOException() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        Mockito.when(idGenerator.generateId(Mockito.any())).thenThrow(new IOException("Test IOException"));

        try {
            writableObjectId.generateId(new Object());
        } catch (IOException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        fail("Expected IOException to be thrown");
    }
}