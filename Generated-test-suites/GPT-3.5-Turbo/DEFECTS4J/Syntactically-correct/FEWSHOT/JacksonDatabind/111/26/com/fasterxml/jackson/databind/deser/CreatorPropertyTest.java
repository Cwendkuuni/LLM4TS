package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.PropertyName;

public class CreatorPropertyTest {

  @Mock
  private PropertyName mockPropertyName;
  
  @Mock
  private JavaType mockJavaType;
  
  @Test
  public void testConstructor() {
    CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockPropertyName, null, null, null, 0, null, null);
    
    assertNotNull(creatorProperty);
    assertSame(mockPropertyName, creatorProperty.getName());
    assertSame(mockJavaType, creatorProperty.getType());
    assertSame(mockPropertyName, creatorProperty.getWrapperName());
    assertNull(creatorProperty.getValueDeserializer());
    assertNull(creatorProperty.getInjectableValueId());
    assertNull(creatorProperty.getFallbackSetter());
    assertFalse(creatorProperty.isIgnorable());
    assertEquals(0, creatorProperty.getCreatorIndex());
  }

  @Test
  public void testWithMethods() {
    CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockPropertyName, null, null, null, 0, null, null);
    
    CreatorProperty newCreatorProperty = creatorProperty.withName(mockPropertyName);
    assertNotSame(creatorProperty, newCreatorProperty);
    assertSame(mockPropertyName, newCreatorProperty.getName());
    assertSame(mockJavaType, newCreatorProperty.getType());
    assertSame(mockPropertyName, newCreatorProperty.getWrapperName());
    assertNull(newCreatorProperty.getValueDeserializer());
    assertNull(newCreatorProperty.getInjectableValueId());
    assertNull(newCreatorProperty.getFallbackSetter());
    assertFalse(newCreatorProperty.isIgnorable());
    assertEquals(0, newCreatorProperty.getCreatorIndex());

    CreatorProperty newCreatorProperty2 = creatorProperty.withValueDeserializer(null);
    assertNotSame(creatorProperty, newCreatorProperty2);
    assertSame(mockPropertyName, newCreatorProperty2.getName());
    assertSame(mockJavaType, newCreatorProperty2.getType());
    assertSame(mockPropertyName, newCreatorProperty2.getWrapperName());
    assertNull(newCreatorProperty2.getValueDeserializer());
    assertNull(newCreatorProperty2.getInjectableValueId());
    assertNull(newCreatorProperty2.getFallbackSetter());
    assertFalse(newCreatorProperty2.isIgnorable());
    assertEquals(0, newCreatorProperty2.getCreatorIndex());

    CreatorProperty newCreatorProperty3 = creatorProperty.withNullProvider(null);
    assertNotSame(creatorProperty, newCreatorProperty3);
    assertSame(mockPropertyName, newCreatorProperty3.getName());
    assertSame(mockJavaType, newCreatorProperty3.getType());
    assertSame(mockPropertyName, newCreatorProperty3.getWrapperName());
    assertNull(newCreatorProperty3.getValueDeserializer());
    assertNull(newCreatorProperty3.getInjectableValueId());
    assertNull(newCreatorProperty3.getFallbackSetter());
    assertFalse(newCreatorProperty3.isIgnorable());
    assertEquals(0, newCreatorProperty3.getCreatorIndex());

    MockitoAnnotations.initMocks(this);
    creatorProperty.setFallbackSetter(mockPropertySetter);
    assertSame(mockPropertySetter, creatorProperty.getFallbackSetter());
    
    creatorProperty.markAsIgnorable();
    assertTrue(creatorProperty.isIgnorable());
    
    assertNull(creatorProperty.getInjectableValueId());
    
    creatorProperty.toString();
  }

  @Test(expected = IOException.class)
  public void testDeserializeAndSet() throws IOException {
    CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockPropertyName, null, null, null, 0, null, null);    
    creatorProperty.deserializeAndSet(null, null, null);
  }

  @Test(expected = IOException.class)
  public void testDeserializeSetAndReturn() throws IOException {
    CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockPropertyName, null, null, null, 0, null, null);    
    creatorProperty.deserializeSetAndReturn(null, null, null);
  }

  @Test(expected = IOException.class)
  public void testSet() throws IOException {
    CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockPropertyName, null, null, null, 0, null, null);    
    creatorProperty.set(null, null);
  }

  @Test(expected = IOException.class)
  public void testSetAndReturn() throws IOException {
    CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockPropertyName, null, null, null, 0, null, null);    
    creatorProperty.setAndReturn(null, null);
  }
}