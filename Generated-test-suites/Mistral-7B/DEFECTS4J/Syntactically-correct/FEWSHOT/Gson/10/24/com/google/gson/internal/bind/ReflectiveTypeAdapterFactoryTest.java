package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ReflectiveTypeAdapterFactoryTest {

  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingPolicy fieldNamingPolicy;
  private Excluder excluder;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    this.gson = new Gson();
    this.constructorConstructor = mock(ConstructorConstructor.class);
    this.fieldNamingPolicy = mock(FieldNamingPolicy.class);
    this.excluder = mock(Excluder.class);
  }

  @After
  public void tearDown() throws Exception {
    this.gson = null;
  }

  @Test
  public void testCreate_InterfaceType_ReturnsNull() {
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<Object> adapter = factory.create(this.gson, new TypeToken<Object>() {});
    assertNull(adapter);
  }

  @Test
  public void testCreate_ClassWithNoFields_ReturnsNull() {
    Class<?> testClass = Object.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<Object> adapter = factory.create(this.gson, new TypeToken<Object>() {{
      setType(testClass);
    }});
    assertNull(adapter);
  }

  @Test
  public void testCreate_ClassWithFields_ReturnsTypeAdapterInstance() {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    assertNotNull(adapter);
  }

  @Test
  public void testGetBoundFields_EmptyInterface_ReturnsEmptyMap() {
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(this.gson, new TypeToken<Object>() {}, Object.class);
    assertNotNull(boundFields);
    assertTrue(boundFields.isEmpty());
  }

  @Test
  public void testGetBoundFields_ClassWithOneField_ReturnsMapWithSingleEntry() {
    Class<?> testClass = TestClassWithOneField.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(this.gson, new TypeToken<TestClassWithOneField>() {{
      setType(testClass);
    }}, testClass);
    assertEquals(1, boundFields.size());
  }

  @Test
  public void testGetBoundFields_ClassWithMultipleFields_ReturnsMapWithMultipleEntries() {
    Class<?> testClass = TestClassWithMultipleFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(this.gson, new TypeToken<TestClassWithMultipleFields>() {{
      setType(testClass);
    }}, testClass);
    assertEquals(3, boundFields.size());
  }

  @Test
  public void testRead_NullValue_ReturnsNull() throws IOException {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    JsonReader reader = new JsonReader(new StringReader("null"));
    TestClassWithFields result = adapter.read(reader);
    assertNull(result);
  }

  @Test
  public void testRead_EmptyJsonObject_ReturnsEmptyInstance() throws IOException {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    JsonReader reader = new JsonReader(new StringReader("{}"));
    TestClassWithFields result = adapter.read(reader);
    assertNotNull(result);
    assertEquals(0, result.getIntValue1());
    assertEquals(0, result.getIntValue2());
    assertEquals(null, result.getStringValue());
  }

  @Test
  public void testRead_JsonObjectWithFields_PopulatesInstance() throws IOException {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    JsonReader reader = new JsonReader(new StringReader("{\"intValue1\":1,\"intValue2\":2,\"stringValue\":\"foo\"}"));
    TestClassWithFields result = adapter.read(reader);
    assertNotNull(result);
    assertEquals(1, result.getIntValue1());
    assertEquals(2, result.getIntValue2());
    assertEquals("foo", result.getStringValue());
  }

  @Test
  public void testWrite_NullValue_WritesNull() throws IOException {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    JsonWriter writer = new JsonWriter(new StringWriter());
    TestClassWithFields value = null;
    adapter.write(writer, value);
    assertEquals("null", writer.toString());
  }

  @Test
  public void testWrite_EmptyInstance_WritesEmptyJsonObject() throws IOException {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    JsonWriter writer = new JsonWriter(new StringWriter());
    TestClassWithFields value = new TestClassWithFields();
    adapter.write(writer, value);
    assertEquals("{}\n", writer.toString());
  }

  @Test
  public void testWrite_InstanceWithFields_WritesJsonObjectWithFields() throws IOException {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    JsonWriter writer = new JsonWriter(new StringWriter());
    TestClassWithFields value = new TestClassWithFields(1, 2, "foo");
    adapter.write(writer, value);
    assertEquals("{\"intValue1\":1,\"intValue2\":2,\"stringValue\":\"foo\"}\n", writer.toString());
  }

  @Test
  public void testWriteField_FieldValueIsSameAsInstanceValue_WritesField() throws IOException, IllegalAccessException {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    JsonWriter writer = new JsonWriter(new StringWriter());
    TestClassWithFields value = new TestClassWithFields(1, 2, "foo");
    when(this.fieldNamingPolicy.translateName(mock(Field.class))).thenReturn("name");
    ReflectiveTypeAdapterFactory.BoundField boundField = mock(ReflectiveTypeAdapterFactory.BoundField.class);
    when(boundField.writeField(value)).thenReturn(true);
    when(boundField.name).thenReturn("name");
    when(adapter.boundFields).thenReturn(Collections.singletonMap("name", boundField));
    adapter.write(writer, value);
    assertEquals("{\"name\":1}\n", writer.toString());
  }

  @Test
  public void testWriteField_FieldValueIsNotSameAsInstanceValue_DoesNotWriteField() throws IOException, IllegalAccessException {
    Class<?> testClass = TestClassWithFields.class;
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(this.constructorConstructor, this.fieldNamingPolicy, this.excluder);
    TypeAdapter<TestClassWithFields> adapter = factory.create(this.gson, new TypeToken<TestClassWithFields>() {{
      setType(testClass);
    }});
    JsonWriter writer = new JsonWriter(new StringWriter());
    TestClassWithFields value = new TestClassWithFields(1, 2, "foo");
    when(this.fieldNamingPolicy.translateName(mock(Field.class))).thenReturn("name");
    ReflectiveTypeAdapterFactory.BoundField boundField = mock(ReflectiveTypeAdapterFactory.BoundField.class);
    when(boundField.writeField(value)).thenReturn(false);
    when(boundField.name).thenReturn("name");
    when(adapter.boundFields).thenReturn(Collections.singletonMap("name", boundField));
    adapter.write(writer, value);
    assertEquals("{}\n", writer.toString());
  }

  static class TestClassWithFields {
    int intValue1;
    int intValue2;
    String stringValue;

    TestClassWithFields() {
    }

    TestClassWithFields(int intValue1, int intValue2, String stringValue) {
      this.intValue1 = intValue1;
      this.intValue2 = intValue2;
      this.stringValue = stringValue;
    }

    int getIntValue1() {
      return intValue1;
    }

    int getIntValue2() {
      return intValue2;
    }

    String getStringValue() {
      return stringValue;
    }
  }

  static class TestClassWithOneField {
    int intValue;

    TestClassWithOneField() {
    }

    TestClassWithOneField(int intValue) {
      this.intValue = intValue;
    }

    int getIntValue() {
      return intValue;
    }
  }

  static class TestClassWithMultipleFields {
    int intValue1;
    int intValue2;
    String stringValue;

    TestClassWithMultipleFields() {
    }

    TestClassWithMultipleFields(int intValue1, int intValue2, String stringValue) {
      this.intValue1 = intValue1;
      this.intValue2 = intValue2;
      this.stringValue = stringValue;
    }

    int getIntValue1() {
      return intValue1;
    }

    int getIntValue2() {
      return intValue2;
    }

    String getStringValue() {
      return stringValue;
    }
  }

}

class StringReader {
  String s;

  StringReader(String s) {
    this.s = s;
  }

  @Override
  public int read() throws IOException {
    int result = 0;
    if (s.length() > 0) {
      result = s.charAt(0);
      s = s.substring(1);
    }
    return result;
  }
}

class StringWriter {
  StringBuilder b;

  StringWriter() {
    this.b = new StringBuilder();
  }

  @Override
  public void write(int c) throws IOException {
    this.b.append((char) c);
  }

  @Override
  public String toString() {
    return this.b.toString();
  }
}