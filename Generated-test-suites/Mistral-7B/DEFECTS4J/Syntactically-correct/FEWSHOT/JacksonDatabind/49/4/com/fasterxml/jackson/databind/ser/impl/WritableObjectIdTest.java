package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectIdGenerator<?> idGenerator;
    private JsonSerializer<Object> idSerializer;
    private ObjectMapper objectMapper;
    private SerializerProvider serializerProvider;
    private TypeSerializer typeSerializer;
    private ObjectIdWriter objectIdWriter;
    private JsonGenerator jsonGenerator;

    @Before
    public void setup() {
        idGenerator = mock(ObjectIdGenerator.class);
        idSerializer = mock(JsonSerializer.class);
        objectMapper = new ObjectMapper();
        serializerProvider = mock(SerializerProvider.class);
        typeSerializer = mock(TypeSerializer.class);
        objectIdWriter = new ObjectIdWriter(null, null, null, null, typeSerializer);
        jsonGenerator = mock(JsonGenerator.class);
    }

    @Test
    public void testWriteAsId() throws IOException {
        when(idSerializer.serialize(any(), eq(jsonGenerator), eq(serializerProvider))).thenReturn(null);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = new Object();
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertNotNull(id);
        assertTrue(result);
    }

    @Test
    public void testWriteAsIdWhenCannotWriteObjectId() throws IOException {
        when(idSerializer.serialize(any(), eq(jsonGenerator), eq(serializerProvider))).thenReturn("idValue");
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = new Object();
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertNotNull(id);
        assertFalse(result);

        // Test serialization of id when writeAsId is called
        when(jsonGenerator.writeFieldName(anyString())).thenAnswer(invocation -> {
            Object name = invocation.getArgument(0);
            jsonGenerator.writeString(name.toString());
            return null;
        });
        when(idSerializer.serialize(id, eq(jsonGenerator), eq(serializerProvider))).thenReturn("serializedIdValue");

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object idToSerialize = new Object();
        writableObjectId.id = idToSerialize;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(2)).writeString(anyString());
        verify(idSerializer, times(1)).serialize(idToSerialize, eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testGenerateId() throws IOException {
        when(idGenerator.generateId(any())).thenReturn("generatedIdValue");

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = writableObjectId.generateId(new Object());
        assertEquals("generatedIdValue", id);
    }

    @Test
    public void testIdWrittenFlag() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(writableObjectId.idWritten);
    }
}