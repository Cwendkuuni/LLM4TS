
package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class WritableObjectIdTest {
  
    private WritableObjectId writableObjectId;
    private ObjectIdGenerator<?> generator;
    private Object id;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter w;
    
    @Before
    public void setup() {
      generator = mock(ObjectIdGenerator.class);
      id = mock(Object.class);
      gen = mock(JsonGenerator.class);
      provider = mock(SerializerProvider.class);
      w = mock(ObjectIdWriter.class);
      writableObjectId = new WritableObjectId(generator);   
    }
    
    // Test for writeAsId method
    @Test
    public void testWriteAsId() throws IOException {
        when(w.alwaysAsId).thenReturn(true);
        when(gen.canWriteObjectId()).thenReturn(true);
        when(generator.generateId(any())).thenReturn(id);
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        assertTrue(result);
        verify(gen, times(1)).writeObjectRef(String.valueOf(id));
        verify(w.serializer, never()).serialize(any(), any(), any());
    }
    
    // Test for writeAsId method with idWritten false
    @Test
    public void testWriteAsIdWithIdNotWritten() throws IOException {
        when(w.alwaysAsId).thenReturn(true);
        when(gen.canWriteObjectId()).thenReturn(true);
        when(generator.generateId(any())).thenReturn(id);
        
        writableObjectId.writeAsField(gen, provider, w);
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        assertTrue(result);
        verify(gen, times(1)).writeObjectRef(String.valueOf(id));
        verify(w.serializer, never()).serialize(any(), any(), any());
    }
    
    // Test for writeAsId method with null id
    @Test
    public void testWriteAsIdWithNullId() throws IOException {
        when(w.alwaysAsId).thenReturn(true);
        when(generator.generateId(any())).thenReturn(null);
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        assertFalse(result);
        verify(gen, never()).writeObjectRef(anyString());
        verify(w.serializer, never()).serialize(any(), any(), any());
    }
    
    // Test for generateId method
    @Test
    public void testGenerateId() {
        Object forPojo = mock(Object.class);
        when(generator.generateId(forPojo)).thenReturn(id);
        
        Object result = writableObjectId.generateId(forPojo);
        
        assertEquals(id, result);
        assertEquals(id, writableObjectId.id);
    }
    
    // Test for writeAsField method with idWritten false
    @Test
    public void testWriteAsFieldWithIdNotWritten() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        
        writableObjectId.writeAsField(gen, provider, w);
        
        assertTrue(writableObjectId.idWritten);
        verify(gen, never()).writeFieldName(any());
        verify(w.serializer, never()).serialize(any(), any(), any());
    }
    
    // Test for writeAsField method with idWritten true and name not null
    @Test
    public void testWriteAsFieldWithIdWrittenAndNameNotNull() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.propertyName).thenReturn(mock(SerializableString.class));
        
        writableObjectId.idWritten = true;
        writableObjectId.writeAsField(gen, provider, w);
        
        assertTrue(writableObjectId.idWritten);
        verify(gen, times(1)).writeFieldName(any());
        verify(w.serializer, times(1)).serialize(any(), any(), any());
    }
    
    // Test for writeAsField method with gen.canWriteObjectId() true
    @Test
    public void testWriteAsFieldWithCanWriteObjectIdTrue() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.propertyName).thenReturn(null);
        when(gen.canWriteObjectId()).thenReturn(true);
        
        writableObjectId.writeAsField(gen, provider, w);
        
        assertTrue(writableObjectId.idWritten);
        verify(gen, times(1)).writeObjectId(anyString());
        verify(w.serializer, never()).serialize(any(), any(), any());
    }
    
}

