package com.example.junit4;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private JsonAdapterAnnotationTypeAdapterFactory factory;

 @Before
 public void setup() {
 gson = new GsonBuilder().create();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
 factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void testStringAdapter() {
 StringJsonAdapter stringAdapter = new StringJsonAdapter();
 Type stringType = TypeToken.get(String.class).getType();
 TypeAdapter<String> stringTypeAdapter = factory.create(gson, TypeToken.get(String.class));
 assertEquals(stringAdapter, stringTypeAdapter);
 }

 @Test
 public void testIntegerAdapter() {
 IntJsonAdapter intAdapter = new IntJsonAdapter();
 Type intType = TypeToken.get(Integer.class).getType();
 TypeAdapter<Integer> intTypeAdapter = factory.create(gson, TypeToken.get(Integer.class));
 assertEquals(intAdapter, intTypeAdapter);
 }

 @Test
 public void testCustomAdapter() {
 MyJsonAdapter myJsonAdapter = new MyJsonAdapter();
 Type myType = new TypeToken<MyClass>() {}.getType();
 TypeAdapter<MyClass> myTypeAdapter = factory.create(gson, (Type) myType);
 assertEquals(myJsonAdapter, myTypeAdapter);
 }

 @Test
 public void testNullAdapter() {
 TypeTypeAdapter nullAdapter = null;
 Type nullType = TypeToken.get(String[].class).getType();
 TypeAdapter<String[]> stringArrayTypeAdapter = factory.create(gson, (Type) nullType);
 assertNull(stringArrayTypeAdapter.read(null));
 assertSame(null, stringArrayTypeAdapter.write(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapter() {
 TypeTypeAdapter invalidAdapter = new TypeTypeAdapter();
 Type invalidType = TypeToken.get(String.class).getType();
 TypeAdapter<String> stringTypeAdapter = factory.create(gson, (Type) invalidType);
 fail("Expected IllegalArgumentException but got " + stringTypeAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapterValueType() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
 fail("Expected IllegalArgumentException but got " + factory);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapterValueInstance() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new Object());
 fail("Expected IllegalArgumentException but got " + factory);
 }
}

class StringJsonAdapter {}
class IntJsonAdapter {}
class MyJsonAdapter {}
class MyClass {}
class TypeTypeAdapter {}