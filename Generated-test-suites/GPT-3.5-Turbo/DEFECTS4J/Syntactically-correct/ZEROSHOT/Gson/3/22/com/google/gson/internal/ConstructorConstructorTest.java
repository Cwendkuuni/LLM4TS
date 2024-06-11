import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.google.gson.JsonIOException;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setUp() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }
  
  @Test
  public void testGet_withInstanceCreator() {
    InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
      @Override
      public Object createInstance(Type type) {
        return new Object();
      }
    };
    constructorConstructor.instanceCreators.put(Object.class, instanceCreator);
    
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(Object.class));
    
    assertNotNull(objectConstructor);
    Object constructedObject = objectConstructor.construct();
    assertNotNull(constructedObject);
    assertTrue(constructedObject instanceof Object);
  }
  
  @Test
  public void testGet_withRawTypeInstanceCreator() {
    InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
      @Override
      public Object createInstance(Type type) {
        return new Object();
      }
    };
    constructorConstructor.instanceCreators.put(Object.class, instanceCreator);
    
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(String.class));
    
    assertNotNull(objectConstructor);
    Object constructedObject = objectConstructor.construct();
    assertNotNull(constructedObject);
    assertTrue(constructedObject instanceof Object);
  }
  
  @Test
  public void testGet_withDefaultConstructor() {
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(Object.class));
    
    assertNotNull(objectConstructor);
    Object constructedObject = objectConstructor.construct();
    assertNotNull(constructedObject);
    assertTrue(constructedObject instanceof Object);
  }
  
  @Test
  public void testGet_withDefaultImplementationConstructor() {
    ObjectConstructor<Set<Object>> objectConstructor = constructorConstructor.get(new TypeToken<Set<Object>>() {});
    
    assertNotNull(objectConstructor);
    Set<Object> constructedSet = objectConstructor.construct();
    assertNotNull(constructedSet);
    assertTrue(constructedSet instanceof LinkedHashSet<?>);
  }
  
  @Test
  public void testGet_withUnsafeAllocator() {
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(ConstructorConstructor.class));
    
    assertNotNull(objectConstructor);
    Object constructedObject = objectConstructor.construct();
    assertNotNull(constructedObject);
    assertTrue(constructedObject instanceof ConstructorConstructor);
  }

  @Test
  public void testNewDefaultConstructor_withExistingConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<ConstructorConstructor> objectConstructor = cc.newDefaultConstructor(ConstructorConstructor.class);
    
    assertNotNull(objectConstructor);
    ConstructorConstructor constructedCC = objectConstructor.construct();
    assertNotNull(constructedCC);
    assertTrue(constructedCC instanceof ConstructorConstructor);
  }
  
  @Test
  public void testNewDefaultConstructor_withNoConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<String> objectConstructor = cc.newDefaultConstructor(String.class);
    
    assertNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withCollection() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<Set<String>> objectConstructor = cc.newDefaultImplementationConstructor(new TypeToken<Set<String>>() {}.getType(), Set.class);
    
    assertNotNull(objectConstructor);
    Set<String> constructedSet = objectConstructor.construct();
    assertNotNull(constructedSet);
    assertTrue(constructedSet instanceof LinkedHashSet<?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withSortedSet() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<TreeSet<String>> objectConstructor = cc.newDefaultImplementationConstructor(new TypeToken<TreeSet<String>>() {}.getType(), TreeSet.class);
    
    assertNotNull(objectConstructor);
    TreeSet<String> constructedSet = objectConstructor.construct();
    assertNotNull(constructedSet);
    assertTrue(constructedSet instanceof TreeSet<?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withEnumSet() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<EnumSet<TestEnum>> objectConstructor = cc.newDefaultImplementationConstructor(new TypeToken<EnumSet<TestEnum>>() {}.getType(), EnumSet.class);
    
    assertNotNull(objectConstructor);
    EnumSet<TestEnum> constructedSet = objectConstructor.construct();
    assertNotNull(constructedSet);
    assertTrue(constructedSet instanceof EnumSet<?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withQueue() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<LinkedList<String>> objectConstructor = cc.newDefaultImplementationConstructor(new TypeToken<LinkedList<String>>() {}.getType(), LinkedList.class);
    
    assertNotNull(objectConstructor);
    LinkedList<String> constructedList = objectConstructor.construct();
    assertNotNull(constructedList);
    assertTrue(constructedList instanceof LinkedList<?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withArrayList() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<ArrayList<String>> objectConstructor = cc.newDefaultImplementationConstructor(new TypeToken<ArrayList<String>>() {}.getType(), ArrayList.class);
    
    assertNotNull(objectConstructor);
    ArrayList<String> constructedList = objectConstructor.construct();
    assertNotNull(constructedList);
    assertTrue(constructedList instanceof ArrayList<?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withMap() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<LinkedHashMap<String, String>> objectConstructor = cc.newDefaultImplementationConstructor(new TypeToken<LinkedHashMap<String, String>>() {}.getType(), LinkedHashMap.class);
    
    assertNotNull(objectConstructor);
    LinkedHashMap<String, String> constructedMap = objectConstructor.construct();
    assertNotNull(constructedMap);
    assertTrue(constructedMap instanceof LinkedHashMap<?, ?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withSortedMap() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<TreeMap<String, String>> objectConstructor = cc.newDefaultImplementationConstructor(new TypeToken<TreeMap<String, String>>() {}.getType(), TreeMap.class);
    
    assertNotNull(objectConstructor);
    TreeMap<String, String> constructedMap = objectConstructor.construct();
    assertNotNull(constructedMap);
    assertTrue(constructedMap instanceof TreeMap<?, ?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withParameterizedMap() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeToken<Map<Integer, Double>> typeToken = new TypeToken<Map<Integer, Double>>() {};
    
    ObjectConstructor<LinkedHashMap<Integer, Double>> objectConstructor = cc.newDefaultImplementationConstructor(typeToken.getType(), LinkedHashMap.class);
    
    assertNotNull(objectConstructor);
    LinkedHashMap<Integer, Double> constructedMap = objectConstructor.construct();
    assertNotNull(constructedMap);
    assertTrue(constructedMap instanceof LinkedHashMap<?, ?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withLinkedTreeMap() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};
    
    ObjectConstructor<LinkedTreeMap<String, String>> objectConstructor = cc.newDefaultImplementationConstructor(typeToken.getType(), LinkedTreeMap.class);
    
    assertNotNull(objectConstructor);
    LinkedTreeMap<String, String> constructedMap = objectConstructor.construct();
    assertNotNull(constructedMap);
    assertTrue(constructedMap instanceof LinkedTreeMap<?, ?>);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withInvalidEnumSetType() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type type = new TypeToken<EnumSet<String>>() {}.getType();
    
    try {
      cc.newDefaultImplementationConstructor(type, EnumSet.class);
      fail("Expected JsonIOException to be thrown");
    } catch (JsonIOException e) {
      assertEquals("Invalid EnumSet type: " + type.toString(), e.getMessage());
    }
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withInvalidEnumSetType_noTypeArguments() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeToken<EnumSet> typeToken = new TypeToken<EnumSet>() {};
    
    try {
      cc.newDefaultImplementationConstructor(typeToken.getType(), EnumSet.class);
      fail("Expected JsonIOException to be thrown");
    } catch (JsonIOException e) {
      assertEquals("Invalid EnumSet type: " + typeToken.getType().toString(), e.getMessage());
    }
  }
  
  @Test
  public void testNewUnsafeAllocator_withExistingConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<ConstructorConstructor> objectConstructor = cc.newUnsafeAllocator(ConstructorConstructor.class);
    
    assertNotNull(objectConstructor);
    ConstructorConstructor constructedCC = objectConstructor.construct();
    assertNotNull(constructedCC);
    assertTrue(constructedCC instanceof ConstructorConstructor);
  }
  
  @Test
  public void testNewUnsafeAllocator_withNoConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<TypeVariable> objectConstructor = cc.newUnsafeAllocator(TypeVariable.class);
    
    assertNotNull(objectConstructor);
    TypeVariable constructedTypeVariable = objectConstructor.construct();
    assertNotNull(constructedTypeVariable);
    assertTrue(constructedTypeVariable instanceof TypeVariable<?>);
  }
}