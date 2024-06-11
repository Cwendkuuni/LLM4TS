package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;

@RunWith(MockitoJUnitRunner.class)
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory() {
    // Create mocks
    Gson gson = Mockito.mock(Gson.class);
    ConstructorConstructor constructorConstructor = Mockito.mock(ConstructorConstructor.class);

    // Test case 1: JsonAdapter annotation is present and TypeAdapter is used
    Type type = TypeToken.get(String.class).getType();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    JsonAdapter annotation = type.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);
    String jsonAdapterClassName = annotation.value();
    Class<?> jsonAdapterClass = Class.forName(jsonAdapterClassName);
    TypeAdapter<?> typeAdapter = factory.create(gson, TypeToken.get(type));
    assertNotNull(typeAdapter);
    assertNotNull(typeAdapter.fromJson("{\"name\": \"John Doe\"}", String.class));

    // Test case 2: JsonAdapter annotation is present and TypeAdapterFactory is used
    type = new TypeToken<MyCustomClass>() {}.getType();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    annotation = type.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);
    String jsonAdapterFactoryClassName = annotation.value();
    Class<?> jsonAdapterFactoryClass = Class.forName(jsonAdapterFactoryClassName);
    TypeAdapterFactory<?> typeAdapterFactory = Mockito.mock(TypeAdapterFactory.class);
    Mockito.when(typeAdapterFactory.create(gson, TypeToken.get(type))).thenReturn(typeAdapter);
    Mockito.when(constructorConstructor.get(TypeToken.get(jsonAdapterFactoryClass))).thenReturn(typeAdapterFactory);
    typeAdapter = factory.create(gson, type);
    assertNotNull(typeAdapter);
    assertNotNull(typeAdapter.fromJson("{\"name\": \"John Doe\"}", MyCustomClass.class));

    // Test case 3: JsonAdapter annotation is not present
    type = new TypeToken<Integer>() {}.getType();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    typeAdapter = factory.create(gson, type);
    assertNull(typeAdapter);

    // Test case 4: JsonAdapter value is not valid
    type = new TypeToken<Object>() {}.getType();
    annotation = new JsonAdapter() {};
    annotation.value("InvalidClass");
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNotNull(annotation);
    try {
      factory.create(gson, type);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}