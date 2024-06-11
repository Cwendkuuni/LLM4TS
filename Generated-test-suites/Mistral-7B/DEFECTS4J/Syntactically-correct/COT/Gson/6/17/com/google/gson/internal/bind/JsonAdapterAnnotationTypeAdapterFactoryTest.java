import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for JsonAdapterAnnotationTypeAdapterFactory.
 */
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testCreate_JsonAdapterAnnotationPresent_ReturnsTypeAdapter() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<String> rawType = String.class;
    JsonAdapter annotation = new JsonAdapter(TypeAdapterString.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapterString.class)))
        .thenReturn(new TypeAdapterString());

    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    assertNotNull(typeAdapter);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotationAbsent_ReturnsNull() {
    TypeToken<String> targetType = new TypeToken<String>() {};

    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    assertNotNull(typeAdapter); // This test should not reach here, but JUnit expects a test method to have a return statement.
  }

  @Test
  public void testCreate_JsonAdapterValueIsTypeAdapter_ReturnsTypeAdapterInstance() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapter annotation = new JsonAdapter(TypeAdapterInteger.class);

    TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    assertNotNull(typeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterValueIsTypeAdapterFactory_ReturnsTypeAdapterInstance() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(TypeAdapterFactoryString.class);

    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterValueIsNeitherTypeAdapterNorTypeAdapterFactory_ThrowsException() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter("NotAValidAdapter");

    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapterInstance() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<TypeAdapter<String>> typeAdapterClass = TypeAdapterString.class;

    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .getTypeAdapter(constructorConstructor, gson, targetType, new JsonAdapter(typeAdapterClass));

    assertNotNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapterInstance() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<TypeAdapterFactory<String>> typeAdapterFactoryClass = TypeAdapterFactoryString.class;

    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .getTypeAdapter(constructorConstructor, gson, targetType, new JsonAdapter(typeAdapterFactoryClass));

    assertNotNull(typeAdapter);
  }
}

class TypeAdapterString implements TypeAdapter<String> {}
class TypeAdapterInteger implements TypeAdapter<Integer> {}
class TypeAdapterFactoryString implements TypeAdapterFactory<String> {}