package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_noAnnotation() {
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    TypeAdapter<Object> result = factory.create(gson, targetType);
    assertNull(result);
  }

  @Test
  public void testCreate_annotationValueIsTypeAdapter() {
    TypeToken<Object> targetType = new TypeToken<Object>() {}.getRawType().getAnnotation(JsonAdapter.class);
    when(constructorConstructor.get(TypeToken.get(Object.class))).thenReturn(mock(TypeAdapter.class));
    TypeAdapter<Object> result = factory.create(gson, targetType);
    // Assert result is type adapter instance
  }

  @Test
  public void testCreate_annotationValueIsTypeAdapterFactory() {
    TypeToken<Object> targetType = new TypeToken<Object>() {}.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(typeAdapterFactory)).construct().create(gson, targetType))
        .thenReturn(mock(TypeAdapter.class));
    TypeAdapter<Object> result = factory.create(gson, targetType);
    // Assert result is type adapter instance
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_annotationValueInvalid() {
    TypeToken<Object> targetType = new TypeToken<Object>() {}.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(typeAdapterFactory)).construct().create(gson, targetType))
        .thenReturn(mock(TypeAdapter.class));
    factory.create(gson, targetType);
  }
}