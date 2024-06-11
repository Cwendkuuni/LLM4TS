package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  private static final class TestData {
    int intField;
    String stringField;
    Map<String, String> mapField;

    TestData() {}

    TestData(int intField, String stringField, Map<String, String> mapField) {
      this.intField = intField;
      this.stringField = stringField;
      this.mapField = mapField;
    }
  }

  private static TestData createTestData() {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    return new TestData(1, "testString", map);
  }

  @Test
  public void testSerializationDeserialization() throws Exception {
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy() {
          @Override public String translateName(Field f) {
            return f.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }
        });

    TypeAdapter<TestData> adapter = factory.create(gson, new TypeToken<TestData>() {});

    TestData testData = createTestData();
    String json = gson.toJson(testData);
    TestData deserialized = gson.fromJson(json, TestData.class);

    assertEquals(testData.intField, deserialized.intField);
    assertEquals(testData.stringField, deserialized.stringField);
    assertEquals(testData.mapField, deserialized.mapField);

    TestData deserializedWithNullMap = gson.fromJson(json, TestData.class);
    assertNull(deserializedWithNullMap.mapField);

    TestData deserializedWithNullString = new TestData(0, null, Collections.emptyMap());
    String jsonWithNullString = gson.toJson(deserializedWithNullString);
    TestData deserializedWithNullString2 = gson.fromJson(jsonWithNullString, TestData.class);
    assertEquals(deserializedWithNullString.intField, deserializedWithNullString2.intField);
    assertNull(deserializedWithNullString2.stringField);
    assertEquals(deserializedWithNullString.mapField, deserializedWithNullString2.mapField);
  }
}