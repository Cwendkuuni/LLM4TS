package com.google.gson.internal.bind;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for {@link JsonAdapterAnnotationTypeAdapterFactory}.
 */
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private static final Class<String> STRING_TYPE = String.class;
  private static final TypeToken<String> STRING_TYPE_TOKEN = new TypeToken<String>() {};

  @InjectMocks
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Mock
  private ConstructorConstructor constructorConstructor;

  @Mock
  private Gson gson;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testNullFieldType() {
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(null);

    TypeAdapter<?> result = factory.create(gson, null);

    assertNull(result);
  }

  @Test
  public void testStringValueTypeAdapter() {
    StringAdapter stringAdapter = mock(StringAdapter.class);
    when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(stringAdapter);

    TypeAdapter<String> result = factory.create(gson, STRING_TYPE_TOKEN);

    assertInstanceOf(StringAdapter.class, result);
  }

  @Test
  public void testStringValueTypeAdapterFactory() {
    StringTypeAdapterFactory stringFactory = mock(StringTypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(StringTypeAdapterFactory.class))).thenReturn(stringFactory);

    TypeAdapter<String> result = factory.create(gson, STRING_TYPE_TOKEN);

    assertInstanceOf(StringTypeAdapterFactory.class, result);
  }

  @Test
  public void testInvalidValueType() {
    JsonAdapter annotation = new JsonAdapter() {};
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(null);

    try {
      factory.create(gson, new TypeToken<Object>() {}.getType());
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
    }
  }

  @Test
  public void testCreateTypeAdapter() {
    StringAdapter stringAdapter = mock(StringAdapter.class);
    when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(stringAdapter);

    JsonAdapter annotation = new JsonAdapter(StringAdapter.class);

    TypeAdapter<String> result = getTypeAdapter(factory, gson, STRING_TYPE_TOKEN, annotation);

    assertInstanceOf(StringAdapter.class, result);
  }

  @Test
  public void testCreateTypeAdapterFactory() {
    StringTypeAdapterFactory stringFactory = mock(StringTypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(StringTypeAdapterFactory.class))).thenReturn(stringFactory);

    JsonAdapter annotation = new JsonAdapter(StringTypeAdapterFactory.class);

    TypeAdapterFactory result = getTypeAdapter(factory, gson, STRING_TYPE_TOKEN, annotation);

    assertInstanceOf(StringTypeAdapterFactory.class, result);
  }

  @Test
  public void testCreateTypeAdapterWithTypeToken() {
    StringAdapter stringAdapter = mock(StringAdapter.class);
    when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(stringAdapter);

    JsonAdapter annotation = new JsonAdapter(StringAdapter.class);

    TypeAdapter<String> result = factory.create(gson, STRING_TYPE_TOKEN);

    assertInstanceOf(StringAdapter.class, result);
  }

  @Test
  public void testCreateTypeAdapterFactoryWithTypeToken() {
    StringTypeAdapterFactory stringFactory = mock(StringTypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(StringTypeAdapterFactory.class))).thenReturn(stringFactory);

    JsonAdapter annotation = new JsonAdapter(StringTypeAdapterFactory.class);

    TypeAdapterFactory result = factory.create(gson, STRING_TYPE_TOKEN);

    assertInstanceOf(StringTypeAdapterFactory.class, result);
  }

  @Test
  public void testTypeAdapterCreation() {
    StringAdapter stringAdapter = mock(StringAdapter.class);
    when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(stringAdapter);

    JsonAdapter annotation = new JsonAdapter(StringAdapter.class);

    TypeAdapter<String> typeAdapter = getTypeAdapter(factory, gson, STRING_TYPE_TOKEN, annotation);

    typeAdapter.write(new JsonWriter(System.out), "");
    typeAdapter.read(new JsonReader(System.in));

    assertNotNull(typeAdapter);
  }

  @Test
  public void testTypeAdapterFactoryCreation() {
    StringTypeAdapterFactory stringFactory = mock(StringTypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(StringTypeAdapterFactory.class))).thenReturn(stringFactory);

    JsonAdapter annotation = new JsonAdapter(StringTypeAdapterFactory.class);

    TypeAdapterFactory factory = getTypeAdapter(factory, gson, STRING_TYPE_TOKEN, annotation);

    assertNotNull(factory);
  }

  private static TypeAdapter<?> getTypeAdapter(JsonAdapterAnnotationTypeAdapterFactory factory, Gson gson,
      TypeToken<?> fieldType, JsonAdapter annotation) {
    return factory.getTypeAdapter(gson, fieldType, annotation);
  }
}

class StringAdapter implements TypeAdapter<String> {
  @Override
  public String read(JsonReader in) throws IOException {
    return in.nextString();
  }

  @Override
  public void write(JsonWriter out, String value) throws IOException {
    out.value(value);
  }
}

class StringTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    return new StringAdapter();
  }
}