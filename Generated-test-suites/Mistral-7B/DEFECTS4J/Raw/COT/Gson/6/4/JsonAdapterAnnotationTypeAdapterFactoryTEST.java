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

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    assertNull(new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor).create(gson, targetType));
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ReturnsTypeAdapter() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<String> stringType = String.class;
    JsonAdapter annotation = new JsonAdapter(String.class, String.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(mock(TypeAdapter.class));

    TypeAdapter<String> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_ValueIsNotTypeAdapterOrTypeAdapterFactory() {
    TypeToken<Object> targetType = new TypeToken<Object>() {};
    JsonAdapter annotation = new JsonAdapter(Object.class, "NonTypeAdapterOrTypeAdapterFactory");

    new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor).create(gson, targetType);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ValueIsTypeAdapter() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    Class<TypeAdapter<Integer>> typeAdapterClass = TypeAdapter.class;

    TypeAdapter<Integer> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    assertNotNull(typeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ValueIsTypeAdapterFactory() {
    TypeToken<Number> targetType = new TypeToken<Number>() {};
    Class<TypeAdapterFactory<Number>> typeAdapterFactoryClass = TypeAdapterFactory.class;

    TypeAdapterFactory<Number> typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass))).thenReturn(typeAdapterFactory);

    TypeAdapter<Number> typeAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
        .create(gson, targetType);
    assertNotNull(typeAdapter);
  }
}