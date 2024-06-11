import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingStrategy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

  private static final TypeAdapterFactory FACTORY = new ReflectiveTypeAdapterFactory(
      new ConstructorConstructor(),
      new FieldNamingStrategy() {
        @Override public String translateName(Field field) {
          return field.getName();
        }
      },
      new Excluder() {
        @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
          return false;
        }

        @Override public boolean excludeField(Field field, boolean serialize) {
          return Modifier.isStatic(field.getModifiers());
        }
      });

  private static final Gson GSON = new GsonBuilder()
      .setFieldNamingStrategy(new FieldNamingStrategy() {
        @Override public String translateName(Field field) {
          return field.getName();
        }
      })
      .setExcluder(new Excluder() {
        @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
          return false;
        }

        @Override public boolean excludeField(Field field, boolean serialize) {
          return Modifier.isStatic(field.getModifiers()) || field.isSynthetic();
        }
      })
      .create();

  private static final class TestClass {
    @SerializedName("name")
    private String name;
    private int integer;
    private boolean booleanValue;
    private TestClass nested;
    private List<String> list;
    private Map<String, String> map;

    public TestClass() {}

    public TestClass(String name, int integer, boolean booleanValue, TestClass nested,
                     List<String> list, Map<String, String> map) {
      this.name = name;
      this.integer = integer;
      this.booleanValue = booleanValue;
      this.nested = nested;
      this.list = list;
      this.map = map;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getInteger() {
      return integer;
    }

    public void setInteger(int integer) {
      this.integer = integer;
    }

    public boolean isBooleanValue() {
      return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
      this.booleanValue = booleanValue;
    }

    public TestClass getNested() {
      return nested;
    }

    public void setNested(TestClass nested) {
      this.nested = nested;
    }

    public List<String> getList() {
      return list;
    }

    public void setList(List<String> list) {
      this.list = list;
    }

    public Map<String, String> getMap() {
      return map;
    }

    public void setMap(Map<String, String> map) {
      this.map = map;
    }
  }

  private TestClass testInstance;
  private Map<String, Object> testMap;

  @Before
  public void setUp() {
    testInstance = new TestClass("TestInstance", 42, true, null, null, null);
    testMap = new HashMap<>();
    testMap.put("key1", "value1");
    testMap.put("key2", "value2");
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    String json = "{\"name\":\"TestInstance\",\"integer\":42,\"booleanValue\":true,\"nested\":null,\"list\":[],\"map\":{\"key1\":\"value1\",\"key2\":\"value2\"}}";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
    GSON.toJson(testInstance, writer);
    writer.flush();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

    TypeAdapter<TestClass> testClassAdapter = FACTORY.create(GSON, TypeToken.getType(TestClass.class));
    TestClass deserializedInstance = testClassAdapter.read(reader);

    assertEquals(testInstance.getName(), deserializedInstance.getName());
    assertEquals(testInstance.getInteger(), deserializedInstance.getInteger());
    assertEquals(testInstance.isBooleanValue(), deserializedInstance.isBooleanValue());
    assertNull(deserializedInstance.getNested());
    assertEquals(testInstance.getList().size(), deserializedInstance.getList().size());
    assertEquals(testInstance.getMap().size(), deserializedInstance.getMap().size());

    for (Map.Entry<String, String> entry : testInstance.getMap().entrySet()) {
      assertEquals(entry.getValue(), deserializedInstance.getMap().get(entry.getKey()));
    }
  }

  @Test
  public void testSerializationAndDeserializationWithNullValues() throws IOException {
    String json = "{\"name\":null,\"integer\":null,\"booleanValue\":null,\"nested\":null,\"list\":null,\"map\":{}}";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
    GSON.toJson(testInstance, writer);
    writer.flush();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

    TypeAdapter<TestClass> testClassAdapter = FACTORY.create(GSON, TypeToken.getType(TestClass.class));
    TestClass deserializedInstance = testClassAdapter.read(reader);

    assertNull(deserializedInstance.getName());
    assertNull(deserializedInstance.getInteger());
    assertNull(deserializedInstance.isBooleanValue());
    assertNull(deserializedInstance.getNested());
    assertNull(deserializedInstance.getList());
    assertEquals(0, deserializedInstance.getMap().size());
  }

  @Test
  public void testSerializationAndDeserializationWithEmptyList() throws IOException {
    String json = "{\"name\":\"TestInstance\",\"integer\":42,\"booleanValue\":true,\"nested\":null,\"list\":[],\"map\":{}}";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
    GSON.toJson(testInstance, writer);
    writer.flush();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

    TypeAdapter<TestClass> testClassAdapter = FACTORY.create(GSON, TypeToken.getType(TestClass.class));
    TestClass deserializedInstance = testClassAdapter.read(reader);

    assertEquals(testInstance.getName(), deserializedInstance.getName());
    assertEquals(testInstance.getInteger(), deserializedInstance.getInteger());
    assertEquals(testInstance.isBooleanValue(), deserializedInstance.isBooleanValue());
    assertNull(deserializedInstance.getNested());
    assertEquals(testInstance.getList().size(), deserializedInstance.getList().size());
    assertEquals(0, deserializedInstance.getList().size());
  }

  @Test
  public void testSerializationAndDeserializationWithEmptyMap() throws IOException {
    String json = "{\"name\":\"TestInstance\",\"integer\":42,\"booleanValue\":true,\"nested\":null,\"list\":[],\"map\":{}}";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
    GSON.toJson(testInstance, writer);
    writer.flush();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

    TypeAdapter<TestClass> testClassAdapter = FACTORY.create(GSON, TypeToken.getType(TestClass.class));
    TestClass deserializedInstance = testClassAdapter.read(reader);

    assertEquals(testInstance.getName(), deserializedInstance.getName());
    assertEquals(testInstance.getInteger(), deserializedInstance.getInteger());
    assertEquals(testInstance.isBooleanValue(), deserializedInstance.isBooleanValue());
    assertNull(deserializedInstance.getNested());
    assertEquals(testInstance.getList().size(), deserializedInstance.getList().size());
    assertEquals(0, deserializedInstance.getMap().size());
  }

  @Test
  public void testSerializationAndDeserializationWithNestedClass() throws IOException {
    TestClass nestedTestInstance = new TestClass("NestedTestInstance", 1, false, null, null, null);
    testInstance.setNested(nestedTestInstance);

    String json = "{\"name\":\"TestInstance\",\"integer\":42,\"booleanValue\":true,\"nested\":{" +
        "\"name\":\"NestedTestInstance\",\"integer\":1,\"booleanValue\":false,\"nested\":null,\"list\":[],\"map\":{}}," +
        "\"list\":[],\"map\":{" +
        "\"key1\":\"value1\",\"key2\":\"value2\"}}";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
    GSON.toJson(testInstance, writer);
    writer.flush();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

    TypeAdapter<TestClass> testClassAdapter = FACTORY.create(GSON, TypeToken.getType(TestClass.class));
    TestClass deserializedInstance = testClassAdapter.read(reader);

    assertEquals(testInstance.getName(), deserializedInstance.getName());
    assertEquals(testInstance.getInteger(), deserializedInstance.getInteger());
    assertEquals(testInstance.isBooleanValue(), deserializedInstance.isBooleanValue());
    assertEquals(nestedTestInstance.getName(), deserializedInstance.getNested().getName());
    assertEquals(nestedTestInstance.getInteger(), deserializedInstance.getNested().getInteger());
    assertEquals(nestedTestInstance.isBooleanValue(), deserializedInstance.getNested().isBooleanValue());
    assertEquals(testInstance.getList().size(), deserializedInstance.getList().size());
    assertEquals(testInstance.getMap().size(), deserializedInstance.getMap().size());

    for (Map.Entry<String, String> entry : testInstance.getMap().entrySet()) {
      assertEquals(entry.getValue(), deserializedInstance.getMap().get(entry.getKey()));
    }
  }
}