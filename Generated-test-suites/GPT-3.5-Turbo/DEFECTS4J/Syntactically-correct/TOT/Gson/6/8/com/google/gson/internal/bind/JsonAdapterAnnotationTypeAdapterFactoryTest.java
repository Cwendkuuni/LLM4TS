import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructorMock;
  private Gson gsonMock;
  private TypeToken typeTokenMock;
  private TypeAdapter typeAdapterMock;
  
  public JsonAdapterAnnotationTypeAdapterFactoryTest() {
    constructorConstructorMock = Mockito.mock(ConstructorConstructor.class);
    gsonMock = Mockito.mock(Gson.class);
    typeTokenMock = Mockito.mock(TypeToken.class);
    typeAdapterMock = Mockito.mock(TypeAdapter.class);
  }
  
  @Test
  public void create_WhenAnnotationIsNull_ReturnsNull() {
    // Arrange
    Mockito.when(typeTokenMock.getRawType()).thenReturn(Object.class);
    // Act
    TypeAdapter<?> result = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock).create(gsonMock, typeTokenMock);
    // Assert
    assertNull(result);
  }
  
  @Test
  public void create_WhenAnnotationIsNotNull_ReturnsTypeAdapter() {
    // Arrange
    Mockito.when(typeTokenMock.getRawType()).thenReturn(Mockito.mock(Class.class));
    Mockito.when(typeTokenMock.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(Mockito.mock(JsonAdapter.class));
    Mockito.when(constructorConstructorMock.get(Mockito.any())).thenReturn(Mockito.mock(Constructor.class));
    Mockito.when(constructorConstructorMock.get(Mockito.any()).construct()).thenReturn(typeAdapterMock);
    // Act
    TypeAdapter<?> result = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock).create(gsonMock, typeTokenMock);
    // Assert
    assertNotNull(result);
  }
  
  @Test
  public void getTypeAdapter_WhenValueIsTypeAdapter_ReturnsTypeAdapter() {
    // Arrange
    JsonAdapter annotationMock = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotationMock.value()).thenReturn(Mockito.mock(Class.class));
    Mockito.when(constructorConstructorMock.get(Mockito.any())).thenReturn(Mockito.mock(Constructor.class));
    Mockito.when(constructorConstructorMock.get(Mockito.any()).construct()).thenReturn(typeAdapterMock);
    // Act
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructorMock, gsonMock, typeTokenMock, annotationMock);
    // Assert
    assertNotNull(result);
  }
  
  @Test
  public void getTypeAdapter_WhenValueIsTypeAdapterFactory_ReturnsTypeAdapter() {
    // Arrange
    JsonAdapter annotationMock = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotationMock.value()).thenReturn(Mockito.mock(Class.class));
    Mockito.when(constructorConstructorMock.get(Mockito.any())).thenReturn(Mockito.mock(Constructor.class));
    Mockito.when(constructorConstructorMock.get(Mockito.any()).construct()).thenReturn(typeAdapterMock);
    TypeAdapterFactory typeAdapterFactoryMock = Mockito.mock(TypeAdapterFactory.class);
    Mockito.when(constructorConstructorMock.get(Mockito.any()).construct().create(Mockito.any(), Mockito.any())).thenReturn(typeAdapterMock);
    // Act
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructorMock, gsonMock, typeTokenMock, annotationMock);
    // Assert
    assertNotNull(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void getTypeAdapter_WhenValueIsNotTypeAdapterOrTypeAdapterFactory_ThrowsIllegalArgumentException() {
    // Arrange
    JsonAdapter annotationMock = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotationMock.value()).thenReturn(Mockito.mock(Class.class));
    Mockito.when(constructorConstructorMock.get(Mockito.any())).thenReturn(Mockito.mock(Constructor.class));
    Mockito.when(constructorConstructorMock.get(Mockito.any()).construct()).thenReturn(Mockito.mock(Object.class));
    // Act
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructorMock, gsonMock, typeTokenMock, annotationMock);
    // Assert
    fail("Expected IllegalArgumentException to be thrown.");
  }
  
}