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
  public void testCreateNullTypeAdapterWhenNoJsonAdapterAnnotationPresent() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    assertNull(factory.create(gson, typeToken));
  }

  @Test
  public void testCreateTypeAdapterWhenJsonAdapterAnnotationValueIsTypeAdapter() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    TypeAdapter<String> stringTypeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(new Object() {
          @SuppressWarnings("unchecked")
          public TypeAdapter<String> construct() {
            return (TypeAdapter<String>) stringTypeAdapter;
          }
        });
    JsonAdapter jsonAdapterAnnotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<String> typeAdapter = factory.create(gson, stringTypeToken);
    assertThat(typeAdapter).isEqualTo(stringTypeAdapter);
  }

  @Test
  public void testCreateTypeAdapterFactoryWhenJsonAdapterAnnotationValueIsTypeAdapterFactory() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    TypeAdapterFactory<String> stringTypeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(new Object() {
          @SuppressWarnings("unchecked")
          public TypeAdapterFactory<String> construct() {
            return (TypeAdapterFactory<String>) stringTypeAdapterFactory;
          }
        });
    JsonAdapter jsonAdapterAnnotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<String> typeAdapter = factory.create(gson, stringTypeToken);
    assertThat(typeAdapter).isInstanceOf(stringTypeAdapterFactory.create(gson, stringTypeToken).getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTypeAdapterWhenJsonAdapterAnnotationValueIsNeitherTypeAdapterNorTypeAdapterFactory() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenThrow(new UnsupportedOperationException());
    factory.create(gson, stringTypeToken);
  }
}