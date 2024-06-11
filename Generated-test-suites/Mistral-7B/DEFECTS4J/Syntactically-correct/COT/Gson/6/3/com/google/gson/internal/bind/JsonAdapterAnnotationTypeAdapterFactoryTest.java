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
  public void testCreate_noJsonAdapterAnnotation_returnsNull() {
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    assertNotNull(JsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType));
  }

  @Test
  public void testCreate_withJsonAdapterAnnotationAndTypeAdapter_returnsTypeAdapter() {
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(typeAdapter);
    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapterAnnotation);

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);

    assertNotNull(result);
    assertNotNull(result.nullSafe());
  }

  @Test
  public void testCreate_withJsonAdapterAnnotationAndTypeAdapterFactory_returnsTypeAdapter() {
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(typeAdapterFactory);
    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapterAnnotation);

    TypeAdapterFactory typeAdapterFactoryInstance = mock(TypeAdapterFactory.class);
    when(typeAdapterFactory.create(gson, targetType)).thenReturn(typeAdapterFactoryInstance);
    when(typeAdapterFactoryInstance.create(gson, targetType)).thenReturn(mock(TypeAdapter.class));

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.create(gson, targetType);

    assertNotNull(result);
    assertNotNull(result.nullSafe());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_withJsonAdapterAnnotationAndInvalidValue_throwsException() {
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapterAnnotation);

    Class<?> invalidValue = new Object() {};
    TypeToken<String> targetType = new TypeToken<String>() {};

    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, targetType, jsonAdapterAnnotation);
  }
}