import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  
  @Before
  public void setUp() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }
  
  @Test
  public void testCreate_ShouldReturnNull_WhenAnnotationNotPresent() {
    Gson gson = new Gson();
    TypeToken<Integer> targetType = TypeToken.get(Integer.class);
    
    TypeAdapter<Integer> adapter = factory.create(gson, targetType);
    
    assertNull(adapter);
  }
  
  @Test
  public void testCreate_ShouldReturnNull_WhenAnnotationPresentButInvalidValue() {
    Gson gson = new Gson();
    TypeToken<TestClass1> targetType = TypeToken.get(TestClass1.class);
    
    TypeAdapter<TestClass1> adapter = factory.create(gson, targetType);
    
    assertNull(adapter);
  }
  
  @Test
  public void testCreate_ShouldReturnTypeAdapter_WhenAnnotationValueIsTypeAdapter() {
    Gson gson = new Gson();
    TypeToken<TestClass2> targetType = TypeToken.get(TestClass2.class);
    
    TypeAdapter<TestClass2> adapter = factory.create(gson, targetType);
    
    assertNotNull(adapter);
    assertTrue(adapter instanceof TestTypeAdapter);
  }
  
  @Test
  public void testCreate_ShouldReturnTypeAdapter_WhenAnnotationValueIsTypeAdapterFactory() {
    Gson gson = new Gson();
    TypeToken<TestClass3> targetType = TypeToken.get(TestClass3.class);
    
    TypeAdapter<TestClass3> adapter = factory.create(gson, targetType);
    
    assertNotNull(adapter);
    assertTrue(adapter instanceof TestTypeAdapterFactory.TestTypeAdapter);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreate_ShouldThrowException_WhenAnnotationValueIsNotValid() {
    Gson gson = new Gson();
    TypeToken<TestClass4> targetType = TypeToken.get(TestClass4.class);
    
    factory.create(gson, targetType);
  }
  
  public static class TestClass1 {
    // no annotation
  }
  
  @JsonAdapter(TestTypeAdapter.class)
  public static class TestClass2 {
    // with TypeAdapter annotation
  }
  
  @JsonAdapter(TestTypeAdapterFactory.class)
  public static class TestClass3 {
    // with TypeAdapterFactory annotation
  }
  
  @JsonAdapter(TestClass5.class)
  public static class TestClass4 {
    // with invalid annotation
  }
  
  public static class TestTypeAdapter extends TypeAdapter<TestClass2> {
    // test type adapter
  }
  
  public static class TestTypeAdapterFactory implements TypeAdapterFactory {
    
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return null;
    }
    
    public static class TestTypeAdapter<T> extends TypeAdapter<T> {
      // test type adapter factory
    }
  }
  
  public static class TestClass5 {
    // invalid annotation value
  }
}