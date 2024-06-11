import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private final ConstructorConstructor constructorConstructor = new ConstructorConstructor();

  @Test
  public void testCreate_ReturnsNullWhenNoAnnotationPresent() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);
    
    TypeAdapter<String> result = factory.create(gson, targetType);
    
    assertNull(result);
  }
  
  @Test
  public void testCreate_ReturnsCorrectTypeAdapter_ForTypeAdapterAnnotation() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    Gson gson = new Gson();
    TypeToken<TestClassWithTypeAdapter> targetType = TypeToken.get(TestClassWithTypeAdapter.class);
    
    TypeAdapter<TestClassWithTypeAdapter> result = factory.create(gson, targetType);
    
    assertNotNull(result);
    assertTrue(result instanceof TestAdapter);
  }
  
  @Test
  public void testCreate_ReturnsCorrectTypeAdapter_ForTypeAdapterFactoryAnnotation() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    Gson gson = new Gson();
    TypeToken<TestClassWithTypeAdapterFactory> targetType = TypeToken.get(TestClassWithTypeAdapterFactory.class);
    
    TypeAdapter<TestClassWithTypeAdapterFactory> result = factory.create(gson, targetType);
    
    assertNotNull(result);
    assertTrue(result instanceof TestAdapterFactory);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreate_ThrowsException_ForInvalidAnnotationValue() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    Gson gson = new Gson();
    TypeToken<TestClassWithInvalidAnnotation> targetType = TypeToken.get(TestClassWithInvalidAnnotation.class);
    
    factory.create(gson, targetType);
  }
  
  @Test
  public void testGetTypeAdapter_ReturnsNullSafeTypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    Gson gson = new Gson();
    TypeToken<TestClassWithTypeAdapter> fieldType = TypeToken.get(TestClassWithTypeAdapter.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(result);
    assertTrue(result instanceof TestAdapter);
    assertTrue(result.isLenient());
  }
  
  @Test
  public void testGetTypeAdapter_ReturnsTypeAdapterForTypeAdapterValue() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    Gson gson = new Gson();
    TypeToken<TestClassWithTypeAdapterValue> fieldType = TypeToken.get(TestClassWithTypeAdapterValue.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(result);
    assertTrue(result instanceof TestAdapter);
  }
  
  @Test
  public void testGetTypeAdapter_ReturnsTypeAdapterForTypeAdapterFactoryValue() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    Gson gson = new Gson();
    TypeToken<TestClassWithTypeAdapterFactoryValue> fieldType = TypeToken.get(TestClassWithTypeAdapterFactoryValue.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(result);
    assertTrue(result instanceof TestAdapterFactory);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_ThrowsExceptionForInvalidAnnotationValue() {
    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    
    Gson gson = new Gson();
    TypeToken<TestClassWithInvalidAnnotationValue> fieldType = TypeToken.get(TestClassWithInvalidAnnotationValue.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
}

@JsonAdapter(TestAdapter.class)
class TestClassWithTypeAdapter {
  private String value;
  
  public TestClassWithTypeAdapter(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}

class TestAdapter extends TypeAdapter<TestClassWithTypeAdapter> {
  @Override
  public void write(JsonWriter out, TestClassWithTypeAdapter value) throws IOException {
    out.beginObject();
    out.name("value").value(value.getValue());
    out.endObject();
  }
  
  @Override
  public TestClassWithTypeAdapter read(JsonReader in) throws IOException {
    in.beginObject();
    String value = null;
    while (in.hasNext()) {
      String name = in.nextName();
      if (name.equals("value")) {
        value = in.nextString();
      } else {
        in.skipValue();
      }
    }
    in.endObject();
    return new TestClassWithTypeAdapter(value);
  }
}

@JsonAdapter(TestAdapterFactory.class)
class TestClassWithTypeAdapterFactory {
  private String value;
  
  public TestClassWithTypeAdapterFactory(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}

class TestAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    return (TypeAdapter<T>) new TestAdapter();
  }
}

@JsonAdapter(value = TestAdapterFactory.class)
class TestClassWithInvalidAnnotation {
  private String value;
  
  public TestClassWithInvalidAnnotation(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}

@JsonAdapter(TestAdapter.class)
class TestClassWithTypeAdapterValue {
  private String value;
  
  public TestClassWithTypeAdapterValue(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}

class TestClassWithTypeAdapterFactoryValue {
  private String value;
  
  public TestClassWithTypeAdapterFactoryValue(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}

class TestAdapterFactoryValue implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    return (TypeAdapter<T>) new TestAdapter();
  }
}

@JsonAdapter(TestAdapterFactoryValue.class)
class TestClassWithInvalidAnnotationValue {
  private String value;
  
  public TestClassWithInvalidAnnotationValue(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}