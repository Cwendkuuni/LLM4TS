package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;
import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrueAndAlwaysAsIdTrueAndGenCanWriteObjectId_ThenReturnTrue() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, mock(SerializableString.class), mock(ObjectIdGenerator.class), true);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(true);

        // Act
        boolean result = writableObjectId.writeAsId(gen, mock(SerializerProvider.class), objectIdWriter);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrueAndAlwaysAsIdFalseAndGenCanWriteObjectId_ThenReturnFalse() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, mock(SerializableString.class), mock(ObjectIdGenerator.class), false);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(true);

        // Act
        boolean result = writableObjectId.writeAsId(gen, mock(SerializerProvider.class), objectIdWriter);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdFalseAndGenCanWriteObjectId_ThenReturnFalse() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, mock(SerializableString.class), mock(ObjectIdGenerator.class), false);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(true);

        // Act
        boolean result = writableObjectId.writeAsId(gen, mock(SerializerProvider.class), objectIdWriter);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testWriteAsField_WhenObjectIdsAvailableAndGenCanWriteObjectId_ThenWriteObjectIdAndReturn() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, mock(SerializableString.class), mock(ObjectIdGenerator.class), false);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(true);

        // Act
        writableObjectId.writeAsField(gen, mock(SerializerProvider.class), objectIdWriter);

        // Assert
        verify(gen).writeObjectId(anyString());
    }

    @Test
    public void testWriteAsField_WhenObjectIdsNotAvailable_ThenWriteFieldNameAndSerializeId() throws IOException {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, mock(SerializableString.class), mock(ObjectIdGenerator.class), false);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(false);

        // Act
        writableObjectId.writeAsField(gen, mock(SerializerProvider.class), objectIdWriter);

        // Assert
        verify(gen).writeFieldName(any(SerializableString.class));
        verify(objectIdWriter.serializer).serialize(writableObjectId.id, gen, any(SerializerProvider.class));
    }

    @Test
    public void testGenerateId_ThenGenerateAndReturnId() {
        // Arrange
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        Object forPojo = new Object();

        // Act
        Object result = writableObjectId.generateId(forPojo);

        // Assert
        assertEquals(writableObjectId.id, result);
        verify(writableObjectId.generator).generateId(forPojo);
    }
}