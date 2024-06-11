package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_ValueIsTypeAdapter() {
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> result = factory.create(gson, targetType);

    assertThat(result).isSameInstanceAs(typeAdapter);
  }

  @Test
  public void testCreate_ValueIsTypeAdapterFactory() {
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(typeAdapterFactory);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    TypeAdapter<Integer> result = factory.create(gson, targetType);

    TypeAdapter<Integer> expected = mock(TypeAdapter.class);
    when(typeAdapterFactory.create(gson, targetType)).thenReturn(expected);

    assertThat(result).isSameInstanceAs(expected);
  }

  @Test
  public void testCreate_ValueIsNotTypeAdapterOrTypeAdapterFactory() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> targetType = new TypeToken<Object>() {};

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      factory.create(gson, targetType);
    });

    String expectedMessage = "@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.";
    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
  }
}