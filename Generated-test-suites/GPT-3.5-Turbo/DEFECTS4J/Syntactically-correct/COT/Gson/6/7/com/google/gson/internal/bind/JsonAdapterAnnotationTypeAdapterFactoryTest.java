import static org.junit.Assert.*;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  // Test create method
  
  @Test
  public void testCreate() {
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    
    assertNull(adapter);
  }
  
  // Test getTypeAdapter method
  
  @Test
  public void testGetTypeAdapter_TypeAdapterValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return StringAdapter.class;
      }
    };
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(adapter);
    assertTrue(adapter instanceof StringAdapter);
  }
  
  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return StringAdapterFactory.class;
      }
    };
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(adapter);
    assertTrue(adapter instanceof StringAdapterFactory);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return Integer.class;
      }
    };
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
  
  // Test getTypeAdapter method with nullSafe
  
  @Test
  public void testGetTypeAdapterWithNullSafe() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return StringAdapter.class;
      }
    };
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    TypeAdapter<?> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(adapter);
    assertTrue(adapter instanceof StringAdapter);
    assertTrue(adapter.nullSafe());
  }
  
  // Edge cases and exception handling scenarios
  
  // Test create method with targetType having JsonAdapter annotation
  
  @Test
  public void testCreate_TargetTypeWithJsonAdapterAnnotation() {
    Gson gson = new Gson();
    TypeToken<CustomObject> targetType = new TypeToken<CustomObject>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    
    TypeAdapter<CustomObject> adapter = factory.create(gson, targetType);
    
    assertNotNull(adapter);
    assertTrue(adapter instanceof CustomObjectAdapter);
  }
  
  @Test
  public void testCreate_TargetTypeWithJsonAdapterAnnotation_NoAnnotation() {
    Gson gson = new Gson();
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    
    TypeAdapter<Object> adapter = factory.create(gson, targetType);
    
    assertNull(adapter);
  }
  
  // Test getTypeAdapter method with invalid value type
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValueType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return Double.class;
      }
    };
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
  
  // Test getTypeAdapter method with null fieldType
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_NullFieldType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = null;
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return StringAdapter.class;
      }
    };
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
  
  // Test getTypeAdapter method with invalid fieldType
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidFieldType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = new TypeToken<Object>() {};
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return StringAdapter.class;
      }
    };
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
  
  // Test getTypeAdapter method with null annotation
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_NullAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = null;
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
  
}