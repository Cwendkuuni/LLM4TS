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
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreateNullTypeAdapter() {
    TypeToken<String> targetType = TypeToken.get(String.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    assertNull(factory.create(gson, targetType));
  }

  @Test
  public void testCreateTypeAdapter() {
    TypeToken<String> targetType = TypeToken.get(String.class);
    JsonAdapter annotation = TypeToken.get(String.class).getRawType().getAnnotation(JsonAdapter.class);
    Class<TypeAdapter<String>> typeAdapterClass = mock(TypeAdapter.class);

    when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(typeAdapterClass);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);

    assertThat(typeAdapter).isInstanceOf(typeAdapterClass);
  }

  @Test
  public void testCreateTypeAdapterFactory() {
    TypeToken<String> targetType = TypeToken.get(String.class);
    JsonAdapter annotation = TypeToken.get(String.class).getRawType().getAnnotation(JsonAdapter.class);
    Class<JsonAdapterAnnotationTypeAdapterFactory> typeAdapterFactoryClass = mock(JsonAdapterAnnotationTypeAdapterFactory.class);

    when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(typeAdapterFactoryClass);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);

    assertThat(typeAdapter).isNotNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateInvalidValue() {
    TypeToken<String> targetType = TypeToken.get(String.class);
    JsonAdapter annotation = TypeToken.get(String.class).getRawType().getAnnotation(JsonAdapter.class);
    Class<?> value = new Object();

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  }
}