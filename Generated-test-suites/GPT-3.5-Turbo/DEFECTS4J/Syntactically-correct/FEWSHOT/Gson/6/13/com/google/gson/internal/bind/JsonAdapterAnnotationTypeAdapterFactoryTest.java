package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  
  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }
  
  @Test
  public void testCreate_ReturnsNull_WhenAnnotationIsNull() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<T> targetType = new TypeToken<T>() {};
    TypeAdapter<T> result = factory.create(gson, targetType);
    
    assertNull(result);
  }
  
  @Test
  public void testCreate_ReturnsTypeAdapter_WhenAnnotationExists() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<T> targetType = new TypeToken<T>() {
      @JsonAdapter(FakeTypeAdapter.class)
      public class T {}
    };
    TypeAdapter<T> result = factory.create(gson, targetType);
    
    assertNotNull(result);
    assertEquals(FakeTypeAdapter.class, result.getClass());
  }
  
  @Test
  public void testGetTypeAdapter_ReturnsTypeAdapter_WhenValueIsTypeAdapter() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<?> fieldType = new TypeToken<Object>() {};
    JsonAdapter annotation = new JsonAdapter(FakeTypeAdapter.class);
    
    TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(result);
    assertEquals(FakeTypeAdapter.class, result.getClass());
  }
  
  @Test
  public void testGetTypeAdapter_ReturnsTypeAdapter_WhenValueIsTypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<?> fieldType = new TypeToken<Object>() {};
    JsonAdapter annotation = new JsonAdapter(FakeTypeAdapterFactory.class);
    
    TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(result);
    assertEquals(FakeTypeAdapter.class, result.getClass());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_ThrowsException_WhenValueIsInvalid() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<?> fieldType = new TypeToken<Object>() {};
    JsonAdapter annotation = new JsonAdapter(Object.class);
    
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
}

class FakeTypeAdapter<T> extends TypeAdapter<T> {
  // ...
}

class FakeTypeAdapterFactory<T> implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    return new FakeTypeAdapter<T>();
  }
}