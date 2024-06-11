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

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_NullTypeToken() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, typeToken));
  }

  @Test
  public void testCreate_JsonAdapterClass() {
    JsonAdapter annotation = new JsonAdapter(MyJsonAdapter.class);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapter<String> expected = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(MyJsonAdapter.class))).thenReturn(expected);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> actual = factory.create(gson, typeToken);
    assertThat(actual).isSameInstanceAs(expected);
  }

  @Test
  public void testCreate_JsonAdapterFactoryClass() {
    JsonAdapter annotation = new JsonAdapter(MyJsonAdapterFactory.class);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapterFactory<MyJsonAdapter> expected = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(MyJsonAdapterFactory.class))).thenReturn(expected);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> actual = factory.create(gson, typeToken);
    assertThat(actual).isInstanceOf(MyJsonAdapter.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidJsonAdapterValue() {
    JsonAdapter annotation = new JsonAdapter("InvalidType");
    TypeToken<String> typeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, typeToken);
  }
}

class MyJsonAdapter implements TypeAdapter<String> {}
class MyJsonAdapterFactory implements TypeAdapterFactory<MyJsonAdapter> {}