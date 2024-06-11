import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Mock
  private Gson gson;
  @Mock
  private ConstructorConstructor constructorConstructor;
  @Mock
  private TypeToken targetType;
  @Mock
  private JsonAdapter annotation;
  private JsonAdapterAnnotationTypeAdapterFactory factory;


  @Test
  public void testCreate_WhenAnnotationPresent_ExpectTypeAdapterReturned() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    targetType = TypeToken.get(String.class);
    annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);

    assertNotNull(typeAdapter);
  }

  @Test
  public void testCreate_WhenAnnotationNotPresent_ExpectNullReturned() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    targetType = TypeToken.get(Integer.class);

    TypeAdapter<Integer> typeAdapter = factory.create(gson, targetType);

    assertNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_WhenNotTypeAdapterNorTypeAdapterFactory_ExpectIllegalArgumentException() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    annotation = new AnnotationNotImplementingTypeAdapter();

    factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  }

  @Test
  public void testGetTypeAdapter_WhenValueIsTypeAdapter_ExpectTypeAdapterReturned() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    annotation = new AnnotationWithValidTypeAdapter();

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

    assertNotNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_WhenValueIsTypeAdapterFactory_ExpectTypeAdapterReturned() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    annotation = new AnnotationWithValidTypeAdapterFactory();

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

    assertNotNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_WhenValueIsNull_ExpectTypeAdapterReturned() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    annotation = new AnnotationWithNullValue();

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

    assertNotNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_WhenValueIsTypeAdapterAndIsNotNull_ExpectTypeAdapterWithNullSafeReturned() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    annotation = new AnnotationWithNotNullTypeAdapter();

    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);

    assertNotNull(typeAdapter);
    assertTrue(typeAdapter.nullSafe());
  }

  private class AnnotationNotImplementingTypeAdapter implements JsonAdapter {
      @Override
      public Class<?> value() {
        return JsonAdapterNotImplementingTypeAdapter.class;
      }
  }

  private class JsonAdapterNotImplementingTypeAdapter {
  }

  private class AnnotationWithValidTypeAdapter implements JsonAdapter {
      @Override
      public Class<?> value() {
        return ValidTypeAdapter.class;
      }
  }

  private class ValidTypeAdapter extends TypeAdapter {
  }

  private class AnnotationWithValidTypeAdapterFactory implements JsonAdapter {
      @Override
      public Class<?> value() {
        return ValidTypeAdapterFactory.class;
      }
  }

  private class ValidTypeAdapterFactory implements TypeAdapterFactory {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        return (TypeAdapter<T>) new ValidTypeAdapter();
      }
  }

  private class AnnotationWithNullValue implements JsonAdapter {
      @Override
      public Class<?> value() {
        return NullTypeAdapter.class;
      }
  }

  private class NullTypeAdapter extends TypeAdapter {
  }

  private class AnnotationWithNotNullTypeAdapter implements JsonAdapter {
      @Override
      public Class<?> value() {
        return TypeAdapterWithNotNull.class;
      }
  }

  private class TypeAdapterWithNotNull extends TypeAdapter {
      @Override
      public TypeAdapter<?> nullSafe() {
        return this;
      }
  }

}