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
 * Tests for {@link JsonAdapterAnnotationTypeAdapterFactory} class.
 */
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
  public void testCreateTypeAdapterWithTypeAdapter() {
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);

    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapter<String> result = factory.create(gson, typeToken);

    assertThat(result).isSameInstanceAs(typeAdapter);
  }

  @Test
  public void testCreateTypeAdapterWithTypeAdapterFactory() {
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(typeAdapterFactory);

    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapter<String> result = factory.create(gson, typeToken);

    TypeAdapter<String> expected = typeAdapterFactory.create(gson, typeToken);
    assertThat(result).isSameInstanceAs(expected);
  }

  @Test
  public void testCreateNullWhenJsonAdapterAnnotationIsMissing() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapter<String> result = factory.create(gson, typeToken);

    assertThat(result).isNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidJsonAdapterValue() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @JsonAdapter(MyAdapter.class)
      public Class value() {
        return Integer.class;
      }
    };
    factory.create(gson, typeToken);
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterClass() {
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);

    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, new TypeToken<String>() {}, new JsonAdapter(String.class));

    assertThat(result).isSameInstanceAs(typeAdapter);
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterFactoryClass() {
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(typeAdapterFactory);

    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, new TypeToken<String>() {}, new JsonAdapter(TypeAdapterFactory.class));

    TypeAdapter<String> expected = typeAdapterFactory.create(gson, new TypeToken<String>() {});
    assertThat(result).isSameInstanceAs(expected);
  }
}