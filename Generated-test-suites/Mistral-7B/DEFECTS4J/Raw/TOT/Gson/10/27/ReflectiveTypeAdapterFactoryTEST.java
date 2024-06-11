import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectionTypeAdapterFactory() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .excludeFieldsWithoutExposeAnnotation()
        .setPrePreProcessor(new Excluder())
        .create();

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder()
    );

    // When
    TypeAdapter<TestClass> testClassTypeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<TestClass>() {});

    // Then
    TestClass testClass = new TestClass();
    testClass.field1 = "field1 value";

    String json = gson.toJson(testClass);
    JsonElement jsonElement = JsonParser.parseString(json);

    TestClass deserializedTestClass = testClassTypeAdapter.read(jsonElement.getAsJsonObject().entrySet().iterator().next().getValue());

    assertEquals(testClass.field1, deserializedTestClass.field1);

    // Edge Case: null value
    TestClass nullTestClass = new TestClass();
    testClass.field1 = null;

    json = gson.toJson(nullTestClass);
    JsonElement nullJsonElement = JsonParser.parseString(json);

    deserializedTestClass = testClassTypeAdapter.read(nullJsonElement.getAsJsonObject().entrySet().iterator().next().getValue());

    assertEquals(null, deserializedTestClass.field1);

    // Error Scenario: missing field
    TestClass missingFieldTestClass = new TestClass();

    json = gson.toJson(missingFieldTestClass);

    TestClass missingFieldDeserializedTestClass = testClassTypeAdapter.read(new JsonParser().parse(json).getAsJsonObject());

    assertEquals(null, missingFieldDeserializedTestClass);
  }

  // Test Class to be tested
  static class TestClass {
    String field1;
  }
}