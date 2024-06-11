package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;
    private ObjectIdGenerator objectIdGenerator;
    private Object forPojo;

    @Before
    public void setUp() {
        objectIdGenerator = mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(objectIdGenerator);
        forPojo = new Object();
    }

    @Test
    public void testWriteAsId_WhenIdIsNull_ShouldReturnFalse() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsTrue_ShouldReturnTrue() throws Exception {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndAlwaysAsIdIsTrue_ShouldReturnTrue() throws Exception {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenGenCanWriteObjectId_ShouldCallWriteObjectRef() throws Exception {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        gen.canWriteObjectId();
        writableObjectId.writeAsId(gen, provider, writer);

        verify(gen).writeObjectRef(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsId_WhenGenCannotWriteObjectId_ShouldCallSerializerSerialize() throws Exception {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        !gen.canWriteObjectId();
        writableObjectId.writeAsId(gen, provider, writer);

        verify(writer.serializer).serialize(writableObjectId.id, gen, provider);
    }

    @Test
    public void testGenerateId_ShouldCallGeneratorGenerateIdAndReturnId() {
        writableObjectId.generateId(forPojo);

        verify(objectIdGenerator).generateId(forPojo);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsFalseAndGenCanWriteObjectId_ShouldCallWriteObjectId() throws Exception {
        writableObjectId.idWritten = false;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        gen.canWriteObjectId();
        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen).writeObjectId(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsTrueAndGenCanWriteObjectId_ShouldNotCallWriteObjectId() throws Exception {
        writableObjectId.idWritten = true;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        gen.canWriteObjectId();
        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen, never()).writeObjectId(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsField_WhenNameIsNotNull_ShouldCallWriteFieldName() throws Exception {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        writer.propertyName = mock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen).writeFieldName(writer.propertyName);
    }

    @Test
    public void testWriteAsField_WhenNameIsNull_ShouldNotCallWriteFieldName() throws Exception {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        writer.propertyName = null;

        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen, never()).writeFieldName(writer.propertyName);
    }

    @Test
    public void testWriteAsField_WhenNameIsNotNull_ShouldCallSerializerSerialize() throws Exception {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        writer.propertyName = mock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, writer);

        verify(writer.serializer).serialize(writableObjectId.id, gen, provider);
    }
}