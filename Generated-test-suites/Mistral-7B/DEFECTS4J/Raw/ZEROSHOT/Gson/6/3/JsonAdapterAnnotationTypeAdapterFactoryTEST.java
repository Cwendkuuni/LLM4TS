import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testNullInput() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testNoJsonAdapterAnnotation() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, new TypeToken<Integer>() {}));
  }

  @Test
  public void testTypeAdapter() {
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter(MyTypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(MyTypeAdapter.class))).thenReturn(new MyTypeAdapter());

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<Integer> typeAdapter = factory.create(gson, new TypeToken<Integer>() {});
    assertNotNull(typeAdapter);
  }

  @Test
  public void testTypeAdapterFactory() {
    MyTypeAdapterFactory typeAdapterFactory = mock(MyTypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(MyTypeAdapterFactory.class))).thenReturn(typeAdapterFactory);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<Integer> typeAdapter = factory.create(gson, new TypeToken<Integer>() {});
    assertNotNull(typeAdapter);

    MyTypeAdapter myTypeAdapter = mock(MyTypeAdapter.class);
    when(typeAdapterFactory.create(gson, new TypeToken<Integer>() {})).thenReturn(myTypeAdapter);

    assertSame(myTypeAdapter, typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonAdapterValue() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, new TypeToken<Integer>() {}, new Object() {});
  }

  private static class MyTypeAdapter implements TypeAdapter<Integer> {}
  private static class MyTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
  }
}