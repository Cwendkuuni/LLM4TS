package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        // Initialize WritableObjectId
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        
        // Test when id is not null and idWritten is false
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter();
        writer.alwaysAsId = false;
        assertFalse(writableObjectId.writeAsId(gen, provider, writer));
        
        // Test when id is not null and idWritten is true
        writableObjectId.idWritten = true;
        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
        
        // Test when id is null
        writableObjectId.id = null;
        assertFalse(writableObjectId.writeAsId(gen, provider, writer));
    }
    
    @Test
    public void testGenerateId() {
        // Initialize WritableObjectId
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        
        // Test generating id for a given object
        Object object = new Object();
        assertNotNull(writableObjectId.generateId(object));
    }

    @Test
    public void testWriteAsField() {
        // Initialize WritableObjectId
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        
        // Test writing field when idWritten is false
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter();
        writer.propertyName = new SerializableString();
        writer.alwaysAsId = false;
        writableObjectId.writeAsField(gen, provider, writer);
        
        // Test writing field when idWritten is true
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        writableObjectId.writeAsField(gen, provider, writer);
    }
}