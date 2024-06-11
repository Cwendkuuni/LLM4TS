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
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_NullTypeToken() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(ConstructorConstructor.class));
    assertNull(factory.create(mock(Gson.class), null));
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(ConstructorConstructor.class));
    TypeToken<String> typeToken = new TypeToken<String>() {};
    assertNull(factory.create(mock(Gson.class), typeToken));
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ValueIsTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(ConstructorConstructor.class));
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(StringAdapter.class);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapter<String> result = factory.create(mock(Gson.class), typeToken);
    assertNotNull(result);
    assertNotNull(result.isNull());
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ValueIsTypeAdapterFactory() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(ConstructorConstructor.class));
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(StringAdapterFactory.class);
    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapter<String> result = factory.create(mock(Gson.class), typeToken);
    assertNotNull(result);
    assertNotNull(result.isNull());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_ValueIsNotTypeAdapterOrTypeAdapterFactory() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(ConstructorConstructor.class));
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(new Object());
    TypeToken<String> typeToken = new TypeToken<String>() {};
    factory.create(mock(Gson.class), typeToken);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapter() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(StringAdapter.class);
    TypeAdapter<String> expected = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(expected);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(result);
    assertSame(expected, result);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(annotation.value()).thenReturn(StringAdapterFactory.class);
    TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
    when(factory.create(gson, fieldType)).thenReturn(mock(TypeAdapter.class));
    JsonAdapterAnnotationTypeAdapterFactory factoryUnderTest = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(result);
    assertNotNull(result.isNull());
    verify(factory).create(gson, fieldType);
  }
}