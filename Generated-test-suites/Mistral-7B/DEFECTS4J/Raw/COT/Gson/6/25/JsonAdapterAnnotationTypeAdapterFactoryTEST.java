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
  public void testCreate_NoJsonAdapterAnnotation() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapter() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<StringToIntTypeAdapter> typeAdapterClass = StringToIntTypeAdapter.class;
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    Assert.assertNotNull(typeAdapter);
    Assert.assertTrue(TypeAdapter.class.isInstance(typeAdapter));
    Assert.assertEquals(StringToIntTypeAdapter.class, typeAdapter.getClass());
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapterFactory() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<StringToIntArrayTypeAdapterFactory> typeAdapterFactoryClass = StringToIntArrayTypeAdapterFactory.class;
    TypeAdapterFactory typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    Assert.assertNotNull(typeAdapterFactory);
    Assert.assertTrue(TypeAdapterFactory.class.isInstance(typeAdapterFactory));
    Assert.assertEquals(StringToIntArrayTypeAdapterFactory.class, typeAdapterFactory.getClass());
    TypeAdapter<String[]> typeAdapter = typeAdapterFactory.create(gson, targetType);
    Assert.assertNotNull(typeAdapter);
    Assert.assertTrue(TypeAdapter.class.isInstance(typeAdapter));
    Assert.assertEquals(StringToIntArrayTypeAdapter.class, typeAdapter.getClass().getComponentType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_InvalidValue() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<?> invalidValue = Object.class;
    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapter() {
    Class<StringToIntTypeAdapter> typeAdapterClass = StringToIntTypeAdapter.class;
    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}, new JsonAdapter() {});
    Assert.assertNotNull(typeAdapter);
    Assert.assertTrue(TypeAdapter.class.isInstance(typeAdapter));
    Assert.assertEquals(StringToIntTypeAdapter.class, typeAdapter.getClass());
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactory() {
    Class<StringToIntArrayTypeAdapterFactory> typeAdapterFactoryClass = StringToIntArrayTypeAdapterFactory.class;
    TypeAdapterFactory typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}, new JsonAdapter() {});
    Assert.assertNotNull(typeAdapterFactory);
    Assert.assertTrue(TypeAdapterFactory.class.isInstance(typeAdapterFactory));
    Assert.assertEquals(StringToIntArrayTypeAdapterFactory.class, typeAdapterFactory.getClass());
    TypeToken<String[]> targetType = new TypeToken<String[]>() {};
    TypeAdapter<String[]> typeAdapter = typeAdapterFactory.create(gson, targetType);
    Assert.assertNotNull(typeAdapter);
    Assert.assertTrue(TypeAdapter.class.isInstance(typeAdapter));
    Assert.assertEquals(StringToIntArrayTypeAdapter.class, typeAdapter.getClass().getComponentType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValueTypeAdapter() {
    Class<Object> invalidValue = Object.class;
    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}, new JsonAdapter() {});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValueTypeAdapterFactory() {
    Class<Object> invalidValue = Object.class;
    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}, new JsonAdapter() {});
  }
}

class StringToIntTypeAdapter implements TypeAdapter<String> {
  @Override
  public String read(java.lang.reflect.Type typeOfT, JsonElement jsonElement) throws JsonParseException {
    return jsonElement.getAsString();
  }

  @Override
  public void write(JsonWriter jsonWriter, String value) throws JsonIOException {
    jsonWriter.value(value);
  }
}

class StringToIntArrayTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    return new StringToIntArrayTypeAdapter();
  }
}

class StringToIntArrayTypeAdapter implements TypeAdapter<String[]> {
  @Override
  public void write(JsonWriter jsonWriter, String[] value) throws JsonIOException {
    jsonWriter.beginArray();
    for (String s : value) {
      jsonWriter.value(s);
    }
    jsonWriter.endArray();
  }

  @Override
  public String[] read(JsonElement jsonElement) throws JsonParseException {
    return jsonElement.getAsJsonArray().map(jsonElement1 -> jsonElement1.getAsString()).toArray(new String[0]);
  }
}