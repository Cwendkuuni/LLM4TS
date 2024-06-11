package com.google.gson.internal.bind.test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 gson = new Gson();
 }

 @Test
 public void testJsonAdapterAnnotationTypeAdapterFactory_TypeAdapter() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> jsonAdapterAnnotationTypeAdapterFactoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<TypeAdapter<String>> stringTypeAdapterClass = TypeAdapter.class;
 TypeToken<TypeAdapter<String>> stringTypeAdapterTypeToken = new TypeToken<TypeAdapter<String>>() {};
 JsonAdapter stringJsonAdapterAnnotation = jsonAdapterAnnotationTypeAdapterFactoryClass.getAnnotation(JsonAdapter.class);
 Class<String> stringType = String.class;

 JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(
 new ConstructorConstructor());
 TypeAdapter<String> stringTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, stringTypeToken);

 assertEquals(String.class, stringTypeAdapter.getType());
 assertEquals(stringJsonAdapterAnnotation.value(), stringTypeAdapter.getClass());
 }

 @Test
 public void testJsonAdapterAnnotationTypeAdapterFactory_TypeAdapterFactory() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> jsonAdapterAnnotationTypeAdapterFactoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<TypeAdapterFactory> typeAdapterFactoryClass = TypeAdapterFactory.class;
 TypeToken<TypeAdapterFactory> typeAdapterFactoryTypeToken = new TypeToken<TypeAdapterFactory>() {};
 JsonAdapter stringJsonAdapterAnnotation = jsonAdapterAnnotationTypeAdapterFactoryClass.getAnnotation(JsonAdapter.class);

 JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(
 new ConstructorConstructor());
 TypeAdapterFactory typeAdapterFactory = jsonAdapterAnnotationTypeAdapterFactory.create(gson, stringTypeToken);

 assertEquals(String.class, typeAdapterFactory.getType());
 assertNotNull(typeAdapterFactory.create(gson, String.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterAnnotationTypeAdapterFactory_InvalidJsonAdapterValue() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> jsonAdapterAnnotationTypeAdapterFactoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<Number> numberType = Number.class;
 JsonAdapter numberJsonAdapterAnnotation = jsonAdapterAnnotationTypeAdapterFactoryClass.getAnnotation(JsonAdapter.class);
 Class<String> stringType = String.class;

 JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(
 new ConstructorConstructor());
 try {
 jsonAdapterAnnotationTypeAdapterFactory.create(gson, numberType);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
 }
 }

 @Test
 public void testJsonAdapterAnnotationTypeAdapterFactory_NullSafe() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> jsonAdapterAnnotationTypeAdapterFactoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<String> stringType = String.class;
 JsonAdapter stringJsonAdapterAnnotation = jsonAdapterAnnotationTypeAdapterFactoryClass.getAnnotation(JsonAdapter.class);
 Class<String> stringTypeNull = String.class;

 JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(
 new ConstructorConstructor());
 TypeAdapter<String> stringTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, stringTypeToken);

 assertNotNull(stringTypeAdapter.nullSafe());

 TypeAdapter<String> stringTypeAdapterNull = jsonAdapterAnnotationTypeAdapterFactory.create(gson, stringTypeToken);
 assertEquals(String.class, stringTypeAdapterNull.getNullValue().getClass());
 }

 @Test
 public void testJsonAdapterAnnotationTypeAdapterFactory_NullJsonAdapterAnnotation() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> jsonAdapterAnnotationTypeAdapterFactoryClass = JsonAdapterAnnotationTypeAdapterFactory.class;
 Class<Number> numberType = Number.class;

 JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(
 new ConstructorConstructor());
 assertNull(jsonAdapterAnnotationTypeAdapterFactory.create(gson, numberType));
 }
}