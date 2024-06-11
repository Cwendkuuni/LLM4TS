package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {
    
    private class TestObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public void bindItem(JsonGenerator gen, SerializableString id, Object item) throws IOException {
            
        }

        @Override
        public Object generateId(Object forPojo) {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return false;
        }

        @Override
        public ObjectIdGenerator<Object> forScope(Class<?> scope) {
            return null;
        }

        @Override
        public ObjectIdGenerator<Object> newForSerialization(Object context) {
            return null;
        }
    }
    
    @Test
    public void testWriteAsId_WithNonNullIdAndIdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(new TestObjectIdGenerator());
        writableObjectId.id = "testId";
        writableObjectId.idWritten = true;
        
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId()).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        
        assertTrue(writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter));
        
        verify(mockGenerator).writeObjectRef("testId");
        verifyNoMoreInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider, mockWriter);
    }
    
    @Test
    public void testWriteAsId_WithNonNullIdAndNotIdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(new TestObjectIdGenerator());
        writableObjectId.id = "testId";
        writableObjectId.idWritten = false;
        
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId()).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        
        assertTrue(writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter));
        
        verify(mockGenerator).writeObjectRef("testId");
        verifyNoMoreInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider, mockWriter);
    }
    
    @Test
    public void testWriteAsId_WithNullIdAndIdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(new TestObjectIdGenerator());
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId()).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        
        assertFalse(writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter));
        
        verifyZeroInteractions(mockGenerator, mockProvider, mockWriter);
    }
    
    @Test
    public void testWriteAsId_WithNullIdAndNotIdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(new TestObjectIdGenerator());
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId()).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        
        assertFalse(writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter));
        
        verifyZeroInteractions(mockGenerator, mockProvider, mockWriter);
    }
    
    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(new TestObjectIdGenerator());
        
        Object generatedId = writableObjectId.generateId("testObject");
        
        assertNull(generatedId);
    }
    
    @Test
    public void testWriteAsField_WithNonNullId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(new TestObjectIdGenerator());
        writableObjectId.id = "testId";
        writableObjectId.idWritten = true;
        
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockGenerator.canWriteObjectId()).thenReturn(false);
        SerializableString mockName = mock(SerializableString.class);
        when(mockWriter.propertyName()).thenReturn(mockName);
        
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);
        
        verify(mockName).writeTo(mockGenerator);
        verify(mockGenerator).writeObject(mockName, "testId");
        verifyNoMoreInteractions(mockGenerator, mockProvider, mockWriter, mockName);
    }
    
    @Test
    public void testWriteAsField_WithNullId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(new TestObjectIdGenerator());
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockGenerator.canWriteObjectId()).thenReturn(false);
        SerializableString mockName = mock(SerializableString.class);
        when(mockWriter.propertyName()).thenReturn(mockName);
        
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);
        
        verifyZeroInteractions(mockGenerator, mockProvider, mockWriter, mockName);
    }
}