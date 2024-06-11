package com.example.junit;

import static org.junit.Assert.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.*;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  private static <T> TypeAdapter<T> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson,
      TypeToken<T> fieldType, JsonAdapter annotation) {
    return JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test
  public void testCreateNormal() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertNotNull(adapter);
  }

  // Test cases for create() method
  @Test(expected = NullPointerException.class)
  public void testCreateNullGson() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> targetType = new TypeToken<String>() {};
    factory.create(null, targetType);
  }

  @Test(expected = NullPointerException.class)
  public void testCreateNullTypeToken() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, null);
  }

  @Test
  public void testCreateNoAnnotation() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    TypeAdapter<Integer> adapter = factory.create(gson, targetType);
    assertNull(adapter);
  }

  // Test cases for getTypeAdapter() method
  @Test
  public void testGetTypeAdapterNormal() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(StringSerializer.class);
    TypeAdapter<String> expected = new StringSerializer();
    TypeAdapter<String> adapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertEquals(expected, adapter);
  }

  @Test
  public void testGetTypeAdapterTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(StringSerializer.class);
    TypeAdapter<String> expected = new StringSerializer();
    TypeAdapter<String> adapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertEquals(expected, adapter);
  }

  @Test
  public void testGetTypeAdapterTypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(JsonStringAdapterFactory.class);
    TypeAdapterFactory factoryInstance = new JsonStringAdapterFactory();
    TypeAdapter<String> expected = factoryInstance.create(gson, fieldType);
    TypeAdapter<String> adapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertEquals(expected, adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapterNonAssignable() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(List.class);
    getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
}