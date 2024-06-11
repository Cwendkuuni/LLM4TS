package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "123";
        
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        //trying to cover "id != null" and "idWritten" is false branch
        assertEquals(false, writableObjectId.writeAsId(gen, provider, new ObjectIdWriter()));
        
        writableObjectId.idWritten = true;
        assertEquals(true, writableObjectId.writeAsId(gen, provider, new ObjectIdWriter()));
    }
    
    @Test
    public void testGenerateId() {
        ObjectIdGenerator<Integer> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        
        assertEquals(1, writableObjectId.generateId(1));
        assertEquals(2, writableObjectId.generateId(2));
        
        //covering the edge case where existing id is available
        assertEquals(2, writableObjectId.generateId(3));
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<String> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "123";
        writableObjectId.writeAsField(Mockito.mock(JsonGenerator.class), Mockito.mock(SerializerProvider.class), new ObjectIdWriter());
        //canWriteObjectId is false branch
    }
    
}