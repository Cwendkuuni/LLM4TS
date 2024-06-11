import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
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

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;

public class ConstructorConstructorTest {
  private ConstructorConstructor constructorConstructor;

  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(null);
  }

  @Test
  public void testGet_WithInstanceCreator_ShouldReturnMatchingObjectConstructor() {
    // Setup
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    final InstanceCreator<Integer> instanceCreator = new InstanceCreator<Integer>() {
      @Override
      public Integer createInstance(Type type) {
        return new Integer(42);
      }
    };
    constructorConstructor.instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>() {{
      put(typeToken.getType(), instanceCreator);
    }};

    // Verify - should return ObjectConstructor with instanceCreator
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    assertEquals(new Integer(42), objectConstructor.construct());
  }

  @Test
  public void testGet_WithRawTypeInstanceCreator_ShouldReturnMatchingObjectConstructor() {
    // Setup
    TypeToken<Number> typeToken = new TypeToken<Number>() {};
    final InstanceCreator<Number> instanceCreator = new InstanceCreator<Number>() {
      @Override
      public Number createInstance(Type type) {
        return new Integer(42);
      }
    };
    constructorConstructor.instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>() {{
      put(typeToken.getRawType(), instanceCreator);
    }};

    // Verify - should return ObjectConstructor with instanceCreator
    ObjectConstructor<Number> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    assertEquals(new Integer(42), objectConstructor.construct());
  }

  @Test
  public void testGet_WithDefaultConstructor_ShouldReturnObjectConstructor() {
    // Setup
    TypeToken<String> typeToken = new TypeToken<String>() {};

    // Verify - should return ObjectConstructor with default constructor
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    assertEquals("", objectConstructor.construct());
  }

  @Test
  public void testGet_WithDefaultImplementationConstructor_ShouldReturnObjectConstructor() {
    // Setup
    TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};

    // Verify - should return ObjectConstructor with default implementation constructor
    ObjectConstructor<Collection<String>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    assertEquals(new ArrayList<String>(), objectConstructor.construct());
  }

  @Test(expected = RuntimeException.class)
  public void testGet_WithInvalidDefaultImplementationConstructor_ShouldThrowException() {
    // Setup
    TypeToken<EnumSet<String>> typeToken = new TypeToken<EnumSet<String>>() {};

    // Verify - should throw JsonIOException due to invalid EnumSet type
    constructorConstructor.get(typeToken);
  }

  @Test
  public void testGet_WithMapTypeAndSortedMapRawType_ShouldReturnObjectConstructorWithSortedMap() {
    // Setup
    TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {};

    // Verify - should return ObjectConstructor with SortedMap
    ObjectConstructor<SortedMap<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    assertEquals(new TreeMap<String, Integer>(), objectConstructor.construct());
  }

  @Test
  public void testGet_WithMapTypeAndParameterizedTypeRawType_ShouldReturnObjectConstructorWithLinkedHashMap() {
    // Setup
    TypeToken<Map<ConstructorConstructorTest, Integer>> typeToken = new TypeToken<Map<ConstructorConstructorTest, Integer>>() {};

    // Verify - should return ObjectConstructor with LinkedHashMap
    ObjectConstructor<Map<ConstructorConstructorTest, Integer>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    assertEquals(new LinkedHashMap<ConstructorConstructorTest, Integer>(), objectConstructor.construct());
  }

  @Test
  public void testGet_WithMapTypeAndStringParameterizedType_ShouldReturnObjectConstructorWithLinkedTreeMap() {
    // Setup
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};

    // Verify - should return ObjectConstructor with LinkedTreeMap
    ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    assertEquals(new LinkedTreeMap<String, Integer>(), objectConstructor.construct());
  }

  @Test
  public void testGet_WithUnsafeConstructor_ShouldReturnObjectConstructor() {
    // Setup
    TypeToken<ConstructorConstructorTest> typeToken = new TypeToken<ConstructorConstructorTest>() {};

    // Verify - should return ObjectConstructor with unsafe constructor
    ObjectConstructor<ConstructorConstructorTest> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    assertEquals(ConstructorConstructorTest.class, objectConstructor.construct().getClass());
  }

  @Test
  public void testToString_ShouldReturnStringRepresentationOfInstanceCreators() {
    // Setup
    constructorConstructor.instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>() {{
      put(String.class, new InstanceCreator<String>() {
        @Override
        public String createInstance(Type type) {
          return "test";
        }
      });
    }};

    // Verify - should return string representation of instanceCreators
    assertEquals("{class java.lang.String=test}", constructorConstructor.toString());
  }
}