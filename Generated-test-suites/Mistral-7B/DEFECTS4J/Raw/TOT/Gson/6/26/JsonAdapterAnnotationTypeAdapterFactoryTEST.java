package com.example.junit4.test.gson;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testJsonAdapterTypeAdapterFactory() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When
    TypeAdapter<?> adapter = factory.create(gson, new TypeToken<String>() {}.getType());

    // Then
    assertNotNull(adapter);
  }

  @Test
  public void testJsonAdapterNull() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When
    TypeAdapter<?> adapter = factory.create(gson, new TypeToken<Object>() {}.getType());

    // Then
    assertNull(adapter);
  }

  @Test
  public void testJsonAdapterTypeAdapter() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When
    TypeAdapter<String> stringAdapter = (TypeAdapter<String>) new GsonBuilder().setPrePreProcessor(
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)).create();
    TypeAdapter<Integer> intAdapter = factory.create(gson, new TypeToken<Integer>() {}.getType());

    // Then
    assertNotNull(stringAdapter);
    assertNotNull(intAdapter);
    assertNull(factory.create(gson, new TypeToken<Number>() {}.getType()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterInvalidValue() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When & Then
    factory.create(gson, new TypeToken<Object>() {}.getType(), new Object() {}.getClass());
  }

  @Test
  public void testJsonAdapterTypeAdapterFactoryWithCustomFactory() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapterFactory customFactory = new MyCustomTypeAdapterFactory<>();

    // When
    TypeAdapter<?> adapter = factory.create(gson, new TypeToken<Object>() {}.getType(), customFactory);

    // Then
    assertNotNull(adapter);
  }
}

class MyCustomTypeAdapterFactory<T> implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    return null; // This factory will return null as placeholder for testing purposes only.
  }
}