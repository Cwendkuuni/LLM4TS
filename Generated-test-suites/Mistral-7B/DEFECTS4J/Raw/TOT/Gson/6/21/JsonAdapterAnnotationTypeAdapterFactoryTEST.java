package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.Collections;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Tests for {@link JsonAdapterAnnotationTypeAdapterFactory}.
 */
public final class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testJsonAdapterTypeAdapter() {
    Gson gson = new GsonBuilder().setPrePreProcessor(new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor(new ConstructorConstructor(TypeAdapter.class)))).create();

    TypeAdapter<String> stringTypeAdapter = gson.getAdapter(String.class);
    JsonAdapter<String> jsonAdapter = new JsonAdapter<String>() {
      @Override
      public String fromJson(JsonElement jsonElement) {
        return jsonElement.getAsString();
      }

      @Override
      public JsonElement toJson(String src) {
        return new JsonPrimitive(src);
      }
    };

    Type type = new TypeToken<String>() {}.getType();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor(JsonAdapterAnnotationTypeAdapterFactory.class));
    TypeAdapter<String> jsonAdapterTypeAdapter = factory.create(gson, TypeToken.get(String.class));

    assertNotNull(jsonAdapterTypeAdapter);
    assertNotNull(jsonAdapterTypeAdapter.fromJson("\"Hello World\""));
    assertNotNull(jsonAdapterTypeAdapter.toJson("Hello World"));

    // Test with custom JsonAdapter
    TypeAdapter<String> stringTypeAdapterWithJsonAdapter = gson.getAdapter(String.class);
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapter<String> jsonAdapterAnnotated = new JsonAdapter<String>() {
      @Override
      public String fromJson(JsonElement jsonElement) {
        return jsonElement.getAsString();
      }

      @Override
      public JsonElement toJson(String src) {
        return new JsonPrimitive(src.toUpperCase());
      }
    };
    TypeAdapter<String> jsonAdapterTypeAdapterAnnotated = factory.create(gson,
        new TypeToken<String>() {}.getDeclaredType());

    assertNotNull(jsonAdapterTypeAdapterAnnotated);
    assertNotNull(jsonAdapterTypeAdapterAnnotated.fromJson("\"HELLO WORLD\""));
    assertNotNull(jsonAdapterTypeAdapterAnnotated.toJson("Hello World"));

    // Test null case
    TypeAdapter<String> stringTypeAdapterNull = gson.getAdapter(String.class);
    TypeToken<String> stringTypeTokenNull = new TypeToken<String>() {};
    TypeAdapter<String> jsonAdapterTypeAdapterNull = factory.create(gson, stringTypeTokenNull);

    assertNotNull(jsonAdapterTypeAdapterNull);
    assertNull(jsonAdapterTypeAdapterNull.fromJson(null));
    assertNull(jsonAdapterTypeAdapterNull.toJson(null));

    // Test empty list case
    TypeAdapter<List<String>> listTypeAdapter = gson.getAdapter(Collections.emptyList().getClass());
    TypeToken<List<String>> listTypeToken = new TypeToken<List<String>>() {};
    TypeAdapter<List<String>> jsonAdapterTypeAdapterList = factory.create(gson, listTypeToken);

    assertNotNull(jsonAdapterTypeAdapterList);
    assertNotNull(jsonAdapterTypeAdapterList.fromJson("[]"));
    assertNotNull(jsonAdapterTypeAdapterList.toJson(Collections.emptyList()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterValueMustBeTypeAdapterOrTypeAdapterFactoryReference() {
    Gson gson = new GsonBuilder().setPrePreProcessor(new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor(JsonAdapterAnnotationTypeAdapterFactory.class)))
        .create();

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor(JsonAdapterAnnotationTypeAdapterFactory.class));
    TypeAdapter<?> jsonAdapterTypeAdapter = factory.create(gson, new TypeToken<Integer>() {}.getType());
  }
}