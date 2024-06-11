import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setup() {
    gson = new Gson();
    constructorConstructor = new ConstructorConstructor();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreate_withJsonAdapterAnnotation() {
    TypeAdapter<?> typeAdapter = factory.create(gson, TypeToken.get(String.class));
    assertNotNull(typeAdapter);
  }

  @Test
  public void testCreate_withoutJsonAdapterAnnotation() {
    TypeAdapter<?> typeAdapter = factory.create(gson, TypeToken.get(Integer.class));
    assertNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_withTypeAdapterValue() {
    JsonAdapter annotation = String.class.getAnnotation(JsonAdapter.class);
    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class), annotation);
    assertNotNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_withTypeAdapterFactoryValue() {
    JsonAdapter annotation = TypeAdapterFactoryTestClass.class.getAnnotation(JsonAdapter.class);
    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class), annotation);
    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_withInvalidValue() {
    JsonAdapter annotation = InvalidClass.class.getAnnotation(JsonAdapter.class);
    factory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class), annotation);
  }

  @Test
  public void testGetTypeAdapter_nullSafe() {
    JsonAdapter annotation = TypeAdapterFactoryTestClass.class.getAnnotation(JsonAdapter.class);
    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class), annotation);
    assertTrue(typeAdapter.nullSafe());
  }

}

class TypeAdapterFactoryTestClass implements TypeAdapterFactory {
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    return null;
  }
}

class InvalidClass {
  // Invalid class for testing
}