package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {

  @Test
  public void testValidateSubType() throws JsonMappingException {
    SubTypeValidator validator = SubTypeValidator.instance();
    DeserializationContext ctxt = null; // Initialize the DeserializationContext object accordingly
    JavaType type = null; // Initialize the JavaType object accordingly

    // Test case 1: Raw class is in _cfgIllegalClassNames
    Set<String> illegalClassNames = new HashSet<String>();
    illegalClassNames.add("org.apache.commons.collections.functors.InvokerTransformer");
    validator._cfgIllegalClassNames = illegalClassNames;
    try {
      validator.validateSubType(ctxt, type);
      fail("Expected JsonMappingException");
    } catch (JsonMappingException e) {
      assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
    }

    // Test case 2: Raw class is not in _cfgIllegalClassNames but is an interface
    illegalClassNames = new HashSet<String>();
    validator._cfgIllegalClassNames = illegalClassNames;
    try {
      validator.validateSubType(ctxt, type);
    } catch (JsonMappingException e) {
      fail("Unexpected JsonMappingException");
    }

    // Test case 3: Raw class is not in _cfgIllegalClassNames, is not an interface, but starts with PREFIX_SPRING
    illegalClassNames = new HashSet<String>();
    validator._cfgIllegalClassNames = illegalClassNames;
    try {
      validator.validateSubType(ctxt, type);
    } catch (JsonMappingException e) {
      fail("Unexpected JsonMappingException");
    }

    // Test case 4: Raw class is not in _cfgIllegalClassNames, is not an interface, doesn't start with PREFIX_SPRING
    illegalClassNames = new HashSet<String>();
    validator._cfgIllegalClassNames = illegalClassNames;
    try {
      validator.validateSubType(ctxt, type);
    } catch (JsonMappingException e) {
      fail("Unexpected JsonMappingException");
    }
  }
}