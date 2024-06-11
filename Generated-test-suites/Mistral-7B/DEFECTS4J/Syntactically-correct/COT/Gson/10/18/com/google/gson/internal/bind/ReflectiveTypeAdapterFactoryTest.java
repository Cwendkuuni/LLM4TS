package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField.AdapterRuntimeTypeWrapper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  private static <T> void testSerializationDeserialization(Class<T> clazz) throws Exception {
    Gson gson = new GsonBuilder().create();

    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

    TypeAdapter<T> adapter = (TypeAdapter<T>) factory.create(gson, new TypeToken<T>() {});

    T original = adapter.fromJson(new JsonReader(new ByteArrayInputStream(new byte[0]))).getValue();

    T copy = adapter.fromJson(new JsonReader(new ByteArrayInputStream(toJson(original).getBytes()))).getValue();

    assertEquals(original, copy);
  }

  private static String toJson(Object obj) throws IOException {
    Gson gson = new GsonBuilder().create();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(outputStream);

    adapter.writeValue(writer, obj);
    writer.flush();

    return outputStream.toString();
  }

  @Test
  public void testBasicFunctionality() throws Exception {
    testSerializationDeserialization(DummyClass.class);
  }

  @Test
  public void testJsonAdapterPresent() throws Exception {
    testSerializationDeserialization(DummyJsonAdapterClass.class);
  }

  @Test
  public void testJsonAdapterAbsent() throws Exception {
    testSerializationDeserialization(DummyClassWithoutJsonAdapter.class);
  }

  @Test
  public void testWriteField() throws Exception {
    DummyClassWithWriteField dummyClass = new DummyClassWithWriteField();
    DummyClassWithWriteField copy = new DummyClassWithWriteField();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

    TypeAdapter<DummyClassWithWriteField> adapter = (TypeAdapter<DummyClassWithWriteField>) factory.create(
        new GsonBuilder().create(), new TypeToken<DummyClassWithWriteField>() {});

    adapter.writeValue(new JsonWriter(new ByteArrayOutputStream()), dummyClass);
    adapter.writeValue(new JsonWriter(new ByteArrayOutputStream()), copy);

    copy.setFieldToWrite(dummyClass.getFieldToWrite());

    assertEquals(dummyClass.getFieldToWrite(), copy.getFieldToWrite());
  }

  @Test
  public void testNullRead() throws Exception {
    DummyClassWithNullField dummyClass = new DummyClassWithNullField();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

    TypeAdapter<DummyClassWithNullField> adapter = (TypeAdapter<DummyClassWithNullField>) factory.create(
        new GsonBuilder().create(), new TypeToken<DummyClassWithNullField>() {});

    DummyClassWithNullField result = adapter.fromJson(new JsonReader(new ByteArrayInputStream(
        new byte[]{"{\"field\":null}".getBytes()}))).getValue();

    assertNull(result.getField());
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingName() throws Exception {
    DummyClassWithMissingName dummyClass = new DummyClassWithMissingName();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

    TypeAdapter<DummyClassWithMissingName> adapter = (TypeAdapter<DummyClassWithMissingName>) factory.create(
        new GsonBuilder().create(), new TypeToken<DummyClassWithMissingName>() {});

    adapter.fromJson(new JsonReader(new ByteArrayInputStream(toJson(dummyClass).getBytes()))).getValue();
  }

  @Test(expected = IllegalStateException.class)
  public void testMultipleFieldsWithSameName() throws Exception {
    DummyClassWithMultipleFields dummyClass = new DummyClassWithMultipleFields();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

    TypeAdapter<DummyClassWithMultipleFields> adapter = (TypeAdapter<DummyClassWithMultipleFields>) factory.create(
        new GsonBuilder().create(), new TypeToken<DummyClassWithMultipleFields>() {});

    adapter.fromJson(new JsonReader(new ByteArrayInputStream(toJson(dummyClass).getBytes()))).getValue();
  }

  @Test
  public void testPrimitiveWriteField() throws Exception {
    DummyClassWithPrimitiveWriteField dummyClass = new DummyClassWithPrimitiveWriteField();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

    TypeAdapter<DummyClassWithPrimitiveWriteField> adapter = (TypeAdapter<DummyClassWithPrimitiveWriteField>) factory.create(
        new GsonBuilder().create(), new TypeToken<DummyClassWithPrimitiveWriteField>() {});

    adapter.writeValue(new JsonWriter(new ByteArrayOutputStream()), dummyClass);

    DummyClassWithPrimitiveWriteField copy = new DummyClassWithPrimitiveWriteField();

    adapter.writeValue(new JsonWriter(new ByteArrayOutputStream()), copy);

    copy.setIntToWrite(dummyClass.getIntToWrite());

    assertEquals(dummyClass.getIntToWrite(), copy.getIntToWrite());
  }

  @Test(expected = AssertionError.class)
  public void testIllegalAccessException() throws Exception {
    DummyClassWithIllegalAccessExceptionField dummyClass = new DummyClassWithIllegalAccessExceptionField();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

    TypeAdapter<DummyClassWithIllegalAccessExceptionField> adapter = (TypeAdapter<DummyClassWithIllegalAccessExceptionField>) factory.create(
        new GsonBuilder().create(), new TypeToken<DummyClassWithIllegalAccessExceptionField>() {});

    adapter.fromJson(new JsonReader(new ByteArrayInputStream(toJson(dummyClass).getBytes()))).getValue();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentException() throws Exception {
    DummyClassWithIllegalArgumentExceptionField dummyClass = new DummyClassWithIllegalArgumentExceptionField();

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

    TypeAdapter<DummyClassWithIllegalArgumentExceptionField> adapter = (TypeAdapter<DummyClassWithIllegalArgumentExceptionField>) factory.create(
        new GsonBuilder().create(), new TypeToken<DummyClassWithIllegalArgumentExceptionField>() {});

    adapter.writeValue(new JsonWriter(new ByteArrayOutputStream()), dummyClass);
  }

  private static class DummyClass {
    private int field = 1;
  }

  @JsonAdapter(DummyJsonAdapter.class)
  private static class DummyJsonAdapterClass {
    private int field = 2;
  }

  private static class DummyClassWithoutJsonAdapter {
    private int field = 3;
  }

  private static class DummyClassWithWriteField {
    private int fieldToWrite = 4;

    public int getFieldToWrite() {
      return fieldToWrite;
    }

    public void setFieldToWrite(int fieldToWrite) {
      this.fieldToWrite = fieldToWrite;
    }
  }

  private static class DummyClassWithNullField {
    private String field;
  }

  private static class DummyClassWithMissingName {
    private int field;
  }

  private static class DummyClassWithMultipleFields {
    private int field1 = 1;
    private int field1 = 2;
  }

  private static class DummyClassWithPrimitiveWriteField {
    private int intToWrite = 5;
  }

  private static class DummyClassWithIllegalAccessExceptionField {
    private int field = 6;

    private void setField(int value) {
      this.field = value;
    }
  }

  private static class DummyClassWithIllegalArgumentExceptionField {
    private int field = 7;

    public void setField(int value) {
      throw new IllegalArgumentException();
    }
  }

  private static class FieldNamingPolicy {
    public String translateName(Field field) {
      return field.getName();
    }
  }

  private static class Excluder {
    public boolean excludeClass(Class<?> clazz, boolean serialize) {
      return false;
    }

    public boolean excludeField(Field field, boolean serialize) {
      return false;
    }
  }

  private static class ConstructorConstructor {
    public <T> ObjectConstructor<T> get(TypeToken<T> type) {
      return new ObjectConstructor<T>() {
        @SuppressWarnings("unchecked")
        public T construct() {
          try {
            return (T) type.getConstructor().newInstance();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      };
    }
  }
}