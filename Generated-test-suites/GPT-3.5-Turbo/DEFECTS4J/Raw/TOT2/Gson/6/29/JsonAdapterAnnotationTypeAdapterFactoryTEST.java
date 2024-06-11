import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_NullAnnotation_ReturnsNull() {
    // Given
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    
    // When
    TypeAdapter<Object> typeAdapter = factory.create(new Gson(), TypeToken.get(Object.class));
    
    // Then
    assertNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterAnnotation_ReturnsTypeAdapter() {
    // Given
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeToken<Object> targetType = TypeToken.get(Object.class);
    JsonAdapter annotation = new JsonAdapter(value = TestTypeAdapter.class);
    
    // When
    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(new ConstructorConstructor(), new Gson(), targetType, annotation);
    
    // Then
    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
    // Given
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeToken<Object> targetType = TypeToken.get(Object.class);
    JsonAdapter annotation = new JsonAdapter(value = TestTypeAdapterFactory.class);
    
    // When
    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(new ConstructorConstructor(), new Gson(), targetType, annotation);
    
    // Then
    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidAnnotation_ThrowsIllegalArgumentException() {
    // Given
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeToken<Object> targetType = TypeToken.get(Object.class);
    JsonAdapter annotation = new JsonAdapter(value = String.class);
    
    // When
    factory.getTypeAdapter(new ConstructorConstructor(), new Gson(), targetType, annotation);
  }

  public static class TestTypeAdapter extends TypeAdapter<Object> {
    // Implement test type adapter
  }

  public static class TestTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      // Implement test type adapter factory
      return null;
    }
  }
}