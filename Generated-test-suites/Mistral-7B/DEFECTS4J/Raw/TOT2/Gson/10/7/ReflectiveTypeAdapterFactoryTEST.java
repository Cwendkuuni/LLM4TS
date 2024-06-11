import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.exclusion.Excluder;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.FieldNamingStrategy;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;
import com.google.gson.internal.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingStrategy(FieldNamingStrategy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder() {
          @Override public boolean excludeField(Field f) {
            return false;
          }
        })
        .setConstructorConstructor(new ConstructorConstructor())
        .create();

    TypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingStrategy.LOWER_CASE_WITH_UNDERSCORES,
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }
        });

    // When
    TypeAdapter<Map<String, String>> mapTypeAdapter = adapterFactory.create(gson, new TypeToken<Map<String, String>>() {}.getType());

    // Then
    assertNotNull(mapTypeAdapter);

    Map<String, String> map = new HashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");

    String json = gson.toJson(map);

    Map<String, String> deserializedMap = mapTypeAdapter.read(new JsonReader(new StringReader(json)));

    assertEquals(map, deserializedMap);
  }

  @Test
  public void testExcludeField() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingStrategy(FieldNamingStrategy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder() {
          @Override public boolean excludeField(Field f, boolean serialize) {
            return true;
          }
        })
        .setConstructorConstructor(new ConstructorConstructor())
        .create();

    TypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingStrategy.LOWER_CASE_WITH_UNDERSCORES,
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return true;
          }
        });

    // When
    TypeAdapter<TestClass> testClassTypeAdapter = adapterFactory.create(gson, new TypeToken<TestClass>() {}.getType());

    // Then
    assertNotNull(testClassTypeAdapter);

    TestClass testClass = new TestClass("value1", "value2");
    String json = gson.toJson(testClass);

    TestClass deserializedTestClass = testClassTypeAdapter.read(new JsonReader(new StringReader(json)));

    assertNull(deserializedTestClass.privateField);
    assertEquals("value1", deserializedTestClass.publicField1);
    assertEquals("value2", deserializedTestClass.publicField2);
  }

  @Test
  public void testJsonAdapter() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingStrategy(FieldNamingStrategy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder() {
          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }
        })
        .setConstructorConstructor(new ConstructorConstructor())
        .create();

    JsonAdapter<String> jsonAdapter = new JsonAdapter<String>() {
      @Override public String read(JsonReader in) throws IOException {
        return in.nextString();
      }

      @Override public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
      }
    };

    TypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        FieldNamingStrategy.LOWER_CASE_WITH_UNDERSCORES,
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }
        },
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(String.class, jsonAdapter)
    );

    // When
    TypeAdapter<TestClassWithJsonAdapter> testClassWithJsonAdapterTypeAdapter = adapterFactory.create(gson, new TypeToken<TestClassWithJsonAdapter>() {}.getType());

    // Then
    assertNotNull(testClassWithJsonAdapterTypeAdapter);

    TestClassWithJsonAdapter testClassWithJsonAdapter = new TestClassWithJsonAdapter("jsonAdapterValue");
    String json = gson.toJson(testClassWithJsonAdapter);

    TestClassWithJsonAdapter deserializedTestClassWithJsonAdapter = testClassWithJsonAdapterTypeAdapter.read(new JsonReader(new StringReader(json)));

    assertEquals("jsonAdapterValue", deserializedTestClassWithJsonAdapter.jsonAdapterField);
  }

  static class TestClass {
    @SerializedName("publicField1")
    String publicField1;
    @SerializedName("publicField2")
    String publicField2;

    @SerializedName("privateField")
    private String privateField;

    TestClass(String publicField1, String publicField2) {
      this.publicField1 = publicField1;
      this.publicField2 = publicField2;
    }
  }

  static class TestClassWithJsonAdapter {
    @SerializedName("jsonAdapterField")
    @JsonAdapter(JsonAdapterForTestClassWithJsonAdapter.class)
    String jsonAdapterField;

    TestClassWithJsonAdapter() {}

    static class JsonAdapterForTestClassWithJsonAdapter extends JsonAdapter<TestClassWithJsonAdapter> {
      @Override public TestClassWithJsonAdapter read(JsonReader in) throws IOException {
        TestClassWithJsonAdapter testClassWithJsonAdapter = new TestClassWithJsonAdapter();
        testClassWithJsonAdapter.jsonAdapterField = in.nextString();
        return testClassWithJsonAdapter;
      }

      @Override public void write(JsonWriter out, TestClassWithJsonAdapter value) throws IOException {
        out.value(value.jsonAdapterField);
      }
    }
  }
}