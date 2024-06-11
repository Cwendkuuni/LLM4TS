package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectIdGenerator<?> idGenerator;
    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        idGenerator = mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testWriteAsIdWithNullJsonGenerator() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        assertNull(writableObjectId.writeAsId(null, provider, idWriter));
    }

    @Test
    public void testWriteAsIdWithNullIdWriter() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);
        when(idWriter.alwaysAsId).thenReturn(false);

        assertNull(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
    }

    @Test
    public void testWriteAsIdWithNullGenerator() throws IOException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        assertNull(writableObjectId.writeAsId(null, serializerProvider, idWriter));
    }

    @Test
    public void testWriteAsIdWithIdWritten() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        when(idWriter.alwaysAsId).thenReturn(false);

        Object id = "someId";
        when(idGenerator.generateId(AnyPojo.class)).thenReturn(id);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
    }

    @Test
    public void testWriteAsIdWithGeneratedId() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        when(idWriter.alwaysAsId).thenReturn(false);

        Object id = "someId";
        when(idGenerator.generateId(AnyPojo.class)).thenReturn(id);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId = new WritableObjectId(idGenerator);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
        assertEquals(id, writableObjectId.id);
    }

    @Test
    public void testWriteAsIdWithAlwaysAsId() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        when(idWriter.alwaysAsId).thenReturn(true);

        Object id = "someId";
        when(idGenerator.generateId(AnyPojo.class)).thenReturn(id);

        writableObjectId = new WritableObjectId(idGenerator);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
        assertEquals(id, writableObjectId.id);
    }

    @Test
    public void testGenerateIdWithNullPojo() {
        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateId() {
        Object id = "someId";
        when(idGenerator.generateId(AnyPojo.class)).thenReturn(id);

        Object generatedId = writableObjectId.generateId(new AnyPojo());
        assertEquals(id, generatedId);
    }

    @Test
    public void testWriteAsFieldWithNullJsonGenerator() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        assertNull(writableObjectId.writeAsField(null, provider, idWriter));
    }

    @Test
    public void testWriteAsFieldWithNullSerializerProvider() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        assertNull(writableObjectId.writeAsField(jsonGenerator, null, idWriter));
    }

    @Test
    public void testWriteAsField() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = mock(ObjectIdWriter.class);

        SerializableString name = mock(SerializableString.class);
        when(idWriter.propertyName).thenReturn(name);

        Object id = "someId";
        when(idGenerator.generateId(AnyPojo.class)).thenReturn(id);

        writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertNotNull(jsonGenerator.getOutputBuffer());
        assertTrue(jsonGenerator.getOutputBuffer().toString().contains(name.getValue()));
        assertTrue(jsonGenerator.getOutputBuffer().toString().contains(id.toString()));
    }
}

class AnyPojo {
    // Replace this with a real Java Pojo class
}