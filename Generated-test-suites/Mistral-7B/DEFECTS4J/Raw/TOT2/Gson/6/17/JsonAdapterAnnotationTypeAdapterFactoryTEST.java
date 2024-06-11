import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  JsonAdapterAnnotationTypeAdapterFactory factory;
  ConstructorConstructor constructorConstructor;
  Gson gson;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() {
    constructorConstructor = new ConstructorConstructor();
    gson = new Gson();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreate_ValidTypeToken() {
    assertNotNull(factory.create(gson, new TypeToken<String>() {}));
  }

  @Test
  public void testCreate_NullTypeToken() {
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreate_NullGson() {
    assertNull(factory.create(null, new TypeToken<String>() {}));
  }

  @Test
  public void testGetTypeAdapter_ValidValue() {
    TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
    JsonAdapter annotation = new JsonAdapter(TypeAdapter.class);
    TypeAdapter<Integer> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValue() {
    TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
    JsonAdapter annotation = new JsonAdapter("InvalidClass");
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_NullAnnotation() {
    TypeAdapter<Integer> adapter = factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<Integer>() {}, null);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetTypeAdapter_TypeAdapterCreationFailed() {
    TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
    JsonAdapter annotation = new JsonAdapter(TypeAdapterFactory.class);
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test
  public void testGetTypeAdapter_CustomTypeAdapterFactory() {
    TypeToken<JsonElement> fieldType = new TypeToken<JsonElement>() {};
    MyCustomTypeAdapterFactory factory = new MyCustomTypeAdapterFactory();
    JsonAdapter annotation = new JsonAdapter(factory.getClass());
    TypeAdapter<JsonElement> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(adapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryWithDependencies() {
    TypeToken<MyCustomModel> fieldType = new TypeToken<MyCustomModel>() {};
    MyCustomTypeAdapterFactory factory = new MyCustomTypeAdapterFactory();
    JsonAdapter annotation = new JsonAdapter(factory.getClass());
    TypeAdapter<MyCustomModel> adapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(adapter);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetTypeAdapter_TypeAdapterFactoryWithNullGson() {
    TypeToken<MyCustomModel> fieldType = new TypeToken<MyCustomModel>() {};
    MyCustomTypeAdapterFactory factory = new MyCustomTypeAdapterFactory();
    JsonAdapter annotation = new JsonAdapter(factory.getClass());
    factory.getTypeAdapter(null, gson, fieldType, annotation);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryWithNullConstructorConstructor() {
    TypeToken<MyCustomModel> fieldType = new TypeToken<MyCustomModel>() {};
    MyCustomTypeAdapterFactory factory = new MyCustomTypeAdapterFactory();
    JsonAdapter annotation = new JsonAdapter(factory.getClass());
    factory.getTypeAdapter(constructorConstructor, null, fieldType, annotation);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryWithNullAnnotation() {
    TypeToken<MyCustomModel> fieldType = new TypeToken<MyCustomModel>() {};
    MyCustomTypeAdapterFactory factory = new MyCustomTypeAdapterFactory();
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, null);
  }

  @Test(expected = NullPointerException.class)
  public void testGetTypeAdapter_TypeAdapterFactoryWithNullFieldType() {
    MyCustomTypeAdapterFactory factory = new MyCustomTypeAdapterFactory();
    JsonAdapter annotation = new JsonAdapter(factory.getClass());
    factory.getTypeAdapter(constructorConstructor, gson, null, annotation);
  }

  @Test(expected = NullPointerException.class)
  public void testGetTypeAdapter_TypeAdapterFactoryWithNullAnnotation() {
    MyCustomTypeAdapterFactory factory = new MyCustomTypeAdapterFactory();
    factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<MyCustomModel>() {}, null);
  }
}