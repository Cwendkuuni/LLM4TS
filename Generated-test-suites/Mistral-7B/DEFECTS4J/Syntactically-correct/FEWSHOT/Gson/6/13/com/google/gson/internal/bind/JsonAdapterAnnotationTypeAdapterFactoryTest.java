package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertNotNull;
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

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
    JsonAdapter annotation = new JsonAdapter(MyTypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(mock(TypeAdapter.class));

    TypeAdapter<MyType> typeAdapter = (TypeAdapter<MyType>) new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, new TypeToken<MyType>() {}.getType());

    assertNotNull(typeAdapter);
  }

  @Test
  public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapterFactory() {
    JsonAdapter annotation = new JsonAdapter(MyTypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(mock(TypeAdapterFactory.class));

    TypeAdapterFactory typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<MyType> typeAdapter = typeAdapterFactory.create(gson, new TypeToken<MyType>() {}.getType());

    assertNotNull(typeAdapter);
  }

  @Test
  public void testCreate_NullAnnotation_ReturnsNull() {
    TypeAdapter<MyType> typeAdapter = (TypeAdapter<MyType>) new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, new TypeToken<MyType>() {}.getType());

    assertNull(typeAdapter);
  }

  @Test
  public void testCreate_InvalidAnnotation_ThrowsIllegalArgumentException() {
    JsonAdapter annotation = new JsonAdapter("InvalidType");

    try {
      new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
          .create(gson, new TypeToken<MyType>() {}.getType());
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
    }
  }
}

class MyType {}
class MyTypeAdapter {}
class MyTypeAdapterFactory implements TypeAdapterFactory {}