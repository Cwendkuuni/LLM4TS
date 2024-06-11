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
  public void testCreate_noJsonAdapterAnnotation_returnsNull() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    TypeAdapter<Integer> expected = null;
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(expected);

    TypeAdapter<Integer> result = factory.create(gson, typeToken);

    assertThat(result).isNull();
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapter() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
    TypeAdapter<String> expected = new StringAdapter();
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(expected);

    TypeAdapter<String> result = factory.create(gson, typeToken);

    assertThat(result).isInstanceOf(StringAdapter.class);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapterFactory() {
    TypeToken<Integer[]> typeToken = new TypeToken<Integer[]>() {};
    JsonAdapter annotation = new JsonAdapter(IntegerArrayTypeAdapterFactory.class);
    TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(factory);
    when(factory.create(gson, typeToken)).thenReturn(new IntegerArrayTypeAdapter(gson));

    TypeAdapter<Integer[]> result = factory.create(gson, typeToken);

    assertThat(result).isInstanceOf(IntegerArrayTypeAdapter.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_invalidValue() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    JsonAdapter annotation = new JsonAdapter(Object.class);

    factory.create(gson, typeToken);
  }
}

class StringAdapter extends TypeAdapter<String> {}
class IntegerArrayTypeAdapter extends TypeAdapter<Integer[]> {}
class IntegerArrayTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public TypeAdapter<Integer[]> create(Gson gson, TypeToken<Integer[]> targetType) {
    return new IntegerArrayTypeAdapter(gson);
  }
}