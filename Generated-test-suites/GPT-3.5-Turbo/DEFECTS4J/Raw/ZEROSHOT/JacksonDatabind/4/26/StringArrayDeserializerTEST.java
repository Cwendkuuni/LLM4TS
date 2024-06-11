package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

  @Test
  public void testDeserialize() {
    StringArrayDeserializer deser = new StringArrayDeserializer();
    assertEquals(null, deser.deserialize(null, null));
    //TODO: test other cases
  }
  
  @Test
  public void testDeserializeWithType() {
    StringArrayDeserializer deser = new StringArrayDeserializer();
    assertEquals(null, deser.deserializeWithType(null, null, null));
    //TODO: test other cases
  }
  
  @Test
  public void testHandleNonArray() throws Exception {
    StringArrayDeserializer deser = new StringArrayDeserializer();
    JsonParser jp = null;
    DeserializationContext ctxt = null;
    try {
        deser.handleNonArray(jp, ctxt);
        fail("Expected an exception");
    } catch (Exception e) {
        //Expected an exception
    }
    //TODO: test other cases
  }
  
  @Test
  public void testCreateContextual() throws Exception {
    StringArrayDeserializer deser = new StringArrayDeserializer();
    DeserializationContext ctxt = null;
    BeanProperty property = null;
    try {
        deser.createContextual(ctxt, property);
        fail("Expected an exception");
    } catch (Exception e) {
        //Expected an exception
    }
    //TODO: test other cases
  }

}