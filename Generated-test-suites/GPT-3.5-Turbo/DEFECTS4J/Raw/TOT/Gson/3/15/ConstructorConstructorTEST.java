import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @Before
  public void setUp() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  @Test
  public void testGet_withInstanceCreatorForType_returnsInstanceCreatorConstructor() {
    // Arrange
    TypeToken<String> typeToken = TypeToken.get(String.class);
    InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
      @Override
      public String createInstance(Type type) {
        return "Test String";
      }
    };
    constructorConstructor.instanceCreators.put(typeToken.getType(), instanceCreator);

    // Act
    ObjectConstructor<String> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertEquals("Test String", actualConstructor.construct());
  }

  @Test
  public void testGet_withInstanceCreatorForRawType_returnsInstanceCreatorConstructor() {
    // Arrange
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    InstanceCreator<Integer> instanceCreator = new InstanceCreator<Integer>() {
      @Override
      public Integer createInstance(Type type) {
        return 100;
      }
    };
    constructorConstructor.instanceCreators.put(typeToken.getRawType(), instanceCreator);

    // Act
    ObjectConstructor<Integer> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertEquals(Integer.valueOf(100), actualConstructor.construct());
  }

  @Test
  public void testGet_withNoInstanceCreatorForTypeAndNoDefaultConstructor_returnsNull() {
    // Arrange
    TypeToken<Map<Integer, String>> typeToken = new TypeToken<Map<Integer, String>>() {};

    // Act
    ObjectConstructor<Map<Integer, String>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNull(actualConstructor);
  }

  @Test
  public void testGet_withDefaultConstructor_returnsDefaultConstructor() {
    // Arrange
    TypeToken<ConstructorConstructor> typeToken = TypeToken.get(ConstructorConstructor.class);

    // Act
    ObjectConstructor<ConstructorConstructor> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertTrue(actualConstructor.construct() instanceof ConstructorConstructor);
  }

  @Test
  public void testGet_withDefaultImplementationConstructorForSortedSet_returnsSortedSetConstructor() {
    // Arrange
    TypeToken<SortedSet<Integer>> typeToken = new TypeToken<SortedSet<Integer>>() {};

    // Act
    ObjectConstructor<SortedSet<Integer>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertTrue(actualConstructor.construct() instanceof TreeSet);
  }

  @Test
  public void testGet_withDefaultImplementationConstructorForEnumSet_returnsEnumSetConstructor() {
    // Arrange
    TypeToken<EnumSet<DayOfWeek>> typeToken = new TypeToken<EnumSet<DayOfWeek>>() {};
    EnumSet<DayOfWeek> expected = EnumSet.noneOf(DayOfWeek.class);

    // Act
    ObjectConstructor<EnumSet<DayOfWeek>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertEquals(expected, actualConstructor.construct());
  }

  @Test
  public void testGet_withDefaultImplementationConstructorForSet_returnsSetConstructor() {
    // Arrange
    TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {};

    // Act
    ObjectConstructor<Set<String>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertTrue(actualConstructor.construct() instanceof LinkedHashSet);
  }

  @Test
  public void testGet_withDefaultImplementationConstructorForQueue_returnsQueueConstructor() {
    // Arrange
    TypeToken<Queue<Integer>> typeToken = new TypeToken<Queue<Integer>>() {};

    // Act
    ObjectConstructor<Queue<Integer>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertTrue(actualConstructor.construct() instanceof LinkedList);
  }

  @Test
  public void testGet_withDefaultImplementationConstructorForList_returnsListConstructor() {
    // Arrange
    TypeToken<ArrayList<Double>> typeToken = new TypeToken<ArrayList<Double>>() {};

    // Act
    ObjectConstructor<ArrayList<Double>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertTrue(actualConstructor.construct() instanceof ArrayList);
  }

  @Test
  public void testGet_withDefaultImplementationConstructorForSortedMap_returnsSortedMapConstructor() {
    // Arrange
    TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {};

    // Act
    ObjectConstructor<SortedMap<String, Integer>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertTrue(actualConstructor.construct() instanceof TreeMap);
  }

  @Test
  public void testGet_withDefaultImplementationConstructorForMapWithStringKeyType_returnsMapConstructor() {
    // Arrange
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};

    // Act
    ObjectConstructor<Map<String, Integer>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertTrue(actualConstructor.construct() instanceof LinkedHashMap);
  }

  @Test
  public void testGet_withDefaultImplementationConstructorForMapWithNonStringKeyType_returnsLinkedTreeMapConstructor() {
    // Arrange
    TypeToken<Map<Integer, String>> typeToken = new TypeToken<Map<Integer, String>>() {};

    // Act
    ObjectConstructor<Map<Integer, String>> actualConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(actualConstructor);
    assertTrue(actualConstructor.construct() instanceof LinkedTreeMap);
  }
}