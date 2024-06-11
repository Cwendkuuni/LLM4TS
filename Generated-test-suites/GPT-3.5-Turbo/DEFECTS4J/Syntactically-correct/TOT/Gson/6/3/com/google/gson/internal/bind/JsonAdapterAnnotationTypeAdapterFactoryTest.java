import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  // Testing create method with valid JsonAdapter annotation
  @Test
  public void testCreate_ValidJsonAdapterAnnotation_ReturnsTypeAdapter() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = 
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);
    
    // Act
    TypeAdapter<String> result = factory.create(gson, targetType);
    
    // Assert
    assertNotNull(result);
  }
  
  // Testing create method with invalid JsonAdapter annotation
  @Test
  public void testCreate_InvalidJsonAdapterAnnotation_ReturnsNull() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = 
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<Integer> targetType = TypeToken.get(Integer.class);
    
    // Act
    TypeAdapter<Integer> result = factory.create(gson, targetType);
    
    // Assert
    assertNull(result);
  }
  
  // Testing getTypeAdapter method with TypeAdapter value in JsonAdapter annotation
  @Test
  public void testGetTypeAdapter_TypeAdapterValue_ReturnsTypeAdapter() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(String.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    // Act
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    // Assert
    assertTrue(result instanceof TypeAdapter);
  }
  
  // Testing getTypeAdapter method with TypeAdapterFactory value in JsonAdapter annotation
  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryValue_ReturnsTypeAdapter() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(String.class);
    JsonAdapter annotation = new JsonAdapter(TypeAdapterFactory.class);
    
    // Act
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    // Assert
    assertTrue(result instanceof TypeAdapter);
  }
  
  // Testing getTypeAdapter method with invalid value in JsonAdapter annotation
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValueInAnnotation_ThrowsIllegalArgumentException() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(String.class);
    JsonAdapter annotation = new JsonAdapter(String.class);
    
    // Act
    JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    // Assert
    // Expects IllegalArgumentException to be thrown
  }
  
  // Testing getTypeAdapter method with null value in JsonAdapter annotation
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_NullValueInAnnotation_ThrowsIllegalArgumentException() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(String.class);
    JsonAdapter annotation = new JsonAdapter();
    
    // Act
    JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    // Assert
    // Expects IllegalArgumentException to be thrown
  }
  
  // Testing getTypeAdapter method with nullSafe method called
  @Test
  public void testGetTypeAdapter_NullSafeCalled_ReturnsNonNullResult() {
    // Arrange
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(String.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    // Act
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    result = result.nullSafe();
    
    // Assert
    assertNotNull(result);
  }
}