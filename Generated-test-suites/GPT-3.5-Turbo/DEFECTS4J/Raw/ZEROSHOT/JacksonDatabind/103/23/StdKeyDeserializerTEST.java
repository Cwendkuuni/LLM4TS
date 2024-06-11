
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        assertNull(deserializer.deserializeKey(null, null));
    }
    
    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        assertEquals(Boolean.class, deserializer.getKeyClass());
    }
    
    @Test
    public void test_parse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        assertEquals(true, deserializer._parse("true", null));
    }
    
    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        StdKeyDeserializer.StringCtorKeyDeserializer deserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));
        assertEquals("test", deserializer._parse("test", null));
    }
    
    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        StdKeyDeserializer.StringFactoryKeyDeserializer deserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(String.class.getMethod("valueOf", String.class));
        assertEquals("test", deserializer._parse("test", null));
    }
}
