import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ConstructorConstructorTest {

  ConstructorConstructor constructorConstructor;
  Map<Type, InstanceCreator<?>> instanceCreators;

  @Before
  public void setup() {
    instanceCreators = new HashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  // test get() method with existing InstanceCreator for provided TypeToken
  @Test
  public void testGetWithExistingInstanceCreator() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>(){};
    Type type = typeToken.getType();
    Class<? super List<String>> rawType = typeToken.getRawType();
  
    instanceCreators.put(type, new TestInstanceCreator<List<String>>());
  
    ObjectConstructor<List<String>> result = constructorConstructor.get(typeToken);
  
    assertNotNull(result);
  }
  
  // test get() method with no existing InstanceCreator for provided TypeToken
  // and existing InstanceCreator for the raw type
  @Test
  public void testGetWithExistingInstanceCreatorForRawType() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>(){};
    Type type = typeToken.getType();
    Class<? super List<String>> rawType = typeToken.getRawType();
  
    instanceCreators.put(rawType, new TestInstanceCreator<List<String>>());
  
    ObjectConstructor<List<String>> result = constructorConstructor.get(typeToken);
  
    assertNotNull(result);
  }
  
  // test get() method with no existing InstanceCreator for provided TypeToken
  // and no default constructor for the raw type
  @Test
  public void testGetWithNoExistingInstanceCreatorAndNoDefaultConstructor() {
    TypeToken<List<ConstructorConstructorTest>> typeToken = new TypeToken<List<ConstructorConstructorTest>>(){};
    Type type = typeToken.getType();
    Class<? super List<ConstructorConstructorTest>> rawType = typeToken.getRawType();
    
    ObjectConstructor<List<ConstructorConstructorTest>> result = constructorConstructor.get(typeToken);
  
    assertNull(result);
  }
  
  // test newDefaultConstructor() method with raw type having a default constructor
  @Test
  public void testNewDefaultConstructorWithDefaultConstructor() {
    Class<LinkedList<String>> rawType = LinkedList.class;
    
    ObjectConstructor<LinkedList<String>> result = constructorConstructor.newDefaultConstructor(rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultConstructor() method with raw type not having a default constructor
  @Test
  public void testNewDefaultConstructorWithNoDefaultConstructor() {
    Class<AbstractList<String>> rawType = AbstractList.class;
    
    ObjectConstructor<AbstractList<String>> result = constructorConstructor.newDefaultConstructor(rawType);
  
    assertNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a Collection
  @Test
  public void testNewDefaultImplementationConstructorWithCollection() {
    Type type = new TypeToken<Collection<Integer>>(){}.getType();
    Class<? super Collection<Integer>> rawType = new TypeToken<Collection<Integer>>(){}.getRawType();
    
    ObjectConstructor<Collection<Integer>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a SortedSet
  @Test
  public void testNewDefaultImplementationConstructorWithSortedSet() {
    Type type = new TypeToken<SortedSet<Integer>>(){}.getType();
    Class<? super SortedSet<Integer>> rawType = new TypeToken<SortedSet<Integer>>(){}.getRawType();
    
    ObjectConstructor<SortedSet<Integer>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being an EnumSet
  @Test
  public void testNewDefaultImplementationConstructorWithEnumSet() {
    Type type = new TypeToken<EnumSet<ConstructorConstructorTest>>(){}.getType();
    Class<? super EnumSet<ConstructorConstructorTest>> rawType = new TypeToken<EnumSet<ConstructorConstructorTest>>(){}.getRawType();
    
    ObjectConstructor<EnumSet<ConstructorConstructorTest>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a Set
  @Test
  public void testNewDefaultImplementationConstructorWithSet() {
    Type type = new TypeToken<Set<String>>(){}.getType();
    Class<? super Set<String>> rawType = new TypeToken<Set<String>>(){}.getRawType();
    
    ObjectConstructor<Set<String>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a Queue
  @Test
  public void testNewDefaultImplementationConstructorWithQueue() {
    Type type = new TypeToken<Queue<String>>(){}.getType();
    Class<? super Queue<String>> rawType = new TypeToken<Queue<String>>(){}.getRawType();
    
    ObjectConstructor<Queue<String>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a List
  @Test
  public void testNewDefaultImplementationConstructorWithList() {
    Type type = new TypeToken<List<String>>(){}.getType();
    Class<? super List<String>> rawType = new TypeToken<List<String>>(){}.getRawType();
    
    ObjectConstructor<List<String>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a Map
  @Test
  public void testNewDefaultImplementationConstructorWithMap() {
    Type type = new TypeToken<Map<String, String>>(){}.getType();
    Class<? super Map<String, String>> rawType = new TypeToken<Map<String, String>>(){}.getRawType();
    
    ObjectConstructor<Map<String, String>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a SortedMap
  @Test
  public void testNewDefaultImplementationConstructorWithSortedMap() {
    Type type = new TypeToken<SortedMap<String, String>>(){}.getType();
    Class<? super SortedMap<String, String>> rawType = new TypeToken<SortedMap<String, String>>(){}.getRawType();
    
    ObjectConstructor<SortedMap<String, String>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a parameterized type
  // where the actual type argument is a subtype of String
  @Test
  public void testNewDefaultImplementationConstructorWithParameterizedType() {
    Type type = new TypeToken<Map<Integer, String>>(){}.getType();
    Class<? super Map<Integer, String>> rawType = new TypeToken<Map<Integer, String>>(){}.getRawType();
    
    ObjectConstructor<Map<Integer, String>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a parameterized type
  // where the actual type argument is String
  @Test
  public void testNewDefaultImplementationConstructorWithParameterizedTypeString() {
    Type type = new TypeToken<Map<String, String>>(){}.getType();
    Class<? super Map<String, String>> rawType = new TypeToken<Map<String, String>>(){}.getRawType();
    
    ObjectConstructor<Map<String, String>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a parameterized type
  // where the actual type argument is not a String and not a subtype of String
  @Test
  public void testNewDefaultImplementationConstructorWithParameterizedTypeInvalid() {
    Type type = new TypeToken<Map<String, Integer>>(){}.getType();
    Class<? super Map<String, Integer>> rawType = new TypeToken<Map<String, Integer>>(){}.getRawType();
    
    ObjectConstructor<Map<String, Integer>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  
    assertNotNull(result);
  }
  
  // test newDefaultImplementationConstructor() method with raw type being a parameterized type
  // where the actual type argument is not a String and not a subtype of String
  @Test(expected = JsonIOException.class)
  public void testNewDefaultImplementationConstructorWithParameterizedTypeInvalidThrowJsonIOException() {
    Type type = new TypeToken<Map<Integer, String>>(){}.getType();
    Class<? super Map<Integer, String>> rawType = new TypeToken<Map<Integer, String>>(){}.getRawType();
    
    constructorConstructor.newDefaultImplementationConstructor(type, rawType);
  }
  
  // test newUnsafeAllocator() method with invalid type
  @Test(expected = RuntimeException.class)
  public void testNewUnsafeAllocatorWithInvalidType() {
    Type type = new TypeToken<ConstructorConstructor>(){}.getType();
    Class<? super ConstructorConstructor> rawType = new TypeToken<ConstructorConstructor>(){}.getRawType();
    
    constructorConstructor.newUnsafeAllocator(type, rawType).construct();
  }
  
  // test toString() method
  @Test
  public void testToString() {
    String result = constructorConstructor.toString();
  
    assertNotNull(result);
  }
  
  // Custom InstanceCreator implementation for testing purposes
  private class TestInstanceCreator<T> implements InstanceCreator<T> {
    @Override
    public T createInstance(Type type) {
      return null;
    }
  }
}