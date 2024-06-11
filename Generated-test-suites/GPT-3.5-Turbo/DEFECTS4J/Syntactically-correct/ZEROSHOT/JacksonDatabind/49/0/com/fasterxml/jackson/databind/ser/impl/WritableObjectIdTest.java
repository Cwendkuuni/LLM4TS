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
    
    private WritableObjectId writableObjectId;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        writableObjectId = new WritableObjectId(generator);
    }
    
    @Test
    public void testWriteAsId_When_IdIsNotNullAndIdWrittenIsTrue_Then_ReturnTrue() throws Exception {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        
        assertTrue(result);
        verify(gen).writeObjectRef("123");
        verifyZeroInteractions(provider);
        verifyZeroInteractions(writer);
    }
    
    @Test
    public void testWriteAsId_When_IdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsTrue_Then_ReturnTrue() throws Exception {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        writer.alwaysAsId = true;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        
        assertTrue(result);
        verify(gen).writeObjectRef("123");
        verifyZeroInteractions(provider);
        verifyZeroInteractions(writer);
    }
    
    @Test
    public void testWriteAsId_When_IdIsNull_Then_ReturnFalse() throws Exception {
        writableObjectId.id = null;
        
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        
        assertFalse(result);
        verifyZeroInteractions(gen);
        verifyZeroInteractions(provider);
        verifyZeroInteractions(writer);
    }
    
    @Test
    public void testWriteAsId_When_IdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalse_Then_WriteObjectRef() throws Exception {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        writer.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        
        assertTrue(result);
        verify(gen).writeObjectRef("123");
        verifyZeroInteractions(provider);
        verifyZeroInteractions(writer);
    }
    
    @Test
    public void testWriteAsId_When_IdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalseAndCannotWriteObjectId_Then_SerializeId() throws Exception {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        writer.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(false);
        
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        
        assertTrue(result);
        verifyZeroInteractions(provider);
        verifyZeroInteractions(writer);
        verify(writer.serializer).serialize("123", gen, provider);
    }
    
    @Test
    public void testGenerateId_When_IdIsGeneratedForPojo_Then_ReturnGeneratedId() {
        Object forPojo = new Object();
        Object expectedId = "123";
        when(generator.generateId(forPojo)).thenReturn(expectedId);
        
        Object result = writableObjectId.generateId(forPojo);
        
        assertEquals(expectedId, result);
        assertEquals(expectedId, writableObjectId.id);
    }
    
    @Test
    public void testWriteAsField_When_IdWrittenIsFalse_Then_SetIdWrittenToTrue() throws Exception {
        writableObjectId.idWritten = false;
        
        writableObjectId.writeAsField(gen, provider, writer);
        
        assertTrue(writableObjectId.idWritten);
        verifyZeroInteractions(gen);
        verifyZeroInteractions(provider);
        verifyZeroInteractions(writer);
    }
    
    @Test
    public void testWriteAsField_When_IdWrittenIsTrueAndCanWriteObjectIdIsTrue_Then_WriteObjectId() throws Exception {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        writableObjectId.writeAsField(gen, provider, writer);
        
        verify(gen).writeObjectId("123");
        verifyZeroInteractions(provider);
        verifyZeroInteractions(writer);
    }
    
    @Test
    public void testWriteAsField_When_IdWrittenIsTrueAndCanWriteObjectIdIsFalse_Then_WriteField() throws Exception {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(gen.canWriteObjectId()).thenReturn(false);
        writer.propertyName = mock(SerializableString.class);
        
        writableObjectId.writeAsField(gen, provider, writer);
        
        verifyZeroInteractions(gen);
        verify(writer.propertyName).isEmpty();
        verify(writer.propertyName).writeTo(gen);
        verify(writer.serializer).serialize("123", gen, provider);
    }
}