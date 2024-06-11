
package com.fasterxml.jackson.databind.ser.impl;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrueAndAlwaysAsIdTrue_ExpectTrue() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = "testId";
        objectId.idWritten = true;
        when(w.alwaysAsId).thenReturn(true);
        when(gen.canWriteObjectId()).thenReturn(true);

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertEquals(true, result);
        verify(gen, times(1)).writeObjectRef("testId");
        verifyZeroInteractions(provider);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrueAndAlwaysAsIdFalseAndGenCanWriteObjectIdTrue_ExpectTrue() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = "testId";
        objectId.idWritten = true;
        when(w.alwaysAsId).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(true);

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertEquals(true, result);
        verify(gen, times(1)).writeObjectRef("testId");
        verifyZeroInteractions(provider);
    }
    
    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrueAndAlwaysAsIdFalseAndGenCanWriteObjectIdFalse_ExpectTrue() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = "testId";
        objectId.idWritten = true;
        when(w.alwaysAsId).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(false);
        SerializableString name = mock(SerializableString.class);
        when(w.propertyName).thenReturn(name);

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertEquals(true, result);
        verify(gen, times(1)).writeFieldName(name);
        verify(w.serializer, times(1)).serialize(objectId.id, gen, provider);
    }
    
    @Test
    public void testWriteAsId_WhenIdNull_ExpectFalse() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = null;
        objectId.idWritten = true;
        when(w.alwaysAsId).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(false);

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertEquals(false, result);
        verifyZeroInteractions(gen);
        verifyZeroInteractions(provider);
        verifyZeroInteractions(w);
    }
    
    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdTrue_ExpectFalse() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = "testId";
        objectId.idWritten = false;
        when(w.alwaysAsId).thenReturn(true);

        // Act
        boolean result = objectId.writeAsId(gen, provider, w);

        // Assert
        assertEquals(false, result);
        verifyZeroInteractions(gen);
        verifyZeroInteractions(provider);
    }
    
    @Test
    public void testGenerateId_WhenIdGeneratedForAlwaysAsIdNotWrittenAsPojo_ExpectIdGenerated() {
        // Arrange
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        WritableObjectId objectId = new WritableObjectId(generator);
        objectId.gen.id = "testId";
        Object forPojo = mock(Object.class);

        // Act
        Object result = objectId.generateId(forPojo);

        // Assert
        assertEquals("testId", result);
        verify(generator, times(1)).generateId(forPojo);
    }
    
    @Test
    public void testGenerateId_WhenIdGeneratedForAlwaysAsIdWrittenAsPojo_ExpectIdGeneratedAndSet() {
        // Arrange
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        WritableObjectId objectId = new WritableObjectId(generator);
        objectId.gen.id = "testId";
        objectId.id = "testOldId";
        Object forPojo = mock(Object.class);

        // Act
        Object result = objectId.generateId(forPojo);

        // Assert
        assertEquals("testId", result);
        assertEquals("testId", objectId.id);
        verify(generator, times(1)).generateId(forPojo);
    }
    
    @Test
    public void testWriteAsField_WhenGenCanWriteObjectIdTrue_ExpectWriteObjectIdCalled() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = "testId";
        when(gen.canWriteObjectId()).thenReturn(true);

        // Act
        objectId.writeAsField(gen, provider, w);

        // Assert
        verify(gen, times(1)).writeObjectId("testId");
        verifyZeroInteractions(provider);
        verifyZeroInteractions(w);
    }
    
    @Test
    public void testWriteAsField_WhenGenCanWriteObjectIdFalse_ExpectFieldNameWrittenAndSerializerSerializeCalled() throws IOException {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = "testId";
        when(gen.canWriteObjectId()).thenReturn(false);
        SerializableString name = mock(SerializableString.class);
        when(w.propertyName).thenReturn(name);

        // Act
        objectId.writeAsField(gen, provider, w);

        // Assert
        verify(gen, times(1)).writeFieldName(name);
        verify(w.serializer, times(1)).serialize(objectId.id, gen, provider);
    }

}
