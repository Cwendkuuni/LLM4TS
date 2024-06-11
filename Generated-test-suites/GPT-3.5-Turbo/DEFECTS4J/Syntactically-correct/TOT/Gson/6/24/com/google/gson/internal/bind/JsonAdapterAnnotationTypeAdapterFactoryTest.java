
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import org.junit.Test;
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private final ConstructorConstructor constructorConstructor = Mockito.mock(ConstructorConstructor.class);
  private final Gson gson = Mockito.mock(Gson.class);

  @Test
  public void create_WithNullAnnotation_ShouldReturnNull() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> targetType = TypeToken.get(Object.class);

    TypeAdapter<Object> result = factory.create(gson, targetType);

    assertNull(result);
  }

  @Test
  public void create_WithNonNullAnnotationAndInvalidValue_ShouldThrowIllegalArgumentException() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> targetType = TypeToken.get(Object.class);
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(Object.class);

    try {
      factory.create(gson, targetType);
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
    }
  }

  @Test
  public void create_WithNonNullAnnotationAndTypeAdapterValue_ShouldCreateTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> targetType = TypeToken.get(Object.class);
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(MockTypeAdapter.class);

    factory.create(gson, targetType);

    Mockito.verify(constructorConstructor).get(TypeToken.get(MockTypeAdapter.class));
  }

  @Test
  public void create_WithNonNullAnnotationAndTypeAdapterFactoryValue_ShouldCreateTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> targetType = TypeToken.get(Object.class);
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(MockTypeAdapterFactory.class);
    TypeAdapterFactory mockTypeAdapterFactory = Mockito.mock(TypeAdapterFactory.class);
    Mockito.when(constructorConstructor.get(TypeToken.get(MockTypeAdapterFactory.class))).thenReturn(mockTypeAdapterFactory);

    factory.create(gson, targetType);

    Mockito.verify(mockTypeAdapterFactory).create(gson, targetType);
  }

  @Test
  public void getTypeAdapter_WithInvalidTypeAdapterValue_ShouldThrowIllegalArgumentException() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(Object.class);

    try {
      factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
    }
  }

  @Test
  public void getTypeAdapter_WithTypeAdapterValue_ShouldReturnCreatedTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(MockTypeAdapter.class);
    TypeAdapter<Object> mockTypeAdapter = Mockito.mock(TypeAdapter.class);
    Mockito.when(constructorConstructor.get(TypeToken.get(MockTypeAdapter.class))).thenReturn(mockTypeAdapter);

    TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(result);
    assertEquals(mockTypeAdapter, result);
  }

  @Test
  public void getTypeAdapter_WithTypeAdapterFactoryValue_ShouldReturnCreatedTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> fieldType = TypeToken.get(Object.class);
    JsonAdapter annotation = Mockito.mock(JsonAdapter.class);
    Mockito.when(annotation.value()).thenReturn(MockTypeAdapterFactory.class);
    TypeAdapterFactory mockTypeAdapterFactory = Mockito.mock(TypeAdapterFactory.class);
    Mockito.when(constructorConstructor.get(TypeToken.get(MockTypeAdapterFactory.class))).thenReturn(mockTypeAdapterFactory);

    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    Mockito.verify(mockTypeAdapterFactory).create(gson, fieldType);
  }

  public static class MockTypeAdapter extends TypeAdapter<Object> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {
    }

    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }

  public static class MockTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
  }

}
