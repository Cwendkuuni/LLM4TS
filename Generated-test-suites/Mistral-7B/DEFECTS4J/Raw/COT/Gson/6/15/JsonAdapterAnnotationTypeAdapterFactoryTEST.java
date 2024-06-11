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
  public void setUp() {
    constructorConstructor = new ConstructorConstructor();
    gson = new Gson();
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_InstanceOfTypeAdapter() {
    int input = 1;
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter(TypeAdapter.class);
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    TypeAdapter<Integer> expected = new MyIntegerTypeAdapter();
    TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    Assert.assertEquals(expected.getClass(), typeAdapter.getClass());
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_InstanceOfTypeAdapterFactory() {
    int input = 1;
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter(MyJsonAdapterFactory.class);
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    TypeAdapter<Integer> expected = new MyIntegerTypeAdapter();
    TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    Assert.assertEquals(expected, typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_ValueNotTypeAdapterOrFactory() {
    int input = 1;
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter(String.class);
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
  }

  @Test
  public void testgetTypeAdapter_TypeAdapter() {
    TypeAdapter<Integer> input = new MyIntegerTypeAdapter();
    Class<? extends TypeAdapter<?>> expectedTypeAdapterClass = MyIntegerTypeAdapter.class;
    Class<?> value = TypeAdapter.class;
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, new TypeToken<Integer>() {},
        new JsonAdapter(TypeAdapter.class));
    Assert.assertEquals(expectedTypeAdapterClass, typeAdapter.getClass().getComponentType());
    Assert.assertEquals(input.getClass(), typeAdapter.getClass());
  }

  @Test
  public void testgetTypeAdapter_TypeAdapterFactory() {
    TypeAdapter<Integer> input = new MyIntegerTypeAdapter();
    Class<? extends TypeAdapterFactory<?>> expectedTypeAdapterFactoryClass = MyJsonAdapterFactory.class;
    Class<?> value = MyJsonAdapterFactory.class;
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(
        constructorConstructor, gson, new TypeToken<Integer>() {},
        new JsonAdapter(MyJsonAdapterFactory.class));
    Assert.assertEquals(expectedTypeAdapterFactoryClass, typeAdapter.getClass().getComponentType().getComponentType());
    Assert.assertEquals(input, typeAdapter.get(gson, new TypeToken<Integer>() {}).create(gson, new TypeToken<Integer>() {}));
  }

  static class MyIntegerTypeAdapter extends TypeAdapter<Integer> {}
  static class MyJsonAdapterFactory implements TypeAdapterFactory {}

}