package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest
{
    @Test
    public void testWriteAsId() throws IOException
    {
        // Create test objects
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        // Test when id is not null and idWritten is false
        writableObjectId.id = new Object();
        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }
    
    @Test
    public void testGenerateId()
    {
        // Create test object
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();
        
        // Test generateId method
        assertNotNull(writableObjectId.generateId(forPojo));
    }
    
    @Test
    public void testWriteAsField() throws IOException
    {
        // Create test objects
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        // Test when idWritten is false
        writableObjectId.id = new Object();
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(writableObjectId.idWritten);
    }
}