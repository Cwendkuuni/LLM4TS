package com.fasterxml.jackson.databind.deser.std;

import java.util.Collection;

import com.fasterxml.jackson.databind.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

  @Test
  public void testDeserialize() throws Exception {
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Collection<String> result = null;

    // Test case 1
    p = null;
    ctxt = null;
    result = null;
    assertEquals(result, StringCollectionDeserializer.deserialize(p, ctxt, result));

    // Test case 2
    p = null;
    ctxt = null;
    result = null;
    assertEquals(result, StringCollectionDeserializer.deserialize(p, ctxt, result));

    // Test case 3
    p = null;
    ctxt = null;
    result = null;
    assertEquals(result, StringCollectionDeserializer.deserialize(p, ctxt, result));

    // Test case 4
    p = null;
    ctxt = null;
    result = null;
    assertEquals(result, StringCollectionDeserializer.deserialize(p, ctxt, result));

  }

  @Test
  public void testDeserializeWithType() throws Exception {
    JsonParser p = null;
    DeserializationContext ctxt = null;
    TypeDeserializer typeDeserializer = null;
    Object result = null;

    // Test case 1
    p = null;
    ctxt = null;
    typeDeserializer = null;
    result = null;
    assertEquals(result, StringCollectionDeserializer.deserializeWithType(p, ctxt, typeDeserializer));

  }

  @Test
  public void testHandleNonArray() throws Exception {
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Collection<String> result = null;

    // Test case 1
    p = null;
    ctxt = null;
    result = null;
    assertEquals(result, StringCollectionDeserializer.handleNonArray(p, ctxt, result));

  }

}