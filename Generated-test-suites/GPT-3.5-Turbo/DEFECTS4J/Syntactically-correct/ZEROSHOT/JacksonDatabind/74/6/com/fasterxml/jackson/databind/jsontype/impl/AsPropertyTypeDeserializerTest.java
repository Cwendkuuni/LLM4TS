package com.fasterxml.jackson.databind.jsontype.impl.test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testConstructor1() {
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        assertNull(typeDeserializer._inclusion);
    }
    
    @Test
    public void testConstructor2() {
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, null);
        assertNull(typeDeserializer._inclusion);
    }
    
    @Test
    public void testConstructor3() {
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        AsPropertyTypeDeserializer copy = new AsPropertyTypeDeserializer(typeDeserializer, null);
        assertEquals(typeDeserializer._inclusion, copy._inclusion);
    }
    
    @Test
    public void testForProperty() {
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        AsPropertyTypeDeserializer copy = (AsPropertyTypeDeserializer) typeDeserializer.forProperty(null);
        assertNotNull(copy);
        assertNotSame(typeDeserializer, copy);
        assertEquals(typeDeserializer._inclusion, copy._inclusion);
    }
    
    @Test
    public void testGetTypeInclusion() {
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        assertEquals(null, typeDeserializer.getTypeInclusion());
    }
}