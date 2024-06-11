package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class WritableObjectIdTest {

    @Mock
    ObjectIdGenerator objectIdGeneratorMock = new ObjectIdGenerator();

    @Mock
    JsonGenerator jsonGeneratorMock;

    @Mock
    SerializerProvider serializerProviderMock;

    @Mock
    ObjectIdWriter objectIdWriterMock;

    @Test
    public void testWriteAsId_WhenIdNotNull_ReturnsTrue() throws Exception {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        writableObjectId.id = "1";
        writableObjectId.idWritten = true;
        writableObjectId.generator = objectIdGeneratorMock;

        Mockito.when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

        // Act
        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(jsonGeneratorMock, Mockito.times(1)).writeObjectRef("1");
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenIsFalse_ReturnsFalse() throws Exception {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        writableObjectId.generator = objectIdGeneratorMock;

        // Act
        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        // Assert
        Assert.assertFalse(result);
        Mockito.verifyZeroInteractions(jsonGeneratorMock);
    }

    @Test
    public void testWriteAsId_WhenIdIsNull_ReturnsFalse() throws Exception {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        writableObjectId.generator = objectIdGeneratorMock;

        // Act
        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        // Assert
        Assert.assertFalse(result);
        Mockito.verifyZeroInteractions(jsonGeneratorMock);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndAlwaysAsIdIsTrue_ReturnsTrue() throws Exception {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        writableObjectId.generator = objectIdGeneratorMock;
        objectIdWriterMock.alwaysAsId = true;

        Mockito.when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

        // Act
        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(jsonGeneratorMock, Mockito.times(1)).writeObjectRef("1");
    }

    @Test
    public void testGenerateId_ReturnsId() throws Exception {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        Object forPojo = new Object();
        
        // Act
        Object result = writableObjectId.generateId(forPojo);

        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(objectIdGeneratorMock, Mockito.times(1)).generateId(forPojo);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsFalse_WritesField() throws Exception {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        writableObjectId.generator = objectIdGeneratorMock;
        SerializableString name = Mockito.mock(SerializableString.class);
        objectIdWriterMock.propertyName = name;

        // Act
        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        // Assert
        Mockito.verify(jsonGeneratorMock, Mockito.times(1)).writeFieldName(name);
        Mockito.verify(objectIdWriterMock.serializer, Mockito.times(1)).serialize(writableObjectId.id, jsonGeneratorMock, serializerProviderMock);
        Assert.assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsTrue_WritesObjectId() throws Exception {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        writableObjectId.id = "1";
        writableObjectId.idWritten = true;
        writableObjectId.generator = objectIdGeneratorMock;
        SerializableString name = Mockito.mock(SerializableString.class);
        objectIdWriterMock.propertyName = name;

        Mockito.when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

        // Act
        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        // Assert
        Mockito.verify(jsonGeneratorMock, Mockito.times(1)).writeObjectId("1");
        Assert.assertTrue(writableObjectId.idWritten);
        Mockito.verifyZeroInteractions(name);
        Mockito.verifyZeroInteractions(objectIdWriterMock.serializer);
    }
}