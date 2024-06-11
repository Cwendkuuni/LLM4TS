package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderTestBase;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsIdNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProviderTestBase();
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        assertNull(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdNullId() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProviderTestBase();
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        assertNull(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProviderTestBase();
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.alwaysAsId).thenReturn(false);

        String idValue = "123";
        when(w.serializer).thenReturn(mock(JsonSerializer.class));

        writableObjectId.id = "idValue";

        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertNotNull(result);

        // Verify that writeObjectId method was not called
        assertFalse(gen.wasWritingObjectIdCalled());

        // Verify that writeFieldName and writeString methods were called to write id as field
        SerializableString name = new SerializableString("propertyName");
        when(w.propertyName).thenReturn(name);

        writableObjectId.writeAsId(gen, provider, w);

        gen.flush(); // Flush to ensure that writeFieldName and writeString were called

        verify(gen).writeFieldName(name);
        verify(w.serializer, times(1)).serialize(idValue, eq(gen), eq(provider));
    }

    @Test
    public void testWriteAsIdWithAlwaysAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProviderTestBase();
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.alwaysAsId).thenReturn(true);

        String idValue = "123";
        when(w.serializer).thenReturn(mock(JsonSerializer.class));

        writableObjectId.id = "idValue";

        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertNotNull(result);

        // Verify that writeObjectId method was called
        verify(gen, times(1)).writeObjectId(eq(idValue));
    }

    @Test
    public void testGenerateIdNullForPojo() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        Object forPojo = null;

        Object generatedId = writableObjectId.generateId(forPojo);

        assertNull(generatedId);
    }

    @Test
    public void testGenerateId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        Object forPojo = new Object();
        Object generatedId = writableObjectId.generateId(forPojo);

        assertNotNull(generatedId);
    }

    @Test
    public void testWriteAsFieldNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProviderTestBase();
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        writableObjectId.writeAsField(gen, provider, w);

        assertNull(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProviderTestBase();
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        SerializableString name = new SerializableString("propertyName");
        when(w.propertyName).thenReturn(name);

        writableObjectId.writeAsField(gen, provider, w);

        assertTrue(writableObjectId.idWritten);

        gen.flush(); // Flush to ensure that writeFieldName and writeString were called

        verify(gen).writeFieldName(name);
    }
}