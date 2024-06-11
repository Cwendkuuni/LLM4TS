package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldAdapter;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for ReflectiveTypeAdapterFactory
 */
public class ReflectiveTypeAdapterFactoryTest {

  private static final Gson gson = new GsonBuilder().create();

  @Test
  public void testCreate() {
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(gson), FieldNamingPolicy.IDENTITY, Excluder.INSTANCE);
    assertNotNull(factory);
  }

  @Test
  public void testExcludeField() {
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(null, false));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(null, true));

    class TestClass {
      private int excludedField;
    }

    assertFalse(ReflectiveTypeAdapterFactory.excludeField(TestClass.class.getDeclaredField("excludedField"), false));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(TestClass.class.getDeclaredField("excludedField"), true));

    assertFalse(ReflectiveTypeAdapterFactory.excludeField(TestClass.class.getDeclaredField("excludedField"), false, Excluder.INSTANCE));
    assertTrue(ReflectiveTypeAdapterFactory.excludeField(TestClass.class.getDeclaredField("excludedField"), true, Excluder.INSTANCE));
  }

  @Test
  public void testGetBoundFields() throws Exception {
    class TestClass {
      @SerializedName("name")
      private String name;
      private int excludedField;
      private List<String> list;

      public TestClass() {
        list = new ArrayList<>();
      }
    }

    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(gson), FieldNamingPolicy.IDENTITY, Excluder.INSTANCE);

    TypeToken<TestClass> type = new TypeToken<TestClass>() {};
    Map<String, FieldAdapter.BoundField> boundFields = factory.create(gson, type.where(new TypeToken<>() {}).getType()).getBoundFields();

    assertNotNull(boundFields);
    assertEquals(3, boundFields.size());

    FieldAdapter.BoundField nameField = boundFields.get("name");
    assertEquals("name", nameField.name);
    assertFalse(nameField.serialized);
    assertFalse(nameField.deserialized);

    FieldAdapter.BoundField excludedField = boundFields.get("excludedField");
    assertNull(excludedField);

    FieldAdapter.BoundField listField = boundFields.get("list");
    assertEquals("list", listField.name);
    assertTrue(listField.serialized);
    assertTrue(listField.deserialized);
  }

  @Test
  public void testWriteRead() throws Exception {
    class TestClass {
      private int id;
      private String name;
      private List<String> list;

      public TestClass() {
        list = new ArrayList<>();
      }
    }

    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(gson), FieldNamingPolicy.IDENTITY, Excluder.INSTANCE);

    TypeToken<TestClass> type = new TypeToken<TestClass>() {};
    Map<String, FieldAdapter.BoundField> boundFields = factory.create(gson, type.where(new TypeToken<>() {}).getType()).getBoundFields();

    TestClass obj = new TestClass();
    obj.id = 1;
    obj.name = "Test";
    obj.list.add("One");
    obj.list.add("Two");

    String json = gson.toJson(obj);

    JsonElement parsedJson = JsonParser.parseString(json);
    JsonReader reader = new JsonReader(new StringReader(json));
    reader.setLenient(true);

    TestClass parsedObj = new TypeAdapter<TestClass>(factory, type).read(reader);

    assertEquals(obj.id, parsedObj.id);
    assertEquals(obj.name, parsedObj.name);
    assertEquals(obj.list.size(), parsedObj.list.size());
    assertEquals(obj.list.get(0), parsedObj.list.get(0));
    assertEquals(obj.list.get(1), parsedObj.list.get(1));
  }

  @Test(expected = JsonIOException.class)
  public void testDeserializeNullValue() throws Exception {
    class TestClass {
      private String name;

      public TestClass() {}
    }

    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(gson), FieldNamingPolicy.IDENTITY, Excluder.INSTANCE);

    TypeToken<TestClass> type = new TypeToken<TestClass>() {};
    Map<String, FieldAdapter.BoundField> boundFields = factory.create(gson, type.where(new TypeToken<>() {}).getType()).getBoundFields();

    TestClass obj = new TestClass();

    JsonReader reader = new JsonReader(new StringReader("{\"name\": null}"));
    reader.setLenient(true);

    FieldAdapter.BoundField nameField = boundFields.get("name");
    assertNotNull(nameField);

    nameField.read(reader, obj);

    assertNull(obj.name);

    reader.nextNull();
    reader.skipValue();

    throw new AssertionError();
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeUnknownField() throws Exception {
    class TestClass {
      private String name;

      public TestClass() {}
    }

    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(gson), FieldNamingPolicy.IDENTITY, Excluder.INSTANCE);

    TypeToken<TestClass> type = new TypeToken<TestClass>() {};
    Map<String, FieldAdapter.BoundField> boundFields = factory.create(gson, type.where(new TypeToken<>() {}).getType()).getBoundFields();

    TestClass obj = new TestClass();

    JsonReader reader = new JsonReader(new StringReader("{\"unknown\": \"value\"}"));
    reader.setLenient(true);

    FieldAdapter.BoundField nameField = boundFields.get("name");
    assertNotNull(nameField);

    nameField.read(reader, obj);

    throw new AssertionError();
  }
}