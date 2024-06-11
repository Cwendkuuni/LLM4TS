import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);

    assertNull(adapter); // No @JsonAdapter annotation for String class
  }

  @Test
  public void testGetTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Double> doubleTypeToken = TypeToken.get(Double.class);
    JsonAdapter annotation = Double.class.getAnnotation(JsonAdapter.class);

    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson,
        doubleTypeToken, annotation);

    assertNotNull(typeAdapter); // TypeAdapter must not be null
  }

  @Test
  public void testGetTypeAdapterWithInvalidValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Integer> integerTypeToken = TypeToken.get(Integer.class);
    JsonAdapter invalidAnnotation = Integer.class.getAnnotation(JsonAdapter.class);

    try {
      JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, 
          integerTypeToken, invalidAnnotation);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected IllegalArgumentException
    }
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Float> floatTypeToken = TypeToken.get(Float.class);
    JsonAdapter adapterAnnotation = Float.class.getAnnotation(JsonAdapter.class);

    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, 
        gson, floatTypeToken, adapterAnnotation);

    assertNotNull(typeAdapter); // TypeAdapter must not be null
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterFactoryValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Long> longTypeToken = TypeToken.get(Long.class);
    JsonAdapter factoryAnnotation = Long.class.getAnnotation(JsonAdapter.class);

    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, 
        gson, longTypeToken, factoryAnnotation);

    assertNotNull(typeAdapter); // TypeAdapter must not be null
  }
}