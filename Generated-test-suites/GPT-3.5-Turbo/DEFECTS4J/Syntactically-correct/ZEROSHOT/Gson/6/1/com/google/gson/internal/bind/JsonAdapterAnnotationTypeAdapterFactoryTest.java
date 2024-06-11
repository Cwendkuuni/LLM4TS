import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private TypeToken<Object> targetType;
  private TypeAdapter<Object> typeAdapter;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    targetType = TypeToken.get(Object.class);
    typeAdapter = mock(TypeAdapter.class);
  }

  @Test
  public void testCreate_AnnotationPresent_TypeAdapterReturned() {
    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(mock(JsonAdapter.class));
    when(constructorConstructor.get(eq(targetType))).thenReturn(constructorConstructor);
    when(constructorConstructor.construct()).thenReturn(typeAdapter);
  
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  
    TypeAdapter<?> result = factory.create(gson, targetType);
  
    assertNotNull(result);
    assertEquals(typeAdapter, result);
  }

  @Test
  public void testCreate_AnnotationAbsent_NullReturned() {
    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(null);
  
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  
    TypeAdapter<?> result = factory.create(gson, targetType);
  
    assertNull(result);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass_TypeAdapterReturned() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    Class<TypeAdapter<?>> typeAdapterClass = mock(Class.class);
    when(annotation.value()).thenReturn(typeAdapterClass);
    when(constructorConstructor.get(eq(TypeToken.get(typeAdapterClass)))).thenReturn(constructorConstructor);
    when(constructorConstructor.construct()).thenReturn(typeAdapter);
  
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  
    assertNotNull(result);
    assertEquals(typeAdapter, result);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactory_TypeAdapterReturned() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    Class<TypeAdapterFactory> typeAdapterFactory = mock(Class.class);
    TypeAdapterFactory typeAdapterFactoryInstance = mock(TypeAdapterFactory.class);
    when(annotation.value()).thenReturn(typeAdapterFactory);
    when(constructorConstructor.get(eq(TypeToken.get(typeAdapterFactory)))).thenReturn(constructorConstructor);
    when(constructorConstructor.construct()).thenReturn(typeAdapterFactoryInstance);
    when(typeAdapterFactoryInstance.create(any(Gson.class), eq(targetType))).thenReturn(typeAdapter);
  
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  
    assertNotNull(result);
    assertEquals(typeAdapter, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValue_ThrowIllegalArgumentException() {
    JsonAdapter annotation = mock(JsonAdapter.class);
    Class<?> invalidValue = mock(Class.class);
    when(annotation.value()).thenReturn(invalidValue);
  
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  }
}