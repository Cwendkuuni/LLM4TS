import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {
  
  @Test
  public void testGet_typeToken_returnsInstanceCreator() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a typeToken
    TypeToken<String> typeToken = new TypeToken<String>() {};
    
    // Call get method and assert that it returns an instance creator
    assertNotNull(constructorConstructor.get(typeToken));
  }
  
  @Test
  public void testGet_classTypeToken_returnsInstanceCreator() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a classTypeToken
    TypeToken<Integer> classTypeToken = new TypeToken<Integer>() {};
    
    // Call get method and assert that it returns an instance creator
    assertNotNull(constructorConstructor.get(classTypeToken));
  }
  
  @Test
  public void testGet_defaultConstructor_returnsObjectConstructor() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a typeToken
    TypeToken<Long> typeToken = new TypeToken<Long>() {};
    
    // Call get method and assert that it returns an object constructor
    assertNotNull(constructorConstructor.get(typeToken));
  }
  
  @Test
  public void testGet_defaultImplementationConstructor_returnsObjectConstructor() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a typeToken
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    
    // Call get method and assert that it returns an object constructor
    assertNotNull(constructorConstructor.get(typeToken));
  }
  
  @Test
  public void testGet_unsafeAllocator_returnsObjectConstructor() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a typeToken
    TypeToken<LinkedHashMap<String, Integer>> typeToken = new TypeToken<LinkedHashMap<String, Integer>>() {};
    
    // Call get method and assert that it returns an object constructor
    assertNotNull(constructorConstructor.get(typeToken));
  }
  
  @Test
  public void testNewDefaultConstructor_validConstructor_returnsObjectConstructor() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a rawType
    Class<Double> rawType = Double.class;
    
    // Call newDefaultConstructor method and assert that it returns an object constructor
    assertNotNull(constructorConstructor.newDefaultConstructor(rawType));
  }
  
  @Test
  public void testNewDefaultConstructor_invalidConstructor_returnsNull() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a rawType
    Class<String> rawType = String.class;
    
    // Call newDefaultConstructor method and assert that it returns null
    assertNull(constructorConstructor.newDefaultConstructor(rawType));
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_validType_returnsObjectConstructor() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a type
    Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
    
    // Create a rawType
    Class<ArrayList<Integer>> rawType = ArrayList.class;
    
    // Call newDefaultImplementationConstructor method and assert that it returns an object constructor
    assertNotNull(constructorConstructor.newDefaultImplementationConstructor(type, rawType));
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_invalidType_returnsNull() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a type
    Type type = new TypeToken<ArrayList<String>>() {}.getType();
    
    // Create a rawType
    Class<ArrayList<String>> rawType = ArrayList.class;
    
    // Call newDefaultImplementationConstructor method and assert that it returns null
    assertNull(constructorConstructor.newDefaultImplementationConstructor(type, rawType));
  }
  
  @Test
  public void testNewUnsafeAllocator_validType_returnsObjectConstructor() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a type
    Type type = new TypeToken<LinkedList<Float>>() {}.getType();
    
    // Create a rawType
    Class<LinkedList<Float>> rawType = LinkedList.class;
    
    // Call newUnsafeAllocator method and assert that it returns an object constructor
    assertNotNull(constructorConstructor.newUnsafeAllocator(type, rawType));
  }
  
  @Test
  public void testNewUnsafeAllocator_invalidType_returnsNull() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Create a type
    Type type = new TypeToken<ArrayDeque<Double>>() {}.getType();
    
    // Create a rawType
    Class<ArrayDeque<Double>> rawType = ArrayDeque.class;
    
    // Call newUnsafeAllocator method and assert that it returns null
    assertNull(constructorConstructor.newUnsafeAllocator(type, rawType));
  }
  
  @Test
  public void testToString_returnsStringRepresentation() {
    // Create ConstructorConstructor object
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    
    // Call toString method and assert that it returns a non-null string
    assertNotNull(constructorConstructor.toString());
  }
  
}