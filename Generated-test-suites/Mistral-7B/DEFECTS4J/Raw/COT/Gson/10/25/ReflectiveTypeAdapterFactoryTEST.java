import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ReflectiveTypeAdapterFactoryTest {
  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Given
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
    Excluder excluder = mock(Excluder.class);

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory =
        new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    Gson gson = new GsonBuilder().setExcluder(excluder).create();

    // Test create method
    TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;
    TypeAdapter<Person> personTypeAdapter = typeAdapterFactory.create(gson, new TypeToken<Person>() {});
    assertNotNull(personTypeAdapter);

    // Test excludeField method
    Class<Person> personClass = Person.class;
    Field nameField = null;
    try {
      nameField = personClass.getDeclaredField("name");
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    boolean excludeNameFieldSerialize = reflectiveTypeAdapterFactory.excludeField(nameField, true);
    boolean excludeNameFieldDeserialize = reflectiveTypeAdapterFactory.excludeField(nameField, false);

    assertFalse("name field should be serialized", excludeNameFieldSerialize);
    assertFalse("name field should be deserialized", excludeNameFieldDeserialize);

    nameField.setAccessible(true);
    nameField.set(new Person(), "John Doe");
    String name = nameField.get(new Person()).toString();
    assertEquals("John Doe", name);

    // Test getFieldNames method
    Field ageField = null;
    try {
      ageField = personClass.getDeclaredField("age");
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(ageField);
    assertEquals(1, fieldNames.size());
    assertEquals("age", fieldNames.get(0));

    // Test createBoundField method
    Type boundFieldType = new TypeToken<Integer>() {}.getType();
    ReflectiveTypeAdapterFactory.BoundField boundField =
        reflectiveTypeAdapterFactory.createBoundField(gson, ageField, "age", new TypeToken<Integer>() {}.getType(), true, false);

    // Test write method
    JsonWriter jsonWriter = mock(JsonWriter.class);
    Object person = new Person();
    int age = 25;
    ageField.setAccessible(true);
    ageField.set(person, age);

    try {
      boundField.write(jsonWriter, person);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String expectedJson = "{\"age\":25}";
    String actualJson = jsonWriter.toString();
    assertEquals(expectedJson, actualJson);

    // Test read method
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.nextInt()).thenReturn(25);

    try {
      Object personFromJson = boundField.read(jsonReader, null);
      assertNotNull(personFromJson);
      Person personFromJsonCast = (Person) personFromJson;
      assertEquals(age, personFromJsonCast.getAge());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Test writeField method (edge case: null value)
    jsonWriter = mock(JsonWriter.class);
    person = new Person();
    int nullAge = 0;

    try {
      boundField.writeField(person);
    } catch (IOException e) {
      // JSON writer should not be called when writing null values
      verify(jsonWriter, never()).value(null);
    }

    // Test read method (edge case: null value)
    when(jsonReader.nextNull()).thenReturn(true);
    person = new Person();

    try {
      boundField.read(jsonReader, person);
    } catch (IOException e) {
      fail("JSON reader should not throw an IOException when reading null values");
    }

    assertNull(person.getName());
    assertEquals(0, person.getAge());

    // Test read method (exception handling: JsonSyntaxException)
    jsonReader = mock(JsonReader.class);
    jsonReader.expectThrows(JsonSyntaxException.class);

    try {
      boundField.read(jsonReader, person);
    } catch (JsonSyntaxException e) {
      // JSON reader should throw a JsonSyntaxException when encountering invalid JSON
    }
  }

  private static class Person {
    private String name;
    private int age;

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
  }

  private static class FieldNamingPolicy extends com.google.gson.FieldNamingPolicy {
    @Override protected String translateName(Field f) {
      return f.getName();
    }
  }
}