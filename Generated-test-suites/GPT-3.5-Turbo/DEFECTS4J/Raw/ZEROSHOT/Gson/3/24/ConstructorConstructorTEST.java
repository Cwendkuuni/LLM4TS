import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(null);
  }
  
  @Test
  public void testGet_InstanceCreator() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    ObjectConstructor<String> actual = constructorConstructor.get(typeToken);
    assertNull(actual);
  }
  
  @Test
  public void testGet_RawTypeCreator() {
    TypeToken<Object> typeToken = new TypeToken<Object>() {};
    ObjectConstructor<Object> actual = constructorConstructor.get(typeToken);
    assertNull(actual);
  }
  
  @Test
  public void testGet_DefaultConstructor() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    ObjectConstructor<Integer> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Collection() {
    TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};
    ObjectConstructor<Collection<String>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Map() {
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
    ObjectConstructor<Map<String, Integer>> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }
  
  @Test
  public void testGet_UnsafeAllocator() {
    TypeToken<Double> typeToken = new TypeToken<Double>() {};
    ObjectConstructor<Double> actual = constructorConstructor.get(typeToken);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultConstructor() {
    Class<Object> rawType = Object.class;
    ObjectConstructor<Object> actual = constructorConstructor.newDefaultConstructor(rawType);
    assertNull(actual);
  }
  
  @Test
  public void testNewDefaultConstructor_NoSuchMethodException() {
    Class<ConstructorConstructor> rawType = ConstructorConstructor.class;
    ObjectConstructor<ConstructorConstructor> actual = constructorConstructor.newDefaultConstructor(rawType);
    assertNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Collection() {
    Type type = new TypeToken<Collection<String>>() {}.getType();
    Class<Collection<String>> rawType = (Class<Collection<String>>) type;
    ObjectConstructor<Collection<String>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Collection_SortedSet() {
    Type type = new TypeToken<SortedSet<String>>() {}.getType();
    Class<SortedSet<String>> rawType = (Class<SortedSet<String>>) type;
    ObjectConstructor<SortedSet<String>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Collection_EnumSet() {
    Type type = new TypeToken<EnumSet<String>>() {}.getType();
    Class<EnumSet<String>> rawType = (Class<EnumSet<String>>) type;
    ObjectConstructor<EnumSet<String>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Collection_Set() {
    Type type = new TypeToken<Set<String>>() {}.getType();
    Class<Set<String>> rawType = (Class<Set<String>>) type;
    ObjectConstructor<Set<String>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Collection_Queue() {
    Type type = new TypeToken<Queue<String>>() {}.getType();
    Class<Queue<String>> rawType = (Class<Queue<String>>) type;
    ObjectConstructor<Queue<String>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Map() {
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    Class<Map<String, Integer>> rawType = (Class<Map<String, Integer>>) type;
    ObjectConstructor<Map<String, Integer>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Map_SortedMap() {
    Type type = new TypeToken<SortedMap<String, Integer>>() {}.getType();
    Class<SortedMap<String, Integer>> rawType = (Class<SortedMap<String, Integer>>) type;
    ObjectConstructor<SortedMap<String, Integer>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Map_ParameterizedType() {
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    Class<Map<String, Integer>> rawType = (Class<Map<String, Integer>>) type;
    ObjectConstructor<Map<String, Integer>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Map_ParameterizedType_String() {
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    Class<Map<String, Integer>> rawType = (Class<Map<String, Integer>>) type;
    ObjectConstructor<Map<String, Integer>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_Map_ParameterizedType_LinkedTreeMap() {
    Type type = new TypeToken<Map<Integer, String>>() {}.getType();
    Class<Map<Integer, String>> rawType = (Class<Map<Integer, String>>) type;
    ObjectConstructor<Map<Integer, String>> actual = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testNewUnsafeAllocator() {
    Type type = new TypeToken<Double>() {}.getType();
    Class<Double> rawType = (Class<Double>) type;
    ObjectConstructor<Double> actual = constructorConstructor.newUnsafeAllocator(type, rawType);
    assertNotNull(actual);
  }
  
  @Test(expected = RuntimeException.class)
  public void testNewUnsafeAllocator_Exception() {
    Type type = new TypeToken<Integer>() {}.getType();
    Class<Integer> rawType = (Class<Integer>) type;
    ObjectConstructor<Integer> actual = constructorConstructor.newUnsafeAllocator(type, rawType);
    assertNotNull(actual);
  }
  
  @Test
  public void testToString() {
    String expected = "null";
    String actual = constructorConstructor.toString();
    assertEquals(expected, actual);
  }
}