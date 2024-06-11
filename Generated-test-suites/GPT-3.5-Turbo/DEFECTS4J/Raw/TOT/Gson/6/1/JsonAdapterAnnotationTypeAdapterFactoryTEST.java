import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_noJsonAdapterAnnotation_returnsNull() {
    Gson gson = new Gson();
    TypeToken<ClassWithoutJsonAdapter> targetType = TypeToken.get(ClassWithoutJsonAdapter.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<ClassWithoutJsonAdapter> typeAdapter = factory.create(gson, targetType);
    
    assertNull(typeAdapter);
  }
  
  @Test
  public void testCreate_TypeAdapterAnnotation_returnsTypeAdapterInstance() {
    Gson gson = new Gson();
    TypeToken<ClassWithTypeAdapterAnnotation> targetType = TypeToken.get(ClassWithTypeAdapterAnnotation.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<ClassWithTypeAdapterAnnotation> typeAdapter = factory.create(gson, targetType);
    
    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapter);
  }
  
  @Test
  public void testCreate_TypeAdapterFactoryAnnotation_returnsTypeAdapterInstance() {
    Gson gson = new Gson();
    TypeToken<ClassWithTypeAdapterFactoryAnnotation> targetType = TypeToken.get(ClassWithTypeAdapterFactoryAnnotation.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<ClassWithTypeAdapterFactoryAnnotation> typeAdapter = factory.create(gson, targetType);
    
    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapterFactory);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidJsonAdapterAnnotation_throwsIllegalArgumentException() {
    Gson gson = new Gson();
    TypeToken<ClassWithInvalidJsonAdapterAnnotation> targetType = TypeToken.get(ClassWithInvalidJsonAdapterAnnotation.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<ClassWithInvalidJsonAdapterAnnotation> typeAdapter = factory.create(gson, targetType);
  }
  
  @Test
  public void testGetTypeAdapter_TypeAdapterClass_returnsTypeAdapterInstance() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<ClassWithTypeAdapterAnnotation> fieldType = TypeToken.get(ClassWithTypeAdapterAnnotation.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapter);
  }
  
  @Test
  public void testGetTypeAdapter_TypeAdapterFactory_returnsTypeAdapterInstance() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<ClassWithTypeAdapterFactoryAnnotation> fieldType = TypeToken.get(ClassWithTypeAdapterFactoryAnnotation.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    
    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof TestTypeAdapterFactory);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_InvalidValueAnnotation_throwsIllegalArgumentException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<ClassWithInvalidJsonAdapterAnnotation> fieldType = TypeToken.get(ClassWithInvalidJsonAdapterAnnotation.class);
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    
    TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }
}

class ClassWithoutJsonAdapter {}

@JsonAdapter(TestTypeAdapter.class)
class ClassWithTypeAdapterAnnotation {}

@JsonAdapter(TestTypeAdapterFactory.class)
class ClassWithTypeAdapterFactoryAnnotation {}

@JsonAdapter(InvalidTypeAdapter.class)
class ClassWithInvalidJsonAdapterAnnotation {}

class TestTypeAdapter extends TypeAdapter<ClassWithTypeAdapterAnnotation > {
  @Override
  public void write(JsonWriter out, ClassWithTypeAdapterAnnotation value) throws IOException {
    // implementation specific to the test
  }
  
  @Override
  public ClassWithTypeAdapterAnnotation read(JsonReader in) throws IOException {
    // implementation specific to the test
    return null;
  }
}

class TestTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    // implementation specific to the test
    return null;
  }
}

class InvalidTypeAdapter {
  public InvalidTypeAdapter() {
    // implementation specific to the test
  }
}