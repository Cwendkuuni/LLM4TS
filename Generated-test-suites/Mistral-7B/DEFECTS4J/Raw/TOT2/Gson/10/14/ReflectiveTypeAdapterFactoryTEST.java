import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() throws IOException, IllegalAccessException {
    // Given
    Class<?> clazz = MyTestClass.class;
    Gson gson = new GsonBuilder()
        .setExclusionStrategy(new Excluder())
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new IdentityFieldNamingPolicy(),
        new IdentityExcluder());

    // When
    TypeAdapter<MyTestClass> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyTestClass>() {});

    // Then
    MyTestClass original = new MyTestClass("Test", 1, 2.0f, 3);
    String json = adapter.toJson(original);
    System.out.println(json);

    MyTestClass deserialized = adapter.fromJson(json, MyTestClass.class);
    assertEquals(original, deserialized);

    // Edge cases

    // Test null object
    MyTestClass nullObject = null;
    String nullJson = adapter.toJson(nullObject);
    assertNotNull(nullJson);
    MyTestClass deserializedNull = adapter.fromJson(nullJson, MyTestClass.class);
    assertNull(deserializedNull);

    // Test empty object
    MyTestClass emptyObject = new MyTestClass();
    String emptyJson = adapter.toJson(emptyObject);
    assertNotNull(emptyJson);
    MyTestClass deserializedEmpty = adapter.fromJson(emptyJson, MyTestClass.class);
    assertEquals(emptyObject, deserializedEmpty);

    // Test empty List
    MyTestClass listObject = new MyTestClass(new ArrayList<>());
    String listJson = adapter.toJson(listObject);
    MyTestClass deserializedList = adapter.fromJson(listJson, MyTestClass.class);
    assertEquals(listObject, deserializedList);

    // Test Map with null values
    Map<String, String> map = new HashMap<>();
    map.put("key1", null);
    MyTestClass mapObject = new MyTestClass(map);
    String mapJson = adapter.toJson(mapObject);
    MyTestClass deserializedMap = adapter.fromJson(mapJson, MyTestClass.class);
    assertEquals(mapObject, deserializedMap);

    // Test Map with empty values
    Map<String, String> emptyMap = new HashMap<>();
    MyTestClass emptyMapObject = new MyTestClass(emptyMap);
    String emptyMapJson = adapter.toJson(emptyMapObject);
    MyTestClass deserializedEmptyMap = adapter.fromJson(emptyMapJson, MyTestClass.class);
    assertEquals(emptyMapObject, deserializedEmptyMap);
  }

  // Test exception scenarios

  // Test JsonSyntaxException when deserializing an invalid JSON
  @Test(expected = JsonSyntaxException.class)
  public void testJsonSyntaxException() throws IOException, IllegalAccessException {
    String invalidJson = "{\"name\": \"Test\", \"age\": [1, 2]";
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new IdentityFieldNamingPolicy(),
        new IdentityExcluder());
    TypeAdapter<MyTestClass> adapter = reflectiveTypeAdapterFactory.create(new Gson(), new TypeToken<MyTestClass>() {});
    adapter.fromJson(invalidJson, MyTestClass.class);
  }

  // Test IllegalAccessException when accessing private fields
  @Test(expected = IllegalAccessException.class)
  public void testIllegalAccessException() throws IOException {
    MyTestClass object = new MyTestClass("Test", 1, 2.0f, 3);
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new IdentityFieldNamingPolicy(),
        new IdentityExcluder());
    TypeAdapter<MyTestClass> adapter = reflectiveTypeAdapterFactory.create(new Gson(), new TypeToken<MyTestClass>() {});
    Field privateField = MyTestClass.class.getDeclaredField("_privateField");
    privateField.setAccessible(true);
    privateField.set(object, "Test");
    String json = adapter.toJson(object);
    MyTestClass deserialized = adapter.fromJson(json, MyTestClass.class);
    assertEquals("Test", privateField.get(deserialized));
  }

  // Test IOException when reading from null JsonReader
  @Test(expected = IOException.class)
  public void testIOException() throws IOException, IllegalAccessException {
    String json = null;
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new IdentityFieldNamingPolicy(),
        new IdentityExcluder());
    TypeAdapter<MyTestClass> adapter = reflectiveTypeAdapterFactory.create(new Gson(), new TypeToken<MyTestClass>() {});
    adapter.fromJson(json, MyTestClass.class);
  }
}

class MyTestClass {
  private String name;
  private int age;
  private float price;
  private List<Integer> numbers;
  private Map<String, String> map;
  private String _privateField;

  public MyTestClass() {
  }

  public MyTestClass(String name, int age, float price, List<Integer> numbers, Map<String, String> map) {
    this.name = name;
    this.age = age;
    this.price = price;
    this.numbers = numbers;
    this.map = map;
  }

  public MyTestClass(Map<String, String> map) {
    this.map = map;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public float getPrice() {
    return price;
  }

  public List<Integer> getNumbers() {
    return numbers;
  }

  public Map<String, String> getMap() {
    return map;
  }

  public String _getPrivateField() {
    return _privateField;
  }
}

class IdentityFieldNamingPolicy implements FieldNamingPolicy {
  @Override public String translateName(Field field) {
    return field.getName();
  }
}

class IdentityExcluder implements ExclusionStrategy {
  @Override public boolean shouldSkipClass(Class<?> clazz) {
    return false;
  }

  @Override public boolean shouldSkipField(Field field) {
    return false;
  }
}