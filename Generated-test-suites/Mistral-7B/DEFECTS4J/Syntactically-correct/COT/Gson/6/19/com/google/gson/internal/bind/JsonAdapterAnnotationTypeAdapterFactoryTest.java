import static org.junit.Assert.*;

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
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    assertNull(factory.create(gson, targetType));
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<?> typeAdapter = factory.create(gson, targetType);
    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_ValueIsNotTypeAdapterOrTypeAdapterFactory_ThrowsException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(); // Annotation without value field
    try {
      TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetTypeAdapter_ValueIsTypeAdapter_ReturnsTypeAdapterInstance() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapterForStrings();
    TypeAdapter<String> expectedTypeAdapter = new StringTypeAdapter();
    TypeAdapter<?> actualTypeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test
  public void testGetTypeAdapter_ValueIsTypeAdapterFactory_ReturnsTypeAdapterInstanceCreatedByFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapterFactoryForStrings annotation = new JsonAdapterFactoryForStrings();
    TypeAdapterFactory<String> expectedTypeAdapterFactory = new StringAdapterFactory();
    TypeAdapter<String> expectedTypeAdapter = new StringTypeAdapter();
    TypeAdapterFactory<String> actualTypeAdapterFactory = (TypeAdapterFactory<String>) JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertEquals(expectedTypeAdapterFactory, actualTypeAdapterFactory);
    TypeAdapter<String> actualTypeAdapter = actualTypeAdapterFactory.create(gson, fieldType);
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  static class JsonAdapterForStrings {}

  static class StringTypeAdapter extends TypeAdapter<String> {}

  static class StringAdapterFactory implements TypeAdapterFactory<String> {
    @Override
    public TypeAdapter<String> create(Gson gson, TypeToken<String> typeToken) {
      return new StringTypeAdapter();
    }
  }
}