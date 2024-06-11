import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreate_NoAnnotation_ReturnsNull() {
    TypeToken targetType = mock(TypeToken.class);
    when(targetType.getRawType()).thenReturn(Object.class);

    TypeAdapter<?> result = factory.create(gson, targetType);

    assertEquals(null, result);
  }

  @Test
  public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
    TypeToken targetType = mock(TypeToken.class);
    Class<Object> rawType = Object.class;
    when(targetType.getRawType()).thenReturn(rawType);

    JsonAdapter jsonAdapter = mock(JsonAdapter.class);
    when(jsonAdapter.value()).thenReturn(TypeAdapter.class);
    when(rawType.getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapter);

    TypeAdapter<Object> typeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(eq(TypeToken.get(TypeAdapter.class)))).thenReturn(mock(
        TypeAdapter.class));
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);

    TypeAdapter<?> result = factory.create(gson, targetType);

    assertEquals(typeAdapter, result);
    verify(constructorConstructor).get(TypeToken.get(TypeAdapter.class));
    verify(typeAdapter).construct();
  }

  @Test
  public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapterFromFactory() {
    TypeToken targetType = mock(TypeToken.class);
    Class<Object> rawType = Object.class;
    when(targetType.getRawType()).thenReturn(rawType);

    JsonAdapter jsonAdapter = mock(JsonAdapter.class);
    when(jsonAdapter.value()).thenReturn(TypeAdapterFactory.class);
    when(rawType.getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapter);

    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(eq(TypeToken.get(TypeAdapterFactory.class)))).thenReturn(mock(
        TypeAdapterFactory.class));
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(
        typeAdapterFactory);
    when(typeAdapterFactory.create(eq(gson), eq(targetType))).thenReturn(mock(TypeAdapter.class));

    TypeAdapter<?> result = factory.create(gson, targetType);

    verify(constructorConstructor).get(TypeToken.get(TypeAdapterFactory.class));
    verify(typeAdapterFactory).create(eq(gson), eq(targetType));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
    TypeToken targetType = mock(TypeToken.class);
    Class<Object> rawType = Object.class;
    when(targetType.getRawType()).thenReturn(rawType);

    JsonAdapter jsonAdapter = mock(JsonAdapter.class);
    when(jsonAdapter.value()).thenReturn(String.class);
    when(rawType.getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapter);

    factory.create(gson, targetType);
  }
}