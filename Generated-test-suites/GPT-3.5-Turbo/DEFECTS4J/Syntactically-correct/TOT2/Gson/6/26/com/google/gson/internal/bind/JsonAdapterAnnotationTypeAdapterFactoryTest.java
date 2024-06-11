import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private JsonAdapterAnnotationTypeAdapterFactory factory;
  
  @Before
  public void setup() {
    gson = new Gson();
    constructorConstructor = new ConstructorConstructor();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }
  
  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    TypeAdapter<TestClass> typeAdapter = factory.create(gson, TypeToken.get(TestClass.class));
    assertNull(typeAdapter);
  }
  
  @Test
  public void testCreate_WithJsonAdapterAnnotation_ReturnsTypeAdapter() {
    TypeAdapter<TestClass> typeAdapter = factory.create(gson, TypeToken.get(TestClassWithAnnotation.class));
    assertNotNull(typeAdapter);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidJsonAdapterAnnotation_ThrowsIllegalArgumentException() {
    JsonAdapter invalidAnnotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return Object.class; // Not a TypeAdapter or TypeAdapterFactory
      }
      
      @Override
      public Class<?> annotationType() {
        return JsonAdapter.class;
      }
    };
    
    factory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(TestClassWithAnnotation.class), invalidAnnotation);
  }
  
  // Edge case: When @JsonAdapter value is TypeAdapterFactory
  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
    JsonAdapter typeAdapterFactoryAnnotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return TestTypeAdapterFactory.class;
      }
      
      @Override
      public Class<?> annotationType() {
        return JsonAdapter.class;
      }
    };
    
    TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, TypeToken.get(TestClassWithAnnotation.class), typeAdapterFactoryAnnotation);
    
    assertNotNull(typeAdapter);
  }
  
  class TestClass {}
  
  @JsonAdapter(TestTypeAdapter.class)
  class TestClassWithAnnotation {}
  
  static class TestTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }
    
    @Override
    public String read(JsonReader in) throws IOException {
      return in.nextString();
    }
  }
  
  static class TestTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return (TypeAdapter<T>) new TestTypeAdapter();
    }
  }
}