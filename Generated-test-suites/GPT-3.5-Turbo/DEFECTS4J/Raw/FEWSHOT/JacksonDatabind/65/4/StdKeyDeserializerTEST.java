package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StdKeyDeserializerTest {
    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer stringKD = new StdKeyDeserializer.StringKD(String.class);
        assertEquals("test", stringKD.deserializeKey("test", null));
        
        StdKeyDeserializer stringCtorKD = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));
        assertEquals("test", stringCtorKD.deserializeKey("test", null));
        
        StdKeyDeserializer stringFactoryKD = new StdKeyDeserializer.StringFactoryKeyDeserializer(String.class.getMethod("valueOf", String.class));
        assertEquals("test", stringFactoryKD.deserializeKey("test", null));
        
        StdKeyDeserializer enumKD = new StdKeyDeserializer.EnumKD(EnumResolver.constructUnsafe(Enum.class, null), null);
        assertNotNull(enumKD.deserializeKey("TEST", null));
        
        StdKeyDeserializer enumKD2 = new StdKeyDeserializer.EnumKD(EnumResolver.constructUnsafeUsingToString(Enum.class, null), null);
        assertNotNull(enumKD2.deserializeKey("TEST", null));
        assertNull(enumKD2.deserializeKey("UNKNOWN", null));
    }
    
    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer stringKD = new StdKeyDeserializer.StringKD(String.class);
        assertNotNull(stringKD._parse("test", null));
        
        StdKeyDeserializer enumKD = new StdKeyDeserializer.EnumKD(EnumResolver.constructUnsafe(Enum.class, null), null);
        assertNotNull(enumKD._parse("TEST", null));
    }
    
    @Test
    public void testHelperMethods() throws Exception {
        StdKeyDeserializer stringKD = new StdKeyDeserializer.StringKD(String.class);
        assertEquals(1, stringKD._parseInt("1"));
        assertEquals(1L, stringKD._parseLong("1"));
        assertEquals(1.0, stringKD._parseDouble("1"), 0.01);
    }
}