package com.google.gson.internal.bind;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private static final class TestClass {
    @JsonAdapter(IntegerJsonAdapter.class)
    private int intField;
    private String stringField;
    private Map<String, String> mapField;

    public TestClass(int intField, String stringField, Map<String, String> mapField) {
      this.intField = intField;
      this.stringField = stringField;
      this.mapField = mapField;
    }

    public int getIntField() {
      return intField;
    }

    public void setIntField(int intField) {
      this.intField = intField;
    }

    public String getStringField() {
      return stringField;
    }

    public void setStringField(String stringField) {
      this.stringField = stringField;
    }

    public Map<String, String> getMapField() {
      return mapField;
    }

    public void setMapField(Map<String, String> mapField) {
      this.mapField = mapField;
    }
  }

  private static final class IntegerJsonAdapter implements JsonAdapter<Integer> {
    @Override public Integer read(JsonReader in) throws IOException {
      return in.nextInt();
    }

    @Override public void write(JsonWriter out, Integer value) throws IOException {
      out.value(value);
    }
  }

  @Test
  public void testSerializationDeserialization() throws IOException {
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return Modifier.isFinal(clazz.getModifiers()) || clazz.isPrimitive();
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }
        })
        .create();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(TestClass.class),
        new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return Modifier.isFinal(clazz.getModifiers()) || clazz.isPrimitive();
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }
        });

    TypeAdapter<TestClass> testClassTypeAdapter = factory.create(gson, new TypeToken<TestClass>() {});

    TestClass testClass = new TestClass(1, "foo", new HashMap<String, String>() {{
      put("bar", "baz");
    }});

    ByteArrayOutputStream jsonOutput = new ByteArrayOutputStream();
    JsonWriter jsonWriter = new JsonWriter(jsonOutput);
    testClassTypeAdapter.write(jsonWriter, testClass);
    jsonWriter.flush();

    JsonElement jsonElement = new JsonParser().parse(new ByteArrayInputStream(jsonOutput.toByteArray()));
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    TestClass deserializedTestClass = testClassTypeAdapter.read(new JsonReader(new ByteArrayInputStream(jsonOutput.toByteArray())));

    assertTrue(deserializedTestClass.getIntField() == 1);
    assertTrue(deserializedTestClass.getStringField().equals("foo"));
    assertTrue(deserializedTestClass.getMapField().get("bar").equals("baz"));
  }
}

class JsonParser {}