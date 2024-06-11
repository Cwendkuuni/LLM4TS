import static org.junit.Assert.*;
import static com.google.gson.reflect.TypeToken.*;

import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceOfString() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(of(String.class));
    String instance = stringConstructor.construct();
    assertEquals("", instance);
  }

  @Test
  public void testGetInstanceOfArrayList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<ArrayList<Integer>> listConstructor = constructorConstructor.get(of(ArrayList.class));
    ArrayList<Integer> instance = listConstructor.construct();
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGetInstanceOfTreeSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<TreeSet<Integer>> setConstructor = constructorConstructor.get(of(TreeSet.class));
    TreeSet<Integer> instance = setConstructor.construct();
    assertTrue(instance.isEmpty());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfNullType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<Object> nullTypeConstructor = constructorConstructor.get(null);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<UnknownType> unknownTypeConstructor = constructorConstructor.get(of(UnknownType.class));
  }

  @Test
  public void testGetInstanceOfTypeWithInstanceCreator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(MapUtilities::newInstance);
    ObjectConstructor<Map<String, Integer>> mapConstructor = constructorConstructor.get(of(Map.class));
    Map<String, Integer> instance = mapConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof Map);
  }

  @Test
  public void testGetInstanceOfTypeWithDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<ArrayList<Integer>> listConstructor = constructorConstructor.get(of(ArrayList.class));
    ArrayList<Integer> instance = listConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof ArrayList);
  }

  @Test
  public void testGetInstanceOfTypeWithDefaultImplementationConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<SortedMap<String, Integer>> mapConstructor = constructorConstructor.get(of(SortedMap.class));
    SortedMap<String, Integer> instance = mapConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof TreeMap);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfTypeWithNoDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<Thread> threadConstructor = constructorConstructor.get(of(Thread.class));
  }

  @Test
  public void testGetInstanceOfTypeWithPrivateConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<EnumSet<Weekday>> weekdaySetConstructor = constructorConstructor.get(
        new TypeToken<EnumSet<Weekday>>() {}.getType());
    EnumSet<Weekday> instance = weekdaySetConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance.isEmpty());
  }

  @Test
  public void testGetInstanceOfTypeWithUnsafeConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<byte[]> byteArrayConstructor = constructorConstructor.get(of(byte[].class));
    byte[] instance = byteArrayConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance.length > 0);
  }

  interface MapUtilities {
    <T> InstanceCreator<T> newInstance();
  }

  enum Weekday {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
  }
}