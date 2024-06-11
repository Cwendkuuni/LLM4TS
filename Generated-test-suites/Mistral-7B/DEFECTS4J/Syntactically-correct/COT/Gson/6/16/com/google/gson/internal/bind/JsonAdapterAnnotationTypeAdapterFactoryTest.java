import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    // Given
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<String> targetType = new TypeToken<String>() {};

    JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When
    TypeAdapter<String> result = adapterFactory.create(gson, targetType);

    // Then
    assertNull(result);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_CreatesTypeAdapter() {
    // Given
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn(StringTypeAdapter.class);

    JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When
    TypeAdapter<String> result = adapterFactory.create(gson, targetType);

    // Then
    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotationValueInvalid_ThrowsException() {
    // Given
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn(new Object() {});

    JsonAdapterAnnotationTypeAdapterFactory adapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When
    adapterFactory.create(gson, targetType);

    // Then
    // The exception should be thrown in this test
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapterInstance() {
    // Given
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn(StringTypeAdapter.class);

    // When
    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, jsonAdapterAnnotation);

    // Then
    assertNotNull(result);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapterFactoryInstance() {
    // Given
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn(JsonAdapterFactory.class);

    // When
    TypeAdapterFactory result = (TypeAdapterFactory) JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, jsonAdapterAnnotation);

    // Then
    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidJsonAdapterAnnotationValue_ThrowsException() {
    // Given
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn(new Object() {});

    // When
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, jsonAdapterAnnotation);

    // Then
    // The exception should be thrown in this test
  }
}

class StringTypeAdapter {}
class JsonAdapterFactory {}