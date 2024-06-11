import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Given
    Gson gson = new GsonBuilder()
        .exclusionStrategy(new Excluder())
        .setFieldNamingStrategy(new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName().toUpperCase();
          }
        })
        .create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName().toUpperCase();
          }
        }, new Excluder());

    // When
    Class testClass = TestClass.class;
    TypeAdapter<TestClass> testClassAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<TestClass>() {});

    // Then
    TestClass testClassInstance = new TestClass();
    testClassInstance.field1 = "field1";
    testClassInstance.field2 = 2;

    // Serialize
    JsonElement serializedJson = testClassAdapter.write(new JsonWriter(System.out), testClassInstance);
    JsonObject serializedJsonObject = serializedJson.getAsJsonObject();
    assertEquals("FIELD1", serializedJsonObject.name(0).getAsString());
    assertEquals("field1", serializedJsonObject.get("FIELD1").getAsString());
    assertEquals("FIELD2", serializedJsonObject.name(1).getAsString());
    assertEquals(2, serializedJsonObject.get("FIELD2").getAsJsonPrimitive().getAsInt());

    // Deserialize
    TestClass deserializedTestClassInstance = testClassAdapter.read(new JsonReader(new JsonReaderTestInputStream(
        "{" +
            "\"FIELD1\":\"testField1\"," +
            "\"FIELD2\":1," +
            "}"
    )));
    assertNotNull(deserializedTestClassInstance);
    assertEquals("testField1", deserializedTestClassInstance.field1);
    assertEquals(1, deserializedTestClassInstance.field2);

    // Edge case: null value
    TestClass nullTestClassInstance = new TestClass();
    assertNull(testClassAdapter.read(new JsonReader(new JsonReaderTestInputStream("null"))));

    // Error case: invalid JSON
    assertThrows(JsonSyntaxException.class, () -> testClassAdapter.read(new JsonReader(new JsonReaderTestInputStream(
        "{" +
            "\"FIELD1\":1," +
            "\"FIELD2\":" +
            "}"
    ))));
  }

  private static class TestClass {
    @JsonAdapter(JsonAdapterTest.JsonAdapterForString.class)
    private String field1;
    private int field2;
  }

  private static class JsonAdapterForString implements JsonAdapter<String> {
    @Override public String read(JsonElement jsonElement) throws JsonSyntaxException {
      throw new UnsupportedOperationException();
    }

    @Override public JsonElement write(JsonWriter jsonWriter, String string) throws IOException {
      jsonWriter.value(string.toUpperCase());
      return null;
    }
  }
}

class JsonReaderTestInputStream extends JsonReader {
  private final String jsonString;

  JsonReaderTestInputStream(String jsonString) {
    this.jsonString = jsonString;
  }

  @Override public JsonToken peek() throws IOException {
    return new JsonToken(JsonToken.NULL); // Force starting from the beginning
  }

  @Override public void close() throws IOException {
    // Empty
  }

  @Override public JsonToken next() throws IOException {
    return new JsonToken(JsonToken.BEGIN_OBJECT);
  }

  @Override public String nextName() throws IOException {
    return jsonString.split("\\s+")[0];
  }

  @Override public JsonElement nextValue() throws IOException {
    return parse(jsonString);
  }
}