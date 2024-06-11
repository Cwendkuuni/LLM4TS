import org.junit.Assert;
import org.junit.Test;
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
    TypeAdapter<Integer> adapter = factory.create(gson, targetType);
    Assert.assertNull(adapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
    TypeAdapter<String> expectedAdapter = constructorConstructor.get(TypeToken.get(typeAdapterClass)).construct();
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    Assert.assertEquals(expectedAdapter, adapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<Number> targetType = new TypeToken<Number>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<TypeAdapterFactory> typeAdapterFactoryClass = TypeAdapterFactory.class;
    TypeAdapterFactory expectedFactory = constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass)).construct();
    TypeAdapter<Number> adapter = factory.create(gson, targetType);
    Assert.assertNotNull(adapter);
    TypeAdapterFactory actualFactory = adapter.getClass().getGenericInterfaces()[0];
    Assert.assertEquals(expectedFactory.getClass(), actualFactory.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_InvalidValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<Boolean> targetType = new TypeToken<Boolean>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<?> invalidValue = String.class;
    TypeAdapter<?> adapter = factory.create(gson, targetType);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
    Class<TypeAdapter<String>> expectedTypeAdapterClass = (Class<TypeAdapter<String>>) typeAdapterClass;
    TypeAdapter<String> expectedAdapter = constructorConstructor.get(TypeToken.get(typeAdapterClass)).construct();
    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    Assert.assertEquals(expectedAdapter, adapter);
    Assert.assertEquals(expectedTypeAdapterClass, adapter.getClass().getGenericSuperclass());
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Number> fieldType = new TypeToken<Number>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    Class<TypeAdapterFactory> typeAdapterFactoryClass = TypeAdapterFactory.class;
    TypeAdapterFactory expectedFactory = constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass)).construct();
    TypeAdapterFactory actualFactory = (TypeAdapterFactory) JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation).getClass().getGenericInterfaces()[0];
    Assert.assertEquals(expectedFactory.getClass(), actualFactory.getClass());
  }

}