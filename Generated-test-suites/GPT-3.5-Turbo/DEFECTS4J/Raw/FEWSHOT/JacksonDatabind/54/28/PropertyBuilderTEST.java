package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

  private PropertyBuilder propertyBuilder;

  @Before
  public void setup() {
    propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
  }

  @Test
  public void testGetDefaultBean() {
    assertNull(propertyBuilder.getDefaultBean());
  }

  @Test
  public void testGetPropertyDefaultValue() throws Exception {
    assertNull(propertyBuilder.getPropertyDefaultValue("name", new AnnotatedMethod(), new JavaType()));
  }

  @Test
  public void testGetDefaultValue() {
    assertNull(propertyBuilder.getDefaultValue(new JavaType()));
  }
}