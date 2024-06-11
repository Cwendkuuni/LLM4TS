import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private Gson gson;

  @Before
  public void setUp() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    gson = new Gson();
  }

  @Test
  public void testCreate_Method_ReturnsTypeAdapter() {
    TypeToken<String> targetType = TypeToken.get(String.class);

    TypeAdapter<String> result = factory.create(gson, targetType);

    assertNotNull(result);
  }

  @Test
  public void testCreate_Method_ReturnsNull_IfNoAnnotation() {
    TypeToken<Integer> targetType = TypeToken.get(Integer.class);

    TypeAdapter<Integer> result = factory.create(gson, targetType);

    assertNull(result);
  }

  @Test
  public void testGetTypeAdapter_Method_ReturnsTypeAdapter_IfTypeAdapterClass() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, annotation);

    assertNotNull(result);
  }

  @Test
  public void testGetTypeAdapter_Method_ReturnsTypeAdapter_IfTypeAdapterFactoryClass() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Double> fieldType = TypeToken.get(Double.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, annotation);

    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_Method_ThrowsIllegalArgumentException_IfInvalidAnnotationValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Boolean> fieldType = TypeToken.get(Boolean.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);

    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, annotation);
  }
}