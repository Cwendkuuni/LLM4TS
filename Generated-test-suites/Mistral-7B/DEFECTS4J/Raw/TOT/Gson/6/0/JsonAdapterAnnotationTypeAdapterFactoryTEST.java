package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testJsonAdapterType() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = gson.getAdapterFactory().getConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = MyClassWithJsonAdapter.class.getAnnotation(JsonAdapter.class);
    TypeAdapter<MyClassWithJsonAdapter> typeAdapter = (TypeAdapter<MyClassWithJsonAdapter>) factory.create(gson, TypeToken.get(MyClassWithJsonAdapter.class));
    assertNotNull(typeAdapter);
  }

  @Test
  public void testJsonAdapterFactory() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = gson.getAdapterFactory().getConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = MyClassWithJsonAdapterFactory.class.getAnnotation(JsonAdapter.class);
    TypeAdapterFactory adapterFactory = (TypeAdapterFactory) factory.create(gson, TypeToken.get(MyClassWithJsonAdapterFactory.class.getGenericSuperclass()));
    TypeAdapter<MyClassWithJsonAdapter> typeAdapter = adapterFactory.create(gson, TypeToken.get(MyClassWithJsonAdapter.class));
    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterInvalidValue() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = gson.getAdapterFactory().getConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = new JsonAdapter() {};
    TypeAdapter<String> typeAdapter = factory.create(gson, TypeToken.get(String.class));
  }

  public static class MyClassWithJsonAdapter {
    @JsonAdapter(MyClassWithJsonAdapterTypeAdapter.class)
    static class NestedClass {
      private int value;

      public int getValue() {
        return value;
      }

      public void setValue(int value) {
        this.value = value;
      }
    }

    private NestedClass nested;

    public NestedClass getNested() {
      return nested;
    }

    public void setNested(NestedClass nested) {
      this.nested = nested;
    }
  }

  public static class MyClassWithJsonAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      if (typeToken.getType() == MyClassWithJsonAdapter.class) {
        return new MyClassWithJsonAdapterTypeAdapter();
      }
      return null;
    }

    private static class MyClassWithJsonAdapterTypeAdapter extends TypeAdapter<MyClassWithJsonAdapter> {
      @Override
      public MyClassWithJsonAdapter read(JsonReader in) throws IOException {
        JsonElement jsonElement = in.nextTree();
        return new Gson().fromJson(jsonElement, MyClassWithJsonAdapter.class);
      }

      @Override
      public void write(JsonWriter out, MyClassWithJsonAdapter value) throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonElement nestedJsonElement = new Gson().toJsonTree(value.getNested());
        jsonObject.add("nested", nestedJsonElement);
        out.jsonValue(jsonObject.toString());
      }
    }
  }
}