import static org.junit.Assert.assertNull;
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
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreateNullTargetType() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreateWithoutJsonAdapterAnnotation() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, typeToken));
  }

  @Test
  public void testCreateWithTypeAdapterValue() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    TypeToken<String> stringTypeTokenWithAnnotation = new TypeToken<String>() {}.where(new TypeParameter<JsonAdapter>() {}.annotatedWith(new JsonAdapter(String.class, StringAdapter.class)));
    when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(mock(TypeAdapter.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = factory.create(gson, stringTypeTokenWithAnnotation);
    assertNotNull(typeAdapter);
  }

  @Test
  public void testCreateWithTypeAdapterFactoryValue() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    TypeToken<String> stringTypeTokenWithAnnotation = new TypeToken<String>() {}.where(new TypeParameter<JsonAdapter>() {}.annotatedWith(new JsonAdapter(String.class, StringAdapterFactory.class)));
    when(constructorConstructor.get(TypeToken.get(StringAdapterFactory.class))).thenReturn(mock(TypeAdapterFactory.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = factory.create(gson, stringTypeTokenWithAnnotation);
    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidValue() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, stringTypeToken);
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterValue() {
    Class<StringAdapter> stringAdapterClass = StringAdapter.class;
    when(constructorConstructor.get(TypeToken.get(stringAdapterClass))).thenReturn(mock(TypeAdapter.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}, new JsonAdapter(String.class, StringAdapter.class));
    assertNotNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterFactoryValue() {
    Class<StringAdapterFactory> stringAdapterFactoryClass = StringAdapterFactory.class;
    when(constructorConstructor.get(TypeToken.get(stringAdapterFactoryClass))).thenReturn(mock(TypeAdapterFactory.class));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}, new JsonAdapter(String.class, StringAdapterFactory.class));
    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapterWithInvalidValue() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>() {}, null);
  }
}