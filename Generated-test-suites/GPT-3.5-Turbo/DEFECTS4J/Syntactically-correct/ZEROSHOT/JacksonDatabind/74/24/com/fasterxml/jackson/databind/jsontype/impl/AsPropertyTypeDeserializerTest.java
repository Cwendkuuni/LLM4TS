package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {
  
  private AsPropertyTypeDeserializer deserializer;
  
  @Before
  public void setup() {
    deserializer = new AsPropertyTypeDeserializer(null, null, null, true, null);
  }

  @Test
  public void testDeserializeTypedFromObject() {
    // TODO: Implement test case
  }

  @Test
  public void testDeserializeTypedForId() {
    // TODO: Implement test case
  }

  @Test
  public void testDeserializeTypedUsingDefaultImpl() {
    // TODO: Implement test case
  }

  @Test
  public void testDeserializeTypedFromAny() {
    // TODO: Implement test case
  }

  // TODO: Add more test methods

}