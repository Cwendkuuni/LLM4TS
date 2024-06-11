package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> generator;

    @Test
    public void testWriteAsId_WhenIdIsNull_ReturnsFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsFalse_ReturnsFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsTrueAndAlwaysAsIdIsFalse_AndGenCannotWriteObjectId_ReturnsFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsTrueAndAlwaysAsIdIsFalse_AndGenCanWriteObjectId_ReturnsTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        Assert.assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsTrueAndAlwaysAsIdIsTrue_AndGenCannotWriteObjectId_ReturnsTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(w.alwaysAsId).thenReturn(true);
        Mockito.when(gen.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        Assert.assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsTrueAndAlwaysAsIdIsTrue_AndGenCanWriteObjectId_ReturnsTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(w.alwaysAsId).thenReturn(true);
        Mockito.when(gen.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        Assert.assertTrue(result);
    }

    @Test
    public void testGenerateId_ReturnsIdFromGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();
        writableObjectId.generateId(forPojo);
        Mockito.verify(generator, Mockito.times(1)).generateId(forPojo);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsFalse_IdWrittenIsSetToTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.idWritten = false;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);

        writableObjectId.writeAsField(gen, provider, w);

        Assert.assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField_WhenGenCannotWriteObjectId_ObjectIdIsWrittenAsStringAndNameIsNull() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.idWritten = true;
        writableObjectId.id = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsField(gen, provider, w);

        Mockito.verify(gen, Mockito.times(1)).writeFieldName(null);
        Mockito.verify(w.serializer, Mockito.times(1)).serialize(writableObjectId.id, gen, provider);
    }

    @Test
    public void testWriteAsField_WhenGenCannotWriteObjectId_ObjectIdIsWrittenAsStringAndNameIsNotNull() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.idWritten = true;
        writableObjectId.id = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(false);
        Mockito.when(w.propertyName).thenReturn(Mockito.mock(SerializableString.class));

        writableObjectId.writeAsField(gen, provider, w);

        Mockito.verify(gen, Mockito.times(1)).writeFieldName(w.propertyName);
        Mockito.verify(w.serializer, Mockito.times(1)).serialize(writableObjectId.id, gen, provider);
    }

    @Test
    public void testWriteAsField_WhenGenCanWriteObjectId_ObjectIdIsWrittenAsString() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.idWritten = true;
        writableObjectId.id = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, w);

        Mockito.verify(gen, Mockito.times(1)).writeObjectId(String.valueOf(writableObjectId.id));
    }
}