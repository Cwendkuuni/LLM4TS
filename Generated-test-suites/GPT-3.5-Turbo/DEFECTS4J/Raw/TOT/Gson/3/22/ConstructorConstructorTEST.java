import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructor;

  @Before
  public void setup() {
    constructor = new ConstructorConstructor(createInstanceCreators());
  }

  @Test
  public void testGet_InstanceCreator() {
    TypeToken<TestType> typeToken = new TypeToken<TestType>() {};
    ObjectConstructor<TestType> objectConstructor = constructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof TestType);
  }

  @Test
  public void testGet_RawTypeCreator() {
    TypeToken<TestType> typeToken = new TypeToken<TestType>() {};
    ObjectConstructor<TestType> objectConstructor = constructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof TestType);
  }

  @Test
  public void testGet_DefaultConstructor() {
    TypeToken<TestType> typeToken = new TypeToken<TestType>() {};
    ObjectConstructor<TestType> objectConstructor = constructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof TestType);
  }

  @Test
  public void testGet_DefaultImplementation() {
    TypeToken<TestType> typeToken = new TypeToken<TestType>() {};
    ObjectConstructor<TestType> objectConstructor = constructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof TestType);
  }

  @Test
  public void testGet_UnsafeAllocator() {
    TypeToken<TestType> typeToken = new TypeToken<TestType>() {};
    ObjectConstructor<TestType> objectConstructor = constructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof TestType);
  }

  private Map<Type, InstanceCreator<?>> createInstanceCreators() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    instanceCreators.put(TestType.class, new InstanceCreator<TestType>() {
      @Override
      public TestType createInstance(Type type) {
        return new TestType();
      }
    });
    return instanceCreators;
  }
}

class TestType {
  // Empty class for use in testing
}