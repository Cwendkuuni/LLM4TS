import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.google.gson.reflect.TypeToken.get;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceOfArrayList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<ArrayList<String>> typeToken = get(ArrayList.class, String.class);
    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(typeToken);
    ArrayList<String> result = objectConstructor.construct();
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceOfArrayListWithElement() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<ArrayList<Integer>> typeToken = get(ArrayList.class, Integer.class);
    ObjectConstructor<ArrayList<Integer>> objectConstructor = constructorConstructor.get(typeToken);
    ArrayList<Integer> result = objectConstructor.construct();
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceOfLinkedList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<LinkedList<String>> typeToken = get(LinkedList.class, String.class);
    ObjectConstructor<LinkedList<String>> objectConstructor = constructorConstructor.get(typeToken);
    LinkedList<String> result = objectConstructor.construct();
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceOfLinkedHashSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<LinkedHashSet<Integer>> typeToken = get(LinkedHashSet.class, Integer.class);
    ObjectConstructor<LinkedHashSet<Integer>> objectConstructor = constructorConstructor.get(typeToken);
    LinkedHashSet<Integer> result = objectConstructor.construct();
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceOfTreeMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<TreeMap<String, String>> typeToken = get(TreeMap.class, String.class, String.class);
    ObjectConstructor<TreeMap<String, String>> objectConstructor = constructorConstructor.get(typeToken);
    TreeMap<String, String> result = objectConstructor.construct();
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceOfTreeSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<TreeSet<String>> typeToken = get(TreeSet.class, String.class);
    ObjectConstructor<TreeSet<String>> objectConstructor = constructorConstructor.get(typeToken);
    TreeSet<String> result = objectConstructor.construct();
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceOfEnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<EnumSet<DayOfWeek>> typeToken = get(EnumSet.class, DayOfWeek.class);
    ObjectConstructor<EnumSet<DayOfWeek>> objectConstructor = constructorConstructor.get(typeToken);
    EnumSet<DayOfWeek> result = objectConstructor.construct();
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceOfArrayListWithCustomInstanceCreator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Map.of(
        get(ArrayList.class, String.class), (t) -> new ArrayList<>()));
    TypeToken<ArrayList<String>> typeToken = get(ArrayList.class, String.class);
    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(typeToken);
    ArrayList<String> result = objectConstructor.construct();
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfNullType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<Object> typeToken = get(Object.class);
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
    objectConstructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfNonSupportedType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<String> typeToken = get(String.class);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    objectConstructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    Type type = new TypeToken<UnknownType>() {}.getType();
    ObjectConstructor<UnknownType> objectConstructor = constructorConstructor.get((Type) type);
    objectConstructor.construct();
  }
}