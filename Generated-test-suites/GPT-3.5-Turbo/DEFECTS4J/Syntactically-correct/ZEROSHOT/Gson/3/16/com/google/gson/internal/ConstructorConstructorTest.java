import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(null);
  }
  
  @Test
  public void testGet_InstanceCreator() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_RawTypeCreator() {
    TypeToken<Integer> typeToken = TypeToken.get(Integer.TYPE);
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultConstructor() {
    TypeToken<ConstructorConstructor> typeToken = TypeToken.get(ConstructorConstructor.class);
    ObjectConstructor<ConstructorConstructor> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Collection_SortedSet() {
    TypeToken<SortedSet<Object>> typeToken = new TypeToken<SortedSet<Object>>() {};
    ObjectConstructor<SortedSet<Object>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Collection_EnumSet() {
    TypeToken<EnumSet<Object>> typeToken = new TypeToken<EnumSet<Object>>() {};
    ObjectConstructor<EnumSet<Object>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Collection_Set() {
    TypeToken<Set<Object>> typeToken = new TypeToken<Set<Object>>() {};
    ObjectConstructor<Set<Object>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Collection_Queue() {
    TypeToken<Queue<Object>> typeToken = new TypeToken<Queue<Object>>() {};
    ObjectConstructor<Queue<Object>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Collection_List() {
    TypeToken<ArrayList<Object>> typeToken = new TypeToken<ArrayList<Object>>() {};
    ObjectConstructor<ArrayList<Object>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Map_SortedMap() {
    TypeToken<SortedMap<Object, Object>> typeToken = new TypeToken<SortedMap<Object, Object>>() {};
    ObjectConstructor<SortedMap<Object, Object>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Map_ParameterizedType() {
    TypeToken<Map<Object, Object>> typeToken = new TypeToken<Map<Object, Object>>() {};
    ObjectConstructor<Map<Object, Object>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_DefaultImplementationConstructor_Map_Default() {
    TypeToken<LinkedTreeMap<String, Object>> typeToken = new TypeToken<LinkedTreeMap<String, Object>>() {};
    ObjectConstructor<LinkedTreeMap<String, Object>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_UnsafeAllocator() {
    TypeToken<Integer> typeToken = TypeToken.get(Integer.TYPE);
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testToString() {
    String expectedToString = "null";
    String actualToString = constructorConstructor.toString();
    assertEquals(expectedToString, actualToString);
  }
}