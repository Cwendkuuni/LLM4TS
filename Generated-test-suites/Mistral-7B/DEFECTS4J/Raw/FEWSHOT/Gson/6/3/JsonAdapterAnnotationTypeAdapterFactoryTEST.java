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
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreateNullTypeToken() {
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreateWithJsonAdapterAnnotation() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter();
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(mock(TypeAdapter.class));

    TypeAdapter<String> typeAdapter = factory.create(gson, typeToken);
    assertThat(typeAdapter).isInstanceOf(TypeAdapter.class);
  }

  @Test
  public void testCreateWithTypeAdapterValue() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
    when(constructorConstructor.get(TypeToken.get(typeAdapterClass)))
        .thenReturn(mock(TypeAdapter.class));

    JsonAdapter annotation = new JsonAdapter(TypeAdapter.class);
    TypeAdapter<String> typeAdapter = factory.create(gson, typeToken);
    assertThat(typeAdapter).isInstanceOf(TypeAdapter.class);
  }

  @Test
  public void testCreateWithTypeAdapterFactoryValue() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    Class<TypeAdapterFactory> typeAdapterFactoryClass = TypeAdapterFactory.class;
    when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass)))
        .thenReturn(mock(TypeAdapterFactory.class));

    JsonAdapter annotation = new JsonAdapter(TypeAdapterFactory.class);
    TypeAdapter<String> typeAdapter = factory.create(gson, typeToken);
    assertThat(typeAdapter).isInstanceOf(TypeAdapter.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidJsonAdapterValue() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter("InvalidValue");
    factory.create(gson, typeToken);
  }
}