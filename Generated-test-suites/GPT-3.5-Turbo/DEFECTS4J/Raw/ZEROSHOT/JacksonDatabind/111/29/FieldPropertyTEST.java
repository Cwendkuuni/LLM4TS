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
        assertNull(fieldProperty._annotated);
        assertNull(fieldProperty._field);
        assertFalse(fieldProperty._skipNulls);
    }

    @Test
    public void testWithName() {
        FieldProperty newFieldProperty = fieldProperty.withName(null);
        assertNotNull(newFieldProperty);
    }

    @Test
    public void testWithValueDeserializer() {
        FieldProperty newFieldProperty = fieldProperty.withValueDeserializer(null);
        assertNotNull(newFieldProperty);
    }

    @Test
    public void testWithNullProvider() {
        FieldProperty newFieldProperty = fieldProperty.withNullProvider(null);
        assertNotNull(newFieldProperty);
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
        assertNull(fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        try {
            fieldProperty.deserializeAndSet(null, null, null);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        try {
            assertNull(fieldProperty.deserializeSetAndReturn(null, null, null));
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testSet() {
        try {
            fieldProperty.set(null, null);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testSetAndReturn() {
        try {
            assertNull(fieldProperty.setAndReturn(null, null));
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testReadResolve() {
        assertNotNull(fieldProperty.readResolve());
    }
  
}