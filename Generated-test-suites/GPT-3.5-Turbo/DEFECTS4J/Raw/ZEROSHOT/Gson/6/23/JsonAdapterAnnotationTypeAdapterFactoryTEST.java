import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_JsonAdapterPresent_TypeAdapterReturned() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeAdapter<TestClass> expectedTypeAdapter = new TestTypeAdapter();
    TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<TestClass> actualTypeAdapter = factory.create(gson, targetType);

    assertNotNull(actualTypeAdapter);
    assertEquals(expectedTypeAdapter.getClass(), actualTypeAdapter.getClass());
  }

  @Test
  public void testCreate_JsonAdapterNotPresent_NullReturned() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<NoJsonAdapterClass> targetType = TypeToken.get(NoJsonAdapterClass.class);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<NoJsonAdapterClass> actualTypeAdapter = factory.create(gson, targetType);

    assertNull(actualTypeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass_ValueIsTypeAdapter_TypeAdapterReturned() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = TestTypeAdapter.class.getAnnotation(JsonAdapter.class);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> actualTypeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(actualTypeAdapter);
    assertEquals(TestTypeAdapter.class, actualTypeAdapter.getClass());
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryClass_ValueIsTypeAdapterFactory_TypeAdapterReturned() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = TestTypeAdapterFactory.class.getAnnotation(JsonAdapter.class);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> actualTypeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(actualTypeAdapter);
    assertEquals(TestTypeAdapter.class, actualTypeAdapter.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_ValueIsInvalid_ThrowIllegalArgumentException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = InvalidTypeAdapterClass.class.getAnnotation(JsonAdapter.class);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  private static class TestClass {}

  @JsonAdapter(TestTypeAdapter.class)
  private static class NoJsonAdapterClass {}

  private static class TestTypeAdapter extends TypeAdapter<TestClass> {
    @Override
    public void write(com.google.gson.stream.JsonWriter jsonWriter, TestClass testClass) {
    }

    @Override
    public TestClass read(com.google.gson.stream.JsonReader jsonReader) {
      return null;
    }
  }

  private static class TestTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
  }

  private static class InvalidTypeAdapterClass {}
}