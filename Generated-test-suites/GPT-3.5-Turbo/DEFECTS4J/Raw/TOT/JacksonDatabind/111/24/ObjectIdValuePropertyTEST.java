package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

  @Test
  public void testWithNullName() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    assertNull(property.withName(null));
  }

  @Test
  public void testWithValueDeserializer() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    JsonDeserializer<?> deser = new JsonDeserializer<>();
    assertEquals(property, property.withValueDeserializer(deser));
  }

  @Test
  public void testWithNullProvider() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    NullValueProvider nva = new NullValueProvider();
    assertEquals(property, property.withNullProvider(nva));
  }

  @Test
  public void testGetAnnotation() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    assertNull(property.getAnnotation(null));
  }

  @Test
  public void testGetMember() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    assertNull(property.getMember());
  }

  @Test
  public void testDeserializeAndSet() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    
    try {
      property.deserializeAndSet(p, ctxt, instance);
    } catch (IOException e) {
      fail("Test failed with IOException");
    }

    // Add assertions here
  }

  @Test
  public void testDeserializeSetAndReturn() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    
    try {
      property.deserializeSetAndReturn(p, ctxt, instance);
    } catch (IOException e) {
      fail("Test failed with IOException");
    }

    // Add assertions here
  }

  @Test
  public void testSet() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    Object instance = new Object();
    Object value = new Object();

    try {
      property.set(instance, value);
    } catch (IOException e) {
      fail("Test failed with IOException");
    }

    // Add assertions here
  }

  @Test
  public void testSetAndReturn() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
    Object instance = new Object();
    Object value = new Object();

    try {
      property.setAndReturn(instance, value);
    } catch (IOException e) {
      fail("Test failed with IOException");
    }

    // Add assertions here
  }

}