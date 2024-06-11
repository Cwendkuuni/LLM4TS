package com.google.gson.internal.bind;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private Gson gson;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testNullInput() {
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testNoJsonAdapterAnnotation() {
    assertNull(factory.create(gson, new TypeToken<Object>() {}));
  }

  @Test
  public void testTypeAdapter() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(TypeAdapter.class);
    Class<TypeAdapter<String>> typeAdapterClass = mock(Class.class);
    when(typeAdapterClass.isAssignableFrom(TypeAdapter.class)).thenReturn(true);
    when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(mock(TypeAdapter.class));

    TypeAdapter<String> expectedTypeAdapter = mock(TypeAdapter.class);
    when(expectedTypeAdapter.nullSafe()).thenReturn(expectedTypeAdapter);

    TypeAdapter<String> actualTypeAdapter = factory.create(gson, new TypeToken<String>() {});

    assertInstanceOf(expectedTypeAdapter.getClass(), actualTypeAdapter);
  }

  @Test
  public void testTypeAdapterFactory() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(TypeAdapterFactory.class);
    Class<TypeAdapterFactory> typeAdapterFactoryClass = mock(Class.class);
    when(typeAdapterFactoryClass.isAssignableFrom(TypeAdapterFactory.class)).thenReturn(true);
    when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass)))
        .thenReturn(mock(TypeAdapterFactory.class));

    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(typeAdapterFactory.create(gson, new TypeToken<Object>() {})).thenReturn(mock(TypeAdapter.class));

    TypeAdapter<Object> expectedTypeAdapter = mock(TypeAdapter.class);
    when(expectedTypeAdapter.nullSafe()).thenReturn(expectedTypeAdapter);

    TypeAdapter<Object> actualTypeAdapter = factory.create(gson, new TypeToken<Object>() {});

    assertInstanceOf(expectedTypeAdapter.getClass(), actualTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonAdapterValue() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn("String");

    factory.create(gson, new TypeToken<Object>() {});
  }
}