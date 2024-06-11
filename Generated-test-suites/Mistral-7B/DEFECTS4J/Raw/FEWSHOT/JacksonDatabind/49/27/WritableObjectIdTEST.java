package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WritableObjectIdTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ObjectIdWriter objectIdWriter;

    @Mock
    private StdSerializer<Object> stdSerializer;

    @InjectMocks
    private WritableObjectId writableObjectId;

    @Mock
    private ObjectIdGenerator<Object> objectIdGenerator;

    @Test
    public void testWriteAsId() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        Object object = new Object();
        Long id = 1L;

        when(objectIdGenerator.generateId(object)).thenReturn(id);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).writeObjectRef(String.valueOf(id));

        when(objectIdWriter.alwaysAsId).thenReturn(true);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId() throws IOException {
        Object object = new Object();
        Object id = writableObjectId.generateId(object);
        assertEquals(id, writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(objectMapper.findSerializableValue("name", stdSerializer)).thenReturn(new SerializableString("name"));

        Object object = new Object();
        Long id = 1L;

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = id;

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeFieldName("name");
        verify(objectIdWriter, times(1)).serializer;
        verify(stdSerializer, times(1)).serialize(id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsField_noName() throws IOException {
        when(objectIdWriter.propertyName).thenReturn(null);

        Object object = new Object();
        Long id = 1L;

        writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = id;

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, never()).writeFieldName(anyString());
    }

    @Test
    public void testWriteAsField_alwaysAsId() throws IOException {
        when(objectMapper.findSerializableValue("name", stdSerializer)).thenReturn(null);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.alwaysAsId).thenReturn(true);

        Object object = new Object();
        Long id = 1L;

        writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = id;

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeObjectId(String.valueOf(id));
    }
}