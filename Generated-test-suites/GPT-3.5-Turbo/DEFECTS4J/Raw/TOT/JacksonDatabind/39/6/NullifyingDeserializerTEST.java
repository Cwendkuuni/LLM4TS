package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullifyingDeserializerTest {

  @Test
  public void testDeserialize() {

    NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
    String json = "{\"field1\": \"value1\", \"field2\": \"value2\"}";

    try {
      JsonParser jsonParser = new JsonFactory().createParser(json);
      Object deserialized = nullifyingDeserializer.deserialize(jsonParser, null);
      
      assertNull(deserialized);
    } catch (Exception e) {
      fail("Error occurred during deserialization");
    }
  }

  @Test
  public void testDeserializeWithType() {

    NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
    String json = "{\"field1\": \"value1\", \"field2\": \"value2\"}";

    try {
      JsonParser jsonParser = new JsonFactory().createParser(json);
      Object deserialized = nullifyingDeserializer.deserializeWithType(jsonParser, null, null);
      
      assertNull(deserialized);
    } catch (Exception e) {
      fail("Error occurred during deserialization");
    }
  }
  
}