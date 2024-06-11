package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

  @Test
  public void testConstructor() {
    // Test constructor with null valueDeserializer and null nullValueProvider
    SettableBeanProperty methodProperty = new MethodProperty(null, null, null, null, null);
    assertNotNull(methodProperty);

    // Test constructor with non-null valueDeserializer and null nullValueProvider
    JsonDeserializer valueDeserializer = new JsonDeserializer();
    methodProperty = new MethodProperty(null, null, valueDeserializer, null, null);
    assertNotNull(methodProperty);

    // Test constructor with null valueDeserializer and non-null nullValueProvider
    NullValueProvider nullValueProvider = new NullValueProvider();
    methodProperty = new MethodProperty(null, null, null, null, nullValueProvider);
    assertNotNull(methodProperty);

    // Test constructor with non-null valueDeserializer and non-null nullValueProvider
    methodProperty = new MethodProperty(null, null, valueDeserializer, null, nullValueProvider);
    assertNotNull(methodProperty);
  }

  @Test
  public void testDeserializeAndSet() {
    // Test when JsonParser has VALUE_NULL token
    JsonParser parser = new JsonParser();
    DeserializationContext context = new DeserializationContext();
    Object instance = new Object();
    MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

    // Test when _skipNulls is false
    methodProperty.deserializeAndSet(parser, context, instance);

    // Test when _skipNulls is true
    methodProperty = new MethodProperty(null, null, null, null, true);
    methodProperty.deserializeAndSet(parser, context, instance);

    // Test when _valueTypeDeserializer is null
    methodProperty = new MethodProperty(null, null, null, null, null);
    methodProperty.deserializeAndSet(parser, context, instance);

    // Test when _valueDeserializer is not null and _valueTypeDeserializer is not null
    JsonDeserializer valueDeserializer = new JsonDeserializer();
    methodProperty = new MethodProperty(null, null, valueDeserializer, null, null);
    methodProperty.deserializeAndSet(parser, context, instance);
  }

  @Test
  public void testDeserializeSetAndReturn() {
    // Test when JsonParser has VALUE_NULL token
    JsonParser parser = new JsonParser();
    DeserializationContext context = new DeserializationContext();
    Object instance = new Object();
    MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

    // Test when _skipNulls is false
    Object result = methodProperty.deserializeSetAndReturn(parser, context, instance);
    assertNotNull(result);

    // Test when _skipNulls is true
    methodProperty = new MethodProperty(null, null, null, null, true);
    result = methodProperty.deserializeSetAndReturn(parser, context, instance);
    assertNotNull(result);

    // Test when _valueTypeDeserializer is null
    methodProperty = new MethodProperty(null, null, null, null, null);
    result = methodProperty.deserializeSetAndReturn(parser, context, instance);
    assertNotNull(result);

    // Test when _valueDeserializer is not null and _valueTypeDeserializer is not null
    JsonDeserializer valueDeserializer = new JsonDeserializer();
    methodProperty = new MethodProperty(null, null, valueDeserializer, null, null);
    result = methodProperty.deserializeSetAndReturn(parser, context, instance);
    assertNotNull(result);
  }

  @Test
  public void testSet() {
    // Test when _setter.invoke throws Exception
    Object instance = new Object();
    Object value = new Object();
    MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

    try {
      methodProperty.set(instance, value);
      fail("Exception not thrown");
    } catch (IOException e) {
      // Exception expected
    }
  }

  @Test
  public void testSetAndReturn() {
    // Test when _setter.invoke throws Exception
    Object instance = new Object();
    Object value = new Object();
    MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

    try {
      methodProperty.setAndReturn(instance, value);
      fail("Exception not thrown");
    } catch (IOException e) {
      // Exception expected
    }
  }

}