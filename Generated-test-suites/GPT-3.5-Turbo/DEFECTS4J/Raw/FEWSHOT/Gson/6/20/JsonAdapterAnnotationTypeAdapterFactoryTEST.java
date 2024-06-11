package com.google.gson.internal.bind;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;

import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private TypeToken<?> targetType;
  private JsonAdapter annotation;
  private TypeAdapter<?> typeAdapter;
  
  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    targetType = mock(TypeToken.class);
    annotation = mock(JsonAdapter.class);
    typeAdapter = mock(TypeAdapter.class);
  }
  
  @Test
  public void testCreate_shouldReturnNullIfAnnotationIsNull() {
    when(targetType.getRawType()).thenReturn(Object.class);
    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(null);
    
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> result = factory.create(gson, targetType);
    
    assertNull(result);
  }
  
  @Test
  public void testCreate_shouldReturnTypeAdapterIfAnnotationIsNotNull() {
    when(targetType.getRawType()).thenReturn(Object.class);
    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
    when(annotation.value()).thenReturn(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(constructorConstructor);
    when(constructorConstructor.construct()).thenReturn(typeAdapter);
    
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> result = factory.create(gson, targetType);
    
    assertNotNull(result);
    verify(typeAdapter).nullSafe();
  }
  
  @Test
  public void testCreate_shouldReturnTypeAdapterFactoryIfAnnotationIsNotNull() {
    when(targetType.getRawType()).thenReturn(Object.class);
    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(annotation);
    when(annotation.value()).thenReturn(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(constructorConstructor);
    when(constructorConstructor.construct()).thenReturn(typeAdapter);
    when(typeAdapter.create(gson, targetType)).thenReturn(typeAdapter);
    
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> result = factory.create(gson, targetType);
    
    assertNotNull(result);
    verify(typeAdapter).nullSafe();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_shouldThrowExceptionIfValueIsNotTypeAdapterOrTypeAdapterFactory() {
    Class<?> value = Integer.class;
    when(annotation.value()).thenReturn(value);
    
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  }
  
}