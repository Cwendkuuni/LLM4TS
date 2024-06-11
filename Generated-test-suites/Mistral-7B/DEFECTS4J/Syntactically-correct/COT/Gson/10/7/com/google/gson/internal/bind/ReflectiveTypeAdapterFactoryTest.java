import static org.junit.Assert.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  private static <T> Gson createGson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(new FieldNamingPolicy())
        .setExcluder(new Excluder())
        .setConstructorConstructor(new ConstructorConstructor())
        .create();
  }

  private static class TestClass {
    private int intField;
    private String stringField;
    private List<String> listField;

    @JsonAdapter(Adapter.class)
    public int getIntField() {
      return intField;
    }

    public void setIntField(int intField) {
      this.intField = intField;
    }

    @JsonAdapter(AdapterString.class)
    public String getStringField() {
      return stringField;
    }

    public void setStringField(String stringField) {
      this.stringField = stringField;
    }

    public List<String> getListField() {
      return listField;
    }

    public void setListField(List<String> listField) {
      this.listField = listField;
    }

    @SuppressWarnings("unused")
    private TestClass(int intField, String stringField, List<String> listField) {
      this.intField = intField;
      this.stringField = stringField;
      this.listField = listField;
    }
  }

  private static class Adapter implements JsonSerializer<TestClass>, JsonDeserializer<TestClass> {
    @Override
    public TestClass deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      JsonObject jsonObject = json.getAsJsonObject();
      TestClass testClass = new TestClass();
      for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        String name = entry.getKey();
        JsonElement value = entry.getValue();
        if ("intField".equals(name)) {
          testClass.setIntField(value.getAsInt());
        } else if ("stringField".equals(name)) {
          testClass.setTextField(value.getAsString());
        } else if ("listField".equals(name)) {
          testClass.setListField(context.deserialize(value, new TypeToken<List<String>>() {}.getType()));
        }
      }
      return testClass;
    }

    @Override
    public JsonElement serialize(TestClass src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject jsonObject = new JsonObject();
      Field[] fields = TestClass.class.getDeclaredFields();
      for (Field field : fields) {
        boolean serialized = ReflectiveTypeAdapterFactory.excludeField(field, true);
        if (serialized) {
          String name = ReflectiveTypeAdapterFactory.getFieldNames(field).get(0);
          Object value = null;
          try {
            value = field.get(src);
          } catch (IllegalAccessException e) {
            throw new AssertionError(e);
          }
          jsonObject.add(name, context.serialize(value));
        }
      }
      return jsonObject;
    }
  }

  private static class AdapterString implements JsonSerializer<String>, JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      return json.getAsString();
    }

    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src);
    }
  }

  @BeforeClass
  public static void setUpBeforeClass() {
    Gson gson = createGson();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory =
        new ReflectiveTypeAdapterFactory(gson.getConstructorConstructor(),
            gson.getFieldNamingPolicy(), gson.getExcluder());
  }

  @Test
  public void testCreate() {
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
    assertNull(reflectiveTypeAdapterFactory.create(null, new TypeToken<TestClass>() {}));

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory2 = new ReflectiveTypeAdapterFactory(
        null, new FieldNamingPolicy(), null);
    TypeAdapter<TestClass> testClassTypeAdapter = reflectiveTypeAdapterFactory2.create(createGson(),
        new TypeToken<TestClass>() {});
    assertNotNull(testClassTypeAdapter);
  }

  @Test
  public void testExcludeField() {
    TestClass testClass = new TestClass();
    Field intField = TestClass.class.getDeclaredField("intField");
    Field stringField = TestClass.class.getDeclaredField("stringField");
    Field listField = TestClass.class.getDeclaredField("listField");

    assertTrue(ReflectiveTypeAdapterFactory.excludeField(intField, true));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(intField, false));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(intField, true));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(stringField, true));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(stringField, false));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(stringField, true));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(listField, true));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(listField, false));
  }

  @Test
  public void testGetFieldNames() {
    TestClass testClass = new TestClass();
    Field intField = TestClass.class.getDeclaredField("intField");
    Field stringField = TestClass.class.getDeclaredField("stringField");
    Field listField = TestClass.class.getDeclaredField("listField");

    List<String> intFieldNames = ReflectiveTypeAdapterFactory.getFieldNames(intField);
    assertEquals(1, intFieldNames.size());
    assertEquals("intField", intFieldNames.get(0));

    List<String> stringFieldNames = ReflectiveTypeAdapterFactory.getFieldNames(stringField);
    assertEquals(1, stringFieldNames.size());
    assertEquals("stringField", stringFieldNames.get(0));

    List<String> listFieldNames = ReflectiveTypeAdapterFactory.getFieldNames(listField);
    assertEquals(1, listFieldNames.size());
    assertEquals("listField", listFieldNames.get(0));
  }

  @Test
  public void testCreateBoundField() {
    TestClass testClass = new TestClass();
    Field intField = TestClass.class.getDeclaredField("intField");
    Field stringField = TestClass.class.getDeclaredField("stringField");
    Field listField = TestClass.class.getDeclaredField("listField");

    BoundField intBoundField = new ReflectiveTypeAdapterFactory.BoundField("intField", true, false) {
      @Override public boolean writeField(Object value) throws IOException, IllegalAccessException {
        fail();
      }
    };
    BoundField stringBoundField = new ReflectiveTypeAdapterFactory.BoundField("stringField", true, false) {
      @Override void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
        fail();
      }
    };
    BoundField listBoundField = new ReflectiveTypeAdapterFactory.BoundField("listField", true, false) {
      @Override void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
        fail();
      }
    };

    assertEquals("intField", intBoundField.name);
    assertTrue(intBoundField.serialized);
    assertFalse(intBoundField.deserialized);

    assertEquals("stringField", stringBoundField.name);
    assertTrue(stringBoundField.serialized);
    assertFalse(stringBoundField.deserialized);

    assertEquals("listField", listBoundField.name);
    assertTrue(listBoundField.serialized);
    assertFalse(listBoundField.deserialized);
  }

  @Test
  public void testCreateAdapter() {
    TestClass testClass = new TestClass();
    Field intField = TestClass.class.getDeclaredField("intField");
    Field stringField = TestClass.class.getDeclaredField("stringField");
    Field listField = TestClass.class.getDeclaredField("listField");

    TypeAdapter<TestClass> testClassTypeAdapter = new ReflectiveTypeAdapterFactory(createGson(), null, null)
        .create(createGson(), new TypeToken<TestClass>() {});

    TestClass deserializedTestClass = testClassTypeAdapter.read(new JsonReader(
        new ByteArrayInputStream(new byte[0])));
    assertEquals(0, deserializedTestClass.getIntField());
    assertNull(deserializedTestClass.getStringField());
    assertNull(deserializedTestClass.getListField());

    JsonWriter jsonWriter = new JsonWriter(new ByteArrayOutputStream());
    testClassTypeAdapter.write(jsonWriter, testClass);
    jsonWriter.close();

    JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(jsonWriter.toString().getBytes()));
    TestClass serializedTestClass = testClassTypeAdapter.read(jsonReader);

    assertEquals(testClass.getIntField(), serializedTestClass.getIntField());
    assertEquals(testClass.getStringField(), serializedTestClass.getStringField());
    assertEquals(testClass.getListField(), serializedTestClass.getListField());
  }

  @Test
  public void testExcludeClass() {
    Excluder excluder = new Excluder() {
      @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
        return clazz.isInterface();
      }
    };

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), excluder);

    TypeAdapter<List<String>> stringListTypeAdapter = reflectiveTypeAdapterFactory.create(createGson(),
        new TypeToken<List<String>>() {});
    assertNotNull(stringListTypeAdapter);

    TypeAdapter<TestClass> testClassTypeAdapter = reflectiveTypeAdapterFactory.create(createGson(),
        new TypeToken<TestClass>() {});
    assertNull(testClassTypeAdapter);
  }

  @Test
  public void testExcludeField() {
    Excluder excluder = new Excluder() {
      @Override public boolean excludeField(Field field, boolean serialize) {
        return Modifier.isStatic(field.getModifiers());
      }
    };

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), excluder);

    TestClass testClass = new TestClass();
    Field intField = TestClass.class.getDeclaredField("intField");
    Field stringField = TestClass.class.getDeclaredField("stringField");
    Field listField = TestClass.class.getDeclaredField("listField");

    List<String> intFieldNames = ReflectiveTypeAdapterFactory.getFieldNames(intField);
    List<String> stringFieldNames = ReflectiveTypeAdapterFactory.getFieldNames(stringField);
    List<String> listFieldNames = ReflectiveTypeAdapterFactory.getFieldNames(listField);

    Map<String, BoundField> intBoundFields = reflectiveTypeAdapterFactory.getBoundFields(createGson(),
        new TypeToken<TestClass>() {}, TestClass.class);
    Map<String, BoundField> stringBoundFields = reflectiveTypeAdapterFactory.getBoundFields(createGson(),
        new TypeToken<TestClass>() {}, TestClass.class);
    Map<String, BoundField> listBoundFields = reflectiveTypeAdapterFactory.getBoundFields(createGson(),
        new TypeToken<TestClass>() {}, TestClass.class);

    assertFalse(intBoundFields.containsKey("intField"));
    assertFalse(stringBoundFields.containsKey("stringField"));
    assertFalse(listBoundFields.containsKey("listField"));
  }
}