import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConstructorConstructorTest {

  private final ConstructorConstructor constructorConstructor;

  public ConstructorConstructorTest() {
    constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
  }

  @Test
  public void testGet_withInstanceCreator_shouldReturnInstanceCreator() {
    InstanceCreator<Integer> instanceCreator = new InstanceCreator<Integer>() {
      @Override
      public Integer createInstance(Type type) {
        return null;
      }
    };

    constructorConstructor.instanceCreators.put(Integer.class, instanceCreator);

    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(TypeToken.get(Integer.class));

    assertNotNull(objectConstructor);
    assertTrue(objectConstructor instanceof InstanceCreator);
  }

  @Test
  public void testGet_withRawTypeInstanceCreator_shouldReturnInstanceCreator() {
    InstanceCreator<Number> instanceCreator = new InstanceCreator<Number>() {
      @Override
      public Number createInstance(Type type) {
        return null;
      }
    };

    constructorConstructor.instanceCreators.put(Number.class, instanceCreator);

    ObjectConstructor<Number> objectConstructor = constructorConstructor.get(TypeToken.get(Number.class));

    assertNotNull(objectConstructor);
    assertTrue(objectConstructor instanceof InstanceCreator);
  }

  @Test
  public void testGet_withDefaultConstructor_shouldReturnDefaultConstructor() {
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(TypeToken.get(String.class));

    assertNotNull(objectConstructor);

    String str = objectConstructor.construct();

    assertNotNull(str);
  }

  @Test
  public void testGet_withDefaultImplementationConstructor_shouldReturnDefaultImplementationConstructor() {
    ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.get(TypeToken.get(Set.class));

    assertNotNull(objectConstructor);

    Set<String> set = objectConstructor.construct();

    assertNotNull(set);
    assertTrue(set instanceof LinkedHashSet);
  }

  @Test
  public void testGet_withEnumSetConstructor_shouldReturnEnumSetConstructor() {
    ObjectConstructor<EnumSet<TestEnum>> objectConstructor = constructorConstructor.get(TypeToken.get(EnumSet.class));

    assertNotNull(objectConstructor);

    EnumSet<TestEnum> enumSet = objectConstructor.construct();

    assertNotNull(enumSet);
    assertTrue(enumSet.isEmpty());
  }

  @Test(expected = RuntimeException.class)
  public void testGet_withUnsafeAllocator_shouldThrowRuntimeException() {
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(TypeToken.get(TestClass.class));

    assertNotNull(objectConstructor);

    objectConstructor.construct();
    fail("Expected RuntimeException to be thrown");
  }

  private enum TestEnum {
    VALUE1,
    VALUE2,
    VALUE3
  }

  private static class TestClass {
    private String value;

    public TestClass() {
      this.value = "default";
    }

    public TestClass(String value) {
      this.value = value;
    }
  }
}