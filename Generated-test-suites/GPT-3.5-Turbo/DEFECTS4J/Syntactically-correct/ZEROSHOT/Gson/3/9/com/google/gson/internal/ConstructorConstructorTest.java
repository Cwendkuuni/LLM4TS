import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {

  @Test
  public void testGet_forInstanceCreator() {
    // Prepare
    TypeToken<String> typeToken = new TypeToken<String>() {};
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(String.class, new TestInstanceCreator<String>());

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals("Hello", objectConstructor.construct());
  }

  @Test
  public void testGet_forRawTypeCreator() {
    // Prepare
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(Number.class, new TestInstanceCreator<Integer>());

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(10, (int) objectConstructor.construct());
  }

  @Test
  public void testGet_forDefaultConstructor() {
    // Prepare
    TypeToken<ArrayList> typeToken = new TypeToken<ArrayList>() {};
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList> objectConstructor = constructorConstructor.get(typeToken);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(ArrayList.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testGet_forDefaultImplementationConstructor() {
    // Prepare
    TypeToken<LinkedHashSet> typeToken = new TypeToken<LinkedHashSet>() {};
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<LinkedHashSet> objectConstructor = constructorConstructor.get(typeToken);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(LinkedHashSet.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testGet_forUnsafeAllocator() {
    // Prepare
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(Integer.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultConstructor_withValidConstructor() {
    // Prepare
    Class<TestClass> rawType = TestClass.class;

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultConstructor_withInvalidConstructor() {
    // Prepare
    Class<Integer> rawType = Integer.class;

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);

    // Verify
    assertNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor_forSortedSet() {
    // Prepare
    TypeToken<SortedSet<Integer>> typeToken = new TypeToken<SortedSet<Integer>>() {};

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<SortedSet<Integer>> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(),
            SortedSet.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(TreeSet.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_forEnumSet() {
    // Prepare
    TypeToken<EnumSet<TestEnum>> typeToken = new TypeToken<EnumSet<TestEnum>>() {};

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<EnumSet<TestEnum>> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(),
            EnumSet.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(EnumSet.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_forSet() {
    // Prepare
    TypeToken<Set<Integer>> typeToken = new TypeToken<Set<Integer>>() {};

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Set<Integer>> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(),
            Set.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(LinkedHashSet.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_forQueue() {
    // Prepare
    TypeToken<Queue<Integer>> typeToken = new TypeToken<Queue<Integer>>() {};

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Queue<Integer>> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(),
            Queue.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(LinkedList.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_forCollection() {
    // Prepare
    TypeToken<Collection<Integer>> typeToken = new TypeToken<Collection<Integer>>() {};

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Collection<Integer>> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(),
            Collection.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(ArrayList.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_forSortedMap() {
    // Prepare
    TypeToken<SortedMap<Integer, String>> typeToken = new TypeToken<SortedMap<Integer, String>>() {};

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<SortedMap<Integer, String>> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(),
            SortedMap.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(TreeMap.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_forMap_withParameterizedType() {
    // Prepare
    TypeToken<Map<Integer, String>> typeToken = new TypeToken<Map<Integer, String>>() {};
    Type type = typeToken.getType();

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Map<Integer, String>> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(type,
            Map.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(LinkedHashMap.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_forMap_withStringType() {
    // Prepare
    TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};
    Type type = typeToken.getType();

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Map<String, String>> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(type,
            Map.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(LinkedTreeMap.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructor_withInvalidType() {
    // Prepare
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    Type type = typeToken.getType();

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Integer> objectConstructor =
        constructorConstructor.newDefaultImplementationConstructor(type, Integer.class);

    // Verify
    assertNull(objectConstructor);
  }

  @Test
  public void testNewUnsafeAllocator_withValidType() {
    // Prepare
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Integer> objectConstructor =
        constructorConstructor.newUnsafeAllocator(typeToken.getType(), Integer.class);

    // Verify
    assertNotNull(objectConstructor);
    assertEquals(Integer.class, objectConstructor.construct().getClass());
  }

  @Test(expected = RuntimeException.class)
  public void testNewUnsafeAllocator_withInvalidType() {
    // Prepare
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};

    // Test
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<TestClass> objectConstructor =
        constructorConstructor.newUnsafeAllocator(typeToken.getType(), TestClass.class);

    // Verify
    assertNotNull(objectConstructor);
    objectConstructor.construct();
  }

  class TestInstanceCreator<T> implements InstanceCreator<T> {
    @Override
    public T createInstance(Type type) {
      if (type.equals(String.class)) {
        return (T) "Hello";
      } else if (type.equals(Integer.class)) {
        return (T) Integer.valueOf(10);
      } else {
        return null;
      }
    }
  }

  enum TestEnum {
    RED, GREEN, BLUE
  }

  class TestClass {
    public TestClass() {
    }
  }
}