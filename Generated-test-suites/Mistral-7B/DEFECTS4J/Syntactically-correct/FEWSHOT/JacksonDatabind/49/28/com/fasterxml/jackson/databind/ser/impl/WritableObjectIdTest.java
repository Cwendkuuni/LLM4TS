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
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.BaseSerializerProvider;

import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdWithNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        assertNull(writableObjectId.generator);
    }

    @Test
    public void testWriteAsIdWithNullJsonGenerator() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        assertFalse(writableObjectId.writeAsId(null, provider, w));
    }

    @Test
    public void testWriteAsIdWithNullId() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithIdWritten() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());
        when(w.alwaysAsId).thenReturn(true);

        Object id = "someId";
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithGeneratorWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        Object id = "someId";
        writableObjectId.id = id;

        when(generator.generateId(writerForObject(provider))).thenReturn(id);
        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithSerializerWriteObject() throws IOException {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        Object id = "someId";
        writableObjectId.id = id;

        SerializableString name = new SerializableString("testProperty");
        when(w.propertyName).thenReturn(name);

        when(w.serializer.serialize(id, eq(gen), eq(provider))).thenReturn(null);

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateIdWithNullForPojo() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateIdWithGeneratedId() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        Object forPojo = new Object();
        Object id = "someId";
        when(generator.generateId(eq(forPojo))).thenReturn(id);

        assertSame(id, writableObjectId.generateId(forPojo));
    }

    @Test
    public void testWriteAsFieldWithNullJsonGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        assertFalse(writableObjectId.writeAsField(null, provider, w));
    }

    @Test
    public void testWriteAsFieldWithNullObjectIdWriter() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        assertFalse(writableObjectId.writeAsField(gen, provider, null));
    }

    @Test
    public void testWriteAsFieldWithIdNotWritten() throws IOException {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        Object id = "someId";
        writableObjectId.id = id;

        SerializableString name = new SerializableString("testProperty");
        when(w.propertyName).thenReturn(name);

        assertFalse(writableObjectId.writeAsField(gen, provider, w));
    }

    @Test
    public void testWriteAsFieldWithWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        Object id = "someId";
        writableObjectId.id = id;

        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsField(gen, provider, w));
    }

    @Test
    public void testWriteAsFieldWithWriteFieldNameAndSerializerWriteObject() throws IOException {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializers()).thenReturn(new BaseSerializerProvider());

        Object id = "someId";
        writableObjectId.id = id;

        SerializableString name = new SerializableString("testProperty");
        when(w.propertyName).thenReturn(name);

        when(w.serializer.serialize(id, eq(gen), eq(provider))).thenReturn(null);

        assertTrue(writableObjectId.writeAsField(gen, provider, w));
    }

    private ObjectIdWriter writerForObject(SerializerProvider provider) {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.alwaysAsId).thenReturn(false);
        when(w.serializer).thenReturn(mock(ObjectSerializer.class));
        when(provider.findValueSerializer(eq(String.class), any())).thenReturn(w);
        return w;
    }
}