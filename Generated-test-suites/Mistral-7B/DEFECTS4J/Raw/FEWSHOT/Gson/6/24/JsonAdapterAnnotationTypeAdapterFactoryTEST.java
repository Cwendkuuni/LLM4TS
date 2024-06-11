package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
 * Test class for {@link JsonAdapterAnnotationTypeAdapterFactory}
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
  public void testCreateNullTypeAdapterIfNoJsonAdapterAnnotationPresent() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
    assertThat(typeAdapter).isNull();
  }

  @Test
  public void testCreateTypeAdapterFromJsonAdapterAnnotationValueTypeAdapter() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    JsonAdapter jsonAdapterAnnotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<String> stringTypeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(any(TypeToken.class))).thenReturn(stringTypeAdapter);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> createdTypeAdapter = factory.create(gson, stringTypeToken);
    assertThat(createdTypeAdapter).isEqualTo(stringTypeAdapter);
  }

  @Test
  public void testCreateTypeAdapterFromJsonAdapterAnnotationValueTypeAdapterFactory() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    JsonAdapter jsonAdapterAnnotation = intTypeToken.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapterFactory intTypeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(any(TypeToken.class))).thenReturn(intTypeAdapterFactory);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<Integer> createdTypeAdapter = factory.create(gson, intTypeToken);
    assertThat(createdTypeAdapter).isNotNull();
    TypeAdapter<?> typeAdapter = intTypeAdapterFactory.create(gson, intTypeToken);
    assertThat(createdTypeAdapter).isSameInstanceAs(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTypeAdapterFromJsonAdapterAnnotationValueInvalidType() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);
    String value = jsonAdapterAnnotation.value().toString();

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, stringTypeToken);

    // This should throw an Exception
  }
}