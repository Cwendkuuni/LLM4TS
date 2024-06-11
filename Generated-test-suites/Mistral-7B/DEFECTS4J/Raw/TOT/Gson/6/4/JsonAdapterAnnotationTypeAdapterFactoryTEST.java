package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private Gson gson;

  public JsonAdapterAnnotationTypeAdapterFactoryTest() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    this.gson = new GsonBuilder()
        .setConstructorConstructor(constructorConstructor)
        .create();
  }

  @Test
  public void testJsonAdapter_TypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = MyJsonAdapter.class.getAnnotation(JsonAdapter.class);
    TypeAdapter<MyJsonAdapter> expectedAdapter = new MyJsonAdapterTypeAdapter();
    TypeAdapter<MyJsonAdapter> actualAdapter = factory.create(gson, new TypeToken<MyJsonAdapter>() {}.getType());

    assertNotNull(actualAdapter);
    assertNotNull(actualAdapter.fromJson("{\"foo\":1}")); // valid JSON
    assertNotNull(actualAdapter.toJson(new MyJsonAdapter())); // valid object

    assert actualAdapter.equals(expectedAdapter);
  }

  @Test
  public void testJsonAdapter_Null() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    assertNull(factory.create(gson, new TypeToken<String>() {}.getType())); // no JsonAdapter
  }

  @Test
  public void testJsonAdapter_TypeAdapterFactory() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    MyJsonAdapterFactory adapterFactory = new MyJsonAdapterFactory();
    TypeAdapter<MyJsonAdapter> expectedAdapter = new MyJsonAdapterTypeAdapter();
    TypeAdapter<MyJsonAdapter> actualAdapter = factory.create(gson, new TypeToken<MyJsonAdapter>() {}.getType());

    assertNotNull(actualAdapter);
    assertNotNull(actualAdapter.fromJson("{\"foo\":1}")); // valid JSON
    assertNotNull(actualAdapter.toJson(new MyJsonAdapter())); // valid object

    assert actualAdapter.equals(expectedAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapter_InvalidType() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<Integer>() {}.getType(), null);
  }

  private static class MyJsonAdapter {
    private int foo;
  }

  private static class MyJsonAdapterTypeAdapter extends TypeAdapter<MyJsonAdapter> {
    @Override
    public MyJsonAdapter read(JsonReader jsonReader) throws IOException {
      JsonElement jsonElement = jsonReader.nextElement();
      assert jsonElement.isJsonObject();
      return gson.fromJson(jsonElement, MyJsonAdapter.class);
    }

    @Override
    public void write(JsonWriter jsonWriter, MyJsonAdapter value) throws IOException {
      jsonWriter.beginObject();
      jsonWriter.name("foo").value(value.foo);
      jsonWriter.endObject();
    }
  }

  private static class MyJsonAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      if (!MyJsonAdapter.class.isAssignableFrom(typeToken.getRawType())) {
        throw new UnsupportedOperationException("Only handles MyJsonAdapter.");
      }
      return new MyJsonAdapterTypeAdapter();
    }
  }
}