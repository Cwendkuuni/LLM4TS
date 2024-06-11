package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {
  
  @Test
  public void testDeserializeKey() {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
    
    try {
      String result = (String) keyDeserializer.deserializeKey("test", null);
      assertEquals("test", result);
    }catch(Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetKeyClass() {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
    
    Class<?> keyClass = keyDeserializer.getKeyClass();
    assertEquals(String.class, keyClass);
  }
  
  @Test
  public void test_parse() {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
    
    try {
      String result = (String) keyDeserializer._parse("test", null);
      assertEquals("test", result);
    }catch(Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void test_parseInt() {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
    
    try {
      int result = keyDeserializer._parseInt("10");
      assertEquals(10, result);
    }catch(IllegalArgumentException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void test_parseLong() {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
    
    try {
      long result = keyDeserializer._parseLong("100");
      assertEquals(100l, result);
    }catch(IllegalArgumentException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void test_parseDouble() {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
    
    try {
      double result = keyDeserializer._parseDouble("10.05");
      assertEquals(10.05, result, 0.00001);
    }catch(IllegalArgumentException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void test_weirdKey() {
    StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
    
    try {
      keyDeserializer._weirdKey(null, "test key", new Exception());
    }catch(Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
}