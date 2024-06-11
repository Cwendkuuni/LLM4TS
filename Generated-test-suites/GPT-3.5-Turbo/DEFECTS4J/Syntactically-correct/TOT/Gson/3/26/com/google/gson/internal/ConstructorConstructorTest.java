import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
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

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
  }
  
  @Test
  public void testGet_withInstanceCreatorForType_returnsObjectConstructorWithCorrectInstance() {
    // Arrange
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    InstanceCreator<Integer> instanceCreator = new InstanceCreator<Integer>() {
      @Override
      public Integer createInstance(Type type) {
        return 10;
      }
    };
    constructorConstructor.instanceCreators.put(typeToken.getType(), instanceCreator);
    
    // Act
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
    Integer result = objectConstructor.construct();
    
    // Assert
    assertEquals(new Integer(10), result);
  }
  
  @Test
  public void testGet_withInstanceCreatorForRawType_returnsObjectConstructorWithCorrectInstance() {
    // Arrange
    TypeToken<Number> typeToken = new TypeToken<Number>() {};
    InstanceCreator<Number> instanceCreator = new InstanceCreator<Number>() {
      @Override
      public Number createInstance(Type type) {
        return 5;
      }
    };
    constructorConstructor.instanceCreators.put(typeToken.getRawType(), instanceCreator);
    
    // Act
    ObjectConstructor<Number> objectConstructor = constructorConstructor.get(typeToken);
    Number result = objectConstructor.construct();
    
    // Assert
    assertEquals(new Integer(5), result);
  }
  
  @Test
  public void testGet_withDefaultConstructor_returnsObjectConstructorWithCorrectInstance() {
    // Arrange
    TypeToken<String> typeToken = new TypeToken<String>() {};
    
    // Act
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    String result = objectConstructor.construct();
    
    // Assert
    assertEquals(null, result);
  }
  
  @Test
  public void testGet_withDefaultImplementationConstructor_returnsObjectConstructorWithCorrectInstance() {
    // Arrange
    TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {};
    
    // Act
    ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.get(typeToken);
    Set<String> result = objectConstructor.construct();
    
    // Assert
    assertTrue(result instanceof LinkedHashSet);
  }
  
  @Test(expected = JsonIOException.class)
  public void testGet_withInvalidEnumSetType_throwsJsonIOException() {
    // Arrange
    TypeToken<EnumSet<Double>> typeToken = new TypeToken<EnumSet<Double>>() {};
    
    // Act
    constructorConstructor.get(typeToken);
  }
  
  @Test
  public void testGet_withUnsafeAllocator_returnsObjectConstructorWithCorrectInstance() {
    // Arrange
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    
    // Act
    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(typeToken);
    ArrayList<String> result = objectConstructor.construct();
    
    // Assert
    assertTrue(result instanceof ArrayList);
  }
}