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
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Test cases for the {@link JsonAdapterAnnotationTypeAdapterFactory} class.
 */
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Mock private ConstructorConstructor constructorConstructor;
  @Mock private Gson gson;
  @InjectMocks private JsonAdapterAnnotationTypeAdapterFactory factory;

  private TypeAdapter<String> stringAdapter;
  private TypeAdapter<Integer> intAdapter;
  private TypeAdapter<Long> longAdapter;

  @Before
  public void setUp() {
    when(constructorConstructor.get(TypeToken.get(String.class))).thenReturn(stringAdapter = mock(TypeAdapter.class));
    when(constructorConstructor.get(TypeToken.get(Integer.class))).thenReturn(intAdapter = mock(TypeAdapter.class));
    when(constructorConstructor.get(TypeToken.get(Long.class))).thenReturn(longAdapter = mock(TypeAdapter.class));
  }

  @Test
  public void testCreate_nullTypeToken() {
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreate_noJsonAdapterAnnotation() {
    assertNull(
        factory.create(gson, new TypeToken<String>() {}.where(new TypeNameTypeToken<String>() {})));
  }

  @Test
  public void testCreate_TypeAdapter() {
    TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);
    JsonAdapter annotation = new JsonAdapter(TypeAdapter.class, String.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);

    TypeAdapter<String> result = (TypeAdapter<String>) factory.create(gson, new TypeToken<String>() {}.where(new TypeNameTypeToken<String>() {}));
    assertThat(result).isSameInstanceAs(typeAdapter);
  }

  @Test
  public void testCreate_TypeAdapterFactory() {
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    JsonAdapter annotation = new JsonAdapter(TypeAdapterFactory.class, String.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(typeAdapterFactory);

    TypeAdapter<String> result =
        (TypeAdapter<String>) factory.create(gson, new TypeToken<String>() {}.where(new TypeNameTypeToken<String>() {}));
    assertThat(result).isInstanceOf(TypeAdapter.class);
    when(typeAdapterFactory.create(gson, new TypeToken<String>() {}.where(new TypeNameTypeToken<String>() {}))).thenReturn(stringAdapter);
    assertThat(result).isSameInstanceAs(stringAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_invalidJsonAdapterAnnotation() {
    JsonAdapter annotation = new JsonAdapter(Object.class, String.class);
    factory.create(gson, new TypeToken<String>() {}.where(new TypeNameTypeToken<String>() {}));
  }

  @Test
  public void testGetTypeAdapter_TypeAdapter() {
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);

    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}.where(new TypeNameTypeToken<String>() {}), new JsonAdapter(TypeAdapter.class, String.class));
    assertThat(result).isSameInstanceAs(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactory() {
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);

    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}.where(new TypeNameTypeToken<String>() {}), new JsonAdapter(TypeAdapterFactory.class, String.class));
    assertThat(result).isInstanceOf(TypeAdapter.class);
    when(typeAdapterFactory.create(gson, new TypeToken<String>() {}.where(new TypeNameTypeToken<String>() {}))).thenReturn(stringAdapter);
    assertThat(result).isSameInstanceAs(stringAdapter);
  }
}