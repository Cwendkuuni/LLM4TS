import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private Gson gson;
  private TypeToken<?> targetType;
  private ConstructorConstructor constructorConstructor;

  @Test
  public void testCreate_NullAnnotation_ReturnNull() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    targetType = TypeToken.get(TestClass.class);
    TypeAdapter<?> result = factory.create(gson, targetType);
    assertNull(result);
  }

  @Test
  public void testCreate_TypeAdapterAnnotation_ReturnTypeAdapterInstance() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    targetType = TypeToken.get(TypeAdapterAnnotationTestClass.class);
    TypeAdapter<?> result = factory.create(gson, targetType);
    assertNotNull(result);
    assertEquals(TypeAdapterTestClass.class, result.getClass());
  }

  @Test
  public void testCreate_TypeAdapterFactoryAnnotation_ReturnTypeAdapterInstance() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    targetType = TypeToken.get(TypeAdapterFactoryAnnotationTestClass.class);
    TypeAdapter<?> result = factory.create(gson, targetType);
    assertNotNull(result);
    assertEquals(TypeAdapterFactoryTestClass.class, result.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidAnnotationValue_ThrowIllegalArgumentException() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    targetType = TypeToken.get(InvalidAnnotationTestClass.class);
    factory.create(gson, targetType);
  }

  private static class TestClass {}

  @JsonAdapter(value = TypeAdapterTestClass.class)
  private static class TypeAdapterAnnotationTestClass {}

  @JsonAdapter(value = TypeAdapterFactoryTestClass.class)
  private static class TypeAdapterFactoryAnnotationTestClass {}

  @JsonAdapter(value = InvalidAnnotationTestClass.class)
  private static class InvalidAnnotationTestClass {}

  private static class TypeAdapterTestClass extends TypeAdapter<TestClass> {
    @Override
    public void write(JsonWriter out, TestClass value) throws IOException {
    }

    @Override
    public TestClass read(JsonReader in) throws IOException {
      return null;
    }
  }

  private static class TypeAdapterFactoryTestClass implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
  }
}