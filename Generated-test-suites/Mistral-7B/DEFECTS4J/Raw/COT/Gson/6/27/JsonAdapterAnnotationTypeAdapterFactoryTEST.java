import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @InjectMocks
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;
  @SuppressWarnings("unchecked")
  private final ConstructorConstructor constructorConstructor = Mockito.mock(ConstructorConstructor.class);
  @SuppressWarnings("unchecked")
  private final Gson gson = Mockito.mock(Gson.class);

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    assertNull(jsonAdapterAnnotationTypeAdapterFactory.create(gson, typeToken));
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ReturnsTypeAdapter() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter() {};
    TypeAdapter<String> typeAdapter = Mockito.mock(TypeAdapter.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(Mockito.callRealMethod(typeAdapter));

    when(typeAdapter.nullSafe()).thenReturn(typeAdapter);

    when(gson.create(Mockito.any(TypeTypeToken.class)))
        .thenReturn(typeAdapter);

    TypeToken<String> fieldType = new TypeToken<String>() {};
    TypeAdapter<String> result = jsonAdapterAnnotationTypeAdapterFactory.create(gson, typeToken);

    assertEquals(typeAdapter, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_InvalidValueType_ThrowIllegalArgumentException() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter() {};
    jsonAdapterAnnotation.value() = new Object() {};

    jsonAdapterAnnotationTypeAdapterFactory.create(gson, typeToken);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapterFactory_ReturnsTypeAdapter() {
    TypeToken<List<Integer>> typeToken = new TypeToken<List<Integer>>() {};
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter() {};
    JsonAdapterFactory jsonAdapterFactory = Mockito.mock(JsonAdapterFactory.class);
    TypeAdapter<List<Integer>> result = Mockito.mock(TypeAdapter.class);

    when(constructorConstructor.get(TypeToken.get(JsonAdapterFactory.class)))
        .thenReturn(jsonAdapterFactory);

    when(jsonAdapterFactory.create(Mockito.any(Gson.class), Mockito.any(TypeToken.class)))
        .thenReturn(result);

    when(result.nullSafe()).thenReturn(result);

    TypeAdapter<List<Integer>> expectedResult = jsonAdapterFactory.create(gson, typeToken);

    assertEquals(expectedResult, jsonAdapterAnnotationTypeAdapterFactory.create(gson, typeToken));
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapter_ReturnsTypeAdapter() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    JsonAdapter jsonAdapterAnnotation = new JsonAdapter() {};
    TypeAdapter<Integer> typeAdapter = Mockito.mock(TypeAdapter.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(typeAdapter);

    when(typeAdapter.nullSafe()).thenReturn(typeAdapter);

    TypeAdapter<Integer> expectedResult = typeAdapter;

    assertEquals(expectedResult, jsonAdapterAnnotationTypeAdapterFactory.create(gson, typeToken));
  }
}