package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testJsonAdapterAnnotationTypeAdapterFactory() {
    // Test with a valid JsonAdapter
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor(new GsonBuilder().create()));

    TypeAdapter<String> stringAdapter = (TypeAdapter<String>) factory.create(new Gson(),
        new TypeToken<String>() {});
    assertNotNull(stringAdapter);

    // Test with a TypeAdapter
    @JsonAdapter(StringSerializer.class)
    static class MyClass {
      private String myString;

      public String getMyString() {
        return myString;
      }

      public void setMyString(String myString) {
        this.myString = myString;
      }
    }

    Class<MyClass> myClassType = MyClass.class;
    TypeAdapter<MyClass> myClassAdapter = (TypeAdapter<MyClass>) factory.create(new Gson(),
        new TypeToken<MyClass>() {});
    assertNotNull(myClassAdapter);

    // Test with a TypeAdapterFactory
    @JsonAdapter(MyNumberAdapterFactory.class)
    static class MyNumber {
      private int number;

      public int getNumber() {
        return number;
      }

      public void setNumber(int number) {
        this.number = number;
      }
    }

    static class MyNumberAdapterFactory implements TypeAdapterFactory {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (MyNumber.class.isAssignableFrom(typeToken.getRawType())) {
          return (TypeAdapter<MyNumber>) new MyNumberTypeAdapter();
        }
        return null;
      }
    }

    static class MyNumberTypeAdapter extends TypeAdapter<MyNumber> {
      @Override
      public MyNumber read(JsonReader jsonReader) throws IOException {
        JsonElement jsonElement = jsonReader.nextElement();
        if (jsonElement.isJsonNull()) {
          return null;
        }
        int value = jsonElement.getAsInt();
        return new MyNumber().setValue(value);
      }

      @Override
      public void write(JsonWriter jsonWriter, MyNumber myNumber) throws IOException {
        jsonWriter.value(myNumber.number);
      }
    }

    TypeAdapter<MyNumber> myNumberAdapter = (TypeAdapter<MyNumber>) factory.create(new Gson(),
        new TypeToken<MyNumber>() {});
    assertNotNull(myNumberAdapter);

    // Test with null
    TypeAdapter<Object> nullAdapter = (TypeAdapter<Object>) factory.create(new Gson(),
        new TypeToken<Object>() {});
    assertNotNull(nullAdapter);

    // Test with invalid JsonAdapter
    MyInvalidClass invalidClass = new MyInvalidClass();
    TypeAdapter<MyInvalidClass> invalidAdapter = (TypeAdapter<MyInvalidClass>) factory.create(new Gson(),
        new TypeToken<MyInvalidClass>() {});
    assertNull(invalidAdapter);
  }

  static class MyClass {
  }

  static class MyInvalidClass {
    @JsonAdapter(StringSerializer.class) // Incorrect value
    static class MyInnerClass {
    }
  }
}