import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor();
    gson = new Gson();
  }

  @Test
  public void testCreateNormalCase() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);
    assertNotNull(adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullGson() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(null, new TypeToken<Integer>() {});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullTargetType() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, null);
  }

  @Test
  public void testCreateJsonAdapterNull() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, stringTypeToken);
    assertNotNull(factory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateInvalidJsonAdapter() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    JsonAdapter annotation = new JsonAdapter() {
      // Invalid annotation implementation
    };
    factory.create(gson, stringTypeToken, annotation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateInvalidTypeAdapter() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = new TypeAdapter<Object>() {};
    factory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateInvalidTypeAdapterFactory() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapterFactory<?> factoryInstance = new TypeAdapterFactory<Object>() {};
    factory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, null, factoryInstance);
  }

  @Test
  public void testCreateJsonAdapterWithValueTypeAdapter() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapter<?>> value() {
        return TypeAdapter.class;
      }
    };
    TypeAdapter<String> adapter = (TypeAdapter<String>) factory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, annotation);
    assertNotNull(adapter);
  }

  @Test
  public void testCreateJsonAdapterWithValueTypeAdapterFactory() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapterFactory<?>> value() {
        return TypeAdapterFactory.class;
      }
    };
    TypeAdapter<String> adapter = (TypeAdapter<String>) factory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, annotation).create(gson, stringTypeToken);
    assertNotNull(adapter);
  }
}