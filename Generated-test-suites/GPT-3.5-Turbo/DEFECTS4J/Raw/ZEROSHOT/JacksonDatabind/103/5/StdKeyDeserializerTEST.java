package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class StdKeyDeserializerTest {
  @Test
  public void testDeserializeKey() throws Exception {
    KeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class) {
        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
           return null;
        }
    };
    
    DeserializationContext ctxt = null;
    
    // Test with null key
    assertNull(keyDeserializer.deserializeKey(null, ctxt));
    
    // Test with empty key
    assertNull(keyDeserializer.deserializeKey("", ctxt));
    
    // Test with non-null non-empty key
    assertNotNull(keyDeserializer.deserializeKey("test", ctxt));
  }
  
    @Test
  public void testGetStringFactoryKeyDeserializer() throws Exception {
    KeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(
        StdKeyDeserializer.class.getDeclaredMethod("_parse", String.class, DeserializationContext.class));
    
    DeserializationContext ctxt = null;
    
    // Test with null key
    assertNull(keyDeserializer.deserializeKey(null, ctxt));
    
    // Test with empty key
    assertNull(keyDeserializer.deserializeKey("", ctxt));
    
    // Test with non-null non-empty key
    assertNotNull(keyDeserializer.deserializeKey("test", ctxt));
  }
  
  @Test
  public void testGetStringCtorKeyDeserializer() throws Exception {
    Constructor<?> constructor = StdKeyDeserializer.class.getDeclaredConstructor(int.class, Class.class);
    constructor.setAccessible(true);
    
    KeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(constructor);
    
    DeserializationContext ctxt = null;
    
    // Test with null key
    assertNull(keyDeserializer.deserializeKey(null, ctxt));
    
    // Test with empty key
    assertNull(keyDeserializer.deserializeKey("", ctxt));
    
    // Test with non-null non-empty key
    assertNotNull(keyDeserializer.deserializeKey("test", ctxt));
  }
  
  @Test
  public void testParseKey() throws Exception {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class) {
        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return key;
        }
    };
    
    DeserializationContext ctxt = null;
    
    // Test with null key
    assertNull(keyDeserializer._parse(null, ctxt));
    
    // Test with empty key
    assertEquals("", keyDeserializer._parse("", ctxt));
    
    // Test with non-null non-empty key
    assertEquals("test", keyDeserializer._parse("test", ctxt));
  }
}