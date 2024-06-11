
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructorConstructor;

  @Before
  public void setUp() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  @Test
  public void testGet_withTypeToken_returnsObjectConstructor() {
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
    ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testGet_withTypeToken_returnsCorrectObjectConstructor() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    ObjectConstructor<List<String>> objectConstructor = constructorConstructor.get(typeToken);
    assertEquals(ArrayList.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testGet_withInstanceCreator_returnsObjectConstructor() {
    Type type = new TypeToken<Set<String>>() {}.getType();
    InstanceCreator<Set<String>> instanceCreator = new InstanceCreator<Set<String>>() {
      @Override
      public Set<String> createInstance(Type type) {
        return new TreeSet<>();
      }
    };
    constructorConstructor.instanceCreators.put(type, instanceCreator);

    TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {};
    ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testGet_withInstanceCreator_returnsCorrectObjectConstructor() {
    Type type = new TypeToken<Queue<Integer>>() {}.getType();
    InstanceCreator<Queue<Integer>> instanceCreator = new InstanceCreator<Queue<Integer>>() {
      @Override
      public Queue<Integer> createInstance(Type type) {
        return new LinkedList<>();
      }
    };
    constructorConstructor.instanceCreators.put(type, instanceCreator);

    TypeToken<Queue<Integer>> typeToken = new TypeToken<Queue<Integer>>() {};
    ObjectConstructor<Queue<Integer>> objectConstructor = constructorConstructor.get(typeToken);
    assertEquals(LinkedList.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testGet_withRawType_returnsObjectConstructor() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testGet_withRawType_returnsCorrectObjectConstructor() {
    TypeToken<Double> typeToken = new TypeToken<Double>() {};
    ObjectConstructor<Double> objectConstructor = constructorConstructor.get(typeToken);
    assertNull(objectConstructor);
  }

  @Test
  public void testNewDefaultConstructor_withExistingConstructor_returnsObjectConstructor() {
    Class<TestObject> rawType = TestObject.class;
    ObjectConstructor<TestObject> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultConstructor_withExistingConstructor_returnsCorrectObjectConstructor() {
    Class<TestObject> rawType = TestObject.class;
    ObjectConstructor<TestObject> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultConstructor_withNonExistingConstructor_returnsNull() {
    Class<TestInterface> rawType = TestInterface.class;
    ObjectConstructor<TestInterface> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
    assertNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withList_returnsObjectConstructor() {
    Type type = new TypeToken<List<String>>() {}.getType();
    Class<List<String>> rawType = (Class<List<String>>) type;
    ObjectConstructor<List<String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withList_returnsCorrectObjectConstructor() {
    Type type = new TypeToken<List<String>>() {}.getType();
    Class<List<String>> rawType = (Class<List<String>>) type;
    ObjectConstructor<List<String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertEquals(ArrayList.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_withEnumSet_returnsObjectConstructor() {
    Type type = new TypeToken<EnumSet<TestEnum>>() {}.getType();
    Class<EnumSet<TestEnum>> rawType = (Class<EnumSet<TestEnum>>) type;
    ObjectConstructor<EnumSet<TestEnum>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
  }

  @Test(expected = JsonIOException.class)
  public void testNewDefaultImplementationConstructor_withInvalidEnumSetType_throwsException() {
    Type type = new TypeToken<EnumSet<Integer>>() {}.getType();
    Class<EnumSet<Integer>> rawType = (Class<EnumSet<Integer>>) type;
    constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withSet_returnsObjectConstructor() {
    Type type = new TypeToken<Set<String>>() {}.getType();
    Class<Set<String>> rawType = (Class<Set<String>>) type;
    ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withSet_returnsCorrectObjectConstructor() {
    Type type = new TypeToken<Set<String>>() {}.getType();
    Class<Set<String>> rawType = (Class<Set<String>>) type;
    ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertEquals(LinkedHashSet.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_withQueue_returnsObjectConstructor() {
    Type type = new TypeToken<Queue<Integer>>() {}.getType();
    Class<Queue<Integer>> rawType = (Class<Queue<Integer>>) type;
    ObjectConstructor<Queue<Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withQueue_returnsCorrectObjectConstructor() {
    Type type = new TypeToken<Queue<Integer>>() {}.getType();
    Class<Queue<Integer>> rawType = (Class<Queue<Integer>>) type;
    ObjectConstructor<Queue<Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertEquals(LinkedList.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_withInvalidType_returnsNull() {
    Type type = new TypeToken<String>() {}.getType();
    Class<String> rawType = (Class<String>) type;
    ObjectConstructor<String> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withMap_returnsObjectConstructor() {
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    Class<Map<String, Integer>> rawType = (Class<Map<String, Integer>>) type;
    ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withMapAndSortedMap_returnsObjectConstructor() {
    Type type = new TypeToken<SortedMap<String, Integer>>() {}.getType();
    Class<SortedMap<String, Integer>> rawType = (Class<SortedMap<String, Integer>>) type;
    ObjectConstructor<SortedMap<String, Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withMapAndStringValueType_returnsObjectConstructor() {
    Type type = new TypeToken<Map<String, String>>() {}.getType();
    Class<Map<String, String>> rawType = (Class<Map<String, String>>) type;
    ObjectConstructor<Map<String, String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_withMap_returnsCorrectObjectConstructor() {
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    Class<Map<String, Integer>> rawType = (Class<Map<String, Integer>>) type;
    ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertEquals(LinkedHashMap.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_withInvalidMapType_returnsNull() {
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    Class<Map<String, Integer>> rawType = (Class<Map<String, Integer>>) type;
    ObjectConstructor<String> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNull(objectConstructor);
  }

  @Test
  public void testNewUnsafeAllocator_withValidType_returnsObjectConstructor() {
    TypeToken<TestAbstractClass> typeToken = new TypeToken<TestAbstractClass>() {};
    ObjectConstructor<TestAbstractClass> objectConstructor = constructorConstructor.newUnsafeAllocator(typeToken.getType(), typeToken.getRawType());
    assertNotNull(objectConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testNewUnsafeAllocator_withInvalidType_throwsException() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    constructorConstructor.newUnsafeAllocator(typeToken.getType(), typeToken.getRawType());
  }
  
  private class TestObject {
    
  }
  
  private interface TestInterface {
    
  }
  
  private enum TestEnum {
    VALUE1, VALUE2, VALUE3
  }
}
