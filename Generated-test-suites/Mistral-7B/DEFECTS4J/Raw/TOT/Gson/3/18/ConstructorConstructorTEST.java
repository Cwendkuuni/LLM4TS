import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.TypeToken;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  @Test
  public void testGetDefaultImplementationForArrayList() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    ObjectConstructor<List<String>> constructorForList = constructor.get(typeToken);
    List<String> list = constructorForList.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }

  @Test
  public void testGetDefaultImplementationForTreeSet() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<SortedSet<Integer>> typeToken = new TypeToken<SortedSet<Integer>>() {};
    ObjectConstructor<SortedSet<Integer>> constructorForTreeSet = constructor.get(typeToken);
    SortedSet<Integer> treeSet = constructorForTreeSet.construct();
    assertNotNull(treeSet);
    assertEquals(0, treeSet.size());
  }

  @Test
  public void testGetInstanceCreatorForArrayList() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.singletonMap(
        new TypeToken<List<String>>() {}, new ArrayListCreator()));
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    ObjectConstructor<List<String>> constructorForList = constructor.get(typeToken);
    List<String> list = constructorForList.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }

  @Test(expected = RuntimeException.class)
  public void testNoDefaultConstructorForMap() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
    ObjectConstructor<Map<String, Integer>> constructorForMap = constructor.get(typeToken);
  }

  @Test
  public void testGetDefaultImplementationForLinkedHashMap() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {
      @Override public boolean equals(Object obj) {
        if (!(obj instanceof Map)) {
          return false;
        }
        Map<?> map = (Map<?>) obj;
        if (this.getClass() != map.getClass()) {
          return false;
        }
        return ((Map<String, String>) map).equals(this);
      }
    };
    ObjectConstructor<Map<String, String>> constructorForMap = constructor.get(typeToken);
    Map<String, String> map = constructorForMap.construct();
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
  public void testGetDefaultImplementationForTreeMap() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {};
    ObjectConstructor<SortedMap<String, Integer>> constructorForTreeMap = constructor.get(typeToken);
    SortedMap<String, Integer> treeMap = constructorForTreeMap.construct();
    assertNotNull(treeMap);
    assertEquals(0, treeMap.size());
  }

  @Test
  public void testGetDefaultImplementationForEnumSet() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.emptyMap());
    TypeToken<EnumSet<MyEnum>> typeToken = new TypeToken<EnumSet<MyEnum>>() {};
    ObjectConstructor<EnumSet<MyEnum>> constructorForEnumSet = constructor.get(typeToken);
    EnumSet<MyEnum> enumSet = constructorForEnumSet.construct();
    assertNotNull(enumSet);
    assertEquals(0, enumSet.size());
  }

  @Test
  public void testGetInstanceCreatorForTreeSet() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.singletonMap(
        new TypeToken<SortedSet<Integer>>() {}, new TreeSetCreator()));
    TypeToken<SortedSet<Integer>> typeToken = new TypeToken<SortedSet<Integer>>() {};
    ObjectConstructor<SortedSet<Integer>> constructorForTreeSet = constructor.get(typeToken);
    SortedSet<Integer> treeSet = constructorForTreeSet.construct();
    assertNotNull(treeSet);
    assertEquals(0, treeSet.size());
  }

  private static class ArrayListCreator implements InstanceCreator<List<String>> {
    @Override public List<String> createInstance(Type type) {
      return new ArrayList<String>();
    }
  }

  private static class TreeSetCreator implements InstanceCreator<SortedSet<Integer>> {
    @Override public SortedSet<Integer> createInstance(Type type) {
      return new TreeSet<Integer>();
    }
  }

  private enum MyEnum {
    FOO, BAR, BAZ
  }
}