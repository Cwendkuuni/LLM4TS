import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertNotNull(adapter);
  }
  
  @Test
  public void testCreate_NoAnnotation() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<Integer> targetType = TypeToken.get(Integer.class);
    TypeAdapter<Integer> adapter = factory.create(gson, targetType);
    assertNull(adapter);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidAnnotationValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = TypeToken.get(String.class);
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return Object.class; // Invalid value
      }
    };
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
  
  @Test
  public void testCreate_TypeAdapterClass() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return IntegerAdapter.class; // Custom TypeAdapter class
      }
    };
    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
    assertNotNull(adapter);
    assertEquals(IntegerAdapter.class, adapter.getClass());
  }
  
  @Test
  public void testCreate_TypeAdapterFactoryClass() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return IntegerAdapterFactory.class; // Custom TypeAdapterFactory class
      }
    };
    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
    assertNotNull(adapter);
    assertEquals(IntegerAdapterFactory.class, adapter.getClass());
  }

}