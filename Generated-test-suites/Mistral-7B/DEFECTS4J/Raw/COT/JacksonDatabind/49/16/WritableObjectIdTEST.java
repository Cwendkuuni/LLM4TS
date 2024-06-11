package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGeneratorTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

/**
 * Test class for WritableObjectId.
 */
public class WritableObjectIdTest {

    private ObjectIdGeneratorTest.SimpleGenerator generator;
    private ObjectIdWriter objectIdWriter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;
    private ObjectWriter objectWriter;

    @Before
    public void setUp() throws Exception {
        generator = Mockito.spy(new ObjectIdGeneratorTest.SimpleGenerator());
        objectIdWriter = Mockito.spy(new ObjectIdWriter(null, null));
        jsonGenerator = Mockito.mock(JsonGenerator.class);
        serializerProvider = Mockito.mock(SerializerProvider.class);
        objectMapper = new ObjectMapper();
        javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        objectWriter = objectMapper.writerWithView(MyView.class);

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        Mockito.when(jsonGenerator.writeObjectId(String.valueOf(any()))).thenCallRealMethod();
    }

    @Test
    public void testConstructor() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
        assertEquals(generator, writableObjectId.generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdTrue() throws IOException {
        Object id = "id1";
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = id;
        writableObjectId.idWritten = false;

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).writeObjectRef(String.valueOf(id));

        Mockito.verify(jsonGenerator, times(1)).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsIdFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdFalseWithAlwaysAsId() throws IOException {
        Object id = "id1";
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = id;
        writableObjectId.idWritten = false;

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        Mockito.when(objectIdWriter.alwaysAsId).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(objectIdWriter, times(1)).serializer.serialize(id, jsonGenerator, serializerProvider);

        Mockito.verify(objectIdWriter, times(1)).serializer.serialize(id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testGenerateId() {
        Object id = "id1";
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        Mockito.when(generator.generateId(anyObject())).thenReturn(id);

        Object result = writableObjectId.generateId(new Object());
        assertEquals(id, result);
    }

    @Test
    public void testWriteAsFieldTrue() throws IOException {
        Object id = "id1";
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = id;

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        Mockito.when(jsonGenerator.writeFieldName(anyString())).thenCallRealMethod();

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeFieldName(anyString());
        verify(objectIdWriter, times(1)).serializer.serialize(id, jsonGenerator, serializerProvider);
    }

    @Test(expected = IOException.class)
    public void testWriteAsFieldIOException() throws IOException {
        Object id = "id1";
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = id;

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        Mockito.when(jsonGenerator.writeFieldName(anyString())).thenCallRealMethod();
        Mockito.when(objectIdWriter.serializer).thenThrow(new IOException());

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }

    @Test
    public void testWriteAsFieldNullPropertyName() throws IOException {
        Object id = "id1";
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = id;

        Mockito.when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, null);

        verify(jsonGenerator, never()).writeFieldName(anyString());
    }
}