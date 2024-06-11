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

/**
 * Test cases for {@link JsonAdapterAnnotationTypeAdapterFactory} class.
 */
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
  public void testCreateWithTypeAdapterAnnotation() {
    // Given
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter();
    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapter<String> expectedTypeAdapter = mock(TypeAdapter.class);

    // When
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(()-> expectedTypeAdapter);

    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> result = factory.create(gson, typeToken);

    // Then
    assertThat(result).isSameInstanceAs(expectedTypeAdapter);
  }

  @Test
  public void testCreateWithTypeAdapterFactoryAnnotation() {
    // Given
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter();
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    TypeAdapterFactory expectedTypeAdapterFactory = mock(TypeAdapterFactory.class);

    // When
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(()-> expectedTypeAdapterFactory);

    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<Integer> result = factory.create(gson, typeToken);

    // Then
    TypeAdapter<?> adapter = expectedTypeAdapterFactory.create(gson, typeToken);
    assertThat(result).isInstanceOf(adapter.getClass());
  }

  @Test
  public void testCreateWithNullAnnotation() {
    // Given
    TypeToken<String> typeToken = new TypeToken<String>() {};

    // When
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> result = factory.create(gson, typeToken);

    // Then
    assertThat(result).isNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidAnnotationValue() {
    // Given
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter();
    TypeToken<String> typeToken = new TypeToken<String>() {};
    jsonAdapterAnnotation.value("invalid");

    // When
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, typeToken);

    // Then
    // The exception should be thrown here.
  }
}