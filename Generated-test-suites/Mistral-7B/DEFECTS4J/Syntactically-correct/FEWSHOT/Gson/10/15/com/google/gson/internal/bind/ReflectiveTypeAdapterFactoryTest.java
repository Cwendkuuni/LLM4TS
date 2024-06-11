package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ReflectiveTypeAdapterFactoryTest {

  private static class TestReflectionTypeAdapterFactory
      implements ReflectiveTypeAdapterFactory {
    @Override
    public ConstructorConstructor constructorConstructor = new ConstructorConstructor() {};

    @Override
    public FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy() {};

    @Override
    public Excluder excluder = new Excluder() {};
  }

  @Test
  public void testCreate() {
    Gson gson = new Gson();
    TypeAdapterFactory factory = new TestReflectionTypeAdapterFactory();

    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
    assertNotNull(adapter);
  }

  @Test
  public void testGetBoundFields_serialization() throws Exception {
    Gson gson = new Gson();
    TypeAdapterFactory factory = new TestReflectionTypeAdapterFactory();

    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
    MyClass obj = adapter.read(new JsonReader(new JsonReaderTestInput(
        "{" +
        "  \"name\": \"John\"," +
        "  \"age\": 30," +
        "  \"address\": {" +
        "    \"street\": \"Main St\"," +
        "    \"city\": \"Anytown\"" +
        "  }," +
        "  \"list\": [1, 2, 3]," +
        "  \"map\": {" +
        "    \"key1\": \"value1\"," +
        "    \"key2\": \"value2\"" +
        "  }" +
        "}")).setLenient(true));

    assertEquals("John", obj.name);
    assertEquals(30, obj.age);
    assertEquals("Main St", obj.address.street);
    assertEquals("Anytown", obj.address.city);
    assertEquals(3, obj.list.size());
    assertEquals(2, obj.map.size());
    assertEquals("value1", obj.map.get("key1"));
    assertEquals("value2", obj.map.get("key2"));
  }

  @Test
  public void testGetBoundFields_deserialization() throws Exception {
    Gson gson = new Gson();
    TypeAdapterFactory factory = new TestReflectionTypeAdapterFactory();

    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});

    JsonElement jsonElement = new JsonObject()
        .add("name", "John")
        .add("age", 30)
        .add("address", new JsonObject()
            .add("street", "Main St")
            .add("city", "Anytown")
        )
        .add("list", new JsonArray()
            .add(1)
            .add(2)
            .add(3)
        )
        .add("map", new JsonObject()
            .addProperty("key1", "value1")
            .addProperty("key2", "value2")
        );

    MyClass obj = adapter.read(new JsonReader(new JsonReaderTestInput(jsonElement.getAsJsonString()))).readValueAsT(MyClass.class);

    assertEquals("John", obj.name);
    assertEquals(30, obj.age);
    assertEquals("Main St", obj.address.street);
    assertEquals("Anytown", obj.address.city);
    assertEquals(3, obj.list.size());
    assertEquals(2, obj.map.size());
    assertEquals("value1", obj.map.get("key1"));
    assertEquals("value2", obj.map.get("key2"));
  }

  @Test
  public void testExcludeField() {
    Field field = MyClass.class.getDeclaredField("excludedField");
    TypeAdapterFactory factory = new TestReflectionTypeAdapterFactory();

    assertFalse(factory.excludeField(field, true));
    assertFalse(factory.excludeField(field, false));

    field.setAccessible(true);
    field.set(new MyClass(), "excluded value");

    TypeAdapter<MyClass> adapter = factory.create(new Gson(), new TypeToken<MyClass>() {});
    MyClass obj = adapter.read(new JsonReader(new JsonReaderTestInput("{\"name\": \"John\", \"list\": [1, 2, 3]}"))).readValueAsT(MyClass.class);

    assertNull(obj.excludedField);
  }

  @Test
  public void testGetFieldNames() {
    Field field = MyClass.class.getDeclaredField("name");
    TypeAdapterFactory factory = new TestReflectionTypeAdapterFactory();

    List<String> fieldNames = factory.getFieldNames(field);

    assertEquals("name", fieldNames.get(0));
  }

  private static class MyClass {
    @SerializedName("name")
    private String name;

    private int age;

    private Address address = new Address();

    private List<Integer> list = new ArrayList<>();

    private Map<String, String> map = new LinkedHashMap<>();

    private String excludedField;

    public static class Address {
      @SerializedName("street")
      private String street;

      private String city;

      public String getStreet() {
        return street;
      }

      public void setStreet(String street) {
        this.street = street;
      }

      public String getCity() {
        return city;
      }

      public void setCity(String city) {
        this.city = city;
      }
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public Address getAddress() {
      return address;
    }

    public void setAddress(Address address) {
      this.address = address;
    }

    public List<Integer> getList() {
      return list;
    }

    public void setList(List<Integer> list) {
      this.list = list;
    }

    public Map<String, String> getMap() {
      return map;
    }

    public void setMap(Map<String, String> map) {
      this.map = map;
    }

    public String getExcludedField() {
      return excludedField;
    }

    public void setExcludedField(String excludedField) {
      this.excludedField = excludedField;
    }

    @Override
    public String toString() {
      return "MyClass{" +
          "name='" + name + '\'' +
          ", age=" + age +
          ", address=" + address +
          ", list=" + list +
          ", map=" + map +
          '}';
    }
  }

  private static class FieldNamingPolicy extends FieldNamingStrategy {
    @Override public String translateName(Field field) {
      return field.getName();
    }
  }

  private static class Excluder {
    public boolean excludeClass(Class<?> clazz, boolean serialize) {
      return false;
    }

    public boolean excludeField(Field field, boolean serialize) {
      return Modifier.isStatic(field.getModifiers());
    }
  }

  private static class JsonReaderTestInput implements JsonReader.Factory {

    private final String jsonString;

    public JsonReaderTestInput(String jsonString) {
      this.jsonString = jsonString;
    }

    @Override public JsonReader create(InputStream inputStream) throws IOException {
      return new JsonReader(new StringReader(jsonString));
    }

    @Override public JsonReader create(Reader reader) throws IOException {
      return new JsonReader(reader);
    }
  }
}