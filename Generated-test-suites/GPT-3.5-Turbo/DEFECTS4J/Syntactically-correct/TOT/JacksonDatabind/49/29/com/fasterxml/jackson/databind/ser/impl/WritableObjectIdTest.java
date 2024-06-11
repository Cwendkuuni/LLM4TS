
package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId_IdNotNull_AndWriteIdFalse_ReturnsFalse() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = "mockId";
        writableObjectId.idWritten = false;
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter writer = Mockito.mock(ObjectIdWriter.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);

        // Act
        boolean result = writableObjectId.writeAsId(generator, provider, writer);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_IdNull_ReturnsFalse() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter writer = new ObjectIdWriter();
        writer.alwaysAsId = true;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);

        // Act
        boolean result = writableObjectId.writeAsId(generator, provider, writer);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_IdNotNull_AndIdWrittenTrue_AndAlwaysAsIdFalse_AndCanWriteObjectIdTrue_WritesObjectRef() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = 123;
        writableObjectId.idWritten = true;
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter writer = new ObjectIdWriter();
        writer.alwaysAsId = false;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Mockito.when(generator.canWriteObjectId()).thenReturn(true);

        // Act
        boolean result = writableObjectId.writeAsId(generator, provider, writer);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(generator, Mockito.times(1)).writeObjectRef("123");
        Mockito.verify(provider, Mockito.never()).serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testWriteAsId_IdNotNull_AndIdWrittenTrue_AndAlwaysAsIdFalse_AndCanWriteObjectIdFalse_SerializesId() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = "abc";
        writableObjectId.idWritten = true;
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter writer = new ObjectIdWriter();
        writer.alwaysAsId = false;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Mockito.when(generator.canWriteObjectId()).thenReturn(false);
        Mockito.when(writer.serializer).thenReturn(Mockito.mock(StringSerializer.class));

        // Act
        boolean result = writableObjectId.writeAsId(generator, provider, writer);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(generator, Mockito.never()).writeObjectId(Mockito.anyString());
        Mockito.verify(provider, Mockito.times(1)).serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testWriteAsField_WithNameNotNull_WritesFieldName_AndSerializesId() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.idWritten = false;
        writableObjectId.id = "mockId";
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter writer = new ObjectIdWriter();
        writer.propertyName = Mockito.mock(SerializableString.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Mockito.when(writer.serializer).thenReturn(Mockito.mock(StringSerializer.class));

        // Act
        writableObjectId.writeAsField(generator, provider, writer);

        // Assert
        Mockito.verify(generator, Mockito.times(1)).writeFieldName(writer.propertyName);
        Mockito.verify(provider, Mockito.times(1)).serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testWriteAsField_WithNameNotNull_FieldNameIsSerializedWithWriterSerializer() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.idWritten = false;
        writableObjectId.id = "mockId";
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter writer = new ObjectIdWriter();
        writer.propertyName = Mockito.mock(SerializableString.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Mockito.when(writer.serializer).thenReturn(Mockito.mock(StringSerializer.class));

        // Act
        writableObjectId.writeAsField(generator, provider, writer);

        // Assert
        Mockito.verify(provider, Mockito.times(1)).serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testWriteAsField_WithNameNull_DoesNotWriteFieldName() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.idWritten = false;
        writableObjectId.id = "mockId";
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter writer = new ObjectIdWriter();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Mockito.when(writer.serializer).thenReturn(Mockito.mock(StringSerializer.class));

        // Act
        writableObjectId.writeAsField(generator, provider, writer);

        // Assert
        Mockito.verify(generator, Mockito.never()).writeFieldName(Mockito.anyString());
        Mockito.verify(provider, Mockito.times(1)).serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }
  
    @Test
    public void testGenerateId_CallsGeneratorGenerateId() {
        // Arrange
        ObjectIdGenerator generator = Mockito.mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        // Act
        writableObjectId.generateId(forPojo);

        // Assert
        Mockito.verify(generator, Mockito.times(1)).generateId(forPojo);
    }

    @Test
    public void testGenerateId_UpdatesIdWithGeneratedId() {
        // Arrange
        ObjectIdGenerator generator = Mockito.mock(ObjectIdGenerator.class);
        Mockito.when(generator.generateId(Mockito.any())).thenReturn("mockId");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        // Act
        writableObjectId.generateId(forPojo);

        // Assert
        Assert.assertEquals("mockId", writableObjectId.id);
    }
}


