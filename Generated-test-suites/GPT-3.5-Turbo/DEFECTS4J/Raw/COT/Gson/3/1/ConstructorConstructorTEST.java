import static org.junit.Assert.*;

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

import org.junit.Before;
import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
  }

  @Test
  public void testGet_WithEmptyInstanceCreators_ReturnsDefaultConstructor() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    String instance = objectConstructor.construct();
    assertNotNull(instance);
  }

  @Test
  public void testGet_WithUnsupportedType_ThrowsJsonIOException() {
    TypeToken<Collection<ConstructorConstructor>> typeToken = new TypeToken<Collection<ConstructorConstructor>>() {};
    try {
      constructorConstructor.get(typeToken);
      fail("Expected JsonIOException to be thrown");
    } catch (JsonIOException e) {
      // Expected
    }
  }

  @Test
  public void testGet_WithMissingTypeInformation_ThrowsJsonIOException() {
    TypeToken<?> typeToken = new TypeToken<>() {};
    try {
      constructorConstructor.get(typeToken);
      fail("Expected JsonIOException to be thrown");
    } catch (JsonIOException e) {
      // Expected
    }
  }

  @Test
  public void testGet_WithUnsupportedGenericInstanceCreator_ReturnsDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(String.class, type -> "Instance created by InstanceCreator");
    constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Collection<ConstructorConstructor>> typeToken = new TypeToken<Collection<ConstructorConstructor>>() {};
    ObjectConstructor<Collection<ConstructorConstructor>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    Collection<ConstructorConstructor> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGet_WithDefaultConstructor_ReturnsDefaultConstructor() {
    TypeToken<ArrayList<ConstructorConstructor>> typeToken = new TypeToken<ArrayList<ConstructorConstructor>>() {};
    ObjectConstructor<ArrayList<ConstructorConstructor>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    ArrayList<ConstructorConstructor> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGet_WithDefaultImplementationConstructor_ReturnsDefaultImplementationConstructor() {
    TypeToken<Set<ConstructorConstructor>> typeToken = new TypeToken<Set<ConstructorConstructor>>() {};
    ObjectConstructor<Set<ConstructorConstructor>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    Set<ConstructorConstructor> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGet_WithUnsafeAllocator_ReturnsUnsafeAllocator() {
    TypeToken<TreeSet<String>> typeToken = new TypeToken<TreeSet<String>>() {};
    ObjectConstructor<TreeSet<String>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    TreeSet<String> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testToString_ReturnsInstanceCreatorsToString() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(String.class, type -> "Instance created by InstanceCreator");
    constructorConstructor = new ConstructorConstructor(instanceCreators);
    String expected = instanceCreators.toString();
    String actual = constructorConstructor.toString();
    assertEquals(expected, actual);
  }
}