package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdWriter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsIdWithNullGenerator() {
        WritableObjectId id = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        assertFalse(id.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithIdWritten() {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        WritableObjectId id = new WritableObjectId(generator);
        Object idValue = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        when(generator.generateId(idValue)).thenReturn(idValue);
        when(writer.alwaysAsId).thenReturn(true);

        assertTrue(id.writeAsId(gen, provider, writer));
        verify(gen).writeObjectRef(String.valueOf(idValue));
    }

    @Test
    public void testWriteAsIdWithGeneratorIdGenerator() {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        WritableObjectId id = new WritableObjectId(generator);
        Object idValue = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        when(generator.generateId(idValue)).thenReturn(idValue);
        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(id.writeAsId(gen, provider, writer));
        verify(gen).writeObjectRef(String.valueOf(idValue));
    }

    @Test
    public void testWriteAsIdWithSerializerWriter() {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        WritableObjectId id = new WritableObjectId(generator);
        Object idValue = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        when(generator.generateId(idValue)).thenReturn(idValue);
        when(gen.canWriteObjectId()).thenReturn(false);

        SerializableString name = new SerializableString("name");
        when(writer.propertyName).thenReturn(name);

        assertTrue(id.writeAsId(gen, provider, writer));
        verify(gen).writeFieldName(name);
        verify(writer.serializer).serialize(idValue, gen, provider);
    }

    @Test
    public void testGenerateIdWithNullGenerator() {
        WritableObjectId id = new WritableObjectId(null);
        Object idValue = new Object();

        assertNull(id.generateId(idValue));
    }

    @Test
    public void testGenerateIdWithGeneratorIdGenerator() {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        WritableObjectId id = new WritableObjectId(generator);
        Object idValue = new Object();

        when(generator.generateId(idValue)).thenReturn(idValue);

        assertNotNull(id.generateId(idValue));
        assertEquals(idValue, id.id);
    }

    @Test
    public void testWriteAsFieldWithNullGenerator() {
        WritableObjectId id = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        assertFalse(id.writeAsField(gen, provider, writer));
    }

    @Test
    public void testWriteAsFieldWithGeneratorIdWriter() {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        WritableObjectId id = new WritableObjectId(generator);
        Object idValue = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        when(generator.generateId(idValue)).thenReturn(idValue);
        when(gen.canWriteObjectId()).thenReturn(true);

        id.id = idValue;

        id.writeAsField(gen, provider, writer);
        verify(gen).writeObjectId(String.valueOf(idValue));
    }

    @Test
    public void testWriteAsFieldWithSerializerWriter() {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        WritableObjectId id = new WritableObjectId(generator);
        Object idValue = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        when(generator.generateId(idValue)).thenReturn(idValue);
        when(gen.canWriteObjectId()).thenReturn(false);

        SerializableString name = new SerializableString("name");
        when(writer.propertyName).thenReturn(name);
        when(writer.serializer).thenReturn(typeSerializer);

        id.id = idValue;

        id.writeAsField(gen, provider, writer);
        verify(gen).writeFieldName(name);
        verify(writer.serializer).serialize(idValue, gen, provider);
    }
}