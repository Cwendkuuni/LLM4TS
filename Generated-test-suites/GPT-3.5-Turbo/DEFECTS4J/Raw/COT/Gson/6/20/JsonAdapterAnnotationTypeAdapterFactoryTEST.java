
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;

  @Test
  public void testCreate_NullAnnotation_ReturnsNull() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};

    TypeAdapter<String> result = factory.create(gson, targetType);

    assertNull(result);
  }

  @Test
  public void testCreate_AnnotationIsTypeAdapter_ReturnsTypeAdapter() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<TestTypeAdapter> targetType = new TypeToken<TestTypeAdapter>() {};

    TypeAdapter<TestTypeAdapter> result = factory.create(gson, targetType);

    assertEquals(TestTypeAdapter.class, result.getClass());
  }

  @Test
  public void testCreate_AnnotationIsTypeAdapterFactory_ReturnsTypeAdapter() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<TestTypeAdapterFactory> targetType = new TypeToken<TestTypeAdapterFactory>() {};

    TypeAdapter<TestTypeAdapterFactory> result = factory.create(gson, targetType);

    assertEquals(TestTypeAdapterFactory.class, result.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_AnnotationIsNeitherTypeAdapterNorTypeAdapterFactory_ThrowsIllegalArgumentException() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<InvalidAdapter> targetType = new TypeToken<InvalidAdapter>() {};

    factory.create(gson, targetType);
  }
}

class TestTypeAdapter extends TypeAdapter<String> {
  @Override
  public void write(com.google.gson.stream.JsonWriter out, String value) {}

  @Override
  public String read(com.google.gson.stream.JsonReader in) {
    return null;
  }
}

class TestTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    return null;
  }
}

class InvalidAdapter {}
