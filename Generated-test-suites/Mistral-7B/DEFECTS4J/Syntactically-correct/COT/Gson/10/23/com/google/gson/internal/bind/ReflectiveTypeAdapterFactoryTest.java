import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private static final class TestTypeAdapterFactory implements TypeAdapterFactory {
    @Override public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
      return new ReflectiveTypeAdapterFactory(null, null, null).create(gson, type);
    }
  }

  private static final class TestType implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonAdapter(TestJsonAdapter.class)
    private int field1;

    private String field2;

    @SerializedName("field3")
    private int field3;

    private transient String transientField;

    public TestType() {}

    public TestType(int field1, String field2, int field3) {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }

    public int getField1() {
      return field1;
    }

    public String getField2() {
      return field2;
    }

    public int getField3() {
      return field3;
    }

    public String getTransientField() {
      return transientField;
    }

    public void setTransientField(String transientField) {
      this.transientField = transientField;
    }
  }

  private static final class TestJsonAdapter extends TypeAdapter<TestType> {
    @Override public TestType read(JsonReader in) throws IOException {
      JsonObject jsonObject = in.peek();
      TestType testType = new TestType();
      if (jsonObject.has("field1")) {
        testType.field1 = jsonObject.get("field1").getAsInt();
      }
      if (jsonObject.has("field2")) {
        testType.field2 = jsonObject.get("field2").getAsString();
      }
      if (jsonObject.has("field3")) {
        testType.field3 = jsonObject.get("field3").getAsInt();
      }
      return testType;
    }

    @Override public void write(JsonWriter out, TestType value) throws IOException {
      out.beginObject();
      if (value.field1 != 0) {
        out.name("field1");
        out.value(value.field1);
      }
      if (value.field2 != null) {
        out.name("field2");
        out.value(value.field2);
      }
      if (value.field3 != 0) {
        out.name("field3");
        out.value(value.field3);
      }
      out.endObject();
    }
  }

  @org.junit.Before
  public void setup() {
    fieldNamingPolicy = new FieldNamingPolicy.Default();
    excluder = mock(Excluder.class);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = new GsonBuilder()
        .setFieldNamingPolicy(fieldNamingPolicy)
        .setExcluder(excluder)
        .setLenient()
        .create();
  }

  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @org.junit.Test
  public void testCreate() {
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder);
    TypeAdapter<TestType> typeAdapter = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(TestType.class));
    assertNotNull(typeAdapter);
  }

  @org.junit.Test
  public void testExcludeField() {
    boolean excludeField = ReflectiveTypeAdapterFactory.excludeField(
        TestType.class.getDeclaredField("transientField"), false);
    assertFalse(excludeField);

    excludeField = ReflectiveTypeAdapterFactory.excludeField(
        TestType.class.getDeclaredField("transientField"), true);
    assertTrue(excludeField);
  }

  @org.junit.Test
  public void testGetFieldNames() {
    Field field1 = TestType.class.getDeclaredField("field1");
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field1);
    assertEquals("field1", fieldNames.get(0));
    assertEquals(1, fieldNames.size());

    field1 = TestType.class.getDeclaredField("field2");
    fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field1);
    assertNull(fieldNames.get(0));
    assertEquals(0, fieldNames.size());

    field1 = TestType.class.getDeclaredField("field3");
    fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field1);
    assertEquals("field3", fieldNames.get(0));
    assertEquals(1, fieldNames.size());
  }

  @org.junit.Test
  public void testCreateBoundField() {
    Type fieldType = Integer.TYPE;
    Field field = TestType.class.getDeclaredField("field1");
    String name = "field1";
    BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField(name, true, true) {
      @Override void write(JsonWriter writer, Object value)
          throws IOException, IllegalAccessException {
        writer.value(boundField.name);
      }

      @Override void read(JsonReader reader, Object value)
          throws IOException, IllegalAccessException {
        // Empty implementation as this test case only checks the creation of the BoundField instance.
      }

      @Override public boolean writeField(Object value) throws IOException, IllegalAccessException {
        return false;
      }
    };
    assertEquals("field1", boundField.name);
    assertTrue(boundField.serialized);
    assertTrue(boundField.deserialized);
  }

  @org.junit.Test
  public void testGetBoundFields() {
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    boundFields.put("field1", new ReflectiveTypeAdapterFactory.BoundField("field1", true, true));
    boundFields.put("field2", new ReflectiveTypeAdapterFactory.BoundField("field2", true, true));
    boundFields.put("field3", new ReflectiveTypeAdapterFactory.BoundField("field3", true, true));
    assertEquals(3, boundFields.size());
  }

  @org.junit.Test
  public void testAdapterCreation() {
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    boundFields.put("field1", new ReflectiveTypeAdapterFactory.BoundField("field1", true, true));
    boundFields.put("field2", new ReflectiveTypeAdapterFactory.BoundField("field2", true, true));
    boundFields.put("field3", new ReflectiveTypeAdapterFactory.BoundField("field3", true, true));
    TypeAdapter<TestType> typeAdapter = new ReflectiveTypeAdapterFactory.Adapter<>(
        constructorConstructor.get(TypeToken.get(TestType.class)), boundFields);
    assertNotNull(typeAdapter);
  }

  @org.junit.Test
  public void testRead() throws IOException {
    TestType testType = new TestType(1, "test", 2);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("field1", 1);
    jsonObject.addProperty("field2", "test");
    jsonObject.addProperty("field3", 2);

    JsonReader jsonReader = new JsonReader(new StringReader(jsonObject.toString()));
    TypeAdapter<TestType> typeAdapter = gson.getAdapter(TestType.class);
    TestType result = typeAdapter.read(jsonReader);
    TestType expected = testType;
    assertEquals(expected.getField1(), result.getField1());
    assertEquals(expected.getField2(), result.getField2());
    assertEquals(expected.getField3(), result.getField3());
  }

  @org.junit.Test
  public void testWrite() throws IOException {
    TestType testType = new TestType(1, "test", 2);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    TypeAdapter<TestType> typeAdapter = gson.getAdapter(TestType.class);
    typeAdapter.write(jsonWriter, testType);

    JsonObject jsonObject = new JsonParser(new StringReader(jsonWriter.toString())).getAsJsonObject();
    assertEquals(3, jsonObject.size());
    assertEquals("field1", jsonObject.keySet().iterator().next());
    assertEquals(1, jsonObject.get("field1").getAsInt());
    assertEquals("field2", jsonObject.keySet().iterator().next());
    assertEquals("test", jsonObject.get("field2").getAsString());
    assertEquals("field3", jsonObject.keySet().iterator().next());
    assertEquals(2, jsonObject.get("field3").getAsInt());
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testJsonSyntaxException() throws IOException {
    TestType testType = new TestType();
    JsonReader jsonReader = new JsonReader(new StringReader("{\"field1\":1,\"field2\":}}"));
    TypeAdapter<TestType> typeAdapter = gson.getAdapter(TestType.class);
    typeAdapter.read(jsonReader);
  }
}