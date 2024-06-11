import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.google.gson.internal.ConstructorConstructor.ObjectConstructor;

public class ConstructorConstructorTest {

  private final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

  @Test
  public void testGetInstance() {
    instanceCreators.put(String.class, new StringCreator());
    ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<String> stringConstructor = constructor.get(TypeToken.get(String.class));
    String string = stringConstructor.construct();
    Assert.assertNotNull(string);
    Assert.assertEquals("", string);

    instanceCreators.put(LinkedHashMap.class, new LinkedHashMapConstructor<>());
    ObjectConstructor<Map<String, String>> mapConstructor = constructor.get(TypeToken.get(Map.class)
        .getType());
    Map<String, String> map = mapConstructor.construct();
    Assert.assertNotNull(map);
    Assert.assertFalse(map.isEmpty());

    instanceCreators.put(TreeSet.class, new TreeSetConstructor<>());
    ObjectConstructor<Set<Integer>> setConstructor = constructor.get(TypeToken.get(Set.class)
        .getType());
    Set<Integer> set = setConstructor.construct();
    Assert.assertNotNull(set);
    Assert.assertEquals(0, set.size());

    try {
      ObjectConstructor<Integer> intConstructor = constructor.get(TypeToken.get(Integer.class));
      int intValue = intConstructor.construct();
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertEquals("Failed to invoke IntConstructor.createInstance(Type) with no args", e.getMessage());
    }

    try {
      ObjectConstructor<String> invalidTypeConstructor = constructor.get(new TypeToken<Collection<String>>() {}.getType());
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertEquals("Unable to invoke no-args constructor for " + Collection.class.getName() + ". "
          + "Register an InstanceCreator with Gson for this type may fix this problem.", e.getMessage());
    }
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidTypeInstanceCreator() {
    instanceCreators.put(String.class, new Object() {}); // Invalid InstanceCreator
    ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
    constructor.get(TypeToken.get(String.class));
  }

  @Test(expected = RuntimeException.class)
  public void testUnsupportedTypeInstanceCreator() {
    instanceCreators.put(String.class, new Object() {}); // Invalid InstanceCreator
    ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
    constructor.get(TypeToken.get(Number.class));
  }

  private static class StringCreator implements InstanceCreator<String> {
    @Override public String createInstance(Type type) {
      return new String();
    }
  }

  private static class LinkedHashMapConstructor<K, V> implements InstanceCreator<LinkedHashMap<K, V>> {
    @Override public LinkedHashMap<K, V> createInstance(Type type) {
      return new LinkedHashMap<>();
    }
  }

  private static class TreeSetConstructor<T> implements InstanceCreator<TreeSet<T>> {
    @Override public TreeSet<T> createInstance(Type type) {
      return new TreeSet<>();
    }
  }
}