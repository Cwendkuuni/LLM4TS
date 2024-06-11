package com.example.junit4;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private JsonAdapterAnnotationTypeAdapterFactory typeAdapterFactory;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testJsonAdapterType() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    String expected = "expectedString";
    String jsonString = "{\"value\":\"" + expected + "\"}";

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(new StringTypeAdapter());

    JsonAdapter jsonAdapterAnnotation = TypeAdapterTestClass.class.getAnnotation(JsonAdapter.class);
    TypeAdapter<String> stringTypeAdapter = typeAdapterFactory.create(gson, targetType);

    assertNotNull(stringTypeAdapter);

    String result = stringTypeAdapter.fromJson(jsonString);
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void testJsonAdapterFactoryType() {
    TypeToken<MyClass> targetType = new TypeToken<MyClass>() {};
    MyClass expected = new MyClass();
    Gson gson = mock(Gson.class);

    when(constructorConstructor.get(TypeToken.get(CustomTypeAdapterFactory.class)))
        .thenReturn(new CustomTypeAdapterFactory());

    JsonAdapter jsonAdapterAnnotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<MyClass> myClassTypeAdapter = typeAdapterFactory.create(gson, targetType);

    assertNotNull(myClassTypeAdapter);

    MyClass result = myClassTypeAdapter.fromJson("{}", MyClass.class);
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterInvalidType() {
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    JsonAdapter jsonAdapterAnnotation = targetType.getRawType().getAnnotation(JsonAdapter.class);

    typeAdapterFactory.create(gson, targetType);
  }

  private static class StringTypeAdapter extends TypeAdapter<String> {
    @Override
    public String fromJson(JsonElement json) throws JsonParseException {
      return json.getAsString();
    }

    @Override
    public JsonElement toJson(String value) {
      return new JsonPrimitive(value);
    }
  }

  private static class MyClass {}

  private static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      return new CustomTypeAdapter<T>(gson);
    }

    private class CustomTypeAdapter<T> extends TypeAdapter<T> {
      private final Gson gson;

      public CustomTypeAdapter(Gson gson) {
        this.gson = gson;
      }

      @Override
      public T fromJson(JsonElement json) throws JsonParseException {
        // Implement actual deserialization logic here
      }

      @Override
      public JsonElement toJson(T value) {
        // Implement actual serialization logic here
      }
    }
  }

  private static class TypeAdapterTestClass {}
}