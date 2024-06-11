package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  private Gson gson = new Gson();
  private ConstructorConstructor constructorConstructor = new ConstructorConstructor();
  private JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  
  @JsonAdapter(MyTypeAdapter.class)
  private class MyClass {
    private String name;
    
    public MyClass(String name) {
      this.name = name;
    }
  }
  
  public class MyTypeAdapter extends TypeAdapter<MyClass> {
    @Override
    public void write(JsonWriter out, MyClass value) throws IOException {
      // write implementation here
    }
    
    @Override
    public MyClass read(JsonReader in) throws IOException {
      // read implementation here
    }
  }
  
  @Test
  public void testCreate_TypeAdapterWithJsonAdapterAnnotation_ReturnsTypeAdapterInstance() {
    TypeToken<MyClass> targetType = new TypeToken<MyClass>() {};
    TypeAdapter<MyClass> adapter = factory.create(gson, targetType);
    
    assertEquals(MyTypeAdapter.class, adapter.getClass());
  }
  
  @JsonAdapter(MyTypeAdapterFactory.class)
  private class MyClass2 {
    private String name;
    
    public MyClass2(String name) {
      this.name = name;
    }
  }
  
  public class MyTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      // create implementation here
    }
  }
  
  @Test
  public void testCreate_TypeAdapterFactoryWithJsonAdapterAnnotation_ReturnsTypeAdapterInstance() {
    TypeToken<MyClass2> targetType = new TypeToken<MyClass2>() {};
    TypeAdapter<MyClass2> adapter = factory.create(gson, targetType);
    
    assertEquals(MyTypeAdapterFactory.class, adapter.getClass());
  }
  
  @JsonAdapter(MyTypeAdapter.class)
  private class MyClass3 {
    private String name;
    
    public MyClass3(String name) {
      this.name = name;
    }
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreate_TypeAdapterWithInvalidJsonAdapterAnnotation_ThrowsIllegalArgumentException() {
    TypeToken<MyClass3> targetType = new TypeToken<MyClass3>() {};
    factory.create(gson, targetType);
  }
  
  @Test
  public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapterInstance() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<MyClass>() {}, JsonAdapter.class);
    
    assertEquals(MyTypeAdapter.class, adapter.getClass());
  }
  
  @Test
  public void testGetTypeAdapter_TypeAdapterFactory_ReturnsTypeAdapterInstance() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<MyClass2>() {}, JsonAdapter.class);
    
    assertEquals(MyTypeAdapterFactory.class, adapter.getClass());
  }
    
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidTypeAdapter_ThrowsIllegalArgumentException() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<MyClass3>() {}, JsonAdapter.class);
  }
  
  @Test
  public void testGetTypeAdapter_NullTypeAdapter_ReturnsNull() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<MyClass>() {}, null);
    
    assertNull(adapter);
  }
  
  @Test
  public void testGetTypeAdapter_TypeAdapterNotNullSafe_ReturnsTypeAdapterInstance() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<MyClass>() {}, JsonAdapter.class);
    
    assertEquals(MyTypeAdapter.class, adapter.getClass());
  }
  
  @Test
  public void testGetTypeAdapter_NullTypeAdapter_ReturnsNullSafeTypeAdapterInstance() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<MyClass>() {}, null);
    
    assertNull(adapter);
  }
  
}