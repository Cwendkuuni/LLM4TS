package com.google.gson.internal.bind;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private Gson gson;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreateNull() {
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreateNoAnnotation() {
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    assertNull(factory.create(gson, targetType));
  }

  @Test
  public void testCreateJsonAdapter() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    TypeToken<String> targetType = new TypeToken<String>() {};
    when(annotation.value()).thenReturn(StringAdapter.class);

    TypeAdapter<String> expected = new StringAdapter();
    when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(expected);

    TypeAdapter<String> actual = factory.create(gson, targetType);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testCreateTypeAdapterFactory() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    when(annotation.value()).thenReturn(NumberAdapterFactory.class);

    TypeAdapterFactory expected = NumberAdapterFactory.INSTANCE;
    when(constructorConstructor.get(TypeToken.get(NumberAdapterFactory.class)))
        .thenReturn(expected);

    TypeAdapterFactory actual = factory.create(gson, targetType);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateInvalidValue() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    when(annotation.value()).thenReturn(Object.class);

    factory.create(gson, targetType);
  }

  private static class StringAdapter extends TypeAdapter<String> {
    @Override
    public String read(JsonReader in) throws IOException {
      return null;
    }

    @Override
    public void write(JsonWriter out, String value) throws IOException {
    }
  }

  private static final class NumberAdapterFactory implements TypeAdapterFactory {
    public static final NumberAdapterFactory INSTANCE = new NumberAdapterFactory();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return (TypeAdapter<T>) new NumberAdapter<T>();
    }
  }

  private static class NumberAdapter<T> extends TypeAdapter<T> {
    @Override
    public T read(JsonReader in) throws IOException {
      return null;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
    }
  }
}