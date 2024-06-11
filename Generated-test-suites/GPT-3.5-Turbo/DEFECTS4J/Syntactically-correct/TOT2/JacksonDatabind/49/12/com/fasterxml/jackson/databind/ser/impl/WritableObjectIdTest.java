package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        assertFalse(result);
    }
    
    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        
        Object testObject = new Object();
        Object generatedId = writableObjectId.generateId(testObject);
        
        assertNotNull(generatedId);
    }
    
    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        writableObjectId.writeAsField(gen, provider, w);
        
        assertTrue(writableObjectId.idWritten);
    }
}