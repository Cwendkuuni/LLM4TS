import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  @Test
  public void testCreate_withJsonAdapterAnnotation_shouldReturnTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
    
    assertNotNull(typeAdapter);
    assertEquals(TypeAdapter.class, typeAdapter.getClass());
  }
  
  @Test
  public void testCreate_withoutJsonAdapterAnnotation_shouldReturnNull() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    
    TypeAdapter<Integer> typeAdapter = factory.create(gson, targetType);
    
    assertNull(typeAdapter);
  }
  
  @Test
  public void testGetTypeAdapter_withTypeAdapterClass_shouldReturnTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(typeAdapter);
    assertEquals(TypeAdapter.class, typeAdapter.getClass());
  }
  
  @Test
  public void testGetTypeAdapter_withTypeAdapterFactoryClass_shouldReturnTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<Integer> fieldType = new TypeToken<Integer>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(typeAdapter);
    assertEquals(TypeAdapter.class, typeAdapter.getClass());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_withInvalidTypeAnnotation_shouldThrowException() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<Double> fieldType = new TypeToken<Double>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    factory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);
  }
}