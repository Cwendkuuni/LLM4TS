import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_JsonAdapterNotPresent_ReturnsNullTypeAdapter() {
    // Arrange
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);

    // Act
    TypeAdapter<String> result = factory.create(gson, targetType);

    // Assert
    assertNull(result);
  }

  @Test
  public void testCreate_ValidJsonAdapter_ReturnsTypeAdapter() {
    // Arrange
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);

    // Act
    TypeAdapter<TestClass> result = factory.create(gson, targetType);

    // Assert
    assertNotNull(result);
    assertTrue(result instanceof TestClassJsonAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidAnnotationValue_ThrowsIllegalArgumentException() {
    // Arrange
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<Object> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = new TestInvalidAnnotation();

    // Act
    factory.getTypeAdapter(factory.constructorConstructor, gson, fieldType, annotation);

    // Assert (Expecting IllegalArgumentException)
  }

  private static class TestClass {
    // Test class
  }

  @JsonAdapter(TestClassJsonAdapter.class)
  private static class TestClassJsonAdapter {
    // Test adapter
  }

  private static class TestInvalidAnnotation {
    // Invalid annotation
  }
}