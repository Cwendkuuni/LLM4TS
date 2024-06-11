package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        // Test when id is null
        WritableObjectId objectId = new WritableObjectId(null);
        assertFalse(objectId.writeAsId(null, null, null));

        // Test when id is not null and idWritten is false
        objectId.id = 1;
        assertFalse(objectId.writeAsId(null, null, null));

        // Test when id is not null, idWritten is false, and alwaysAsId is true
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        assertTrue(objectId.writeAsId(null, null, writer));
    }
    
    @Test
    public void testGenerateId() {
        // Test when generator is null
        WritableObjectId objectId = new WritableObjectId(null);
        assertNull(objectId.generateId(null));

        // Test when generator is not null
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return forPojo.hashCode();
            }
        };
        objectId = new WritableObjectId(generator);
        Object forPojo = new Object();
        assertEquals(forPojo.hashCode(), objectId.generateId(forPojo));
        assertEquals(forPojo.hashCode(), objectId.id);
    }

    @Test
    public void testWriteAsField() {
        // Test when idWritten is false
        WritableObjectId objectId = new WritableObjectId(null);
        objectId.id = 1;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        objectId.writeAsField(null, null, writer);
        assertTrue(objectId.idWritten);

        // Test when idWritten is true
        objectId = new WritableObjectId(null);
        objectId.id = 1;
        writer = new ObjectIdWriter(null, null, null, true);
        objectId.writeAsField(null, null, writer);
        assertTrue(objectId.idWritten);
    }
}