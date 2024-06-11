package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private Gson gson;
  private ConstructorConstructor constructorConstructor;

  @Before
  public void setup() {
    gson = mock(Gson.class);
    constructorConstructor = mock(ConstructorConstructor.class);
  }

  @Test
  public void testCreate_AnnotationPresent_TypeAdapterClass() {
    // Given
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(TypeAdapterStringAdapter.class);
    TypeToken<String> targetType = new TypeToken<String>() {};

    // When
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    // Then
    assertThat(typeAdapter).isInstanceOf(TypeAdapterStringAdapter.class);
  }

  @Test
  public void testCreate_AnnotationPresent_TypeAdapterFactoryClass() {
    // Given
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(JsonAdapterStringFactory.class);
    TypeToken<String> targetType = new TypeToken<String>() {};

    // When
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(typeAdapterFactory.create(gson, targetType)).thenReturn(new StringAdapter());
    when(constructorConstructor.get(TypeToken.get(JsonAdapterStringFactory.class))).thenReturn(typeAdapterFactory);
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    // Then
    assertThat(typeAdapter).isInstanceOf(StringAdapter.class);
  }

  @Test
  public void testCreate_AnnotationAbsent() {
    // Given
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};

    // When
    TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    // Then
    assertThat(typeAdapter).isNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_AnnotationValueInvalid() {
    // Given
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(new Object() {});
    TypeToken<String> targetType = new TypeToken<String>() {};

    // When & Then
    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
  }
}

class StringAdapter extends TypeAdapter<String> {}
class JsonAdapterStringFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    return new StringAdapter();
  }
}
class TypeAdapterStringAdapter extends TypeAdapter<String> {}