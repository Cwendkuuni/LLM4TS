package com.fasterxml.jackson.databind.ser.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdWriter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsIdWithNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithNullObjectId() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithWrittenId() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        Object id = new Object();

        when(w.alwaysAsId).thenReturn(true);
        when(writableObjectId.id).thenReturn(id);
        when(w.serializer).thenReturn(new StdSerializer<Object>(Object.class));

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithGeneratedId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        Object forPojo = new Object();

        when(w.propertyName).thenReturn(new SerializableString("name"));
        when(w.serializer).thenReturn(new StdSerializer<Object>(Object.class));
        when(w.generateId(forPojo)).thenReturn(new Object());

        Object id = writableObjectId.generateId(forPojo);
        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        // Check if id was written to the JsonGenerator
        verify(gen, times(1)).writeObjectId(String.valueOf(id));
        verifyNoMoreInteractions(gen);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithoutObjectIdWriter() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        assertTrue(writableObjectId.writeAsId(gen, provider, null));
    }

    @Test
    public void testWriteAsFieldWithNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        writableObjectId.writeAsField(gen, provider, w);
        verifyNoInteractions(gen);
    }

    @Test
    public void testWriteAsFieldWithNullPropertyName() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        writableObjectId.writeAsField(gen, provider, w);
        verifyNoInteractions(gen);
    }

    @Test
    public void testWriteAsFieldWithNullId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        ObjectIdWriter wWithPropertyName = new ObjectIdWriter() {
            @Override
            public ObjectIdGenerator<?> generator() {
                return mock(ObjectIdGenerator.class);
            }

            @Override
            public String propertyName() {
                return null;
            }
        };

        writableObjectId.writeAsField(gen, provider, wWithPropertyName);
        verifyNoInteractions(gen);
    }

    @Test
    public void testWriteAsFieldWithGeneratedId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        Object forPojo = new Object();

        when(w.propertyName).thenReturn(new SerializableString("name"));
        when(w.serializer).thenReturn(new StdSerializer<Object>(Object.class));
        when(w.generateId(forPojo)).thenReturn(new Object());

        writableObjectId.writeAsField(gen, provider, w);

        // Check if id was written to the JsonGenerator
        verify(gen, times(1)).writeFieldName("name");
        verify(gen, times(1)).writeObjectId(String.valueOf(writableObjectId.id));
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testWriteAsFieldWithoutObjectIdWriter() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writableObjectId.writeAsField(gen, provider, null);
        verifyNoInteractions(gen);
    }
}