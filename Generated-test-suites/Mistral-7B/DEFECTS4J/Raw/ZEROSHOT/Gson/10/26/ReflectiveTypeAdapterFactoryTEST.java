package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectConstructor;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  private static class TestClass {
    @SerializedName("name1")
    private String name1;
    @SerializedName("name2")
    private int name2;
    private double value;
    private TestClass inner;

    @JsonAdapter(ValueAdapter.class)
    private Value valueObject;
  }

  private static class Value {
    private int id;
    private String name;
  }

  private static class ValueAdapter extends TypeAdapter<Value> {
    @Override public Value read(JsonReader in) throws IOException {
      return new Value();
    }

    @Override public void write(JsonWriter out, Value value) throws IOException {
      out.value("{}");
    }
  }

  @org.junit.Test
  public void testCreate() throws Exception {
    Gson gson = new GsonBuilder().setExcluder(new Excluder() {
      @Override public boolean excludeField(Field f, boolean serialize) {
        return false;
      }

      @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return false;
      }
    }).create();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<>(TestClass.class),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }

          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));

    TestClass testClass = new TestClass();
    testClass.name1 = "Name1";
    testClass.name2 = 2;
    testClass.value = 3.14;
    testClass.inner = new TestClass();
    testClass.inner.name1 = "InnerName1";
    testClass.inner.name2 = 3;
    testClass.valueObject = new Value();

    String json = gson.toJson(testClass);
    System.out.println(json);

    TestClass deserialized = gson.fromJson(json, TestClass.class);

    assertEquals(testClass.name1, deserialized.name1);
    assertEquals(testClass.name2, deserialized.name2);
    assertEquals(testClass.value, deserialized.value, 0.001);
    assertEquals(testClass.inner.name1, deserialized.inner.name1);
    assertEquals(testClass.inner.name2, deserialized.inner.name2);
    assertNull(deserialized.valueObject);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testExcludeFields() throws Exception {
    Gson gson = new GsonBuilder().setExcluder(new Excluder() {
      @Override public boolean excludeField(Field f, boolean serialize) {
        return true;
      }

      @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return false;
      }
    }).create();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<>(TestClass.class),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeField(Field f, boolean serialize) {
            return true;
          }

          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));

    TestClass testClass = new TestClass();
    TestClass deserialized = adapter.fromJson(gson.toJson(testClass), TestClass.class);
    fail();
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testJsonSyntaxException() throws Exception {
    Gson gson = new GsonBuilder().setExcluder(new Excluder() {
      @Override public boolean excludeField(Field f, boolean serialize) {
        return false;
      }

      @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return false;
      }
    }).create();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<>(TestClass.class),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }

          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));

    JsonReader jsonReader = new JsonReader(new StringReader("{\"name1\":1,\"name2\":3}"));

    TestClass testClass = new TestClass();
    try {
      adapter.read(jsonReader);
      fail();
    } catch (JsonSyntaxException e) {
      // expected
    }
  }

  @org.junit.Test(expected = IllegalAccessException.class)
  public void testIllegalAccessException() throws Exception {
    Gson gson = new GsonBuilder().setExcluder(new Excluder() {
      @Override public boolean excludeField(Field f, boolean serialize) {
        return false;
      }

      @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return false;
      }
    }).create();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<>(TestClass.class),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }

          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));

    TestClass testClass = new TestClass();
    TestClass deserialized = adapter.read(new JsonReader(new StringReader("{}")));

    Field innerField = TestClass.class.getDeclaredField("inner");
    innerField.setAccessible(true);
    Field name1Field = TestClass.class.getDeclaredField("name1");
    name1Field.setAccessible(true);

    try {
      name1Field.set(deserialized, "InnerName1");
      innerField.set(deserialized, new TestClass());
    } catch (IllegalAccessException e) {
      throw new AssertionError("Unexpected exception", e);
    }

    TestClass updatedTestClass = new TestClass();
    updatedTestClass.name1 = "TestName1";
    updatedTestClass.inner = new TestClass();

    String json = gson.toJson(updatedTestClass);

    TestClass updatedDeserialized = adapter.read(new JsonReader(new StringReader(json)));

    assertEquals(updatedTestClass.name1, updatedDeserialized.name1);
    assertEquals(updatedTestClass.inner.name1, updatedDeserialized.inner.name1);

    try {
      name1Field.set(deserialized, "TestName1");
    } catch (IllegalAccessException e) {
      throw new AssertionError("Unexpected exception", e);
    }

    assertEquals(updatedDeserialized.name1, deserialized.name1);
  }
}