import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_ReturnsTypeAdapter() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(String.class, StringAdapter.class);
    when(constructorConstructor.get(TypeToken.get(StringAdapter.class)))
        .thenReturn(new StringAdapter());

    TypeAdapter<String> result = factory.create(gson, targetType);
    assertNotNull(result);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationAbsent_ReturnsNull() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};

    TypeAdapter<Integer> result = factory.create(gson, targetType);
    assertNull(result);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass_ReturnsTypeAdapterInstance() {
    TypeToken<String> fieldType = new TypeToken<String>() {};
    StringAdapter adapter = new StringAdapter();

    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, Mockito.mock(JsonAdapter.class));
    assertNotNull(result);
    assertSame(adapter, result);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsTypeAdapterInstance() {
    TypeToken<String> fieldType = new TypeToken<String>() {};
    TypeAdapterFactory adapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(adapterFactory);

    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, Mockito.mock(JsonAdapter.class));
    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidJsonAdapterValue_ThrowsException() {
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(String[].class, StringAdapter.class);

    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
}

class StringAdapter extends TypeAdapter<String> {
  // Empty implementation for testing purposes.
}