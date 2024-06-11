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
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link WritableObjectId} class.
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<?> idGenerator;
    private ObjectIdWriter idWriter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private TypeSerializer typeSerializer;

    @Before
    public void setUp() {
        idGenerator = mock(ObjectIdGenerator.class);
        idWriter = mock(ObjectIdWriter.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        typeSerializer = mock(TypeSerializer.class);
    }

    @Test
    public void testWriteAsIdWithNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
    }

    @Test
    public void testWriteAsIdWithNullId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsIdWithIdWritten() {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;
        when(idWriter.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
        assertSame(id, writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithIdWrittenAndAlwaysAsId() {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;
        when(idWriter.alwaysAsId).thenReturn(true);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
        assertSame(id, writableObjectId.id);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithoutCanWriteObjectId() {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;
        when(idWriter.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(new SerializableString("name"));

        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializerProvider.findValueSerializer(Object.class, typeSerializer)).thenReturn(serializer);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
        assertSame(id, writableObjectId.id);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        when(generator.generateId(forPojo)).thenReturn(id);

        assertNull(writableObjectId.id);
        writableObjectId.generateId(forPojo);
        assertSame(id, writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithNullId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithIdWritten() {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertTrue(writableObjectId.idWritten);
        assertSame(id, writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithoutCanWriteObjectId() {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(new SerializableString("name"));

        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializerProvider.findValueSerializer(Object.class, typeSerializer)).thenReturn(serializer);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertTrue(writableObjectId.idWritten);
        assertSame(id, writableObjectId.id);
    }
}