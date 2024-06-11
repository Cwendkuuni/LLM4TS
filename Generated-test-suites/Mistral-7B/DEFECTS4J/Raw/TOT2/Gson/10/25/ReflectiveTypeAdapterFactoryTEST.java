import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  private static class TestClass {
    @SerializedName("name")
    private String name;
    private int age;
    private String nestedClassField;

    @JsonAdapter(NestedClassJsonAdapter.class)
    private NestedClass nestedClassObject;

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }

    public String getNestedClassField() {
      return nestedClassField;
    }

    public NestedClass getNestedClassObject() {
      return nestedClassObject;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public void setNestedClassField(String nestedClassField) {
      this.nestedClassField = nestedClassField;
    }

    public void setNestedClassObject(NestedClass nestedClassObject) {
      this.nestedClassObject = nestedClassObject;
    }

    public static class NestedClass {
      private String nestedName;

      public String getNestedName() {
        return nestedName;
      }

      public void setNestedName(String nestedName) {
        this.nestedName = nestedName;
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> TypeAdapter<T> createTypeAdapter(Gson gson, Class<T> type) {
    return (TypeAdapter<T>) new ReflectiveTypeAdapterFactory(ObjectConstructor.create(),
        TypeAdapterAnnotationTypeAdapterFactory.getDefaultFieldNamingPolicy(),
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }).create(gson, new TypeToken<T>() {});
  }

  @org.junit.Test
  public void testSerializerDeserializer() throws IOException {
    Gson gson = new GsonBuilder().create();

    TestClass testClass = new TestClass();
    testClass.setName("John Doe");
    testClass.setAge(30);
    testClass.setNestedClassField("Nested Class Field");

    // Serialize
    JsonWriter jsonWriter = new JsonWriter(System.out);
    createTypeAdapter(gson, TestClass.class).write(jsonWriter, testClass);
    jsonWriter.flush();

    // Deserialize
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));
    TestClass deserializedTestClass = createTypeAdapter(gson, TestClass.class).read(jsonReader);

    assertEquals("John Doe", deserializedTestClass.getName());
    assertEquals(30, deserializedTestClass.getAge());
    assertEquals("Nested Class Field", deserializedTestClass.getNestedClassField());
  }

  @org.junit.Test
  public void testNestedClassSerializationDeserialization() throws IOException {
    Gson gson = new GsonBuilder().create();

    TestClass testClass = new TestClass();
    testClass.setName("John Doe");
    testClass.setAge(30);
    testClass.setNestedClassField("Nested Class Field");
    testClass.setNestedClassObject(new TestClass.NestedClass());
    testClass.getNestedClassObject().setNestedName("Nested Nested Class");

    // Serialize
    JsonWriter jsonWriter = new JsonWriter(System.out);
    createTypeAdapter(gson, TestClass.class).write(jsonWriter, testClass);
    jsonWriter.flush();

    // Deserialize
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));
    TestClass deserializedTestClass = createTypeAdapter(gson, TestClass.class).read(jsonReader);

    assertEquals("John Doe", deserializedTestClass.getName());
    assertEquals(30, deserializedTestClass.getAge());
    assertEquals("Nested Class Field", deserializedTestClass.getNestedClassField());
    assertEquals("Nested Nested Class", deserializedTestClass.getNestedClassObject().getNestedName());
  }

  @org.junit.Test(expected = IllegalAccessException.class)
  public void testSettingValueWithImmutableType() throws IOException {
    Gson gson = new GsonBuilder().create();

    TestClass testClass = new TestClass();
    testClass.setName("John Doe");
    testClass.setAge(30);
    testClass.setNestedClassField("Nested Class Field");

    // Serialize
    JsonWriter jsonWriter = new JsonWriter(System.out);
    createTypeAdapter(gson, TestClass.class).write(jsonWriter, testClass);
    jsonWriter.flush();

    // Deserialize with immutable type
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));
    final Class<TestClass> testClassType = TestClass.class;
    TestClass deserializedTestClass = null;
    try {
      deserializedTestClass = createTypeAdapter(gson, testClassType).read(jsonReader);
    } catch (IllegalAccessException e) {
      throw e;
    }

    // Try to change an immutable value
    deserializedTestClass.setName("New Name");
  }

  @org.junit.Test
  public void testExclusion() throws IOException {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    TestClass testClass = new TestClass();
    testClass.setName("John Doe");
    testClass.setAge(30);
    testClass.setNestedClassField("Nested Class Field");

    // Serialize
    JsonWriter jsonWriter = new JsonWriter(System.out);
    createTypeAdapter(gson, TestClass.class).write(jsonWriter, testClass);
    jsonWriter.flush();

    // Deserialize
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));
    TestClass deserializedTestClass = createTypeAdapter(gson, TestClass.class).read(jsonReader);

    // Check that some fields are excluded
    assertEquals(30, deserializedTestClass.getAge());
    assertNull(deserializedTestClass.getName());
    assertNull(deserializedTestClass.getNestedClassField());
  }
}