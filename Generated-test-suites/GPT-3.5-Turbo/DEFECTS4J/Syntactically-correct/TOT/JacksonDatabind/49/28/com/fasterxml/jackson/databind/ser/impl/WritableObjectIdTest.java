package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class WritableObjectIdTest {
    private WritableObjectId writableObjectId;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setup() {
        ObjectIdGenerator<?> objectIdGenerator = new ObjectIdGenerator<>();
        writableObjectId = new WritableObjectId(objectIdGenerator);
        jsonGenerator = new JsonGenerator();
        serializerProvider = new SerializerProvider();
        objectIdWriter = new ObjectIdWriter();
    }

    @Test
    public void testWriteAsId_NullId_WriteAsIdReturnsFalse() throws IOException {
        // Arrange
        writableObjectId.id = null;

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_IdIsNull_IdWrittenReturnsFalse_WriteAsIdAlwaysAsIdReturnsFalse_WriteObjectIdReturnsTrue() throws IOException {
        // Arrange
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        objectIdWriter.alwaysAsId = false;

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testWriteAsId_IdIsNull_IdWrittenReturnsTrue_WriteAsIdAlwaysAsIdReturnsFalse_WriteObjectIdReturnsFalse() throws IOException {
        // Arrange
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        objectIdWriter.alwaysAsId = false;

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_IdIsNull_IdWrittenReturnsFalse_WriteAsIdAlwaysAsIdReturnsTrue_WriteObjectIdReturnsTrue() throws IOException {
        // Arrange
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        objectIdWriter.alwaysAsId = true;

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testWriteAsId_IdIsNull_IdWrittenReturnsTrue_WriteAsIdAlwaysAsIdReturnsTrue_WriteObjectIdReturnsFalse() throws IOException {
        // Arrange
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        objectIdWriter.alwaysAsId = true;

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_IdNotNull_IdWrittenReturnsFalse_WriteAsIdAlwaysAsIdReturnsFalse_CanWriteObjectIdReturnsTrue() throws IOException {
        // Arrange
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        objectIdWriter.alwaysAsId = false;
        jsonGenerator.setCanWriteObjectId(true);

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testWriteAsId_IdNotNull_IdWrittenReturnsTrue_WriteAsIdAlwaysAsIdReturnsFalse_CanWriteObjectIdReturnsTrue() throws IOException {
        // Arrange
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        objectIdWriter.alwaysAsId = false;
        jsonGenerator.setCanWriteObjectId(true);

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_IdNotNull_IdWrittenReturnsFalse_WriteAsIdAlwaysAsIdReturnsTrue_CanWriteObjectIdReturnsTrue() throws IOException {
        // Arrange
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        objectIdWriter.alwaysAsId = true;
        jsonGenerator.setCanWriteObjectId(true);

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testWriteAsId_IdNotNull_IdWrittenReturnsTrue_WriteAsIdAlwaysAsIdReturnsTrue_CanWriteObjectIdReturnsTrue() throws IOException {
        // Arrange
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        objectIdWriter.alwaysAsId = true;
        jsonGenerator.setCanWriteObjectId(true);

        // Act
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsField_IdWrittenFalse_CanWriteObjectIdTrue_PropertyNameIsNull() throws IOException {
        // Arrange
        writableObjectId.idWritten = false;
        jsonGenerator.setCanWriteObjectId(true);
        objectIdWriter.propertyName = null;

        // Act
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        // Verify that no exception is thrown
    }

    @Test
    public void testWriteAsField_IdWrittenTrue_CanWriteObjectIdTrue_PropertyNameIsNull() throws IOException {
        // Arrange
        writableObjectId.idWritten = true;
        jsonGenerator.setCanWriteObjectId(true);
        objectIdWriter.propertyName = null;

        // Act
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        // Verify that no exception is thrown
    }

    @Test
    public void testWriteAsField_IdWrittenFalse_CanWriteObjectIdFalse_PropertyNameIsNull() throws IOException {
        // Arrange
        writableObjectId.idWritten = false;
        jsonGenerator.setCanWriteObjectId(false);
        objectIdWriter.propertyName = null;

        // Act
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        // Verify that no exception is thrown
    }

    @Test
    public void testWriteAsField_IdWrittenTrue_CanWriteObjectIdFalse_PropertyNameIsNull() throws IOException {
        // Arrange
        writableObjectId.idWritten = true;
        jsonGenerator.setCanWriteObjectId(false);
        objectIdWriter.propertyName = null;

        // Act
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        // Verify that no exception is thrown
    }

    @Test
    public void testWriteAsField_IdWrittenTrue_CanWriteObjectIdTrue_PropertyNameNotNull() throws IOException {
        // Arrange
        writableObjectId.idWritten = true;
        jsonGenerator.setCanWriteObjectId(true);
        objectIdWriter.propertyName = new SerializableString();

        // Act
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        // Assert
        // Verify that no exception is thrown
    }

    @Test
    public void testGenerateId_GeneratorGeneratesId_IdSet() {
        // Arrange
        Object forPojo = new Object();

        // Act
        Object result = writableObjectId.generateId(forPojo);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result, writableObjectId.id);
    }
}