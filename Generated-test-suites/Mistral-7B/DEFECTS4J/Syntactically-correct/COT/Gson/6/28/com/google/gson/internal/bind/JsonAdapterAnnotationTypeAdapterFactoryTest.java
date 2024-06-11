package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_NullGson() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNotNull(factory.create(null, new TypeToken<String>() {})); // Null Gson should not cause an exception
  }

  @Test
  public void testCreate_NullTargetType() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, null)); // Null targetType should return null
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> targetType = new TypeToken<String>() {};
    assertNull(factory.create(gson, targetType)); // Type without JsonAdapter annotation should return null
  }

  @Test
  public void testCreate_TypeAdapter() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
    TypeAdapter<String> expectedTypeAdapter = constructorConstructor.get(TypeToken.get(typeAdapterClass)).construct();
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
    assertSame(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test
  public void testCreate_TypeAdapterFactory() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<TypeAdapterFactory<String>> typeAdapterFactoryClass = TypeAdapterFactory.class;
    TypeAdapterFactory<String> expectedTypeAdapterFactory = constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass)).construct();
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
    assertEquals(expectedTypeAdapterFactory.create(gson, targetType.getType()), actualTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidJsonAdapter() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<?> invalidJsonAdapter = Object.class;
    assertNotNull(factory.create(gson, targetType)); // Invalid JsonAdapter should throw an exception
  }
}