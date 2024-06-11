import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

  private Gson gson;
  private ConstructorConstructor constructorConstructor;

  @Before
  public void setup() {
    gson = mock(Gson.class);
    constructorConstructor = mock(ConstructorConstructor.class);
  }

  @Test
  public void testNullTypeAdapterFactory() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testNullTargetType() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testNoJsonAdapterAnnotation() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, targetType));
  }

  @Test
  public void testJsonAdapterValueIsTypeAdapter() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(String.class);
    when(gson.getDelegate()).thenReturn(gson);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(mock(TypeAdapter.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertNotNull(adapter);
  }

  @Test
  public void testJsonAdapterValueIsTypeAdapterFactory() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(MyTypeAdapterFactory.class);
    when(gson.getDelegate()).thenReturn(gson);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(mock(MyTypeAdapterFactory.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertNotNull(adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterValueIsNotTypeAdapterOrTypeAdapterFactory() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(String.class.getDeclaredClass());
    when(gson.getDelegate()).thenReturn(gson);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  }

  @Test
  public void testJsonAdapterValueIsTypeAdapterWithNullSafe() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(String.class);
    when(gson.getDelegate()).thenReturn(gson);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(mock(TypeAdapter.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertNotNull(adapter.nullSafe());
  }

  @Test
  public void testJsonAdapterValueIsTypeAdapterFactoryWithNullSafe() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(MyTypeAdapterFactory.class);
    when(gson.getDelegate()).thenReturn(gson);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(mock(MyTypeAdapterFactory.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertNotNull(adapter.nullSafe());
  }

  static class MyTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
  }
}