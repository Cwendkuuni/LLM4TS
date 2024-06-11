package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdReader;

public class ObjectIdValuePropertyTest {

  @Test
  public void testConstructor() {
    ObjectIdReader reader = new ObjectIdReader();
    ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, PropertyMetadata.STD_REQUIRED);
    assertNotNull(prop);
  }

  @Test
  public void testWithAnnotation() {
    ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_REQUIRED);
    assertNull(prop.getAnnotation(Annotation.class));
  }

  @Test
  public void testGetMember() {
    ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_REQUIRED);
    assertNull(prop.getMember());
  }

  @Test
  public void testDeserializeAndSet() {
    ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_REQUIRED);
    JsonParser jp = null;
    DeserializationContext ctxt = null;
    Object instance = new Object();
    assertThrows(IOException.class, () -> {
      prop.deserializeAndSet(jp, ctxt, instance);
    });
  }

  @Test
  public void testDeserializeSetAndReturn() {
    ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_REQUIRED);
    JsonParser jp = null;
    DeserializationContext ctxt = null;
    Object instance = new Object();
    assertThrows(IOException.class, () -> {
      prop.deserializeSetAndReturn(jp, ctxt, instance);
    });
  }

  @Test
  public void testSet() {
    ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_REQUIRED);
    Object instance = new Object();
    Object value = new Object();
    assertThrows(UnsupportedOperationException.class, () -> {
      prop.set(instance, value);
    });
  }

  @Test
  public void testSetAndReturn() {
    ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_REQUIRED);
    Object instance = new Object();
    Object value = new Object();
    assertThrows(UnsupportedOperationException.class, () -> {
      prop.setAndReturn(instance, value);
    });
  }
}