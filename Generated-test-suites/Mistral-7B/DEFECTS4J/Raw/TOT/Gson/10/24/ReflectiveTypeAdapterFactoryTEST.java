import com.google.gson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReflectiveTypeAdapterFactoryTest {

  private static final Class<?> CLASS_TO_TEST = ReflectiveTypeAdapterFactory.class;
  private static final TypeAdapterFactory TYPE_ADAPTER_FACTORY = new ReflectiveTypeAdapterFactory(
      new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

  private JsonElement jsonInput;
  private Tester tester;

  private static class Tester {
    private int intField;
    private String stringField;
    private List<Integer> listField;

    public Tester(int intField, String stringField, List<Integer> listField) {
      this.intField = intField;
      this.stringField = stringField;
      this.listField = listField;
    }
  }

  @BeforeAll
  void setup() {
    tester = new Tester(1, "test", new ArrayList<>());
  }

  @Test
  void testSerializeAndDeserialize() throws JsonParseException, JsonSyntaxException {
    Gson gson = new GsonBuilder().setLenient().create();

    JsonWriter writer = new JsonWriter(new StringWriter());
    gson.toJson(tester, writer);
    jsonInput = writer.getJsonElement();

    Gson reverseGson = new GsonBuilder().create();
    Tester deserializedObject = reverseGson.fromJson(jsonInput, Tester.class);

    assertEquals(tester.intField, deserializedObject.intField);
    assertEquals(tester.stringField, deserializedObject.stringField);
    assertEquals(tester.listField, deserializedObject.listField);
  }

  @Test
  void testSerializeNullObject() throws JsonParseException, JsonSyntaxException {
    Gson gson = new GsonBuilder().setLenient().create();

    JsonWriter writer = new JsonWriter(new StringWriter());
    gson.toJson(null, writer);
    jsonInput = writer.getJsonElement();

    assertNull(reverseGson.fromJson(jsonInput, Tester.class));
  }

  @Test
  void testDeserializeNullField() throws JsonParseException, JsonSyntaxException {
    jsonInput = new JsonParser().parse("{\"stringField\":null,\"intField\":1}").getAsJsonObject();

    Tester deserializedObject = reverseGson.fromJson(jsonInput, Tester.class);

    assertNull(deserializedObject.stringField);
    assertEquals(tester.intField, deserializedObject.intField);
  }

  @Test
  void testDeserializeEmptyList() throws JsonParseException, JsonSyntaxException {
    jsonInput = new JsonParser().parse("{\"intField\":1,\"listField\":[]}").getAsJsonObject();

    Tester deserializedObject = reverseGson.fromJson(jsonInput, Tester.class);

    assertTrue(deserializedObject.listField.isEmpty());
    assertEquals(tester.intField, deserializedObject.intField);
  }

  @Test
  void testDeserializeListWithSingleElement() throws JsonParseException, JsonSyntaxException {
    jsonInput = new JsonParser().parse("{\"intField\":1,\"listField\":[1]}").getAsJsonObject();

    Tester deserializedObject = reverseGson.fromJson(jsonInput, Tester.class);

    List<Integer> expectedList = new ArrayList<>();
    expectedList.add(1);

    assertEquals(tester.intField, deserializedObject.intField);
    assertEquals(expectedList, deserializedObject.listField);
  }

  @Test
  void testDeserializeListWithMultipleElements() throws JsonParseException, JsonSyntaxException {
    jsonInput = new JsonParser().parse("{\"intField\":1,\"listField\":[1,2,3]}").getAsJsonObject();

    Tester deserializedObject = reverseGson.fromJson(jsonInput, Tester.class);

    List<Integer> expectedList = new ArrayList<>();
    expectedList.add(1);
    expectedList.add(2);
    expectedList.add(3);

    assertEquals(tester.intField, deserializedObject.intField);
    assertEquals(expectedList, deserializedObject.listField);
  }

  @Test
  void testDeserializeObjectWithFieldsOfDifferentTypes()
      throws JsonParseException, JsonSyntaxException {
    jsonInput = new JsonParser().parse("{\"intField\":1,\"stringField\":\"test\",\"listField\":[1,2]}")
        .getAsJsonObject();

    Tester deserializedObject = reverseGson.fromJson(jsonInput, Tester.class);

    assertEquals(tester.intField, deserializedObject.intField);
    assertEquals(tester.stringField, deserializedObject.stringField);
    assertEquals(tester.listField, deserializedObject.listField);
  }
}