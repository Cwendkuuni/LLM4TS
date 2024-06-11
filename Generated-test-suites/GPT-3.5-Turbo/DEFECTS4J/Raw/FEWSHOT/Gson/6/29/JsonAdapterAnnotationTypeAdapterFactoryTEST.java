package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

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
  public void testCreateTypeAdapter_withAnnotation() {
    TypeToken targetType = TypeToken.get(TestClass.class);
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    when(constructorConstructor.get(TypeToken.get(annotation.value()))).thenReturn(mock(TypeAdapter.class));

    TypeAdapter result = factory.create(gson, targetType);

    assertNotNull(result);
  }

  @Test
  public void testCreateTypeAdapter_withoutAnnotation() {
    TypeToken targetType = TypeToken.get(NoAnnotationClass.class);
    when(gson.getAdapter(targetType)).thenReturn(null);

    TypeAdapter result = factory.create(gson, targetType);

    assertNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_invalidValue() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken fieldType = TypeToken.get(TestClass.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    Class value = String.class;
    when(constructorConstructor.get(TypeToken.get(value)))
        .thenReturn(mock(TypeAdapter.class));

    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterValue() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken fieldType = TypeToken.get(TestClass.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    Class value = TestTypeAdapter.class;
    when(constructorConstructor.get(TypeToken.get(value)))
        .thenReturn(mock(TypeAdapter.class));

    TypeAdapter result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(result);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryValue() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken fieldType = TypeToken.get(TestClass.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    Class value = TestTypeAdapterFactory.class;
    when(constructorConstructor.get(TypeToken.get(value))).thenReturn(mock(TypeAdapterFactory.class));
    when(constructorConstructor
        .get(TypeToken.get(value))
        .construct()
        .create(gson, fieldType)).thenReturn(mock(TypeAdapter.class));

    TypeAdapter result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    assertNotNull(result);
  }

  private static class TestClass {}

  private static class NoAnnotationClass {}

  private static class TestTypeAdapter extends TypeAdapter<TestClass> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, TestClass value) {}

    @Override
    public TestClass read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }

  private static class TestTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
  }
}