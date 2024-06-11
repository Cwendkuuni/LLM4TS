package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  private static <T> void testSerializationDeserialization(ReflectiveTypeAdapterFactory factory, Class<T> clazz)
      throws IOException {
    Gson gson = new GsonBuilder().setExcluder(new Excluder() {
      @Override public boolean excludeField(Field f, boolean serialize) {
        return false;
      }

      @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return false;
      }
    }).create();

    TypeAdapter<T> adapter = factory.create(gson, TypeToken.get(clazz));
    T obj = adapter.fromJson(createJsonObjectForSerializationDeserializationTest(clazz), clazz);
    T objRoundTrip = adapter.fromJson(createJsonObjectForSerializationDeserializationTest(clazz), clazz);

    assertNotNull(obj);
    assertNotNull(objRoundTrip);
    assertEquals(obj, objRoundTrip);
  }

  private static JsonObject createJsonObjectForSerializationDeserializationTest(Class<?> clazz) {
    JsonObject jsonObject = new JsonObject();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      boolean serialize = excludeField(field, true);
      boolean deserialize = excludeField(field, false);
      if (!serialize && !deserialize) continue;
      List<String> fieldNames = getFieldNames(field);
      for (String name : fieldNames) {
        JsonElement value = createJsonElementForJsonObjectForSerializationDeserializationTest(field.getType(), name);
        jsonObject.add(name, value);
      }
    }
    return jsonObject;
  }

  private static JsonElement createJsonElementForJsonObjectForSerializationDeserializationTest(Type type, String name) {
    if (type.isPrimitive()) {
      switch (type.getName()) {
        case "boolean":
          return JsonElement.getAsJsonPrimitive(true);
        case "byte":
          return JsonElement.getAsJsonPrimitive((byte) 1);
        case "short":
          return JsonElement.getAsJsonPrimitive((short) 1);
        case "int":
          return JsonElement.getAsJsonPrimitive(1);
        case "long":
          return JsonElement.getAsJsonPrimitive(1L);
        case "char":
          return JsonElement.getAsJsonPrimitive('a');
        case "float":
          return JsonElement.getAsJsonPrimitive(1.0f);
        case "double":
          return JsonElement.getAsJsonPrimitive(1.0);
      }
    }
    return JsonElement.getAsJsonObject(createJsonObjectForSerializationDeserializationTest(type));
  }

  private static List<String> getFieldNames(Field field) {
    SerializedName annotation = field.getAnnotation(SerializedName.class);
    if (annotation != null) {
      String serializedName = annotation.value();
      String[] alternates = annotation.alternate();
      if (alternates.length == 0) {
        return Collections.singletonList(serializedName);
      }
      List<String> fieldNames = new ArrayList<>(alternates.length + 1);
      fieldNames.add(serializedName);
      for (String alternate : alternates) {
        fieldNames.add(alternate);
      }
      return fieldNames;
    }

    String name = FieldNamingPolicy.DEFAULT.translateName(field);
    return Collections.singletonList(name);
  }

  private static boolean excludeField(Field f, boolean serialize) {
    return false;
  }

  @Test
  public void testReflectionAdapter() {
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), FieldNamingPolicy.DEFAULT, new Excluder() {
          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }

          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }
        });

    testSerializationDeserialization(factory, TestReflectiveTypeAdapterFactory.TestClass1.class);

    testSerializationDeserialization(factory, TestReflectiveTypeAdapterFactory.TestClass2.class);

    testSerializationDeserialization(factory, TestReflectiveTypeAdapterFactory.TestClass3.class);
  }

  static class TestClass1 {
    private int field1;
    private String field2;

    public TestClass1() {
      //empty constructor
    }

    public TestClass1(int field1, String field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    @JsonAdapter(TestReflectiveTypeAdapterFactory.TestJsonAdapter.class)
    public int getField1() {
      return field1;
    }

    @SerializedName("field3")
    public String getField2() {
      return field2;
    }

    public static class TestJsonAdapter extends JsonAdapter<TestClass1> {
      @Override public TestClass1 fromJson(JsonElement jsonElement) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return new TestClass1(jsonObject.get("field1").getAsInt(), jsonObject.get("field2").getAsString());
      }

      @Override public JsonElement toJson(TestClass1 src) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("field1", src.field1);
        jsonObject.addProperty("field2", src.field2);
        jsonObject.addProperty("field3", src.field2);
        return jsonObject;
      }
    }
  }

  static class TestClass2 {
    private int field1;
    private String field2;

    public TestClass2() {
      //empty constructor
    }

    public TestClass2(int field1, String field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    @JsonAdapter(TestReflectiveTypeAdapterFactory.TestJsonAdapter2.class)
    public int getField1() {
      return field1;
    }

    @JsonAdapter(TestReflectiveTypeAdapterFactory.TestJsonAdapter3.class)
    public String getField2() {
      return field2;
    }

    static class TestJsonAdapter1 extends JsonAdapter<TestClass2> {
      @Override public TestClass2 fromJson(JsonElement jsonElement) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return new TestClass2(jsonObject.get("field1").getAsInt(), jsonObject.get("field2").getAsString());
      }

      @Override public JsonElement toJson(TestClass2 src) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("field1", src.field1);
        jsonObject.addProperty("field2", src.field2);
        jsonObject.addProperty("field3", src.field2);
        return jsonObject;
      }
    }

    static class TestJsonAdapter2 extends JsonAdapter<TestClass2> {
      @Override public TestClass2 fromJson(JsonElement jsonElement) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return new TestClass2(jsonObject.get("field1").getAsInt(), jsonObject.get("field4").getAsString());
      }

      @Override public JsonElement toJson(TestClass2 src) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("field1", src.field1);
        jsonObject.addProperty("field4", src.field2);
        return jsonObject;
      }
    }

    static class TestJsonAdapter3 extends JsonAdapter<TestClass2> {
      @Override public TestClass2 fromJson(JsonElement jsonElement) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return new TestClass2(jsonObject.get("field1").getAsInt(), jsonObject.get("field5").getAsString());
      }

      @Override public JsonElement toJson(TestClass2 src) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("field1", src.field1);
        jsonObject.addProperty("field5", src.field2);
        return jsonObject;
      }
    }
  }

  static class TestClass3 {
    private int field1;
    private String field2;

    public TestClass3() {
      //empty constructor
    }

    public TestClass3(int field1, String field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    @JsonAdapter(TestReflectiveTypeAdapterFactory.TestJsonAdapter4.class)
    public int getField1() {
      return field1;
    }

    @JsonAdapter(TestReflectiveTypeAdapterFactory.TestJsonAdapter5.class)
    public String getField2() {
      return field2;
    }

    static class TestJsonAdapter4 extends JsonAdapter<TestClass3> {
      @Override public TestClass3 fromJson(JsonElement jsonElement) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return new TestClass3(jsonObject.get("field1").getAsInt(), jsonObject.get("field6").getAsString());
      }

      @Override public JsonElement toJson(TestClass3 src) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("field1", src.field1);
        jsonObject.addProperty("field6", src.field2);
        return jsonObject;
      }
    }

    static class TestJsonAdapter5 extends JsonAdapter<TestClass3> {
      @Override public TestClass3 fromJson(JsonElement jsonElement) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return new TestClass3(jsonObject.get("field1").getAsInt(), jsonObject.get("field7").getAsString());
      }

      @Override public JsonElement toJson(TestClass3 src) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("field1", src.field1);
        jsonObject.addProperty("field7", src.field2);
        return jsonObject;
      }
    }
  }
}