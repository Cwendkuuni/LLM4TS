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
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testCreateNullAnnotation() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    assertNull(factory.create(gson, targetType));
  }

  @Test
  public void testCreateTypeAdapterFromTypeAdapterClass() {
    Class<TypeAdapter<String>> typeAdapterClass = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(typeAdapterClass);

    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(typeAdapterClass);

    TypeAdapter<String> expectedTypeAdapter = typeAdapterClass;
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);

    assertThat(actualTypeAdapter).isEqualTo(expectedTypeAdapter);
  }

  @Test
  public void testCreateTypeAdapterFromTypeAdapterFactoryClass() {
    Class<JsonAdapterAnnotationTypeAdapterFactory> typeAdapterFactoryClass =
        JsonAdapterAnnotationTypeAdapterFactory.class;
    JsonAdapterAnnotationTypeAdapterFactory factoryMock = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    when(factoryMock.create(gson, TypeToken.get(String.class))).thenReturn(mock(TypeAdapter.class));

    when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(factoryMock);

    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(typeAdapterFactoryClass);

    TypeAdapter<String> expectedTypeAdapter = mock(TypeAdapter.class);
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);

    assertThat(actualTypeAdapter).isNotNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTypeAdapterFromInvalidValue() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn("InvalidValueClass");

    factory.create(gson, targetType);
  }
}