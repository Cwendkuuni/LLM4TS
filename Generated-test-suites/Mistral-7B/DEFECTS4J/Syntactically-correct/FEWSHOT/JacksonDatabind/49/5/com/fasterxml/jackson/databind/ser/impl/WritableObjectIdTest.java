package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ObjectIdWriter;

import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId_IdPresentAndWritten() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        when(idWriter.alwaysAsId).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = new Object();

        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockSerializerProvider.getObjectIdWriter()).thenReturn(idWriter);

        assertTrue(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, idWriter));
        assertEquals(id.toString(), mockJsonGenerator.getOutputBuilder().getQuotedString());
    }

    @Test
    public void testWriteAsId_IdPresentButNotWritten() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        when(idWriter.alwaysAsId).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = new Object();

        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockSerializerProvider.getObjectIdWriter()).thenReturn(idWriter);

        assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, idWriter));
    }

    @Test
    public void testWriteAsId_IdNotPresent() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        when(idWriter.alwaysAsId).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockSerializerProvider.getObjectIdWriter()).thenReturn(idWriter);

        assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, idWriter));
        assertNull(writableObjectId.id);
    }

    @Test
    public void testGenerateId() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();

        Object id = idGenerator.generateId(forPojo);
        assertEquals(id, writableObjectId.id);
    }

    @Test(expected = JsonMappingException.class)
    public void testGenerateId_IdGenerationFails() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        when(idGenerator.generateId(any())).thenThrow(new JsonMappingException("test error"));

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();

        writableObjectId.generateId(forPojo);
    }

    @Test
    public void testWriteAsField() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = new Object();

        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.getWriter();

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);

        SerializableString name = new SerializableString("name");
        when(idWriter.propertyName).thenReturn(name);

        when(mockSerializerProvider.getSerializerProvider()).thenReturn(mockSerializerProvider);
        when(mockSerializerProvider.findValueSerializer(any())).thenReturn(writer.findValueSerializer(id.getClass()));

        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, idWriter);
        assertEquals(name.getValue(), mockJsonGenerator.getCurrentName());
        assertEquals(id.toString(), mockJsonGenerator.getOutputBuilder().getQuotedString());
    }

    @Test
    public void testWriteAsField_NullPropertyName() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = new Object();

        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
        when(mockSerializerProvider.getObjectIdWriter()).thenReturn(idWriter);

        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, idWriter);
        assertNull(mockJsonGenerator.getCurrentName());
        assertNull(mockJsonGenerator.getOutputBuilder().getQuotedString());
    }
}