package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        // Test case 1
        // given
        WritableObjectId obj = new WritableObjectId(null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        
        // when
        boolean result = obj.writeAsId(gen, provider, w);
        
        // then
        assertFalse(result);
        
        // Test case 2
        // given
        obj.id = new Object();
        obj.idWritten = false;
        w = new ObjectIdWriter(null, null, null, false);
        
        // when
        result = obj.writeAsId(gen, provider, w);
        
        // then
        assertFalse(result);
        
        // Test case 3
        // given
        obj.id = new Object();
        obj.idWritten = true;
        w = new ObjectIdWriter(null, null, null, false);
        
        // when
        result = obj.writeAsId(gen, provider, w);
        
        // then
        assertTrue(result);
    }
    
    @Test
    public void testGenerateId() {
        // given
        WritableObjectId obj = new WritableObjectId(null);
        Object forPojo = new Object();
        
        // when
        Object result = obj.generateId(forPojo);
        
        // then
        assertNotNull(result);
        assertEquals(result, obj.id);
    }
    
    @Test
    public void testWriteAsField() {
        // Test case 1
        // given
        WritableObjectId obj = new WritableObjectId(null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        
        // when
        obj.writeAsField(gen, provider, w);
        
        // then
        assertFalse(obj.idWritten);
        
        // Test case 2
        // given
        obj.id = new Object();
        obj.idWritten = false;
        w = new ObjectIdWriter(null, null, null, false);
        
        // when
        obj.writeAsField(gen, provider, w);
        
        // then
        assertTrue(obj.idWritten);
        
        // Test case 3
        // given
        obj.id = new Object();
        obj.idWritten = true;
        w = new ObjectIdWriter(null, null, null, false);
        
        // when
        obj.writeAsField(gen, provider, w);
        
        // then
        assertTrue(obj.idWritten);
    }
}