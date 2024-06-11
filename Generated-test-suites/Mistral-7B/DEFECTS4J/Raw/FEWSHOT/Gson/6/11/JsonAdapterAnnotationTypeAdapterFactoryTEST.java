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

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    TypeAdapter<?> adapter = factory.create(gson, targetType);
    assertNull(adapter);
  }

  @Test
  public void testCreate_JsonAdapterValueIsTypeAdapter_CreatesTypeAdapterInstance() {
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
    when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(typeAdapter);

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertThat(adapter).isSameInstanceAs(typeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterValueIsTypeAdapterFactory_CreatesTypeAdapterFromTypeAdapterFactory() {
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    Class<TypeAdapterFactory> typeAdapterFactoryClass = TypeAdapterFactory.class;
    when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(typeAdapterFactory);

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    TypeAdapter<?> expectedAdapter = mock(TypeAdapter.class);
    when(typeAdapterFactory.create(gson, targetType)).thenReturn(expectedAdapter);
    assertThat(adapter).isSameInstanceAs(expectedAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterValueIsNeitherTypeAdapterNorTypeAdapterFactory_ThrowsException() {
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, targetType);
  }
}