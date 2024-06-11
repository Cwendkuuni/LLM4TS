import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    gson = new Gson();
  }

  @Test
  public void testCreate() {
    TypeAdapter<String> adapter = factory.create(gson, TypeToken.get(String.class));
    assertNull(adapter);
  }

  @Test
  public void testCreateWithAnnotation() {
    TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
    assertNull(adapter);
  }

  @Test
  public void testGetAdapterWithTypeAdapterClass() {
    TypeAdapter<String> adapter = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class),
            createJsonAdapter(StringAdapter.class));
    assertNull(adapter);
  }

  @Test
  public void testGetAdapterWithTypeAdapterFactoryClass() {
    TypeAdapter<String> adapter = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class),
            createJsonAdapter(FactoryAdapter.class));
    assertNull(adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAdapterWithInvalidAnnotationValue() {
    JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, TypeToken.get(String.class),
            createJsonAdapter(String.class));
  }

  private <T> JsonAdapter createJsonAdapter(Class<T> value) {
    return new JsonAdapter() {
      @Override
      public Class<?> value() {
        return value;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return JsonAdapter.class;
      }
    };
  }

  private static class TestClass {
    // Test class for reflective lookup
  }

  private static class StringAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
    }

    @Override
    public String read(JsonReader in) throws IOException {
      return null;
    }
  }

  private static class FactoryAdapter implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
  }

}