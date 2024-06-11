package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;
 private JsonAdapterAnnotationTypeAdapterFactory factory;

 @Before
 public void setup() {
 gson = mock(Gson.class);
 constructorConstructor = mock(ConstructorConstructor.class);
 factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void testNullTargetType() {
 TypeAdapter<?> result = factory.create(gson, null);
 assertNull(result);
 }

 @Test
 public void testNoJsonAdapterAnnotation() {
 TypeToken<Integer> targetType = new TypeToken<Integer>() {};
 TypeAdapter<?> result = factory.create(gson, targetType);
 assertNull(result);
 }

 @Test
 public void testJsonAdapterIsTypeAdapter() {
 int value = 1;
 JsonAdapter annotation = new JsonAdapter(TypeAdapter.class, Integer.TYPE);
 TypeToken<Integer> targetType = new TypeToken<Integer>() {};
 TypeAdapter<Integer> expected = mock(TypeAdapter.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(expected);

 TypeAdapter<Integer> result = factory.create(gson, targetType);
 assertThat(result).isSameInstanceAs(expected);
 }

 @Test
 public void testJsonAdapterIsTypeAdapterFactory() {
 int value = 1;
 JsonAdapter annotation = new JsonAdapter(JsonAdapterFactory.class, Integer.TYPE);
 TypeToken<Integer> targetType = new TypeToken<Integer>() {};
 TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(factory);
 when(factory.create(gson, targetType)).thenReturn(mock(TypeAdapter.class));

 TypeAdapter<Integer> result = factory.create(gson, targetType);
 TypeAdapter<Integer> expected = mock(TypeAdapter.class);
 assertThat(result).isSameInstanceAs(expected);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterValueIsNotTypeAdapterOrTypeAdapterFactory() {
 JsonAdapter annotation = new JsonAdapter(String.class, Integer.TYPE);
 TypeToken<Integer> targetType = new TypeToken<Integer>() {};
 throw new IllegalArgumentException(
     "@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
 }
}