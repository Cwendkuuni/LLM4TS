import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> typeToken = new TypeToken<String>() {};
    assertNull(factory.create(gson, typeToken));
  }

  @Test
  public void testCreate_JsonAdapterAnnotationPresent_ReturnsTypeAdapterInstance() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    JsonAdapter annotation = intTypeToken.getRawType().getAnnotation(JsonAdapter.class);
    TypeAdapter<Integer> intTypeAdapter = new JsonAdapterIntTypeAdapter();
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(intTypeAdapter);
    TypeAdapter<Integer> adapter = factory.create(gson, intTypeToken);
    assertEquals(intTypeAdapter, adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotationValueNotTypeAdapterOrTypeAdapterFactory_ThrowsException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotation annotation = new JsonAdapterAnnotation() {};
    assertNull(factory.create(gson, stringTypeToken));
  }

  @Test
  public void testCreate_JsonAdapterAnnotationValueTypeAdapter_ReturnsTypeAdapterInstance() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotation annotation = new JsonAdapterAnnotation();
    annotation.value(StringAdapter.class);
    TypeAdapter<String> stringAdapter = new StringAdapter();
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(stringAdapter);
    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);
    assertEquals(stringAdapter, adapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotationValueTypeAdapterFactory_ReturnsTypeAdapterInstance() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotation annotation = new JsonAdapterAnnotation();
    annotation.value(StringAdapterFactory.class);
    TypeAdapterFactory factoryInstance = mock(TypeAdapterFactory.class);
    when(factoryInstance.create(gson, stringTypeToken))
        .thenReturn(new StringTypeAdapter());
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(factoryInstance);
    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);
    assertEquals(StringTypeAdapter.class, adapter.getClass());
  }

  @Test
  public void testCreate_JsonAdapterAnnotationValueTypeAdapterFactoryWithNullConstructor_ThrowsException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotation annotation = new JsonAdapterAnnotation();
    annotation.value(StringAdapterFactoryWithNullConstructor.class);
    assertNull(factory.create(gson, stringTypeToken));
  }

  @Test(expected = NullPointerException.class)
  public void testCreate_JsonAdapterAnnotationValueTypeAdapterFactoryWithNullConstructor_ThrowsNullPointerException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotation annotation = new JsonAdapterAnnotation();
    annotation.value(StringAdapterFactoryWithNullConstructor.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenThrow(new NullPointerException());
    factory.create(gson, stringTypeToken);
  }

  @Test(expected = IllegalStateException.class)
  public void testCreate_JsonAdapterAnnotationValueTypeAdapterFactoryWithIllegalConstructor_ThrowsIllegalStateException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapterAnnotation annotation = new JsonAdapterAnnotation();
    annotation.value(IllegalConstructorTypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenThrow(new IllegalStateException());
    factory.create(gson, stringTypeToken);
  }
}

class JsonAdapterAnnotation {
  private Class<?> value;

  @JsonAdapter public Class<?> value() {
    return value;
  }

  @JsonAdapter public JsonAdapterAnnotation value(Class<?> value) {
    this.value = value;
    return this;
  }
}

interface TypeAdapter<T> {}

class StringTypeAdapter implements TypeAdapter<String> {}

class StringAdapterFactory implements TypeAdapterFactory<String> {
  public TypeAdapter<String> create(Gson gson, TypeToken<String> stringTypeToken) {
    return new StringTypeAdapter();
  }
}

class StringAdapter implements TypeAdapter<String> {}

class IllegalConstructorTypeAdapterFactory implements TypeAdapterFactory<String> {
  public TypeAdapter<String> create(Gson gson, TypeToken<String> stringTypeToken) {
    throw new IllegalStateException();
  }
}

class StringAdapterWithNullConstructor implements TypeAdapterFactory<String> {
  public TypeAdapter<String> create(Gson gson, TypeToken<String> stringTypeToken) {
    return null;
  }
}