import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory adapterFactory;

  @Before
  public void setUp() {
    adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
  }

  @Test
  public void testCreate() {
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);

    TypeAdapter<String> adapter = adapterFactory.create(gson, targetType);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  @Test
  public void testCreateWithAnnotation() {
    Gson gson = new Gson();
    TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);

    TypeAdapter<TestClass> adapter = adapterFactory.create(gson, targetType);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterClass() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);
    Class<?> value = annotation.value();
    assertTrue(TypeAdapter.class.isAssignableFrom(value));

    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterFactoryClass() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);
    Class<?> value = annotation.value();
    assertTrue(TypeAdapterFactory.class.isAssignableFrom(value));

    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapterWithInvalidAnnotationValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);

    annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapter<?>> value() {
        return null;
      }

      @Override
      public Class<?>[] classes() {
        return null;
      }
    };

    JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test
  public void testGetTypeAdapterWithNullSafeTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);
    Class<?> value = annotation.value();
    assertTrue(TypeAdapter.class.isAssignableFrom(value));

    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
    assertTrue(adapter.isNullSafe());
  }

  private static class TestClass {
    // Test class for @JsonAdapter
  }
}