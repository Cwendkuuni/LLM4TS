package com.example.junit4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 constructorConstructor = new ConstructorConstructor();
 gson = new Gson();
 }

 @Test
 public void testNullType() {
 assertNull(
 new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, null));
 }

 @Test
 public void testNoJsonAdapterAnnotation() {
 assertNull(new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, TypeToken.get(String.class)));
 }

 @JsonAdapter(StringAdapter.class)
 static class MyClass {
 String field;
 }

 @Test
 public void testTypeAdapter() {
 TypeAdapter<MyClass> adapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, TypeToken.get(MyClass.class));
 assertNotNull(adapter);
 }

 @Test
 public void testTypeAdapterFactory() {
 TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<MyClass> adapter = factory.create(gson, TypeToken.get(MyClass.class));
 assertNotNull(adapter);

 TypeAdapter<String> stringAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, TypeToken.get(String.class));
 assertNotNull(stringAdapter);

 TypeAdapter<String[]> stringArrayAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, TypeToken.get(String[].class));
 assertNotNull(stringArrayAdapter);

 TypeAdapter<MyClass[]> myClassArrayAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, TypeToken.get(MyClass[].class));
 assertNotNull(myClassArrayAdapter);
 }

 @Test
 public void testTypeAdapterWithFactory() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapterFactory innerFactory = mock(TypeAdapterFactory.class);
 when(innerFactory.create(eq(gson), any(TypeToken.class))).thenReturn(new StringAdapter());
 when(innerFactory.create(eq(gson), eq(Integer.TYPE))).thenReturn(new IntegerAdapter());

 TypeAdapter<MyClassWithCustomFactory> myClassWithCustomFactory = new JsonAdapterAnnotationTypeAdapterFactory(innerFactory)
 .create(gson, TypeToken.get(MyClassWithCustomFactory.class));
 assertNotNull(myClassWithCustomFactory);
 }

 @SuppressWarnings("unchecked")
 static class StringAdapter extends TypeAdapter<String> {
 @Override
 public String read(JsonReader jsonReader) throws IOException {
 return jsonReader.nextString();
 }

 @Override
 public void write(JsonWriter jsonWriter, String value) throws IOException {
 jsonWriter.value(value);
 }
 }

 @SuppressWarnings("unchecked")
 static class IntegerAdapter extends TypeAdapter<Integer> {
 @Override
 public Integer read(JsonReader jsonReader) throws IOException {
 return jsonReader.nextInt();
 }

 @Override
 public void write(JsonWriter jsonWriter, Integer value) throws IOException {
 jsonWriter.value(value);
 }
 }

 static class MyClassWithCustomFactory {
 @JsonAdapter(CustomIntAdapterFactory.class)
 int field;
 }

 static class CustomIntAdapterFactory implements TypeAdapterFactory {
 @Override
 public TypeAdapter<Integer> create(Gson gson, TypeToken<Integer> type) {
 return new CustomIntAdapter();
 }
 }

 @SuppressWarnings("unchecked")
 static class CustomIntAdapter extends TypeAdapter<Integer> {
 @Override
 public Integer read(JsonReader jsonReader) throws IOException {
 return jsonReader.nextInt() * 2;
 }

 @Override
 public void write(JsonWriter jsonWriter, Integer value) throws IOException {
 jsonWriter.value(value / 2);
 }
 }
}