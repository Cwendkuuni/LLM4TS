import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @Before
  public void setup() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  @Test
  public void testGet_withInstanceCreator_shouldReturnInstance() {
    // Setup
    TypeToken<String> typeToken = new TypeToken<String>() {};
    InstanceCreator<String> instanceCreator = mock(InstanceCreator.class);
    when(instanceCreator.createInstance(typeToken.getType())).thenReturn("mockedInstance");
    constructorConstructor.instanceCreators.put(typeToken.getType(), instanceCreator);

    // Exercise
    ObjectConstructor<String> result = constructorConstructor.get(typeToken);
    String instance = result.construct();

    // Verify
    verify(instanceCreator).createInstance(typeToken.getType());
    assertEquals("mockedInstance", instance);
  }

  @Test
  public void testGet_withRawTypeInstanceCreator_shouldReturnInstance() {
    // Setup
    TypeToken<LinkedList<String>> typeToken = new TypeToken<LinkedList<String>>() {};
    InstanceCreator<LinkedList<String>> instanceCreator = mock(InstanceCreator.class);
    when(instanceCreator.createInstance(typeToken.getType())).thenReturn(new LinkedList<>());
    constructorConstructor.instanceCreators.put(typeToken.getRawType(), instanceCreator);

    // Exercise
    ObjectConstructor<LinkedList<String>> result = constructorConstructor.get(typeToken);
    LinkedList<String> instance = result.construct();

    // Verify
    verify(instanceCreator).createInstance(typeToken.getType());
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGet_withDefaultConstructor_shouldReturnInstance() {
    // Setup
    TypeToken<ArrayList<Integer>> typeToken = new TypeToken<ArrayList<Integer>>() {};

    // Exercise
    ObjectConstructor<ArrayList<Integer>> result = constructorConstructor.get(typeToken);
    ArrayList<Integer> instance = result.construct();

    // Verify
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGet_withDefaultImplementationConstructor_shouldReturnInstance() {
    // Setup
    TypeToken<HashSet<Double>> typeToken = new TypeToken<HashSet<Double>>() {};

    // Exercise
    ObjectConstructor<HashSet<Double>> result = constructorConstructor.get(typeToken);
    HashSet<Double> instance = result.construct();

    // Verify
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGet_withUnsafeAllocator_shouldReturnInstance() {
    // Setup
    TypeToken<LinkedList<String>> typeToken = new TypeToken<LinkedList<String>>() {};

    // Exercise
    ObjectConstructor<LinkedList<String>> result = constructorConstructor.get(typeToken);
    LinkedList<String> instance = result.construct();

    // Verify
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test(expected = RuntimeException.class)
  public void testGet_withInvalidType_shouldThrowException() {
    // Setup
    TypeToken<Float> typeToken = new TypeToken<Float>() {};

    // Exercise & Verify
    ObjectConstructor<Float> result = constructorConstructor.get(typeToken);
    result.construct();
  }

  @Test
  public void testToString_shouldReturnCorrectString() {
    // Setup
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    instanceCreators.put(Integer.class, mock(InstanceCreator.class));
    instanceCreators.put(Double.class, mock(InstanceCreator.class));
    constructorConstructor.instanceCreators = instanceCreators;

    // Exercise
    String result = constructorConstructor.toString();

    // Verify
    assertEquals(instanceCreators.toString(), result);
  }
}