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
  public void setup() {
    MockitoAnnotations.initMocks(this);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreate_NullTypeToken_ReturnsNull() {
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_ReturnsTypeAdapter() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    String value = TypeAdapter.class.getName();
    JsonAdapter annotation = new JsonAdapter(value);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(new DummyTypeAdapter());

    TypeAdapter<String> typeAdapter = factory.create(gson, typeToken);
    assertThat(typeAdapter).isNotNull();
    assertThat(typeAdapter).isInstanceOf(DummyTypeAdapter.class);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_ReturnsTypeAdapterFactory() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    String value = TypeAdapterFactory.class.getName();
    JsonAdapter annotation = new JsonAdapter(value);

    TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(factoryMock);
    when(factoryMock.create(gson, typeToken)).thenReturn(new DummyTypeAdapter());

    TypeAdapter<Integer> typeAdapter = factory.create(gson, typeToken);
    assertThat(typeAdapter).isNotNull();
    assertThat(typeAdapter).isInstanceOf(DummyTypeAdapter.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotationValueInvalidType_ThrowsException() {
    TypeToken<Double> typeToken = new TypeToken<Double>() {};
    String value = Object.class.getName();
    JsonAdapter annotation = new JsonAdapter(value);

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(null);

    factory.create(gson, typeToken);
  }

  static class DummyTypeAdapter extends TypeAdapter<Object> {}
}