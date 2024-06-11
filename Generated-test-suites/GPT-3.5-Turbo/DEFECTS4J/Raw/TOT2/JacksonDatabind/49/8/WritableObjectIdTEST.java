package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {
    
    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId objectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        assertFalse(objectId.writeAsId(gen, provider, w));
        
        objectId.id = 123;
        assertFalse(objectId.writeAsId(gen, provider, w));
        
        objectId.idWritten = true;
        assertTrue(objectId.writeAsId(gen, provider, w));
    }
    
    @Test
    public void testGenerateId() {
        WritableObjectId objectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        Object forPojo = new Object();
        
        assertEquals(forPojo, objectId.generateId(forPojo));
    }
    
    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId objectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        objectId.id = 123;
        
        objectId.writeAsField(gen, provider, w);
        assertTrue(objectId.idWritten);
    }
}