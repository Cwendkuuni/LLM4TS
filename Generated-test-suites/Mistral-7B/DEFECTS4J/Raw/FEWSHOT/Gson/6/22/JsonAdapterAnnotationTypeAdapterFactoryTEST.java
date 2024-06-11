package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_NoAnnotation() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(null);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> result = factory.create(gson, targetType);
    assertNull(result);
  }

  @Test
  public void testCreate_TypeAdapter() {
    String adapterClassName = "com.example.MyTypeAdapter";
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<TypeAdapter<String>> typeAdapterClass = (Class<TypeAdapter<String>>) (Class<?>) Class.forName(adapterClassName);
    when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(mock(TypeAdapter.class));
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(typeAdapterClass);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> result = factory.create(gson, targetType);
    assertThat(result).isInstanceOf(TypeAdapter.class);
  }

  @Test
  public void testCreate_TypeAdapterFactory() {
    String adapterFactoryClassName = "com.example.MyTypeAdapterFactory";
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<TypeAdapterFactory<String>> typeAdapterFactoryClass = (Class<TypeAdapterFactory<String>>) (Class<?>) Class.forName(adapterFactoryClassName);
    when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(mock(TypeAdapterFactory.class));
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(typeAdapterFactoryClass);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> result = factory.create(gson, targetType);
    assertTrue(result instanceof TypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_IllegalAnnotationValue() {
    String adapterClassName = "com.example.MyClass";
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<TypeAdapter<String>> typeAdapterClass = (Class<TypeAdapter<String>>) (Class<?>) Class.forName(adapterClassName);
    when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(mock(TypeAdapter.class));
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(adapterClassName); // incorrect value type
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, targetType);
  }
}