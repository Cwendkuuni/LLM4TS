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

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreateTypeAdapter_JsonAdapterAnnotation_TypeAdapter() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(new StringAdapter());

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> actual = factory.create(gson, targetType);

    TypeAdapter<String> expected = new StringAdapter();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testCreateTypeAdapter_JsonAdapterAnnotation_TypeAdapterFactory() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(StringAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(new StringAdapterFactory());

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> actual = factory.create(gson, targetType);

    TypeAdapterFactory factoryInstance = new StringAdapterFactory();
    TypeAdapter<String> expected = factoryInstance.create(gson, targetType);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testCreateTypeAdapter_NoJsonAdapterAnnotation() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(null);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> actual = factory.create(gson, targetType);

    assertThat(actual).isNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTypeAdapter_InvalidJsonAdapterAnnotation() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(Object.class);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, targetType);
  }
}

class StringAdapter implements TypeAdapter<String> {}
class StringAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    return new StringAdapter();
  }
}