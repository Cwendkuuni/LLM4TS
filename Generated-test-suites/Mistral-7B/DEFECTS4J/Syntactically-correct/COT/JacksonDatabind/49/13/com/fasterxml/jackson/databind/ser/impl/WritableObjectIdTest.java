package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Test class for {@link WritableObjectId}
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<?> idGenerator;
    private WritableObjectId writableObjectId;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;
    private TypeSerializer typeSerializer;

    @Before
    public void setup() {
        idGenerator = mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(idGenerator);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = mock(ObjectIdWriter.class);
        typeSerializer = mock(TypeSerializer.class);
    }

    @Test
    public void testWritableObjectIdConstructor() {
        assertNotNull(writableObjectId);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsId() throws IOException {
        when(idGenerator.generateId(null)).thenReturn(new Object());
        when(idGenerator.canGenerateId(null)).thenReturn(true);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        writableObjectId.id = new Object();
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.serializer).thenReturn(mock(Serializer.class));
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("name"));

        writableObjectId.id = null;
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.alwaysAsId).thenReturn(true);
        writableObjectId.id = new Object();
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("name"));
        writableObjectId.id = null;
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
    }

    @Test
    public void testGenerateId() {
        when(idGenerator.generateId(null)).thenReturn(new Object());
        when(idGenerator.generateId(null)).thenReturn(new Object());

        assertNull(writableObjectId.id);
        writableObjectId.generateId(null);
        assertNotNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(idGenerator.generateId(null)).thenReturn(new Object());
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.generateId(null);
        assertNull(writableObjectId.idWritten);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertNotNull(writableObjectId.idWritten);
    }

    @Test(expected = IOException.class)
    public void testWriteAsFieldWithNullJsonGenerator() throws IOException {
        writableObjectId.generateId(null);
        writableObjectId.writeAsField(null, serializerProvider, objectIdWriter);
    }

    @Test(expected = IOException.class)
    public void testWriteAsFieldWithNullSerializerProvider() throws IOException {
        writableObjectId.generateId(null);
        writableObjectId.writeAsField(jsonGenerator, null, objectIdWriter);
    }

    @Test(expected = IOException.class)
    public void testWriteAsFieldWithNullObjectIdWriter() throws IOException {
        writableObjectId.generateId(null);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, null);
    }
}