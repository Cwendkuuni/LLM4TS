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
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCreateNullTargetType() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreateTypeAdapterClass() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(mock(TypeAdapter.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
    assertThat(typeAdapter).isInstanceOf(TypeAdapter.class);
  }

  @Test
  public void testCreateTypeAdapterFactory() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(typeAdapterFactory);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
    assertThat(typeAdapter).isInstanceOf(TypeAdapter.class);
  }

  @Test
  public void testCreateJsonAdapterValueIsNotNull() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    String value = "TypeAdapterClass";
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(mock(TypeAdapter.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    JsonAdapter annotation = new JsonAdapter();
    annotation.value(value);
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
    assertThat(typeAdapter).isInstanceOf(TypeAdapter.class);
    TypeAdapter<?> expectedTypeAdapter = mock(TypeAdapter.class);
    when(expectedTypeAdapter.nullSafe()).thenReturn(expectedTypeAdapter);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(expectedTypeAdapter);
    TypeAdapter<?> actualTypeAdapter = factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
    assertThat(actualTypeAdapter).isSameInstanceAs(expectedTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateJsonAdapterValueIsNull() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    JsonAdapter annotation = new JsonAdapter();
    factory.create(gson, targetType);
  }
}