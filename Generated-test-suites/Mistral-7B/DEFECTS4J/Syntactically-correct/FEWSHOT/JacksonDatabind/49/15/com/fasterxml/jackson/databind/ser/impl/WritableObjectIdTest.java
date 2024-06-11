package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> objectIdGenerator;
    private ObjectIdWriter objectIdWriter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        objectIdGenerator = mock(ObjectIdGenerator.class);
        objectIdWriter = mock(ObjectIdWriter.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testWriteAsIdWithIdAndIdWrittenTrue() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        when(objectIdGenerator.generateId(any())).thenReturn(null);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsIdWithIdAndIdWrittenFalse() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = id;

        when(objectIdGenerator.generateId(any())).thenReturn(null);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWithNullId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        when(objectIdGenerator.generateId(any())).thenReturn(null);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWithNoObjectIdSupport() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        when(objectIdGenerator.generateId(any())).thenReturn(null);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        when(objectIdGenerator.generateId(any())).thenReturn(id);

        Object newId = writableObjectId.generateId(new Object());
        assertEquals(id, newId);
    }

    @Test
    public void testWriteAsField() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        SerializableString name = new SerializableString("name");

        when(objectIdGenerator.generateId(any())).thenReturn(null);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator).writeFieldName(eq("name"));
        verify(objectIdWriter.serializer).serialize(eq(id), any(), eq(serializerProvider));
    }

    @Test
    public void testWriteAsFieldWithNullName() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verifyNoMoreInteractions(jsonGenerator);
    }
}