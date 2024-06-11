import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.After;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructorConstructor;
  private Map<Type, InstanceCreator<?>> instanceCreators;
  
  @Before
  public void setup() {
    instanceCreators = new HashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }
  
  @After
  public void teardown() {
    instanceCreators = null;
    constructorConstructor = null;
  }
  
  @Test
  public void testGet_withInstanceCreator_shouldReturnCorrectObjectConstructor() {
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    InstanceCreator<ArrayList<String>> instanceCreator = new InstanceCreator<ArrayList<String>>() {
      @Override
      public ArrayList<String> createInstance(Type type) {
        return new ArrayList<String>();
      }
    };
    instanceCreators.put(typeToken.getType(), instanceCreator);
    
    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof ArrayList);
  }
  
  @Test
  public void testGet_withRawTypeInstanceCreator_shouldReturnCorrectObjectConstructor() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
      @Override
      public String createInstance(Type type) {
        return "Test";
      }
    };
    instanceCreators.put(String.class, instanceCreator);
    
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof String);
    assertEquals("Test", objectConstructor.construct());
  }
  
  @Test
  public void testGet_withDefaultConstructor_shouldReturnCorrectObjectConstructor() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof Integer);
  }
  
  @Test
  public void testGet_withDefaultImplementationConstructor_shouldReturnCorrectObjectConstructor() {
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof ArrayList);
  }
  
  @Test
  public void testGet_withUnsafeConstructor_shouldReturnCorrectObjectConstructor() {
    TypeToken<HashMap<String, Integer>> typeToken = new TypeToken<HashMap<String, Integer>>() {};
    ObjectConstructor<HashMap<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
    assertTrue(objectConstructor.construct() instanceof HashMap);
  }
}