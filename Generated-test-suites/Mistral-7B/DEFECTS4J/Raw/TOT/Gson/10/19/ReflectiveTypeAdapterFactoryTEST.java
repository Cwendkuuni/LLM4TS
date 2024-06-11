import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .setTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(),
            FieldNamingPolicy.IDENTITY,
            new Excluder()))
        .create();

    // When
    TestClass testClass = new TestClass();
    TestClass testClassSerialized = gson.fromJson(
        "{ \"field1\": 1, \"field2\": \"hello\", \"field3\": null }", TestClass.class);
    TestClass testClassDeserialized = gson.toJson(testClassSerialized);

    // Then
    Assert.assertEquals(testClassSerialized, gson.fromJson(testClassDeserialized, TestClass.class));

    // When setting values via JSON
    TestClass testClassJsonValues = new TestClass();
    gson.fromJson(
        "{ \"field1\": 2, \"field2\": \"world\", \"field3\": null }", TestClass.class, testClassJsonValues);
    Assert.assertEquals(2, testClassJsonValues.field1);
    Assert.assertEquals("world", testClassJsonValues.field2);
    Assert.assertNull(testClassJsonValues.field3);

    // When excluding fields
    TestClassExcluding excludingTestClass = new TestClassExcluding();
    gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation(FieldNames.class)
        .setTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(),
            FieldNamingPolicy.IDENTITY,
            new Excluder()))
        .create();
    TestClassExcluding testClassExcludingSerialized = gson.fromJson(
        "{ \"field1\": 1, \"field2\": \"hello\", \"field3\": null }", TestClassExcluding.class);
    TestClassExcluding testClassExcludingDeserialized = gson.toJson(testClassExcludingSerialized);
    Assert.assertEquals(testClassExcludingSerialized, gson.fromJson(testClassExcludingDeserialized, TestClassExcluding.class));
    Assert.assertNull(testClassExcludingSerialized.getField3());
    Assert.assertNull(testClassExcludingDeserialized.getField3());

    // When deserializing with JsonAdapter
    TestClassWithJsonAdapter testClassWithJsonAdapter = new TestClassWithJsonAdapter();
    gson = new GsonBuilder()
        .setTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(),
            FieldNamingPolicy.IDENTITY,
            new Excluder()))
        .create();
    TestClassWithJsonAdapter testClassWithJsonAdapterSerialized = gson.toJson(testClassWithJsonAdapter);
    TestClassWithJsonAdapter testClassWithJsonAdapterDeserialized = gson.fromJson(testClassWithJsonAdapterSerialized, TestClassWithJsonAdapter.class);
    Assert.assertEquals(testClassWithJsonAdapterSerialized, testClassWithJsonAdapterDeserialized);

    // When serializing with JsonAdapter
    TestClassWithJsonAdapter testClassWithJsonAdapterInstance = gson.fromJson(testClassWithJsonAdapterSerialized, TestClassWithJsonAdapter.class);
    String testClassWithJsonAdapterJson = gson.toJson(testClassWithJsonAdapterInstance);
    TestClassWithJsonAdapter testClassWithJsonAdapterJsonParsed = gson.fromJson(testClassWithJsonAdapterJson, TestClassWithJsonAdapter.class);
    Assert.assertEquals(testClassWithJsonAdapterInstance, testClassWithJsonAdapterJsonParsed);
  }

  // Test classes used for testing
  static class TestClass {
    int field1;
    String field2;
    Object field3;
  }

  static class TestClassExcluding {
    int field1;
    @Exclude
    String excludedField2;
    Object field3;
  }

  static class TestClassWithJsonAdapter {
    @JsonAdapter(TestClassWithJsonAdapterSerializer.class)
    int field1;
    String field2;
    Object field3;
  }

  static class TestClassWithJsonAdapterSerializer
      extends TypeAdapter<TestClassWithJsonAdapter> {
    @Override public TestClassWithJsonAdapter read(JsonReader in) throws IOException {
      TestClassWithJsonAdapter testClassWithJsonAdapter = new TestClassWithJsonAdapter();
      in.beginObject();
      while (in.hasNext()) {
        String name = in.nextName();
        if (name.equals("field1")) {
          testClassWithJsonAdapter.field1 = in.nextInt();
        } else {
          in.skipValue();
        }
      }
      in.endObject();
      return testClassWithJsonAdapter;
    }

    @Override public void write(JsonWriter out, TestClassWithJsonAdapter value) throws IOException {
      out.beginObject();
      out.name("field1");
      out.value(value.field1);
      out.endObject();
    }
  }

  // Test utility classes
  static class Excluder implements JsonExcluder {
    @Override public boolean shouldSkipField(FieldAttributes field, Object source) {
      return false;
    }

    @Override public boolean shouldSkipClass(Class<?> clazz, Map<Class<?>,Boolean> map) {
      return false;
    }
  }

  static class FieldNamingPolicy implements FieldNamingStrategy {
    @Override public String translateName(Field f) {
      return f.getName();
    }
  }
}