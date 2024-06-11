import org.junit.Assert;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ConstructorConstructorTest {

  private static final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

  static {
    instanceCreators.put(String.class, new StringCreator());
    instanceCreators.put(Integer.class, new IntCreator());
    instanceCreators.put(Integer[].class, new IntArrayCreator());
    instanceCreators.put(Map.class, new MapCreator<>());
    instanceCreators.put(SortedMap.class, new SortedMapCreator<>());
    instanceCreators.put(Collection.class, new CollectionCreator<>());
    instanceCreators.put(Set.class, new SetCreator<>());
    instanceCreators.put(Queue.class, new QueueCreator<>());
  }

  @Test
  public void testGetStringConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<String> constructor = constructorConstructor.get(TypeToken.of(String.class));
    String result = constructor.construct();

    Assert.assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  public void testGetIntConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Integer> constructor = constructorConstructor.get(TypeToken.of(Integer.class));
    Integer result = constructor.construct();

    Assert.assertNotNull(result);
    assertEquals(0, result.intValue());
  }

  @Test
  public void testGetIntArrayConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Integer[]> constructor = constructorConstructor.get(TypeToken.get(int[].class));
    Integer[] result = constructor.construct();

    Assert.assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  public void testGetMapConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Map<String, String>> constructor = constructorConstructor.get(TypeToken.mapFrom(
        String.class, String.class));
    Map<String, String> result = constructor.construct();

    Assert.assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetSortedMapConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<SortedMap<String, String>> constructor = constructorConstructor.get(TypeToken.mapFrom(
        String.class, String.class, TreeMap.class));
    SortedMap<String, String> result = constructor.construct();

    Assert.assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetCollectionConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Collection<String>> constructor = constructorConstructor.get(Collection.class);
    Collection<String> result = constructor.construct();

    Assert.assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetSetConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Set<String>> constructor = constructorConstructor.get(Set.class);
    Set<String> result = constructor.construct();

    Assert.assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetQueueConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Queue<String>> constructor = constructorConstructor.get(Queue.class);
    Queue<String> result = constructor.construct();

    Assert.assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetUnknownTypeConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Number> constructor = constructorConstructor.get(TypeToken.of(Number.class));
    constructor.construct();
  }

  private static final class StringCreator implements InstanceCreator<String> {
    @Override public String createInstance(Type type) {
      return "";
    }
  }

  private static final class IntCreator implements InstanceCreator<Integer> {
    @Override public Integer createInstance(Type type) {
      return 0;
    }
  }

  private static final class IntArrayCreator implements InstanceCreator<Integer[]> {
    @Override public Integer[] createInstance(Type type) {
      return new Integer[0];
    }
  }

  private static final class MapCreator<K extends Serializable, V extends Serializable>
      implements InstanceCreator<Map<K, V>> {
    @Override public Map<K, V> createInstance(Type type) {
      return new LinkedHashMap<K, V>();
    }
  }

  private static final class SortedMapCreator<K extends Comparable<? super K>, V extends Serializable>
      implements InstanceCreator<SortedMap<K, V>> {
    @Override public SortedMap<K, V> createInstance(Type type) {
      return new TreeMap<K, V>();
    }
  }

  private static final class CollectionCreator<T> implements InstanceCreator<Collection<T>> {
    @Override public Collection<T> createInstance(Type type) {
      return new ArrayList<T>();
    }
  }

  private static final class SetCreator<T> implements InstanceCreator<Set<T>> {
    @Override public Set<T> createInstance(Type type) {
      return new LinkedHashSet<T>();
    }
  }

  private static final class QueueCreator<T> implements InstanceCreator<Queue<T>> {
    @Override public Queue<T> createInstance(Type type) {
      return new LinkedList<T>();
    }
  }
}