import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setUp() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreate_TypeAdapter() {
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testCreate_TypeAdapterFactory() {
    Gson gson = new Gson();
    TypeToken<Integer> targetType = TypeToken.get(Integer.class);
    TypeAdapter<Integer> typeAdapter = factory.create(gson, targetType);
    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testCreate_NullAnnotation() {
    Gson gson = new Gson();
    TypeToken<Double> targetType = TypeToken.get(Double.class);
    TypeAdapter<Double> typeAdapter = factory.create(gson, targetType);
    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = TypeToken.get(String.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_ThrowException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Double> fieldType = TypeToken.get(Double.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    try {
      JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
    }
  }
}