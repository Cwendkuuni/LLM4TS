import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

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
    assertNotNull(annotation);
    TypeAdapter<String> expectedTypeAdapter = new StringAdapter(); // Assuming StringAdapter is a TypeAdapter for String.
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ReturnsTypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<List<Integer>> targetType = new TypeToken<List<Integer>>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);
    TypeAdapterFactory expectedTypeAdapterFactory = new ArrayListTypeAdapterFactory(); // Assuming ArrayListTypeAdapterFactory is a TypeAdapterFactory for List<Integer>.
    TypeAdapterFactory actualTypeAdapterFactory = factory.create(gson, targetType);
    assertEquals(expectedTypeAdapterFactory, actualTypeAdapterFactory);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_ValueIsNotTypeAdapterOrTypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);
    Class<?> value = annotation.value();
    assertNotSame(TypeAdapter.class, value.getClass());
    assertNotSame(TypeAdapterFactory.class, value.getClass());
    factory.create(gson, targetType, value);
  }
}