import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy.LowrerCaseWithUnderscoresStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

  private FieldNamingStrategy fieldNamingPolicy;
  private ConstructorConstructor constructorConstructor;
  private Excluder excluder;
  private Gson gson;

  @Before
  public void setUp() {
    fieldNamingPolicy = new LowrerCaseWithUnderscoresStrategy();
    constructorConstructor = new ConstructorConstructor();
    excluder = mock(Excluder.class);
    gson = new GsonBuilder()
        .setExcluder(excluder)
        .setFieldNamingPolicy(fieldNamingPolicy)
        .setConstructorConstructor(constructorConstructor)
        .create();
  }

  @Test
  public void testCreateMethod() {
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor,
        fieldNamingPolicy, excluder);
    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
    assertNotNull(adapter);
  }

  @Test
  public void testExcludeFieldMethod() {
    boolean result = ReflectiveTypeAdapterFactory.excludeField(new Field() {}, true);
    assertFalse(result);

    Field field = new Field() {};
    Class<?> type = Integer.TYPE;
    result = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);
    assertFalse(result);

    result = ReflectiveTypeAdapterFactory.excludeField(field, false, excluder);
    assertTrue(result);

    result = ReflectiveTypeAdapterFactory.excludeField(new Field() {}, false, excluder);
    assertFalse(result);
  }

  @Test
  public void testGetFieldNamesMethod() throws NoSuchFieldException, IOException {
    Field field = new Field() {};
    field.setName("testField");
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
    assertEquals(1, fieldNames.size());
    assertEquals("testField", fieldNames.get(0));

    field.setAnnotation(new SerializedName("testAlternateName"));
    fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
    assertEquals(2, fieldNames.size());
    assertEquals("testAlternateName", fieldNames.get(1));
    assertEquals("testField", fieldNames.get(0));
  }

  @Test
  public void testCreateBoundFieldMethod() throws IOException, NoSuchFieldException {
    Class<?> rawType = MyClass.class;
    Field field = new Field() {};
    String name = "testName";
    TypeToken<?> fieldType = new TypeToken<String>() {};
    boolean serialize = true;
    boolean deserialize = true;
    ReflectiveTypeAdapterFactory.BoundField boundField = ReflectiveTypeAdapterFactory.createBoundField(
        gson, field, name, fieldType, serialize, deserialize);

    assertEquals(name, boundField.name);
    assertEquals(serialize, boundField.serialized);
    assertEquals(deserialize, boundField.deserialized);

    byte[] json = getJsonBytes(name, "testValue");
    JsonElement jsonElement = gson.fromJson(new InputStreamReader(new ByteArrayInputStream(json)),
        JsonElement.class);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement fieldValueElement = jsonObject.get(name);
    String fieldValue = fieldValueElement.getAsString();

    Object value = new MyClass();
    field.setAccessible(true);
    field.set(value, fieldValue);

    assertEquals(fieldValue, boundField.read(new JsonReader(new InputStreamReader(new ByteArrayInputStream(json))), value));
  }

  @Test
  public void testGetBoundFieldsMethod() throws IOException, NoSuchFieldException {
    Class<?> rawType = MyClass.class;
    Field field1 = new Field() {};
    field1.setName("testField1");
    TypeToken<String> fieldType1 = new TypeToken<String>() {};
    boolean serialize1 = true;
    boolean deserialize1 = true;
    ReflectiveTypeAdapterFactory.BoundField boundField1 = ReflectiveTypeAdapterFactory.createBoundField(
        gson, field1, "testName1", fieldType1, serialize1, deserialize1);

    Field field2 = new Field() {};
    field2.setName("testField2");
    TypeToken<Integer> fieldType2 = new TypeToken<Integer>() {};
    boolean serialize2 = true;
    boolean deserialize2 = false;
    ReflectiveTypeAdapterFactory.BoundField boundField2 = ReflectiveTypeAdapterFactory.createBoundField(
        gson, field2, "testName2", fieldType2, serialize2, deserialize2);

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = ReflectiveTypeAdapterFactory.getBoundFields(
        gson, new TypeToken<MyClass>() {}, rawType);

    assertEquals(2, boundFields.size());
    assertEquals(boundField1, boundFields.get("testName1"));
    assertEquals(boundField2, boundFields.get("testName2"));
  }

  @Test
  public void testAdapterMethod() throws IOException, NoSuchFieldException {
    Class<?> rawType = MyClass.class;
    Field field1 = new Field() {};
    field1.setName("testField1");
    TypeToken<String> fieldType1 = new TypeToken<String>() {};
    boolean serialize1 = true;
    boolean deserialize1 = true;
    ReflectiveTypeAdapterFactory.BoundField boundField1 = ReflectiveTypeAdapterFactory.createBoundField(
        gson, field1, "testName1", fieldType1, serialize1, deserialize1);

    Field field2 = new Field() {};
    field2.setName("testField2");
    TypeToken<Integer> fieldType2 = new TypeToken<Integer>() {};
    boolean serialize2 = true;
    boolean deserialize2 = false;
    ReflectiveTypeAdapterFactory.BoundField boundField2 = ReflectiveTypeAdapterFactory.createBoundField(
        gson, field2, "testName2", fieldType2, serialize2, deserialize2);

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    boundFields.put("testName1", boundField1);
    boundFields.put("testName2", boundField2);

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor,
        fieldNamingPolicy, excluder);
    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});

    TypeAdapter<MyClass> expectedAdapter = new ReflectiveTypeAdapterFactory.Adapter<MyClass>(
        constructorConstructor.get(new TypeToken<MyClass>() {}), boundFields) {};

    TypeAdapter<?> adapter1 = adapter.getRawType().getDeclaredMethod("get", new Class[] {String.class})
        .invoke(adapter, "testName1");
    assertEquals(expectedAdapter.getClass(), adapter1.getClass());

    TypeAdapter<?> adapter2 = adapter.getRawType().getDeclaredMethod("get", new Class[] {String.class})
        .invoke(adapter, "testName2");
    assertEquals(expectedAdapter.getClass(), adapter2.getClass());
  }

  private byte[] getJsonBytes(String name, String value) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
    writer.beginObject();
    writer.name(name);
    writer.value(value);
    writer.endObject();
    writer.flush();
    return outputStream.toByteArray();
  }

  @SuppressWarnings("unused")
  private static class MyClass {
    private String testName1;
    @JsonAdapter(MyStringAdapter.class)
    private String testName2;

    public String getTestName1() {
      return testName1;
    }

    public void setTestName1(String testName1) {
      this.testName1 = testName1;
    }

    public String getTestName2() {
      return testName2;
    }

    public void setTestName2(String testName2) {
      this.testName2 = testName2;
    }

    static class MyStringAdapter extends TypeAdapter<String> {
    }
  }

  @SuppressWarnings("unused")
  private static class Field {
    private int modifiers;
    private String name;

    public Field() {
      this.modifiers = Modifier.PRIVATE | Modifier.FINAL;
    }

    public boolean isSynthetic() {
      return (modifiers & Modifier.SYNTHETIC) != 0;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}