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
  public void testCreate_noJsonAdapterAnnotation_returnsNull() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> result = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    assertNull(result);
  }

  @Test
  public void testCreate_TypeAdapterAnnotated_returnsTypeAdapterInstance() {
    String value = "TypeAdapterClass";
    Class<TypeAdapter<String>> typeAdapterClass = (Class<TypeAdapter<String>>) () -> new TypeAdapter<String>() {};
    when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(new TypeAdapter<String>() {});

    JsonAdapter annotation = new JsonAdapter();
    annotation.value(value);
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> result = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    TypeAdapter<String> expected = new TypeAdapter<String>() {};
    assertThat(result).isSameInstance(expected);
  }

  @Test
  public void testCreate_TypeAdapterFactoryAnnotated_returnsTypeAdapterInstance() {
    String value = "TypeAdapterFactoryClass";
    Class<TypeAdapterFactory<TypeAdapter<String>>> typeAdapterFactoryClass =
        (Class<TypeAdapterFactory<TypeAdapter<String>>>) () -> new TypeAdapterFactory<TypeAdapter<String>>() {};
    when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(
        new TypeAdapterFactory<TypeAdapter<String>>() {
          @Override
          public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return new TypeAdapter<T>() {};
          }
        });

    JsonAdapter annotation = new JsonAdapter();
    annotation.value(value);
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> result = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    TypeAdapter<String> expected = new TypeAdapter<String>() {};
    assertThat(result).isInstanceOf(expected.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_invalidJsonAdapterValue_throwsIllegalArgumentException() {
    JsonAdapter annotation = new JsonAdapter();
    annotation.value(new Object());
    TypeToken<String> targetType = new TypeToken<String>() {};
    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
  }
}