package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for WritableObjectId class
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private ObjectIdWriter idWriter;

    @Before
    public void setup() {
        idGenerator = mock(ObjectIdGenerator.class);
        idWriter = mock(ObjectIdWriter.class);
    }

    @Test
    public void testWriteAsIdNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        assertFalse(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsIdNullId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        assertFalse(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsIdFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        when(idWriter.alwaysAsId).thenReturn(false);
        assertFalse(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsIdTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        when(idWriter.alwaysAsId).thenReturn(true);
        when(idGenerator.generateId(writableObjectId)).thenReturn("idValue");
        when(gen.canWriteObjectId()).thenReturn(true);
        when(gen.writeObjectRef(anyString())).thenAnswer(i -> i.getArgumentAt(0, String.class));
        Object id = writableObjectId.writeAsId(gen, provider, idWriter);
        assertNotNull(id);
        assertEquals("idValue", id);
    }

    @Test
    public void testGenerateIdNullForPojo() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = null;
        Object id = writableObjectId.generateId(forPojo);
        assertNull(id);
    }

    @Test
    public void testGenerateId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();
        Object id = writableObjectId.generateId(forPojo);
        assertNotNull(id);
    }

    @Test
    public void testWriteAsFieldNullGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        writableObjectId.writeAsField(gen, provider, idWriter);
        assertFalse(idWriter.idWritten);
    }

    @Test
    public void testWriteAsFieldNullPropertyName() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        writableObjectId.writeAsField(gen, provider, idWriter);
        assertFalse(idWriter.idWritten);
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        when(idGenerator.generateId(writableObjectId)).thenReturn("idValue");
        when(gen.canWriteObjectId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(new SerializableString("propertyName"));
        writableObjectId.writeAsField(gen, provider, idWriter);
        assertTrue(idWriter.idWritten);
        verify(gen).writeFieldName("propertyName");
        verify(idWriter).serializer.serialize("idValue", gen, provider);
    }
}