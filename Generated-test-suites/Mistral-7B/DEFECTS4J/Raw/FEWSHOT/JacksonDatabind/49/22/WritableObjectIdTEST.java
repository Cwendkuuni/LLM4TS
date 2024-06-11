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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;
    private StdSerializer<Object> objectSerializer;

    @Before
    public void setup() throws Exception {
        idGenerator = mock(ObjectIdGenerator.class);
        objectMapper = new ObjectMapper();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, null);
        objectSerializer = mock(StdSerializer.class);
        when(idGenerator.generateId(any())).thenReturn(new Object());
        when(objectMapper.findValueSerializer(anyClass(), serializerProvider)).thenReturn(objectSerializer);
    }

    @Test
    public void testWriteAsIdWithIdWritten() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.generateId(forPojo);

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(jsonGenerator).writeObjectRef(stringArgumentCaptor.capture());
        assertSame(String.valueOf(writableObjectId.getId()), stringArgumentCaptor.getValue());
    }

    @Test
    public void testWriteAsIdWithIdNotWritten() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        verify(objectSerializer).serialize(writableObjectId.getId(), jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsIdWithNullJsonGenerator() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        writableObjectId.writeAsId(null, serializerProvider, objectIdWriter);

        assertFalse("writeAsId should not be called with null JsonGenerator", writableObjectId.isIdWritten());
    }

    @Test
    public void testWriteAsIdWithNullSerializerProvider() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        writableObjectId.writeAsId(jsonGenerator, null, objectIdWriter);

        assertFalse("writeAsId should not be called with null SerializerProvider", writableObjectId.isIdWritten());
    }

    @Test
    public void testWriteAsIdWithNullObjectIdWriter() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        writableObjectId.writeAsId(jsonGenerator, serializerProvider, null);

        assertFalse("writeAsId should not be called with null ObjectIdWriter", writableObjectId.isIdWritten());
    }

    @Test
    public void testGenerateId() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        Object generatedId = writableObjectId.generateId(forPojo);

        assertSame(writableObjectId.getId(), generatedId);
    }

    @Test
    public void testGenerateIdWithNullForPojo() throws Exception {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        assertNull("generateId should return null for null forPojo", writableObjectId.generateId(null));
    }

    @Test(expected = IOException.class)
    public void testGenerateIdWithIOException() throws Exception {
        ObjectIdGenerator<Object> exceptionGenerator = mock(ObjectIdGenerator.class);
        when(exceptionGenerator.generateId(any())).thenThrow(new IOException());

        WritableObjectId writableObjectId = new WritableObjectId(exceptionGenerator);
        Object forPojo = new Object();

        writableObjectId.generateId(forPojo);
    }

    @Test
    public void testWriteAsField() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        String propertyName = "propertyName";

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(jsonGenerator).writeFieldName(stringArgumentCaptor.capture());
        assertSame(propertyName, stringArgumentCaptor.getValue());
        verify(objectSerializer).serialize(writableObjectId.getId(), jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsFieldWithNoPropertyName() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, null);

        verify(objectSerializer, never()).serialize(writableObjectId.getId(), jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsFieldWithNullJsonGenerator() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        writableObjectId.writeAsField(null, serializerProvider, objectIdWriter);

        assertFalse("writeAsField should not be called with null JsonGenerator", writableObjectId.isIdWritten());
    }

    @Test
    public void testWriteAsFieldWithNullSerializerProvider() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        writableObjectId.writeAsField(jsonGenerator, null, objectIdWriter);

        assertFalse("writeAsField should not be called with null SerializerProvider", writableObjectId.isIdWritten());
    }

    @Test
    public void testWriteAsFieldWithNullObjectIdWriter() throws Exception {
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, null);

        assertFalse("writeAsField should not be called with null ObjectIdWriter", writableObjectId.isIdWritten());
    }
}