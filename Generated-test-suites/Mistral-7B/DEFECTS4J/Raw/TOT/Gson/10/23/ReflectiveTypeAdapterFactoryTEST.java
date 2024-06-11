import com.google.gson.*;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory_serializeAndDeserializeSimpleClass() {
    // Given
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy.Standard(), new Excluder());
    TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;
    SimpleClass simpleClass = new SimpleClass(1, "Hello");

    // When
    String json = gson.toJson(simpleClass, SimpleClass.class);
    SimpleClass deserializedSimpleClass = gson.fromJson(json, SimpleClass.class);

    // Then
    assertEquals(simpleClass, deserializedSimpleClass);
  }

  @Test
  public void testReflectiveTypeAdapterFactory_serializeAndDeserializeClassWithExcludedField() {
    // Given
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy.Standard(), new Excluder());
    TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;
    ExcludedClass excludedClass = new ExcludedClass(1, "Hello", "ExcludedField");

    // When
    String json = gson.toJson(excludedClass, ExcludedClass.class);
    ExcludedClass deserializedExcludedClass = gson.fromJson(json, ExcludedClass.class);

    // Then
    assertEquals(excludedClass, deserializedExcludedClass);
    assertNull(deserializedExcludedClass.excludedField);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReflectiveTypeAdapterFactory_deserializeInvalidJson() {
    // Given
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy.Standard(), new Excluder());
    TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;
    SimpleClass simpleClass = new SimpleClass(1, "Hello");

    // When
    String invalidJson = "{ \"invalid\": \"json\" }";
    SimpleClass deserializedSimpleClass = gson.fromJson(invalidJson, SimpleClass.class);

    // Then
    fail("Expected JsonSyntaxException but got: " + deserializedSimpleClass);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReflectiveTypeAdapterFactory_deserializeClassWithMissingField() {
    // Given
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy.Standard(), new Excluder());
    TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;
    SimpleClassMissingField simpleClassMissingField = new SimpleClassMissingField(1, "Hello");

    // When
    String json = gson.toJson(simpleClassMissingField);
    SimpleClass deserializedSimpleClass = gson.fromJson(json, SimpleClass.class);

    // Then
    fail("Expected JsonSyntaxException but got: " + deserializedSimpleClass);
  }

  @Test
  public void testReflectiveTypeAdapterFactory_serializeAndDeserializeClassWithJsonAdapter() {
    // Given
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy.Standard(), new Excluder());
    TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;
    JsonAdapter jsonAdapter = new JsonAdapterForTest();
    SimpleClassWithJsonAdapter simpleClassWithJsonAdapter = new SimpleClassWithJsonAdapter(1, "Hello");
    gson.registerTypeAdapterFactory(TypeAdapterFactory.newInstance(JsonAdapter.class, jsonAdapter));

    // When
    String json = gson.toJson(simpleClassWithJsonAdapter, SimpleClassWithJsonAdapter.class);
    SimpleClassWithJsonAdapter deserializedSimpleClassWithJsonAdapter = gson.fromJson(json, SimpleClassWithJsonAdapter.class);

    // Then
    assertEquals(simpleClassWithJsonAdapter, deserializedSimpleClassWithJsonAdapter);
  }
}

class SimpleClass {
  private int intField;
  private String stringField;

  public SimpleClass(int intField, String stringField) {
    this.intField = intField;
    this.stringField = stringField;
  }

  public int getIntField() {
    return intField;
  }

  public String getStringField() {
    return stringField;
  }
}

class SimpleClassMissingField {
  private int intField;
  private String stringField;

  public SimpleClassMissingField(int intField, String stringField) {
    this.intField = intField;
    this.stringField = stringField;
  }

  public int getIntField() {
    return intField;
  }

  @JsonAdapter(JsonAdapterForTest.class)
  public String getNonExistentField() {
    throw new UnsupportedOperationException();
  }
}

class JsonAdapterForTest extends TypeAdapter<String> {
  @Override public String read(JsonReader in) throws IOException {
    return in.nextString();
  }

  @Override public void write(JsonWriter out, String value) throws IOException {
    out.value(value);
  }
}

class ExcludedClass {
  private int intField;
  private String stringField;
  private String excludedField;

  public ExcludedClass(int intField, String stringField, String excludedField) {
    this.intField = intField;
    this.stringField = stringField;
    this.excludedField = excludedField;
  }

  public int getIntField() {
    return intField;
  }

  public String getStringField() {
    return stringField;
  }

  public String getExcludedField() {
    return excludedField;
  }
}