
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SubTypeValidatorTest {

  private SubTypeValidator subTypeValidator;

  @Before
  public void setUp() {
    subTypeValidator = SubTypeValidator.instance();
  }

  @Test
  public void testValidateSubType() throws JsonMappingException {
    DeserializationContext ctxt = mock(DeserializationContext.class);
    JavaType type = mock(JavaType.class);

    // Test valid type
    subTypeValidator.validateSubType(ctxt, type);

    // Test invalid type
    try {
      subTypeValidator.validateSubType(ctxt, new JavaType(null, null, null, null));
      fail("JsonMappingException not thrown for invalid type");
    } catch (JsonMappingException e) {
      assertEquals("Illegal type (java.lang.Object) to deserialize: prevented for security reasons", e.getMessage());
    }
  }
}
