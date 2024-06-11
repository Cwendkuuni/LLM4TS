import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_TypeAdapterReturned() {
    // Arrange
    TypeToken targetType = mock(TypeToken.class);
    Class rawType = mock(Class.class);
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(targetType.getRawType()).thenReturn(rawType);
    when(rawType.getAnnotation(JsonAdapter.class)).thenReturn(annotation);

    TypeAdapter<?> expectedTypeAdapter = mock(TypeAdapter.class);
    when(annotation.value()).thenReturn(expectedTypeAdapter);

    // Act
    TypeAdapter<?> actualTypeAdapter = factory.create(gson, targetType);

    // Assert
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test
  public void testCreate_NoAnnotation_ReturnsNullTypeAdapter() {
    // Arrange
    TypeToken targetType = mock(TypeToken.class);
    Class rawType = mock(Class.class);
    when(targetType.getRawType()).thenReturn(rawType);
    when(rawType.getAnnotation(JsonAdapter.class)).thenReturn(null);

    // Act
    TypeAdapter<?> actualTypeAdapter = factory.create(gson, targetType);

    // Assert
    assertEquals(null, actualTypeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapter() {
    // Arrange
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<?> fieldType = mock(TypeToken.class);
    JsonAdapter annotation = mock(JsonAdapter.class);
    TypeAdapter<?> expectedTypeAdapter = mock(TypeAdapter.class);

    when(constructorConstructor.get(TypeToken.get(annotation.value()))).thenReturn(constructorConstructor);
    when(constructorConstructor.construct()).thenReturn(expectedTypeAdapter);
    when(annotation.value()).thenReturn((Class) expectedTypeAdapter.getClass());

    // Act
    TypeAdapter<?> actualTypeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    // Assert
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapter() {
    // Arrange
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<?> fieldType = mock(TypeToken.class);
    JsonAdapter annotation = mock(JsonAdapter.class);
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    TypeAdapter<?> expectedTypeAdapter = mock(TypeAdapter.class);

    when(constructorConstructor.get(TypeToken.get(annotation.value()))).thenReturn(constructorConstructor);
    when(constructorConstructor.construct()).thenReturn(typeAdapterFactory);
    when(typeAdapterFactory.create(gson, fieldType)).thenReturn(expectedTypeAdapter);
    when(annotation.value()).thenReturn((Class) typeAdapterFactory.getClass());

    // Act
    TypeAdapter<?> actualTypeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    // Assert
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValue_ThrowsException() {
    // Arrange
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<?> fieldType = mock(TypeToken.class);
    JsonAdapter annotation = mock(JsonAdapter.class);
    Class<?> invalidValueClass = mock(Class.class);

    when(annotation.value()).thenReturn(invalidValueClass);

    // Act
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
}