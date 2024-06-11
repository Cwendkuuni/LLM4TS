import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testSimpleSerialDeserial() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(factory)
        .create();

    TestClass testClass = new TestClass("John Doe", 30);
    String json = gson.toJson(testClass);

    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
    TestClass deserializedObject = gson.fromJson(jsonObject, TestClass.class);

    assertEquals(testClass.name, deserializedObject.name);
    assertEquals(testClass.age, deserializedObject.age);
  }

  @Test
  public void testFieldAnnotationSerialization() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(factory)
        .create();

    TestClassWithAnnotation testClass = new TestClassWithAnnotation("John Doe", 30, "alias");
    String json = gson.toJson(testClass);

    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
    TestClassWithAnnotation deserializedObject = gson.fromJson(jsonObject, TestClassWithAnnotation.class);

    assertEquals(testClass.name, deserializedObject.name);
    assertEquals(testClass.age, deserializedObject.age);
    assertEquals(testClass.alias, deserializedObject.alias);
  }

  @Test
  public void testFieldExclusion() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            if (field.getName().equals("age")) {
              return serialize;
            }
            return false;
          }
        }
    );

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(factory)
        .create();

    TestClass testClass = new TestClass("John Doe", 30);
    String json = gson.toJson(testClass);

    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
    TestClass deserializedObject = gson.fromJson(jsonObject, TestClass.class);

    assertNull(deserializedObject.age);
  }

  @Test
  public void testClassExclusion() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return clazz.isAssignableFrom(TestClassWithInner.class);
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(factory)
        .create();

    TestClassWithInner testClass = new TestClassWithInner("John Doe", new TestClassWithInnerInner());
    String json = gson.toJson(testClass);

    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
    TestClassWithInner deserializedObject = gson.fromJson(jsonObject, TestClassWithInner.class);

    assertNull(deserializedObject.inner);
  }

  @Test
  public void testJsonAdapterUsage() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(factory)
        .create();

    TestClassWithJsonAdapter testClass = new TestClassWithJsonAdapter("John Doe", new DateAdapter());
    String json = gson.toJson(testClass);

    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
    TestClassWithJsonAdapter deserializedObject = gson.fromJson(jsonObject, TestClassWithJsonAdapter.class);

    assertEquals(testClass.dateAdapter.toString(), deserializedObject.dateAdapter.toString());
  }

  private static class TestClass {
    String name;
    int age;

    TestClass(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  private static class TestClassWithAnnotation {
    String name;
    int age;
    String alias;

    @SerializedName("aliasName")
    TestClassWithAnnotation(String name, int age, String alias) {
      this.name = name;
      this.age = age;
      this.alias = alias;
    }
  }

  private static class TestClassWithInner {
    String name;
    TestClassWithInnerInner inner;

    TestClassWithInner(String name, TestClassWithInnerInner inner) {
      this.name = name;
      this.inner = inner;
    }
  }

  private static class TestClassWithInnerInner {
  }

  private static class TestClassWithJsonAdapter {
    String name;
    DateAdapter dateAdapter;

    TestClassWithJsonAdapter(String name, DateAdapter dateAdapter) {
      this.name = name;
      this.dateAdapter = dateAdapter;
    }
  }

  private static class DateAdapter extends TypeAdapter<Date> {
    @Override public Date read(JsonReader in) throws IOException {
      return new Date();
    }

    @Override public void write(JsonWriter out, Date value) throws IOException {
      out.value("2022-12-31T23:59:59Z"); // just a placeholder value
    }
  }
}