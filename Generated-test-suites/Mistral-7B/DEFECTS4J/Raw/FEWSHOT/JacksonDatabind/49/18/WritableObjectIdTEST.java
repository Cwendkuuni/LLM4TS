package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ObjectIdWriter;

import org.junit.Test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test cases for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        assertNull(writableObjectId.id);
        assertTrue(writableObjectId.idWritten);
        assertEquals(generatorMock, writableObjectId.generator);
    }

    @Test
    public void testWriteAsIdFalse() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        when(generatorMock.generateId(Mockito.any())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "generatedId";
            }
        });
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider provider = objectMapper.getSerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        assertFalse(writableObjectId.writeAsId(gen, provider, objectIdWriter));
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsIdTrue() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        when(generatorMock.generateId(Mockito.any())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "generatedId";
            }
        });
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider provider = objectMapper.getSerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null) {
            @Override
            public boolean alwaysAsId() {
                return true;
            }
        };
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(true);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        writableObjectId.id = "expectedId";
        Object idToWrite = "expectedId";
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        when(provider.findValueSerializer(Mockito.eq(Object.class), Mockito.eq(typeSerializer))).thenReturn(null);
        when(objectMapper.findValueSerializer(Mockito.eq(Object.class))).thenReturn(null);
        when(objectIdWriter.serializer).thenReturn(mock(JsonSerializer.class));
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("propertyName"));
        WritableObjectId testWritableObjectId = new WritableObjectId(generatorMock);
        testWritableObjectId.id = idToWrite;
        assertTrue(testWritableObjectId.writeAsId(gen, provider, objectIdWriter));
        assertEquals("generatedId", writableObjectId.id);
        assertEquals("propertyName", testWritableObjectId.idWritten);
        // Check if ObjectIdWriter.serializer was called
        ObjectIdWriter testObjectIdWriter = objectIdWriter;
        testObjectIdWriter.serializer.serialize(idToWrite, gen, provider);
        assertNotSame(testWritableObjectId, testObjectIdWriter);
        assertEquals("expectedId", testObjectIdWriter.id);
        // Check if JsonSerializer was not called
        JsonSerializer jsonSerializer = mock(JsonSerializer.class);
        when(objectIdWriter.serializer).thenReturn(jsonSerializer);
        testWritableObjectId.writeAsId(gen, provider, objectIdWriter);
        verify(jsonSerializer, Mockito.never()).serialize(Mockito.anyObject(), Mockito.any(JsonGenerator.class), Mockito.any(SerializerProvider.class));
    }

    @Test
    public void testGenerateId() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider provider = objectMapper.getSerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null);
        Object forPojo = new Object() {
            public String toString() {
                return "forPojo";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        assertEquals("generatedId", writableObjectId.generateId(forPojo));
        when(generatorMock.generateId(forPojo)).thenReturn("generatedId");
        Object generatedId = writableObjectId.generateId(forPojo);
        assertEquals("generatedId", generatedId);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldFalse() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider provider = objectMapper.getSerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        assertFalse(writableObjectId.writeAsField(gen, provider, objectIdWriter));
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldTrue() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider provider = objectMapper.getSerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null) {
            @Override
            public boolean alwaysAsId() {
                return false;
            }
        };
        JsonGenerator gen = mock(JsonGenerator.class);
        Object forPojo = new Object() {
            public String toString() {
                return "forPojo";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        when(generatorMock.generateId(forPojo)).thenReturn("generatedId");
        when(objectMapper.findValueSerializer(Mockito.eq(Object.class))).thenReturn(null);
        when(objectIdWriter.serializer).thenReturn(mock(JsonSerializer.class));
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("propertyName"));
        WritableObjectId testWritableObjectId = new WritableObjectId(generatorMock);
        testWritableObjectId.generateId(forPojo);
        Object idToWrite = "expectedId";
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        when(provider.findValueSerializer(Mockito.eq(Object.class), Mockito.eq(typeSerializer))).thenReturn(null);
        when(objectMapper.findValueSerializer(Mockito.eq(Object.class))).thenReturn(null);
        when(objectIdWriter.serializer).thenReturn(mock(JsonSerializer.class));
        when(testWritableObjectId.id).thenReturn(idToWrite);
        when(testWritableObjectId.generator).thenReturn(generatorMock);
        testWritableObjectId.writeAsField(gen, provider, objectIdWriter);
        verify(testWritableObjectId, Mockito.times(1)).generateId(forPojo);
        verify(gen, Mockito.times(1)).writeFieldName(Mockito.eq("propertyName"));
        verify(gen, Mockito.times(1)).writeObjectId(Mockito.eq("generatedId"));
        verify(objectIdWriter, Mockito.times(1)).serializer.serialize(Mockito.anyObject(), Mockito.any(JsonGenerator.class), Mockito.any(SerializerProvider.class));
    }
}