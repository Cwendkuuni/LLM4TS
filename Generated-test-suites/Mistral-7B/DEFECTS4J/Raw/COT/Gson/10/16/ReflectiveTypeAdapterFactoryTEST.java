import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private static final ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
      new ConstructorConstructor(), // dummy constructor constructor
      new FieldNamingStrategy() { // dummy field naming strategy
        @Override public String translateName(Field f) {
          return f.getName();
        }
      }, // end of FieldNamingStrategy
      new Excluder() { // dummy excluder
        @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
          return false;
        }

        @Override public boolean excludeField(Field f, boolean serialize) {
          return false;
        }
      } // end of Excluder
  );

  @SuppressWarnings("unchecked")
  private static <T> TypeAdapter<T> createTypeAdapter(Gson gson, TypeToken<T> type) {
    return (TypeAdapter<T>) factory.create(gson, type);
  }

  @SuppressWarnings("rawtypes")
  private static Map getBoundFields(Gson gson, Class rawType) {
    return (Map) factory.getBoundFields(gson, TypeToken.get(rawType.getName()), rawType).getValue();
  }

  @Test
  public void testCreate() {
    Class dummyClass = new DummyClass();
    TypeAdapter<DummyClass> adapter = createTypeAdapter(new GsonBuilder().create(), TypeToken.get(DummyClass.class));
    assertNotNull(adapter);
  }

  @Test
  public void testCreate_WhenClassIsInterface_ThenReturnNull() {
    TypeAdapter<Object> adapter = createTypeAdapter(new GsonBuilder().create(), TypeToken.get(Object.class));
    assertNull(adapter);
  }

  @Test
  public void testGetBoundFields_WhenTypeIsInterface_ThenReturnEmptyMap() {
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = getBoundFields(new GsonBuilder().create(), DummyInterface.class);
    assertEquals(0, boundFields.size());
  }

  @Test
  public void testGetBoundFields() throws Exception {
    Gson gson = new GsonBuilder().create();
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = getBoundFields(gson, DummyClass.class);

    Field field = DummyClass.class.getDeclaredField("field1");
    ReflectiveTypeAdapterFactory.BoundField boundField = boundFields.get("field1");

    assertNotNull(boundField);
    assertEquals(field.getName(), boundField.name);
    assertTrue(boundField.deserialized);
    assertTrue(boundField.serialized);
  }

  @Test
  public void testWriteRead() throws Exception {
    Gson gson = new GsonBuilder().create();
    TypeAdapter<DummyClass> adapter = createTypeAdapter(gson, TypeToken.get(DummyClass.class));

    DummyClass original = new DummyClass("Hello", 42);
    String json = gson.toJson(original);
    DummyClass deserialized = adapter.read(JsonParser.parseString(json));

    assertEquals(original, deserialized);
  }

  @Test
  public void testWriteField() throws Exception {
    Gson gson = new GsonBuilder().create();
    TypeAdapter<DummyClass> adapter = createTypeAdapter(gson, TypeToken.get(DummyClass.class));

    DummyClass original = new DummyClass("Hello", 42);
    DummyClass copy = new DummyClass("World", 43);

    Field field = DummyClass.class.getDeclaredField("field1");
    assertTrue(field.writeField(original));
    assertFalse(field.writeField(copy));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testJsonSyntaxException_WhenReadingNullJson() throws Exception {
    Gson gson = new GsonBuilder().create();
    TypeAdapter<DummyClass> adapter = createTypeAdapter(gson, TypeToken.get(DummyClass.class));

    adapter.read(JsonParser.parseString(null));
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException_WhenReadingNullValue() throws Exception {
    Gson gson = new GsonBuilder().create();
    TypeAdapter<DummyClass> adapter = createTypeAdapter(gson, TypeToken.get(DummyClass.class));

    JsonElement jsonElement = new JsonParser().parse("{}");
    JsonReader jsonReader = new JsonReader(jsonElement.getAsReader());
    jsonReader.nextNull();
    adapter.read(jsonReader);
  }

  @Test(expected = IOException.class)
  public void testIOException_WhenWritingToInvalidJsonWriter() throws Exception {
    Gson gson = new GsonBuilder().create();
    TypeAdapter<DummyClass> adapter = createTypeAdapter(gson, TypeToken.get(DummyClass.class));

    DummyClass original = new DummyClass("Hello", 42);
    JsonWriter jsonWriter = new JsonWriter(new Exception("Invalid writer"));
    adapter.write(jsonWriter, original);
  }

  @Test(expected = IllegalAccessException.class)
  public void testIllegalAccessException_WhenAccessingPrivateField() throws Exception {
    Gson gson = new GsonBuilder().create();
    TypeAdapter<DummyClass> adapter = createTypeAdapter(gson, TypeToken.get(DummyClass.class));

    DummyClass original = new DummyClass("Hello", 42);
    Field privateField = DummyClass.class.getDeclaredField("privateField");
    privateField.setAccessible(true);
    privateField.set(original, "test");

    adapter.write(new JsonWriter(System.out), original);
  }

  @Test(expected = AssertionError.class)
  public void testAssertionError_WhenDeserializingToInvalidType() throws Exception {
    Gson gson = new GsonBuilder().create();
    TypeAdapter<DummyClass> adapter = createTypeAdapter(gson, TypeToken.get(DummyClass.class));

    adapter.read(new JsonParser().parse("{}"));
  }

  static class DummyClass {
    @SerializedName("field1")
    private String field1;
    private String privateField;
    @JsonAdapter(StringAdapter.class)
    private String adapterField;

    public DummyClass(String field1, int i) {
      this.field1 = field1;
    }

    public String getField1() {
      return field1;
    }

    @SuppressWarnings("unused")
    private void setField1(String field1) {
      this.field1 = field1;
    }

    @SuppressWarnings("unused")
    public String getPrivateField() {
      return privateField;
    }

    @SuppressWarnings("unused")
    private void setPrivateField(String privateField) {
      this.privateField = privateField;
    }

    public String getAdapterField() {
      return adapterField;
    }

    @SuppressWarnings("unused")
    private void setAdapterField(String adapterField) {
      this.adapterField = adapterField;
    }

    @Override public String toString() {
      return "DummyClass{" +
          "field1='" + field1 + '\'' +
          ", privateField='" + privateField + '\'' +
          ", adapterField='" + adapterField + '\'' +
          '}';
    }

    private static class StringAdapter extends TypeAdapter<String> {
      @Override public String read(JsonReader in) throws IOException {
        return in.nextString();
      }

      @Override public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
      }
    }
  }

  static class DummyInterface {
  }
}