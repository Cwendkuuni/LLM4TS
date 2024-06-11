import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstance() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
    String constructedString = stringConstructor.construct();
    assertEquals("", constructedString);

    // Test with raw type
    ObjectConstructor<String> rawStringConstructor = constructorConstructor.get(String.class);
    String constructedRawString = rawStringConstructor.construct();
    assertEquals("", constructedRawString);

    // Test with custom InstanceCreator
    instanceCreators.put(String[].class, new StringArrayInstanceCreator());
    ObjectConstructor<String[]> stringArrayConstructor = constructorConstructor.get(new TypeToken<String[]>() {}.getType());
    String[] constructedStringArray = stringArrayConstructor.construct();
    assertArrayEquals(new String[0], constructedStringArray);

    // Test with null typeToken
    assertNull(constructorConstructor.get(null));

    // Test with unsupported type
    assertNull(constructorConstructor.get(new TypeToken<Object>() {}));

    // Test with exception when no constructor found
    try {
      constructorConstructor.get(ArrayList.class);
      fail("Should have thrown an exception");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("Invalid ArrayList type"));
    }
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceWithUnsupportedConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Type type = new TypeToken<ArrayList<String>>() {}.getType();
    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(type);
    constructor.construct();
  }

  @Test
  public void testNewDefaultConstructor() {
    ObjectConstructor<String> stringDefaultConstructor = new ConstructorConstructor(String.class).newDefaultConstructor(String.class);
    String constructedString = stringDefaultConstructor.construct();
    assertEquals("", constructedString);
  }

  @Test(expected = JsonIOException.class)
  public void testNewDefaultConstructorWithInvalidType() {
    new ConstructorConstructor(Object.class).newDefaultConstructor(int.class);
  }

  @Test
  public void testNewDefaultImplementationConstructorMap() {
    ObjectConstructor<Map<String, Object>> mapDefaultConstructor = new ConstructorConstructor(Map.class)
        .newDefaultImplementationConstructor(Map.class, Map.class);
    Map<String, Object> constructedMap = mapDefaultConstructor.construct();
    assertTrue(constructedMap instanceof LinkedHashMap);
  }

  @Test
  public void testNewDefaultImplementationConstructorCollection() {
    ObjectConstructor<Set<Object>> setDefaultConstructor = new ConstructorConstructor(Set.class)
        .newDefaultImplementationConstructor(Set.class, Object.class);
    Set<Object> constructedSet = setDefaultConstructor.construct();
    assertTrue(constructedSet instanceof LinkedHashSet);
  }

  @Test
  public void testNewUnsafeAllocator() {
    ObjectConstructor<TreeMap<String, Object>> treeMapUnsafeAllocator = new ConstructorConstructor(Object.class)
        .newUnsafeAllocator(TreeMap.class, Object.class);
    TreeMap<String, Object> constructedTreeMap = treeMapUnsafeAllocator.construct();
    assertTrue(constructedTreeMap instanceof TreeMap);
  }
}