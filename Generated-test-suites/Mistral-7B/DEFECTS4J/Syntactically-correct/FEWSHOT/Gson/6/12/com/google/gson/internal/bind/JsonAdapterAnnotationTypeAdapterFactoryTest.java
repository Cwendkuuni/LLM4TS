package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_TypeAdapterInstanceReturned() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(TypeAdapterStringAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterStringAdapter.class)))
        .thenReturn(TypeAdapterStringAdapter.INSTANCE);

    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    assertNotNull(typeAdapter);
    verify(constructorConstructor, times(1)).get(TypeToken.get(TypeAdapterStringAdapter.class));
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_TypeAdapterFactoryInstanceReturned() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(TypeAdapterStringFactory.class);
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(typeAdapterFactory);

    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    assertNotNull(typeAdapter);
    verify(constructorConstructor, times(1)).get(TypeToken.get(TypeAdapterFactory.class));
    verify(typeAdapterFactory, times(1)).create(eq(gson), eq(targetType));
  }

  @Test
  public void testCreate_JsonAdapterAnnotationAbsent_NullReturned() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    assertNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotationValueInvalid_ExceptionThrown() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(Object.class);

    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
  }
}

class TypeAdapterStringAdapter implements TypeAdapter<String> {
  public static final TypeAdapter<String> INSTANCE = new TypeAdapterStringAdapter();

  @Override
  public String read(JsonReader in) throws IOException {
    return null;
  }

  @Override
  public void write(JsonWriter out, String value) throws IOException {

  }
}

class TypeAdapterStringFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    return new TypeAdapterStringAdapter();
  }
}