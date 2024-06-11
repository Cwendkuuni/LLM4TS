import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.google.gson.internal.ConstructorConstructor.ObjectConstructor;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceForMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<Map<String, String>> mapConstructor = constructorConstructor.get(new TypeToken<Map<String, String>>() {}.getType());
    Map<String, String> map = mapConstructor.construct();
    Assert.assertNotNull(map);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceForUnsupportedType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<Integer> intConstructor = constructorConstructor.get(new TypeToken<Integer>() {}.getType());
    intConstructor.construct();
  }

  @Test
  public void testGetInstanceForList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new ArrayList<>());
    ObjectConstructor<ArrayList<Integer>> listConstructor = constructorConstructor.get(new TypeToken<ArrayList<Integer>>() {}.getType());
    ArrayList<Integer> list = listConstructor.construct();
    Assert.assertNotNull(list);
  }

  @Test
  public void testGetInstanceForEnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new EnumSet<String>() {});
    ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(new TypeToken<EnumSet<String>>() {}.getType());
    EnumSet<String> enumSet = enumSetConstructor.construct();
    Assert.assertNotNull(enumSet);
  }

  @Test
  public void testGetInstanceForTreeMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new TreeMap<>());
    ObjectConstructor<TreeMap<String, String>> treeMapConstructor = constructorConstructor.get(new TypeToken<TreeMap<String, String>>() {}.getType());
    TreeMap<String, String> treeMap = treeMapConstructor.construct();
    Assert.assertNotNull(treeMap);
  }

  @Test
  public void testGetInstanceForLinkedHashMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<LinkedHashMap<String, String>> linkedHashMapConstructor = constructorConstructor.get(new TypeToken<LinkedHashMap<String, String>>() {}.getType());
    LinkedHashMap<String, String> linkedHashMap = linkedHashMapConstructor.construct();
    Assert.assertNotNull(linkedHashMap);
  }

  @Test
  public void testGetInstanceForLinkedHashSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashSet<>());
    ObjectConstructor<LinkedHashSet<String>> linkedHashSetConstructor = constructorConstructor.get(new TypeToken<LinkedHashSet<String>>() {}.getType());
    LinkedHashSet<String> linkedHashSet = linkedHashSetConstructor.construct();
    Assert.assertNotNull(linkedHashSet);
  }

  @Test
  public void testGetInstanceForLinkedList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedList<>());
    ObjectConstructor<LinkedList<String>> linkedListConstructor = constructorConstructor.get(new TypeToken<LinkedList<String>>() {}.getType());
    LinkedList<String> linkedList = linkedListConstructor.construct();
    Assert.assertNotNull(linkedList);
  }
}