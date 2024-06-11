package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import org.junit.Assert;

public class CreatorPropertyTest {
  
  @Test
  public void testConstructor() {
    PropertyName name = new PropertyName("name");
    JavaType type = JavaType.STRING;
    PropertyName wrapperName = new PropertyName("wrapperName");
    TypeDeserializer typeDeser = new TypeDeserializer(null, null, null);
    Annotations contextAnnotations = null;
    AnnotatedParameter param = new AnnotatedParameter(null, null, null);
    int index = 0;
    Object injectableValueId = new Object();
    PropertyMetadata metadata = new PropertyMetadata(null, null);
    
    CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
    
    Assert.assertEquals(name, property.getFullName());
    Assert.assertEquals(type, property.getType());
    Assert.assertEquals(wrapperName, property.getWrapperName());
    Assert.assertEquals(typeDeser, property.getTypeDeserializer());
    Assert.assertEquals(contextAnnotations, property.getContextAnnotations());
    Assert.assertEquals(param, property.getMember());
    Assert.assertEquals(index, property.getCreatorIndex());
    Assert.assertEquals(injectableValueId, property.getInjectableValueId());
    Assert.assertEquals(metadata, property.getMetadata());
  }
  
  @Test
  public void testSetFallbackSetter() {
    CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    SettableBeanProperty fallbackSetter = new SettableBeanProperty(null, null, null, null, null, null, null);
    
    property.setFallbackSetter(fallbackSetter);
    
    Assert.assertEquals(fallbackSetter, property.getFallbackSetter());
  }
  
  @Test
  public void testMarkAsIgnorable() {
    CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    
    property.markAsIgnorable();
    
    Assert.assertTrue(property.isIgnorable());
  }
  
  @Test
  public void testGetAnnotation() {
    CreatorProperty property = new CreatorProperty(null, null, null, null, null, new AnnotatedParameter(null, null, null), 0, null, null);
    Class<A> acls = A.class;
    
    Assert.assertNull(property.getAnnotation(acls));
  }
  
  @Test
  public void testDeserializeAndSet() {
    CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Object instance = new Object();
    
    property.deserializeAndSet(p, ctxt, instance);
  }
  
  @Test
  public void testDeserializeSetAndReturn() {
    CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Object instance = new Object();
    
    Object result = property.deserializeSetAndReturn(p, ctxt, instance);
    
    Assert.assertNull(result);
  }
  
  @Test
  public void testSet() {
    CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    Object instance = new Object();
    Object value = new Object();
    
    property.set(instance, value);
  }
  
  @Test
  public void testSetAndReturn() {
    CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    Object instance = new Object();
    Object value = new Object();
    
    Object result = property.setAndReturn(instance, value);
    
    Assert.assertNull(result);
  }
  
  @Test
  public void testToString() {
    CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    
    Assert.assertEquals("[creator property, name 'null'; inject id 'null']", property.toString());
  }
  
  private static class A {}
}