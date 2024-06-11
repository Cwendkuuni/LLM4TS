import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;

  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test(timeout = 1000)
  public void testCreate_ValidJsonAdapter_ReturnsTypeAdapter() {
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);

    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);

    assertNotNull(typeAdapter);
  }

  @Test(timeout = 1000)
  public void testCreate_NullJsonAdapter_ReturnsNull() {
    Gson gson = new Gson();
    TypeToken<Integer> targetType = TypeToken.get(Integer.class);

    TypeAdapter<Integer> typeAdapter = factory.create(gson, targetType);

    assertNull(typeAdapter);
  }

  @Test(timeout = 1000)
  public void testCreate_InvalidJsonAdapter_ThrowsIllegalArgumentException() {
    Gson gson = new Gson();
    TypeToken<Double> targetType = TypeToken.get(Double.class);

    try {
      factory.create(gson, targetType);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.",
          e.getMessage());
    }
  }

  @Test(timeout = 1000)
  public void testGetTypeAdapter_ValidValue_ReturnsTypeAdapter() {
    String validClass = "com.google.gson.internal.bind.TestTypeAdapter";
    JsonAdapter annotation = createJsonAdapter(validClass);
    TypeToken<String> fieldType = TypeToken.get(String.class);

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, null, fieldType, annotation);

    assertNotNull(typeAdapter);
  }

  @Test(timeout = 1000)
  public void testGetTypeAdapter_InvalidValue_ThrowsIllegalArgumentException() {
    String invalidClass = "com.google.gson.internal.bind.TestTypeAdapterFactory";
    JsonAdapter annotation = createJsonAdapter(invalidClass);
    TypeToken<Integer> fieldType = TypeToken.get(Integer.class);

    try {
      factory.getTypeAdapter(constructorConstructor, null, fieldType, annotation);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.",
          e.getMessage());
    }
  }

  @Test(timeout = 1000)
  public void testGetTypeAdapter_ValidTypeAdapterClass_ReturnsTypeAdapter() {
    constructorConstructor.put(TestTypeAdapter.class, new TestTypeAdapter());
    constructorConstructor.put(TestTypeAdapterClass.class, new TestTypeAdapterClass());
    String validClass = "com.google.gson.internal.bind.TestTypeAdapter";
    JsonAdapter annotation = createJsonAdapter(validClass);
    TypeToken<TestTypeAdapter> fieldType = TypeToken.get(TestTypeAdapter.class);

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, null, fieldType, annotation);

    assertNotNull(typeAdapter);
  }

  @Test(timeout = 1000)
  public void testGetTypeAdapter_InvalidTypeAdapterClass_ThrowsJsonSyntaxException()
      throws JsonSyntaxException {
    constructorConstructor.put(TestTypeAdapter.class, new TestTypeAdapter());
    constructorConstructor.put(TestTypeAdapterClass.class, new TestTypeAdapterClass());
    String invalidClass = "com.google.gson.internal.bind.TestTypeAdapterClass";
    JsonAdapter annotation = createJsonAdapter(invalidClass);
    TypeToken<TestTypeAdapterClass> fieldType = TypeToken.get(TestTypeAdapterClass.class);

    try {
      factory.getTypeAdapter(constructorConstructor, null, fieldType, annotation);
      fail("Expected JsonSyntaxException to be thrown");
    } catch (JsonSyntaxException e) {
      assertEquals("java.lang.ClassCastException: com.google.gson.internal.bind.TestTypeAdapterClass cannot be cast to com.google.gson.TypeAdapter",
          e.getMessage());
    }
  }

  @JsonAdapter(TestTypeAdapter.class)
  private static class TestClass {
  }

  private static class TestTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
      return in.nextString();
    }
  }

  private static class TestTypeAdapterClass implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return null;
    }
  }

  private JsonAdapter createJsonAdapter(String value) {
    return new JsonAdapter() {
      @Override
      public Class<?> value() {
        try {
          return Class.forName(value);
        } catch (ClassNotFoundException e) {
          return null;
        }
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return JsonAdapter.class;
      }
    };
  }
}