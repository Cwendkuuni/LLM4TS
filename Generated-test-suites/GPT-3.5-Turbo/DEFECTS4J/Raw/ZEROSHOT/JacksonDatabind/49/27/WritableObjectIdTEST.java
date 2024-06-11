package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.mockito.Mockito.*;

public class WritableObjectIdTest {
    
    @Mock
    private ObjectIdGenerator<Object> generator;
    
    @Mock
    private JsonGenerator gen;
    
    @Mock
    private SerializerProvider provider;
    
    @Mock
    private ObjectIdWriter writer;
    
    private WritableObjectId objectId;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectId = new WritableObjectId(generator);
    }
    
    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWritten_ThenReturnTrue() throws Exception {
        // Arrange
        objectId.id = new Object();
        objectId.idWritten = true;
        writer.alwaysAsId = false;
        
        // Act
        boolean result = objectId.writeAsId(gen, provider, writer);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testWriteAsId_WhenIdNotNullAndIdNotWrittenAndAlwaysAsIdTrue_ThenReturnTrue() throws Exception {
        // Arrange
        objectId.id = new Object();
        objectId.idWritten = false;
        writer.alwaysAsId = true;
        
        // Act
        boolean result = objectId.writeAsId(gen, provider, writer);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testWriteAsId_WhenIdNotNullAndIdNotWrittenAndAlwaysAsIdFalseAndCanWriteObjectIdTrue_ThenReturnTrue() throws Exception {
        // Arrange
        objectId.id = new Object();
        objectId.idWritten = false;
        writer.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        // Act
        boolean result = objectId.writeAsId(gen, provider, writer);
        
        // Assert
        assertTrue(result);
        verify(gen).writeObjectRef(String.valueOf(objectId.id));
    }
    
    @Test
    public void testWriteAsId_WhenIdNotNullAndIdNotWrittenAndAlwaysAsIdFalseAndCanWriteObjectIdFalse_ThenReturnTrue() throws Exception {
        // Arrange
        objectId.id = new Object();
        objectId.idWritten = false;
        writer.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(false);
        
        // Act
        boolean result = objectId.writeAsId(gen, provider, writer);
        
        // Assert
        assertTrue(result);
        verify(writer.serializer).serialize(objectId.id, gen, provider);
    }
    
    @Test
    public void testWriteAsId_WhenIdNull_ThenReturnFalse() throws Exception {
        // Arrange
        objectId.id = null;
        objectId.idWritten = false;
        writer.alwaysAsId = true;
        
        // Act
        boolean result = objectId.writeAsId(gen, provider, writer);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testWriteAsId_WhenIdNullAndAlwaysAsIdFalseAndCanWriteObjectIdTrue_ThenReturnFalse() throws Exception {
        // Arrange
        objectId.id = null;
        objectId.idWritten = false;
        writer.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        // Act
        boolean result = objectId.writeAsId(gen, provider, writer);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testGenerateId_WhenForPojoNotNull_ThenReturnId() throws Exception {
        // Arrange
        Object forPojo = new Object();
        Object generatedId = new Object();
        when(generator.generateId(forPojo)).thenReturn(generatedId);
        
        // Act
        Object result = objectId.generateId(forPojo);
        
        // Assert
        assertEquals(generatedId, result);
        verify(generator).generateId(forPojo);
    }
    
    @Test
    public void testGenerateId_WhenForPojoNull_ThenReturnNull() throws Exception {
        // Arrange
        Object forPojo = null;
        
        // Act
        Object result = objectId.generateId(forPojo);
        
        // Assert
        assertNull(result);
        verify(generator, never()).generateId(forPojo);
    }
    
    @Test
    public void testWriteAsField_WhenIdWrittenFalseAndCanWriteObjectIdTrue_ThenWriteObjectId() throws Exception {
        // Arrange
        objectId.idWritten = false;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        // Act
        objectId.writeAsField(gen, provider, writer);
        
        // Assert
        verify(gen).writeObjectId(String.valueOf(objectId.id));
    }
    
    @Test
    public void testWriteAsField_WhenIdWrittenFalseAndCanWriteObjectIdFalseAndPropertyNameNotNull_ThenWriteFieldNameAndSerializeId() throws Exception {
        // Arrange
        objectId.idWritten = false;
        when(gen.canWriteObjectId()).thenReturn(false);
        when(writer.propertyName).thenReturn(mock(SerializableString.class));
        
        // Act
        objectId.writeAsField(gen, provider, writer);
        
        // Assert
        verify(gen).writeFieldName(writer.propertyName);
        verify(writer.serializer).serialize(objectId.id, gen, provider);
    }
    
    @Test
    public void testWriteAsField_WhenIdWrittenFalseAndCanWriteObjectIdFalseAndPropertyNameNull_ThenDoNothing() throws Exception {
        // Arrange
        objectId.idWritten = false;
        when(gen.canWriteObjectId()).thenReturn(false);
        when(writer.propertyName).thenReturn(null);
        
        // Act
        objectId.writeAsField(gen, provider, writer);
        
        // Assert
        verify(gen, never()).writeFieldName(writer.propertyName);
        verify(writer.serializer, never()).serialize(objectId.id, gen, provider);
    }
    
    @Test
    public void testWriteAsField_WhenIdWrittenTrue_ThenDoNothing() throws Exception {
        // Arrange
        objectId.idWritten = true;
        
        // Act
        objectId.writeAsField(gen, provider, writer);
        
        // Assert
        verify(gen, never()).writeObjectId(String.valueOf(objectId.id));
        verify(writer.serializer, never()).serialize(objectId.id, gen, provider);
    }
}