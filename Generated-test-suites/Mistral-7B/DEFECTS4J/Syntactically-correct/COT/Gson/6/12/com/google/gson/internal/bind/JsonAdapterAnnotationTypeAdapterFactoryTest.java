import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

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
  public void testCreate_JsonAdapterAnnotationWithTypeAdapterValue_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<StringToIntTypeAdapter> typeAdapterClass = StringToIntTypeAdapter.class;
    TypeAdapter<String> expectedTypeAdapter = constructorConstructor.get(TypeToken.get(StringToIntTypeAdapter.class)).construct();
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationWithTypeAdapterFactoryValue_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    StringToIntTypeAdapterFactory adapterFactory = new StringToIntTypeAdapterFactory();
    TypeAdapter<String> expectedTypeAdapter = constructorConstructor.get(TypeToken.get(StringToIntTypeAdapter.class)).construct();
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotationWithInvalidValue_ThrowsIllegalArgumentException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<Double> targetType = new TypeToken<Double>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Integer number = 42;
    TypeAdapter<?> value = number;
    try {
      factory.create(gson, targetType);
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
      throw e;
    }
  }

  @Test
  public void testCreate_JsonAdapterAnnotationWithNullValue_ReturnsNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<?> value = null;
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
    assertNull(actualTypeAdapter);
  }

  @Test
  public void testGetTypeAdapter_NoJsonAdapterAnnotation_ReturnsNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Integer> fieldType = new TypeToken<Integer>() {};
    assertNull(JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, null));
  }

  @Test
  public void testGetTypeAdapter_JsonAdapterAnnotationWithTypeAdapterValue_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    Class<StringToIntTypeAdapter> typeAdapterClass = StringToIntTypeAdapter.class;
    TypeAdapter<String> expectedTypeAdapter = constructorConstructor.get(TypeToken.get(StringToIntTypeAdapter.class)).construct();
    TypeAdapter<String> actualTypeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test
  public void testGetTypeAdapter_JsonAdapterAnnotationWithTypeAdapterFactoryValue_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    StringToIntTypeAdapterFactory adapterFactory = new StringToIntTypeAdapterFactory();
    TypeAdapter<String> expectedTypeAdapter = constructorConstructor.get(TypeToken.get(StringToIntTypeAdapter.class)).construct();
    TypeAdapter<String> actualTypeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertEquals(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_JsonAdapterAnnotationWithInvalidValue_ThrowsIllegalArgumentException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Double> fieldType = new TypeToken<Double>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    Integer number = 42;
    TypeAdapter<?> value = number;
    try {
      JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
      throw e;
    }
  }
}

class StringToIntTypeAdapter implements TypeAdapter<String> {
  @Override
  public String read(JsonReader in) throws IOException {
    return null;
  }

  @Override
  public int read(JsonReader in) throws IOException {
    return Integer.parseInt(in.nextString());
  }

  @Override
  public void write(JsonWriter out, String value) throws IOException {
    out.value(value);
  }

  @Override
  public void write(JsonWriter out, int value) throws IOException {
    out.value(value);
  }
}

class StringToIntTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    return new StringToIntTypeAdapter();
  }
}