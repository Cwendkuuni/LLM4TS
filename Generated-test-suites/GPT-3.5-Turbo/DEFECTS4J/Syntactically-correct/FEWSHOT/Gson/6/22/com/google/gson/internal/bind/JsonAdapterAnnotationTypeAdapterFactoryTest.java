import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructorMock;
  private Gson gsonMock;
  private TypeToken targetTypeMock;
  
  @Before
  public void setup() {
    constructorConstructorMock = mock(ConstructorConstructor.class);
    gsonMock = mock(Gson.class);
    targetTypeMock = mock(TypeToken.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);
  }
  
  @Test
  public void testCreate_WithJsonAdapterAnnotation_ReturnsTypeAdapter() {
    TypeAdapter typeAdapterMock = mock(TypeAdapter.class);
    Class rawTypeMock = mock(Class.class);
    JsonAdapter annotationMock = mock(JsonAdapter.class);
    
    when(targetTypeMock.getRawType()).thenReturn(rawTypeMock);
    when(rawTypeMock.getAnnotation(JsonAdapter.class)).thenReturn(annotationMock);
    when(constructorConstructorMock.get(TypeToken.get(rawTypeMock))).thenReturn(constructorConstructorMock);
    when(constructorConstructorMock.construct()).thenReturn(typeAdapterMock);
    
    TypeAdapter result = factory.create(gsonMock, targetTypeMock);
    
    assertEquals(typeAdapterMock, result);
    verify(constructorConstructorMock).get(TypeToken.get(rawTypeMock));
    verify(constructorConstructorMock).construct();
  }
  
  @Test
  public void testCreate_WithoutJsonAdapterAnnotation_ReturnsNull() {
    Class rawTypeMock = mock(Class.class);
    
    when(targetTypeMock.getRawType()).thenReturn(rawTypeMock);
    when(rawTypeMock.getAnnotation(JsonAdapter.class)).thenReturn(null);
    
    TypeAdapter result = factory.create(gsonMock, targetTypeMock);
    
    assertNull(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_WithInvalidJsonAdapterValue_ThrowsException() {
    ConstructorConstructor constructorConstructorMock = mock(ConstructorConstructor.class);
    Gson gsonMock = mock(Gson.class);
    TypeToken fieldTypeMock = mock(TypeToken.class);
    JsonAdapter annotationMock = mock(JsonAdapter.class);
    Class valueMock = mock(Class.class);
    
    when(annotationMock.value()).thenReturn(valueMock);
    when(valueMock.isAssignableFrom(TypeAdapter.class)).thenReturn(false);
    when(valueMock.isAssignableFrom(TypeAdapterFactory.class)).thenReturn(false);
    
    factory.getTypeAdapter(constructorConstructorMock, gsonMock, fieldTypeMock, annotationMock);
  }
  
  @Test
  public void testGetTypeAdapter_WithValidTypeAdapterValue_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructorMock = mock(ConstructorConstructor.class);
    Gson gsonMock = mock(Gson.class);
    TypeToken fieldTypeMock = mock(TypeToken.class);
    JsonAdapter annotationMock = mock(JsonAdapter.class);
    Class valueMock = mock(Class.class);
    TypeAdapter typeAdapterMock = mock(TypeAdapter.class);
    ConstructorConstructor typeAdapterConstructorConstructorMock = mock(ConstructorConstructor.class);
    
    when(annotationMock.value()).thenReturn(valueMock);
    when(valueMock.isAssignableFrom(TypeAdapter.class)).thenReturn(true);
    when(constructorConstructorMock.get(TypeToken.get(valueMock))).thenReturn(typeAdapterConstructorConstructorMock);
    when(typeAdapterConstructorConstructorMock.construct()).thenReturn(typeAdapterMock);
    
    TypeAdapter result = factory.getTypeAdapter(constructorConstructorMock, gsonMock, fieldTypeMock, annotationMock);
    
    assertEquals(typeAdapterMock, result);
    verify(constructorConstructorMock).get(TypeToken.get(valueMock));
    verify(typeAdapterConstructorConstructorMock).construct();
  }
  
  @Test
  public void testGetTypeAdapter_WithValidTypeAdapterFactoryValue_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructorMock = mock(ConstructorConstructor.class);
    Gson gsonMock = mock(Gson.class);
    TypeToken fieldTypeMock = mock(TypeToken.class);
    JsonAdapter annotationMock = mock(JsonAdapter.class);
    Class valueMock = mock(Class.class);
    TypeAdapterFactory typeAdapterFactoryMock = mock(TypeAdapterFactory.class);
    TypeAdapter typeAdapterMock = mock(TypeAdapter.class);
    ConstructorConstructor typeAdapterFactoryConstructorConstructorMock = mock(ConstructorConstructor.class);
    
    when(annotationMock.value()).thenReturn(valueMock);
    when(valueMock.isAssignableFrom(TypeAdapter.class)).thenReturn(false);
    when(valueMock.isAssignableFrom(TypeAdapterFactory.class)).thenReturn(true);
    when(constructorConstructorMock.get(TypeToken.get(valueMock))).thenReturn(typeAdapterFactoryConstructorConstructorMock);
    when(typeAdapterFactoryConstructorConstructorMock.construct()).thenReturn(typeAdapterFactoryMock);
    when(typeAdapterFactoryMock.create(gsonMock, fieldTypeMock)).thenReturn(typeAdapterMock);
    
    TypeAdapter result = factory.getTypeAdapter(constructorConstructorMock, gsonMock, fieldTypeMock, annotationMock);
    
    assertEquals(typeAdapterMock, result);
    verify(constructorConstructorMock).get(TypeToken.get(valueMock));
    verify(typeAdapterFactoryConstructorConstructorMock).construct();
    verify(typeAdapterFactoryMock).create(gsonMock, fieldTypeMock);
  }
  
  @Test
  public void testGetTypeAdapter_WithNullSafe_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructorMock = mock(ConstructorConstructor.class);
    Gson gsonMock = mock(Gson.class);
    TypeToken fieldTypeMock = mock(TypeToken.class);
    JsonAdapter annotationMock = mock(JsonAdapter.class);
    Class valueMock = mock(Class.class);
    TypeAdapterFactory typeAdapterFactoryMock = mock(TypeAdapterFactory.class);
    TypeAdapter typeAdapterMock = mock(TypeAdapter.class);
    ConstructorConstructor typeAdapterFactoryConstructorConstructorMock = mock(ConstructorConstructor.class);
    
    when(annotationMock.value()).thenReturn(valueMock);
    when(valueMock.isAssignableFrom(TypeAdapter.class)).thenReturn(false);
    when(valueMock.isAssignableFrom(TypeAdapterFactory.class)).thenReturn(true);
    when(constructorConstructorMock.get(TypeToken.get(valueMock))).thenReturn(typeAdapterFactoryConstructorConstructorMock);
    when(typeAdapterFactoryConstructorConstructorMock.construct()).thenReturn(typeAdapterFactoryMock);
    when(typeAdapterFactoryMock.create(gsonMock, fieldTypeMock)).thenReturn(typeAdapterMock);
    when(typeAdapterMock.nullSafe()).thenReturn(typeAdapterMock);
    
    TypeAdapter result = factory.getTypeAdapter(constructorConstructorMock, gsonMock, fieldTypeMock, annotationMock);
    
    assertEquals(typeAdapterMock, result);
    verify(constructorConstructorMock).get(TypeToken.get(valueMock));
    verify(typeAdapterFactoryConstructorConstructorMock).construct();
    verify(typeAdapterFactoryMock).create(gsonMock, fieldTypeMock);
    verify(typeAdapterMock).nullSafe();
  }
}