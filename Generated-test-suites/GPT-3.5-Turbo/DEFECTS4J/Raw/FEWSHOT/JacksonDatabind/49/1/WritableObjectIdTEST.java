package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {
    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
    }

    @Test
    public void testWriteAsId() {
        assertFalse(writableObjectId.writeAsId(null, null, null));
        writableObjectId.id = "testId";
        assertTrue(writableObjectId.writeAsId(null, null, null));
        
        writableObjectId.id = null;
        assertFalse(writableObjectId.writeAsId(null, null, null));
        
        writableObjectId.id = "testId";
        writableObjectId.idWritten = true;
        assertTrue(writableObjectId.writeAsId(null, null, new ObjectIdWriter(null, null, true)));
        
        writableObjectId.id = "testId";
        writableObjectId.idWritten = false;
        assertTrue(writableObjectId.writeAsId(new JsonGenerator(), new SerializerProvider(), new ObjectIdWriter(null, null, true)));
    }

    @Test
    public void testGenerateId() {
        assertNull(writableObjectId.generateId(null));
        Object forPojo = new Object();
        assertEquals(forPojo, writableObjectId.generateId(forPojo));
    }

    @Test
    public void testWriteAsField() {
        writableObjectId.idWritten = true;
        writableObjectId.writeAsField(null, null, null);
        
        writableObjectId.id = "testId";
        writableObjectId.idWritten = false;
        writableObjectId.writeAsField(null, null, null);
    }
}