import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  @Test
  public void testConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNotNull(factory);
  }
  
  @Test
  public void testCreate() {
    Gson gson = new Gson();
    TypeToken<T> targetType = new TypeToken<T>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<T> typeAdapter = factory.create(gson, targetType);
    assertNull(typeAdapter);
  }
  
  @Test
  public void testGetTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<T> fieldType = new TypeToken<T>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<T> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNull(typeAdapter);
  }
}