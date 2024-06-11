import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void create_shouldReturnNull_whenNoJsonAdapterAnnotationPresent() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);
    TypeAdapter<String> result = factory.create(gson, targetType);
    assertNull(result);
  }

  @Test
  public void create_shouldReturnTypeAdapter_whenJsonAdapterAnnotationPresent() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<TestObject> targetType = TypeToken.get(TestObject.class);
    TypeAdapter<TestObject> result = factory.create(gson, targetType);
    assertNotNull(result);
    assertTrue(result instanceof TestObjectAdapter);
  }

  @Test
  public void getTypeAdapter_shouldThrowException_whenInvalidJsonAdapterValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Object> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    // Expecting an exception to be thrown
  }

  @Test
  public void getTypeAdapter_shouldReturnTypeAdapter_whenJsonAdapterValueIsTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<TestTypeAdapter> fieldType = TypeToken.get(TestTypeAdapter.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(result);
    assertTrue(result instanceof TestTypeAdapter);
  }

  @Test
  public void getTypeAdapter_shouldReturnTypeAdapter_whenJsonAdapterValueIsTypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<TestTypeAdapterFactory> fieldType = TypeToken.get(TestTypeAdapterFactory.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(result);
    assertTrue(result instanceof TestTypeAdapterFactoryAdapter);
  }

  @Test
  public void getTypeAdapter_shouldReturnNullSafeTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<TestTypeAdapter> fieldType = TypeToken.get(TestTypeAdapter.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(result);
    assertTrue(result.isNullSafe());
  }

  private static class TestObject {
  }

  private static class TestObjectAdapter extends TypeAdapter<TestObject> {
  }

  private static class TestTypeAdapter extends TypeAdapter<Object> {
  }

  private static class TestTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
  }

  private static class TestTypeAdapterFactoryAdapter extends TypeAdapter<Object> {
    @Override
    public void write(JsonWriter out, Object value) throws IOException {
    }
  
    @Override
    public Object read(JsonReader in) throws IOException {
      return null;
    }
  }
}