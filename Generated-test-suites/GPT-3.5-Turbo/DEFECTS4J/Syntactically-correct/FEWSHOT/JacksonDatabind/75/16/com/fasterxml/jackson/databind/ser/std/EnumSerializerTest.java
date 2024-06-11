package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class EnumSerializerTest {
    
    @Test
    public void testSerialize() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, Locale.class), false);
        assertEquals("en_US", serializer.serialize(Locale.US, null, null));
    }
    
    @Test
    public void testSerializeAsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, Locale.class), true);
        assertEquals(1, serializer.serialize(Locale.US, null, null));
    }
    
    @Test
    public void testCreateContextual() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, Locale.class), true);
        assertTrue(serializer.createContextual(null, null) instanceof EnumSerializer);
    }
    
    @Test
    public void testGetEnumValues() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, Locale.class), true);
        assertNotNull(serializer.getEnumValues());
    }
    
    @Test
    public void testGetSchema() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, Locale.class), true);
        assertNotNull(serializer.getSchema(null, null));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, Locale.class), true);
        assertEquals(null, serializer.acceptJsonFormatVisitor(null, null));
    }
}