package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;

public class SetterlessPropertyTest {
  private SetterlessProperty setterlessProperty;
  private BeanPropertyDefinition propertyDefinition;
  private JavaType type;
  private TypeDeserializer deserializer;
  private Annotations contextAnnotations;
  private AnnotatedMethod method;
  
  @Before
  public void setUp() {
    propertyDefinition = mock(BeanPropertyDefinition.class);
    type = mock(JavaType.class);
    deserializer = mock(TypeDeserializer.class);
    contextAnnotations = mock(Annotations.class);
    method = mock(AnnotatedMethod.class);
    
    setterlessProperty = new SetterlessProperty(propertyDefinition, type, deserializer, contextAnnotations, method);
  }
  
  @Test
  public void testConstructorAndGetters() {
    assertEquals(propertyDefinition, setterlessProperty.getMember());
  }
  
  @Test
  public void testWithMethods() {
    SetterlessProperty result = setterlessProperty.withName(mock(PropertyName.class));
    assertEquals(setterlessProperty, result);
    
    result = setterlessProperty.withValueDeserializer(mock(JsonDeserializer.class));
    assertEquals(setterlessProperty, result);
    
    result = setterlessProperty.withNullProvider(mock(NullValueProvider.class));
    assertEquals(setterlessProperty, result);
  }
  
  @Test
  public void testFixAccess() {
    setterlessProperty.fixAccess(mock(DeserializationConfig.class));
  }
  
  @Test
  public void testGetAnnotation() {
    Annotation result = setterlessProperty.getAnnotation(mock(Class.class));
    assertNull(result);
  }
  
  @Test
  public void testDeserializeAndSet() throws IOException {
    JsonParser parser = mock(JsonParser.class);
    DeserializationContext context = mock(DeserializationContext.class);
    Object instance = mock(Object.class);
    
    setterlessProperty.deserializeAndSet(parser, context, instance);
  }
  
  @Test
  public void testDeserializeSetAndReturn() throws IOException {
    JsonParser parser = mock(JsonParser.class);
    DeserializationContext context = mock(DeserializationContext.class);
    Object instance = mock(Object.class);
    
    Object result = setterlessProperty.deserializeSetAndReturn(parser, context, instance);
    assertEquals(instance, result);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testSet() throws IOException {
    setterlessProperty.set(mock(Object.class), mock(Object.class));
  }
  
  @Test
  public void testSetAndReturn() throws IOException {
    Object instance = mock(Object.class);
    Object value = mock(Object.class);
    
    Object result = setterlessProperty.setAndReturn(instance, value);
    assertEquals(instance, result);
  }
}