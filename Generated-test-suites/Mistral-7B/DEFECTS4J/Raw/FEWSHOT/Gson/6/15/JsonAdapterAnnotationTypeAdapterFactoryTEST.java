package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
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
import org.mockito.MockitoAnnotations;

/**
 * Test cases for {@link JsonAdapterAnnotationTypeAdapterFactory}.
 */
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testNullTypeToken() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = factory.create(gson, null);
    assertNull(adapter);
  }

  @Test
  public void testNoJsonAdapterAnnotation() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(String.class));
    assertNull(adapter);
  }

  @Test
  public void testTypeAdapterValue() {
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(new TestTypeAdapter());

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, TypeToken.get(String.class));
    assertThat(adapter).isInstanceOf(TestTypeAdapter.class);
  }

  @Test
  public void testTypeAdapterFactoryValue() {
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(new TestTypeAdapterFactory());

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, TypeToken.get(String.class));
    assertThat(adapter).isInstanceOf(TestTypeAdapter.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonAdapterValue() {
    when(constructorConstructor.get(TypeToken.get(String.class)))
        .thenThrow(new InstantiationException("TestException"));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, TypeToken.get(String.class));
  }

  @Test
  public void testNullSafe() {
    TypeAdapter<String> stringTypeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(new TestNullSafeTypeAdapter(stringTypeAdapter));

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(String.class));
    assertThat(adapter).isInstanceOf(TestNullSafeTypeAdapter.class);
  }
}

class TestTypeAdapter extends TypeAdapter<String> {}
class TestTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    return new TestTypeAdapter();
  }
}
class TestNullSafeTypeAdapter<T> extends TypeAdapter<T> {
  private final TypeAdapter<T> delegate;

  TestNullSafeTypeAdapter(TypeAdapter<T> delegate) {
    this.delegate = delegate;
  }

  @Override
  public T read(JsonReader jsonReader) throws IOException {
    return delegate.read(jsonReader);
  }

  @Override
  public void write(JsonWriter jsonWriter, T value) throws IOException {
    delegate.write(jsonWriter, value);
  }

  @Override
  public T nullSafeGet(JsonElement jsonElement) {
    return delegate.nullSafeGet(jsonElement);
  }
}