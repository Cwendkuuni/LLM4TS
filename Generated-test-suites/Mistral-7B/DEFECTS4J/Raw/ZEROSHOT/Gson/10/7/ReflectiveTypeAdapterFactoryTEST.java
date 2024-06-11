package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingStrategy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private static final class TestClass {
    @SerializedName("name1")
    private String name1;
    @JsonAdapter(StringAdapter.class)
    @SerializedName("name2")
    private String name2;
    private int id;
    private transient String transientField;

    public TestClass() {
    }

    public TestClass(String name1, String name2, int id) {
      this.name1 = name1;
      this.name2 = name2;
      this.id = id;
    }

    public String getName1() {
      return name1;
    }

    public void setName1(String name1) {
      this.name1 = name1;
    }

    public String getName2() {
      return name2;
    }

    public void setName2(String name2) {
      this.name2 = name2;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getTransientField() {
      return transientField;
    }

    public void setTransientField(String transientField) {
      this.transientField = transientField;
    }
  }

  private static final class StringAdapter extends TypeAdapter<String> {
    @Override public String read(JsonReader in) throws IOException {
      return in.nextString();
    }

    @Override public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }
  }

  @org.junit.BeforeClass
  public static void setup() {
    Gson gson = new GsonBuilder()
        .setExcluder(new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return f.isSynthetic() || f.isTransient();
          }
        })
        .setFieldNamingStrategy(FieldNamingStrategy.LOWER_CASE_WITH_UNDERSCORES)
        .setConstructorConstructor(new ConstructorConstructor())
        .create();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field f) {
            return f.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return f.isSynthetic() || f.isTransient();
          }
        }
    );
    Class<?> testClassType = TestClass.class;
    TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
  }

  @org.junit.Test
  public void testSerializer() throws IOException {
    TestClass testClass = new TestClass("name1_value1", "name2_value2", 123);
    JsonWriter writer = new JsonWriter(System.out);
    adapter.write(writer, testClass);
    writer.flush();
  }

  @org.junit.Test
  public void testDeserializer() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("{\"name1\":\"name1_value1\",\"id\":123,\"name2\":\"name2_value2\"}"));
    TestClass testClass = adapter.read(reader);
    assertEquals("name1_value1", testClass.getName1());
    assertEquals("name2_value2", testClass.getName2());
    assertEquals(123, testClass.getId());
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testExcludedAnnotation() throws IOException {
    TestClass testClass = new TestClass("name1_value1", null, 123);
    JsonWriter writer = new JsonWriter(System.out);
    adapter.write(writer, testClass);
    writer.flush();
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testExcludedField() throws IOException {
    TestClass testClass = new TestClass("name1_value1", null, 123) {
      @Override public String getTransientField() {
        return "transientFieldValue";
      }
    };
    JsonReader reader = new JsonReader(new StringReader("{\"name1\":\"name1_value1\",\"id\":123,\"transientField\":\"transientFieldValue\"}"));
    adapter.read(reader);
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testJsonAdapter() throws IOException {
    TestClass testClass = new TestClass("name1_value1", null, 123);
    JsonReader reader = new JsonReader(new StringReader("{\"name1\":\"name1_value1\",\"name2\":\"name2_value2\"}"));
    adapter.read(reader);
  }
}