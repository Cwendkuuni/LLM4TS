package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        // Setup
        WritableObjectId objId = new WritableObjectId(null);
        
        // Test
        assertFalse(objId.writeAsId(null, null, null));
        
        objId.id = "123";
        assertTrue(objId.writeAsId(null, null, new ObjectIdWriter()));
    }

    @Test
    public void testGenerateId() {
        // Setup
        WritableObjectId objId = new WritableObjectId(null);
        
        // Test
        assertNull(objId.generateId(null));
        
        Object id = objId.generateId("test");
        assertNotNull(id);
    }

    @Test
    public void testWriteAsField() {
        // Setup
        WritableObjectId objId = new WritableObjectId(null);
        objId.id = "123";
        
        // Test
        try {
            objId.writeAsField(null, null, new ObjectIdWriter());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}