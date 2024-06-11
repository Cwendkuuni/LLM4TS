import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Map;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.LinkedTreeMap;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;
  private TypeToken<String> stringToken;
  private TypeToken<Integer> integerToken;
  private TypeToken<Map<String, Integer>> mapToken;

  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(null);
    stringToken = new TypeToken<String>() {};
    integerToken = new TypeToken<Integer>() {};
    mapToken = new TypeToken<Map<String, Integer>>() {};
  }

  @Test
  public void testGet_InstanceCreatorExists_ReturnInstanceCreatorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedTreeMap<>();
    instanceCreators.put(stringToken.getType(), new StringInstanceCreator());
    constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<String> result = constructorConstructor.get(stringToken);

    assertNotNull(result);
    assertTrue(result instanceof StringInstanceCreator);
  }

  @Test
  public void testGet_RawTypeInstanceCreatorExists_ReturnRawTypeInstanceCreatorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedTreeMap<>();
    instanceCreators.put(Number.class, new NumberInstanceCreator());
    constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Double> result = constructorConstructor.get(new TypeToken<Double>() {});

    assertNotNull(result);
    assertTrue(result instanceof NumberInstanceCreator);
  }

  @Test
  public void testGet_DefaultConstructorExists_ReturnDefaultConstructor() {
    ObjectConstructor<String> result = constructorConstructor.get(stringToken);

    assertNotNull(result);
    assertTrue(result.construct() instanceof String);
  }

  @Test
  public void testGet_DefaultImplementationConstructorExists_ReturnDefaultImplementationConstructor() {
    ObjectConstructor<Map<String, Integer>> result = constructorConstructor.get(mapToken);

    assertNotNull(result);
    assertTrue(result.construct() instanceof LinkedTreeMap);
  }

  @Test
  public void testGet_NoConstructorExists_ThrowRuntimeException() {
    try {
      ObjectConstructor<Type> result = constructorConstructor.get(new TypeToken<Type>() {});
      fail("Expected RuntimeException to be thrown");
    } catch (RuntimeException e) {
      assertEquals("Failed to invoke <init> with no args", e.getMessage());
      assertNotNull(e.getCause());
      assertTrue(e.getCause() instanceof IllegalAccessException);
    }
  }

  @Test
  public void testGet_UnsafeAllocatorExists_ReturnUnsafeAllocatorConstructor() {
    UnsafeAllocatorInstanceCreator instanceCreator = new UnsafeAllocatorInstanceCreator();
    constructorConstructor = new ConstructorConstructor(null);
    
    ObjectConstructor<UnsafeAllocator> result = constructorConstructor.get(new TypeToken<UnsafeAllocator>() {});

    assertNotNull(result);
    assertTrue(result.construct() instanceof UnsafeAllocator);
  }

  private static class StringInstanceCreator implements InstanceCreator<String> {
    @Override
    public String createInstance(Type type) {
      return "test";
    }
  }

  private static class NumberInstanceCreator implements InstanceCreator<Number> {
    @Override
    public Number createInstance(Type type) {
      return 10;
    }
  }

  private static class UnsafeAllocatorInstanceCreator implements InstanceCreator<UnsafeAllocator> {
    @Override
    public UnsafeAllocator createInstance(Type type) {
      return new UnsafeAllocator();
    }
  }
}