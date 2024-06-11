import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
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

  @Test
  public void testCreate_NoAnnotation() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testCreate_TypeAdapterValue() {
    JsonAdapter annotation = new JsonAdapter(TypeAdapter.class);
    TypeToken<String> targetType = new TypeToken<String>() {};

    TypeAdapter<String> expectedTypeAdapter = new TypeAdapter<String>() {};
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    Assert.assertEquals(expectedTypeAdapter, typeAdapter);
  }

  @Test
  public void testCreate_TypeAdapterFactoryValue() {
    JsonAdapter annotation = new JsonAdapter(MyTypeAdapterFactory.class);
    TypeToken<String> targetType = new TypeToken<String>() {};

    TypeAdapter<String> expectedTypeAdapter = new MyTypeAdapterFactory().create(gson, targetType);
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);

    Assert.assertEquals(expectedTypeAdapter, typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidValueType() {
    JsonAdapter annotation = new JsonAdapter(String.class);
    TypeToken<String> targetType = new TypeToken<String>() {};

    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass() {
    JsonAdapter annotation = new JsonAdapter(TypeAdapter.class);
    TypeToken<String> fieldType = new TypeToken<String>() {};

    TypeAdapter<?> expectedTypeAdapter = new TypeAdapter<String>() {};
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, annotation);

    Assert.assertEquals(expectedTypeAdapter, typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryClass() {
    JsonAdapter annotation = new JsonAdapter(TypeAdapterFactory.class);
    TypeToken<String> fieldType = new TypeToken<String>() {};

    TypeAdapterFactory typeAdapterFactory = new MyTypeAdapterFactory();
    TypeAdapter<?> expectedTypeAdapter = typeAdapterFactory.create(gson, fieldType);
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, fieldType, annotation);

    Assert.assertEquals(expectedTypeAdapter, typeAdapter);
  }
}

class MyTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    return new MyTypeAdapter<T>(gson);
  }
}

class MyTypeAdapter<T> implements TypeAdapter<T> {
  private final Gson gson;

  public MyTypeAdapter(Gson gson) {
    this.gson = gson;
  }

  @Override
  public T read(JsonReader jsonReader) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void write(JsonWriter jsonWriter, T value) throws IOException {
    throw new UnsupportedOperationException();
  }
}