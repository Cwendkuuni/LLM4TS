import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_NullGson() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Assert.assertNull(jsonAdapterAnnotationTypeAdapterFactory.create(null, new TypeToken<Object>() {}));
  }

  @Test
  public void testCreate_NullTypeToken() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Assert.assertNull(jsonAdapterAnnotationTypeAdapterFactory.create(gson, null));
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    Assert.assertNull(jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType));
  }

  @Test
  public void testCreate_JsonAdapterValueIsTypeAdapter() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTestClass> targetType = new TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTestClass>() {};
    JsonAdapterAnnotationTypeAdapterFactoryTestClass jsonAdapter = new JsonAdapterAnnotationTypeAdapterFactoryTestClass();
    TypeAdapterFactory typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor) {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
        return jsonAdapter;
      }
    };
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Assert.assertNotNull(jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType));
    Assert.assertSame(jsonAdapter, jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType).getDelegate());
  }

  @Test
  public void testCreate_JsonAdapterValueIsTypeAdapterFactory() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTestClass> targetType = new TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTestClass>() {};
    JsonAdapterAnnotationTypeAdapterFactoryTestClassJsonAdapterFactory jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactoryTestClassJsonAdapterFactory();
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Assert.assertNotNull(jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType));
    TypeAdapter<JsonAdapterAnnotationTypeAdapterFactoryTestClass> typeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
    Assert.assertSame(jsonAdapterFactory, typeAdapter.getDelegate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidJsonAdapterValue() {
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    Class<?> value = annotation.value();
    if (TypeAdapter.class.isAssignableFrom(value)) {
      Assert.fail("Should not reach this point.");
    }
    jsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);
  }
}

class JsonAdapterAnnotationTypeAdapterFactoryTestClass {
  // Your test class implementation here
}

class JsonAdapterAnnotationTypeAdapterFactoryTestClassJsonAdapterFactory implements TypeAdapterFactory {
  // Your test class implementation here
}