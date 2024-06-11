package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
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
  public void testCreate_valueIsTypeAdapter() {
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> expected = typeAdapter;

    TypeAdapter<String> result = factory.create(gson, targetType);

    assertThat(result).isSameInstanceAs(expected);
  }

  @Test
  public void testCreate_valueIsTypeAdapterFactory() {
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(typeAdapterFactory);

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> expected = mock(TypeAdapter.class);
    when(typeAdapterFactory.create(gson, targetType)).thenReturn(expected);

    TypeAdapter<String> result = factory.create(gson, targetType);

    assertThat(result).isSameInstanceAs(expected);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_valueIsNotTypeAdapterOrTypeAdapterFactory() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    TypeToken<String> targetType = new TypeToken<String>() {};
    String value = "NotTypeAdapterOrTypeAdapterFactory";

    factory.create(gson, targetType);
  }

  @Test
  public void testCreate_nullGson() {
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    TypeToken<String> targetType = new TypeToken<String>() {};
    Gson gson = null;

    TypeAdapter<String> result = factory.create(gson, targetType);

    assertNull(result);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass() {
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(TypeAdapter.class);

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> expected = typeAdapter;

    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, targetType, annotation);

    assertThat(result).isSameInstanceAs(expected);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryClass() {
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(typeAdapterFactory);

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(TypeAdapterFactory.class);

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> expected = mock(TypeAdapter.class);
    when(typeAdapterFactory.create(gson, targetType)).thenReturn(expected);

    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, targetType, annotation);

    assertThat(result).isSameInstanceAs(expected);
  }
}