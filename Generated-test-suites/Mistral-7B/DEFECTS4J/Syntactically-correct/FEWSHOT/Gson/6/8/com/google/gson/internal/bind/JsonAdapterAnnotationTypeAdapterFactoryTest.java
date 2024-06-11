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
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_ReturnsTypeAdapter() {
    // Given
    TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @JsonAdapter Class<? extends TypeAdapter<Integer>> value() {
        return TypeAdapter.class;
      }
    };

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(mock(TypeAdapter.class));

    // When
    TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, integerTypeToken);

    // Then
    assertThat(typeAdapter).isInstanceOf(TypeAdapter.class);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_TypeAdapterClass() {
    // Given
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @JsonAdapter Class<? extends TypeAdapter<String>> value() {
        return StringTypeAdapter.class;
      }
    };
    TypeAdapter<String> stringTypeAdapter = mock(StringTypeAdapter.class);

    when(constructorConstructor.get(TypeToken.get(StringTypeAdapter.class)))
        .thenReturn(stringTypeAdapter);

    // When
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, stringTypeToken);

    // Then
    assertThat(typeAdapter).isSameInstanceAs(stringTypeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_TypeAdapterFactoryClass() {
    // Given
    TypeToken<List<Integer>> listIntegerTypeToken = new TypeToken<List<Integer>>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @JsonAdapter Class<? extends TypeAdapterFactory<?>> value() {
        return ListTypeAdapterFactory.class;
      }
    };
    ListTypeAdapterFactory listTypeAdapterFactory = mock(ListTypeAdapterFactory.class);
    when(listTypeAdapterFactory.create(gson, listIntegerTypeToken)).thenReturn(mock(TypeAdapter.class));

    // When
    TypeAdapter<List<Integer>> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, listIntegerTypeToken);

    // Then
    assertThat(typeAdapter).isInstanceOf(TypeAdapter.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotationNotFound() {
    // Given
    TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {};

    // When
    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor).create(gson, integerTypeToken);

    // Then
    // This test should throw an IllegalArgumentException
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotationValueNotTypeAdapterOrFactory() {
    // Given
    TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @JsonAdapter String value() {
        return "Not a valid value";
      }
    };

    // When
    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor).create(gson, integerTypeToken);

    // Then
    // This test should throw an IllegalArgumentException
  }
}

class StringTypeAdapter {}
class ListTypeAdapterFactory implements TypeAdapterFactory<List<?>> {
  @Override
  public <T> TypeAdapter<List<T>> create(Gson gson, TypeToken<List<T>> typeToken) {
    return null;
  }
}