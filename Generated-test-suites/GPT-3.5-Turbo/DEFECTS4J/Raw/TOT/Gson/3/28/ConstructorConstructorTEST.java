import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;
  private Map<Type, InstanceCreator<?>> instanceCreators;

  @Before
  public void setup() {
    instanceCreators = new HashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  @Test
  public void testGet_Success_WithInstanceCreator() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    InstanceCreator<List<String>> instanceCreator = new InstanceCreator<List<String>>() {
      @Override
      public List<String> createInstance(Type type) {
        return new ArrayList<>();
      }
    };
    instanceCreators.put(typeToken.getType(), instanceCreator);

    ObjectConstructor<List<String>> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    List<String> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGet_Success_WithRawTypeCreator() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    InstanceCreator<List<String>> instanceCreator = new InstanceCreator<List<String>>() {
      @Override
      public List<String> createInstance(Type type) {
        return new ArrayList<>();
      }
    };
    instanceCreators.put(typeToken.getRawType(), instanceCreator);

    ObjectConstructor<List<String>> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    List<String> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGet_Success_WithDefaultConstructor() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};

    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    TestClass instance = objectConstructor.construct();
    assertNotNull(instance);
    assertEquals("default", instance.getValue());
  }

  @Test
  public void testGet_Success_WithDefaultImplementationConstructor() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};

    ObjectConstructor<List<String>> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    List<String> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof ArrayList);
  }

  @Test
  public void testGet_Success_WithUnsafeAllocator() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};

    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    TestClass instance = objectConstructor.construct();
    assertNotNull(instance);
    assertNull(instance.getValue());
  }

  @Test(expected = RuntimeException.class)
  public void testGet_Failure_NoArgsConstructorNotFound() {
    TypeToken<TestClassWithoutConstructor> typeToken = new TypeToken<TestClassWithoutConstructor>() {};

    constructorConstructor.get(typeToken);
  }

  @Test(expected = RuntimeException.class)
  public void testGet_Failure_UncheckedExceptionInConstruction() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};

    instanceCreators.put(typeToken.getType(), new InstanceCreator<TestClass>() {
      @Override
      public TestClass createInstance(Type type) {
        throw new RuntimeException("Exception in instance creation");
      }
    });

    constructorConstructor.get(typeToken);
  }

  @Test(expected = JsonIOException.class)
  public void testGet_Failure_InvalidEnumSetType() {
    TypeToken<InvalidEnumSetClass> typeToken = new TypeToken<InvalidEnumSetClass>() {};

    constructorConstructor.get(typeToken);
  }

  @Test(expected = AssertionError.class)
  public void testGet_Failure_IllegalAccessExceptionInConstructor() {
    TypeToken<IllegalAccessTestClass> typeToken = new TypeToken<IllegalAccessTestClass>() {};

    constructorConstructor.get(typeToken);
  }

  private static class TestClass {
    private String value;

    private TestClass() {
      this.value = "default";
    }

    public String getValue() {
      return value;
    }
  }

  private static class TestClassWithoutConstructor {
    private String value;
  }

  private static class InvalidEnumSetClass {
    private EnumSet<String> enumSet;
  }

  private static class IllegalAccessTestClass {
    private IllegalAccessTestClass() {
      // private constructor, should throw IllegalAccessException
    }
  }

}