package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

public class StdKeyDeserializerTest {
  
  @Test
  public void testDeserializeKey() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer();
    String key = "testKey";
    DeserializationContext ctxt = new DeserializationContext();
    Object result = deserializer.deserializeKey(key, ctxt);
    Assert.assertNotNull(result);
  }
  
  @Test
  public void testGetKeyClass() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer();
    Class<?> keyClass = deserializer.getKeyClass();
    Assert.assertNotNull(keyClass);
  }
  
  @Test
  public void testParse() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer();
    String key = "testKey";
    DeserializationContext ctxt = new DeserializationContext();
    Object result = deserializer._parse(key, ctxt);
    Assert.assertNotNull(result);
  }
  
  @Test
  public void testParseInt() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer();
    String key = "10";
    int result = deserializer._parseInt(key);
    Assert.assertEquals(10, result);
  }
  
  @Test
  public void testParseLong() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer();
    String key = "123456789";
    long result = deserializer._parseLong(key);
    Assert.assertEquals(123456789, result);
  }
  
  @Test
  public void testParseDouble() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer();
    String key = "3.14";
    double result = deserializer._parseDouble(key);
    Assert.assertEquals(3.14, result, 0.001);
  }
}