import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setUp() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
  }

  @Test
  public void testCreate_WithNoAnnotation_ReturnsNull() {
    TypeAdapter<?> result = factory.create(new Gson(), TypeToken.get(Object.class));

    assertNull(result);
  }

  @Test
  public void testCreate_WithTypeAdapterAnnotation_ReturnsTypeAdapterInstance() {
    TypeAdapter<?> result = factory.create(new Gson(), TypeToken.get(MyType.class));

    assertNotNull(result);
    assertTrue(result instanceof MyTypeAdapter);
  }

  @Test
  public void testCreate_WithTypeAdapterFactoryAnnotation_ReturnsTypeAdapterInstance() {
    TypeAdapter<?> result = factory.create(new Gson(), TypeToken.get(MyType2.class));

    assertNotNull(result);
    assertTrue(result instanceof MyTypeAdapterFactory.MyTypeAdapterFactoryAdapter);
  }

  @Test
  public void testCreate_WithInvalidAnnotation_ThrowsIllegalArgumentException() {
    try {
      factory.create(new Gson(), TypeToken.get(MyType3.class));
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Success
    }
  }

  @Test
  public void testGetAdapter_WithTypeAdapterClass_ReturnsNonNullInstance() {
    TypeAdapter<?> result = factory.getTypeAdapter(new ConstructorConstructor(), new Gson(),
        TypeToken.get(MyType.class), new JsonAdapter() {
          @Override
          public Class<?> value() {
            return MyTypeAdapter.class;
          }

          @Override
          public Class<? extends java.lang.annotation.Annotation> annotationType() {
            return JsonAdapter.class;
          }
        });

    assertNotNull(result);
    assertTrue(result instanceof MyTypeAdapter);
  }

  @Test
  public void testGetAdapter_WithTypeAdapterFactoryClass_ReturnsNonNullInstance() {
    TypeAdapter<?> result = factory.getTypeAdapter(new ConstructorConstructor(), new Gson(),
        TypeToken.get(MyType2.class), new JsonAdapter() {
          @Override
          public Class<?> value() {
            return MyTypeAdapterFactory.class;
          }

          @Override
          public Class<? extends java.lang.annotation.Annotation> annotationType() {
            return JsonAdapter.class;
          }
        });

    assertNotNull(result);
    assertTrue(result instanceof MyTypeAdapterFactory.MyTypeAdapterFactoryAdapter);
  }

  @Test
  public void testGetAdapter_WithInvalidClass_ThrowsIllegalArgumentException() {
    try {
      factory.getTypeAdapter(new ConstructorConstructor(), new Gson(), TypeToken.get(MyType3.class),
          new JsonAdapter() {
            @Override
            public Class<?> value() {
              return MyInvalidType.class;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
              return JsonAdapter.class;
            }
          });
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Success
    }
  }

  @Test
  public void testGetAdapter_ReturnsTypeAdapterWithNullSafe() {
    TypeAdapter<?> result = factory.getTypeAdapter(new ConstructorConstructor(), new Gson(),
        TypeToken.get(MyType.class), new JsonAdapter() {
          @Override
          public Class<?> value() {
            return MyTypeAdapter.class;
          }

          @Override
          public Class<? extends java.lang.annotation.Annotation> annotationType() {
            return JsonAdapter.class;
          }
        });

    assertNotNull(result);
    assertTrue(result.nullSafe());
  }

  private static class MyType {}

  private static class MyTypeAdapter extends TypeAdapter<MyType> {
    @Override
    public void write(JsonWriter out, MyType value) throws IOException {}

    @Override
    public MyType read(JsonReader in) throws IOException {
        return null;
    }
  }

  private static class MyType2 {}

  private static class MyTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }

    public static class MyTypeAdapterFactoryAdapter extends TypeAdapter<MyType2> {
      @Override
      public void write(JsonWriter out, MyType2 value) throws IOException {}

      @Override
      public MyType2 read(JsonReader in) throws IOException {
          return null;
      }
    }
  }

  private static class MyType3 {}

  private static class MyInvalidType {}

}