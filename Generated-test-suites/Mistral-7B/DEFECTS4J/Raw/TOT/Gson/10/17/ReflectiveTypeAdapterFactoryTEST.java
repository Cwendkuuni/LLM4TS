import com.google.gson.*;
import org.junit.Assert;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Prepare test data
    Class testClass = TestClass.class;
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder() {
          @Override
          public boolean excludeClass(Class clazz, boolean serialize) {
            return false;
          }

          @Override
          public boolean excludeField(Field field, boolean serialize) {
            return field.getName().equalsIgnoreCase("excludedField");
          }
        }).create();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy.Standard(),
        new Excluder() {
          @Override
          public boolean excludeClass(Class clazz, boolean serialize) {
            return false;
          }

          @Override
          public boolean excludeField(Field field, boolean serialize) {
            return field.getName().equalsIgnoreCase("excludedField");
          }
        });

    // Test serialization
    TestClass testObject = new TestClass("testValue", 123, new NestedTestClass("nestedTestValue"));
    JsonElement jsonElement = gson.toJsonTree(testObject, testClass);
    String jsonString = jsonElement.getAsJsonObject().toString();
    Assert.assertEquals("{\"value\":\"testValue\",\"intField\":123,\"nested\":" +
        "{\"value\":\"nestedTestValue\"}}", jsonString);

    // Test deserialization
    TestClass deserializedObject = gson.fromJson(jsonString, testClass);
    Assert.assertEquals("testValue", deserializedObject.value);
    Assert.assertEquals(123, deserializedObject.intField);
    Assert.assertEquals("nestedTestValue", deserializedObject.nested.value);

    // Test error cases
    try {
      gson.fromJson(jsonString, String.class);
      Assert.fail();
    } catch (JsonSyntaxException e) {
      // Expected
    }

    try {
      gson.toJson(null, testClass);
      Assert.fail();
    } catch (JsonException e) {
      // Expected
    }

    try {
      gson.fromJson("{\"value\":123}", testClass);
      Assert.fail();
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  static class NestedTestClass {
    private String value;

    public NestedTestClass(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  static class TestClass {
    private String value;
    private int intField;
    private NestedTestClass nested;

    public TestClass(String value, int intField, NestedTestClass nested) {
      this.value = value;
      this.intField = intField;
      this.nested = nested;
    }

    @JsonAdapter(NestedTestClassJsonAdapter.class)
    public NestedTestClass getNested() {
      return nested;
    }

    @SerializedName("excludedField")
    private String excludedField;
  }
}

class NestedTestClassJsonAdapter extends TypeAdapter<NestedTestClass> {
  @Override
  public NestedTestClass read(JsonReader jsonReader) throws IOException {
    return new NestedTestClass(jsonReader.nextString());
  }

  @Override
  public void write(JsonWriter jsonWriter, NestedTestClass value) throws IOException {
    jsonWriter.value(value.getValue());
  }
}