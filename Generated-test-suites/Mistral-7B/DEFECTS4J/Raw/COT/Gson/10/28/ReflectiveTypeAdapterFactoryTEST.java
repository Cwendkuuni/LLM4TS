import static org.junit.Assert.*;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  interface MyTestClass {
    String getName();
    int getAge();
    List<String> getList();
  }

  static class MyTestClassExcluder implements ExclusionStrategy {
    @Override public boolean shouldSkipClass(Class<?> clazz) {
      return false;
    }

    @Override public boolean shouldSkipField(Field f) {
      return false;
    }
  }

  static class MyTestClassFieldNamingPolicy implements FieldNamingPolicy {
    @Override public String translateName(Field f) {
      return "new_" + f.getName();
    }
  }

  static class MyTestClassJsonAdapter implements JsonAdapter<MyTestClass> {
    @Override public MyTestClass read(JsonElement jsonElement) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override public JsonElement write(JsonElement jsonElement, MyTestClass myTestClass) throws IOException {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add("name", new JsonPrimitive(myTestClass.getName()));
      jsonObject.add("age", new JsonPrimitive(myTestClass.getAge()));
      jsonObject.add("list", new JsonArray());
      for (String s : myTestClass.getList()) {
        jsonObject.getAsJsonArray("list").add(new JsonPrimitive(s));
      }
      return jsonObject;
    }
  }

  private static <T> void testSerializationDeserialization(MyTestClass original, String expectedJsonString)
      throws IOException {
    Gson gson = new GsonBuilder()
        .exclusionStrategy(new MyTestClassExcluder())
        .fieldNamingPolicy(new MyTestClassFieldNamingPolicy())
        .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(new ObjectConstructor<MyTestClass>() {}),
            new FieldNamingPolicy() {
              @Override public String translateName(Field f) {
                return f.getName();
              }
            },
            new MyTestClassExcluder()
        ))
        .registerTypeAdapter(MyTestClass.class, new MyTestClassJsonAdapter())
        .create();

    JsonElement jsonElement = gson.toJsonTree(original);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    String actualJsonString = jsonObject.toString();
    assertEquals(expectedJsonString, actualJsonString);
  }

  @org.junit.Test
  public void testSerializationDeserialization() throws IOException {
    MyTestClass myTestClass = new MyTestClass() {
      @Override public String getName() {
        return "John Doe";
      }

      @Override public int getAge() {
        return 30;
      }

      @Override public List<String> getList() {
        return new ArrayList<String>() {{
          add("apple");
          add("banana");
        }};
      }
    };

    String expectedJsonString = "{"
        + "\"name\":\"John Doe\","
        + "\"age\":30,"
        + "\"list\":[\"apple\",\"banana\"]"
        + "}";

    testSerializationDeserialization(myTestClass, expectedJsonString);
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testSerializationDeserializationWithIllegalAccessException() throws IOException {
    MyTestClass myTestClass = new MyTestClass() {
      private String name;
      private int age;
      private List<String> list;

      @Override public String getName() {
        return name;
      }

      @Override public int getAge() {
        return age;
      }

      @Override public List<String> getList() {
        return list;
      }

      @Override public void setName(String name) {
        this.name = name;
      }

      @Override public void setAge(int age) {
        this.age = age;
      }

      @Override public void setList(List<String> list) {
        this.list = list;
      }
    };

    Gson gson = new GsonBuilder()
        .exclusionStrategy(new MyTestClassExcluder())
        .fieldNamingPolicy(new MyTestClassFieldNamingPolicy())
        .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(new ObjectConstructor<MyTestClass>() {}),
            new FieldNamingPolicy() {
              @Override public String translateName(Field f) {
                return f.getName();
              }
            },
            new MyTestClassExcluder()
        ))
        .registerTypeAdapter(MyTestClass.class, new MyTestClassJsonAdapter())
        .create();

    gson.fromJson(expectedJsonString, MyTestClass.class);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testSerializationDeserializationWithAssertionError() throws IOException {
    MyTestClass myTestClass = new MyTestClass() {
      private String name;
      private int age;
      private List<String> list;

      @Override public String getName() {
        return name;
      }

      @Override public int getAge() {
        return age;
      }

      @Override public List<String> getList() {
        return list;
      }

      @Override public void setName(String name) {
        this.name = name;
      }

      @Override public void setAge(int age) {
        this.age = age;
      }

      @Override public void setList(List<String> list) {
        this.list = list;
      }
    };

    Gson gson = new GsonBuilder()
        .exclusionStrategy(new MyTestClassExcluder())
        .fieldNamingPolicy(new MyTestClassFieldNamingPolicy())
        .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(new ObjectConstructor<MyTestClass>() {}),
            new FieldNamingPolicy() {
              @Override public String translateName(Field f) {
                return f.getName();
              }
            },
            new MyTestClassExcluder()
        ))
        .registerTypeAdapter(MyTestClass.class, new MyTestClassJsonAdapter())
        .create();

    MyTestClass deserializedMyTestClass = gson.fromJson(expectedJsonString, MyTestClass.class);
    deserializedMyTestClass.setName("updatedName");
    deserializedMyTestClass.setAge(31);
    deserializedMyTestClass.getList().add("newElement");
  }

  @org.junit.Test
  public void testExclusionStrategy() throws IOException {
    MyTestClass myTestClass = new MyTestClass() {
      @JsonAdapter(MyTestClassPrivateAdapter.class)
      @SerializedName("private_name")
      private String privateName;

      @JsonAdapter(MyTestClassPrivateAdapter.class)
      @SerializedName("private_age")
      private int privateAge;

      @Override public String getName() {
        return "John Doe";
      }

      @Override public int getAge() {
        return 30;
      }

      @Override public List<String> getList() {
        return new ArrayList<String>() {{
          add("apple");
          add("banana");
        }};
      }
    };

    Gson gson = new GsonBuilder()
        .exclusionStrategy(new MyTestClassExcluder())
        .fieldNamingPolicy(new MyTestClassFieldNamingPolicy())
        .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(new ObjectConstructor<MyTestClass>() {}),
            new FieldNamingPolicy() {
              @Override public String translateName(Field f) {
                return f.getName();
              }
            },
            new MyTestClassExcluder()
        ))
        .registerTypeAdapter(MyTestClass.class, new MyTestClassJsonAdapter())
        .create();

    String expectedJsonString = "{"
        + "\"name\":\"John Doe\","
        + "\"age\":30,"
        + "\"list\":[\"apple\",\"banana\"]"
        + "}";

    testSerializationDeserialization(myTestClass, expectedJsonString);

    MyTestClass deserializedMyTestClass = gson.fromJson(expectedJsonString, MyTestClass.class);

    assertNull(deserializedMyTestClass.privateName);
    assertEquals(30, deserializedMyTestClass.privateAge);
  }

  static class MyTestClassPrivateAdapter implements JsonAdapter<MyTestClass> {
    @Override public MyTestClass read(JsonElement jsonElement) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override public JsonElement write(JsonElement jsonElement, MyTestClass myTestClass) throws IOException {
      JsonObject jsonObject = new JsonObject();
      return jsonObject;
    }
  }
}