import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = new ConstructorConstructor();
    gson = new Gson();
  }

  // Test create method with no JsonAdapter annotation
  @Test
  public void testCreateWithNoJsonAdapterAnnotation() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    TypeAdapter<Integer> result = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    assertNull(result);
  }

  // Test getTypeAdapter method with TypeAdapter value
  @Test
  public void testGetTypeAdapterWithTypeAdapterValue() {
    Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(TypeAdapter.class);

    TypeAdapter<String> expected = new StubStringTypeAdapter();
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, annotation);
    assertSame(expected, result);
  }

  // Test getTypeAdapter method with TypeAdapterFactory value
  @Test
  public void testGetTypeAdapterWithTypeAdapterFactoryValue() {
    Class<MyNumberTypeAdapterFactory> typeAdapterFactoryClass = MyNumberTypeAdapterFactory.class;
    TypeToken<Number> fieldType = new TypeToken<Number>() {};
    JsonAdapter annotation = new JsonAdapter(MyNumberTypeAdapterFactory.class);

    TypeAdapterFactory myNumberTypeAdapterFactory = new MyNumberTypeAdapterFactory();
    TypeAdapter<?> expected = myNumberTypeAdapterFactory.create(gson, fieldType);
    TypeAdapter<?> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, annotation);
    assertSame(expected, result);
  }

  // Test getTypeAdapter method with invalid JsonAdapter value
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapterWithInvalidJsonAdapterValue() {
    Class<String> value = String.class;
    TypeToken<Number> fieldType = new TypeToken<Number>() {};
    JsonAdapter annotation = new JsonAdapter(value);

    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, annotation);
  }

  // Test create method with null constructor constructor
  @Test(expected = NullPointerException.class)
  public void testCreateWithNullConstructorConstructor() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
    factory.create(gson, targetType);
  }

  // Test create method with null gson
  @Test(expected = NullPointerException.class)
  public void testCreateWithNullGson() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(null, targetType);
  }

  // Test create method with null fieldType
  @Test(expected = NullPointerException.class)
  public void testCreateWithNullFieldType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, null);
  }

  // Test create method with null JsonAdapter annotation
  @Test(expected = NullPointerException.class)
  public void testCreateWithNullJsonAdapterAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, targetType, null);
  }

  static class StubStringTypeAdapter extends TypeAdapter<String> {
    @Override public String read(JsonElement json) throws JsonParseException {
      return "stub";
    }

    @Override public void write(JsonWriter out, String value) throws JsonWriteException {
      out.value(value);
    }
  }

  static class MyNumberTypeAdapterFactory implements TypeAdapterFactory {
    @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return new MyNumberTypeAdapter<T>(gson, targetType);
    }
  }

  static class MyNumberTypeAdapter<T> extends TypeAdapter<T> {
    private final Gson gson;
    private final TypeToken<T> targetType;

    public MyNumberTypeAdapter(Gson gson, TypeToken<T> targetType) {
      this.gson = gson;
      this.targetType = targetType;
    }

    @Override public T read(JsonElement json) throws JsonParseException {
      return null;
    }

    @Override public void write(JsonWriter out, T value) throws JsonWriteException {
      out.value(value.toString());
    }
  }
}