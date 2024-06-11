package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.mockito.Mockito.*;

public class WritableObjectIdTest {
    
    private WritableObjectId writableObjectId;
    private ObjectIdGenerator<?> generator;
    private Object id;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter w;
    
    @Before
    public void setUp() {
        generator = mock(ObjectIdGenerator.class);
        id = mock(Object.class);
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        w = mock(ObjectIdWriter.class);
        writableObjectId = new WritableObjectId(generator);
    }
    
    @Test
    public void testWriteAsIdWhenIdNotNullAndIdWrittenOrAlwaysAsIdThenWritesObjectId() {
        // Arrange
        writableObjectId.id = id;
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        // Assert
        assertTrue(result);
        verify(gen).writeObjectRef(String.valueOf(id));
        verifyNoMoreInteractions(gen, provider, w);
    }
    
    @Test
    public void testWriteAsIdWhenIdNotNullAndIdNotWrittenButNotAlwaysAsIdThenSerializesId() throws IOException {
        // Arrange
        writableObjectId.id = id;
        w.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(false);
        
        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        // Assert
        assertTrue(result);
        verify(w.serializer).serialize(id, gen, provider);
        verifyNoMoreInteractions(gen, provider, w);
    }
    
    @Test
    public void testWriteAsIdWhenIdIsNullThenReturnsFalse() throws IOException {
        // Arrange
        writableObjectId.id = null;
        
        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        // Assert
        assertFalse(result);
        verifyNoInteractions(gen, provider, w);
    }
    
    @Test
    public void testGenerateIdThenGeneratesIdAndStoresIt() {
        // Arrange
        Object forPojo = mock(Object.class);
        Object generatedId = mock(Object.class);
        when(generator.generateId(forPojo)).thenReturn(generatedId);
        
        // Act
        Object result = writableObjectId.generateId(forPojo);
        
        // Assert
        assertEquals(generatedId, result);
        assertEquals(generatedId, writableObjectId.id);
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testWriteAsFieldWhenGenCanWriteObjectIdThenWritesObjectId() throws IOException {
        // Arrange
        gen = mock(JsonGenerator.class, withSettings().extraInterfaces(ObjectIdGenerator.class));
        when(gen.canWriteObjectId()).thenReturn(true);
        
        // Act
        writableObjectId.writeAsField(gen, provider, w);
        
        // Assert
        verify((ObjectIdGenerator)gen).writeObjectId(String.valueOf(writableObjectId.id));
        verifyNoMoreInteractions(gen, provider, w);
    }
    
    @Test
    public void testWriteAsFieldWhenGenCannotWriteObjectIdThenWritesFieldNameAndSerializesId() throws IOException {
        // Arrange
        SerializableString name = mock(SerializableString.class);
        w.propertyName = name;
        
        // Act
        writableObjectId.writeAsField(gen, provider, w);
        
        // Assert
        verify(gen).writeFieldName(name);
        verify(w.serializer).serialize(writableObjectId.id, gen, provider);
        verifyNoMoreInteractions(gen, provider, w);
    }
    
    @Test
    public void testWriteAsFieldWhenIdIsNullThenDoesNotWriteAnything() throws IOException {
        // Arrange
        gen = mock(JsonGenerator.class, withSettings().extraInterfaces(ObjectIdGenerator.class));
        w.propertyName = mock(SerializableString.class);
        writableObjectId.id = null;
        
        // Act
        writableObjectId.writeAsField(gen, provider, w);
        
        // Assert
        verifyZeroInteractions(gen, w.serializer);
    }
}