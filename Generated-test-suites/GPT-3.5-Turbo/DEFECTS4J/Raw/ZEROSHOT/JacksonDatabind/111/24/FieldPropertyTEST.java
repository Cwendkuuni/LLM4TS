package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {
    
    private FieldProperty fieldProperty;
    
    @Before
    public void setUp() {
        fieldProperty = new FieldProperty(null, null, null, null, null);
    }
    
    @Test
    public void testConstructor() {
        FieldProperty fp = new FieldProperty(null, null, null, null, null);
        assertNotNull(fp);
    }
    
    @Test
    public void testWithNullProvider() {
        FieldProperty fp = fieldProperty.withNullProvider(null);
        assertNotNull(fp);
    }
    
    @Test
    public void testFixAccess() {
        fieldProperty.fixAccess(null);
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(fieldProperty.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        assertNotNull(fieldProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        fieldProperty.deserializeAndSet(null, null, null);
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        fieldProperty.deserializeSetAndReturn(null, null, null);
    }
    
    @Test
    public void testSet() {
        try {
            fieldProperty.set(null, null);
        } catch (Exception e) {
            fail("Expected no exception");
        }
    }
    
    @Test
    public void testSetAndReturn() {
        try {
            fieldProperty.setAndReturn(null, null);
        } catch (Exception e) {
            fail("Expected no exception");
        }
    }
    
    @Test
    public void testReadResolve() {
        Object obj = fieldProperty.readResolve();
        assertNotNull(obj);
    }
}