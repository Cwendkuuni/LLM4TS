import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    gson = new Gson();
  }

  @Test
  public void testCreate_NullAnnotation_ReturnNullTypeAdapter() {
    TypeToken<Object> targetType = TypeToken.get(Object.class);
    TypeAdapter<Object> typeAdapter = factory.create(gson, targetType);
    
    assertNull(typeAdapter);
  }

  @Test
  public void testCreate_ValidAnnotation_ReturnTypeAdapter() {
    TypeToken<TestClass> targetType = TypeToken.get(TestClass.class);
    TypeAdapter<TestClass> typeAdapter = factory.create(gson, targetType);

    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidAnnotationValue_ThrowIllegalArgumentException() {
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(String.class);

    TypeToken<TestClass> fieldType = TypeToken.get(TestClass.class);

    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterAnnotationValue_ReturnTypeAdapterInstance() {
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(TestTypeAdapter.class);

    TypeToken<TestClass> fieldType = TypeToken.get(TestClass.class);

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryAnnotationValue_ReturnTypeAdapterInstance() {
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(TestTypeAdapterFactory.class);

    TypeToken<TestClass> fieldType = TypeToken.get(TestClass.class);

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapterFactory);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryAnnotationValue_ReturnNullSafeTypeAdapterInstance() {
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(TestTypeAdapterFactory.class);

    TypeToken<TestClass> fieldType = TypeToken.get(TestClass.class);

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapterFactory);
    assertTrue(typeAdapter.isLenient());
  }
}

class TestClass {
  // Test class code
}

class TestTypeAdapter extends TypeAdapter<TestClass> {
  // Test type adapter code
}

class TestTypeAdapterFactory implements TypeAdapterFactory {
  // Test type adapter factory code
}