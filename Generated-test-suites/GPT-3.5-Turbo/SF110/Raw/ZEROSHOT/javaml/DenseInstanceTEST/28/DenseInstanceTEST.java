import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

  @Test
  public void testValue() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertEquals(1.0, instance.value(0), 0.001);
    assertEquals(2.0, instance.value(1), 0.001);
    assertEquals(3.0, instance.value(2), 0.001);
  }

  @Test
  public void testClear() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    instance.clear();
    assertEquals(0.0, instance.value(0), 0.001);
    assertEquals(0.0, instance.value(1), 0.001);
    assertEquals(0.0, instance.value(2), 0.001);
  }

  @Test
  public void testContainsKey() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertTrue(instance.containsKey(0));
    assertTrue(instance.containsKey(1));
    assertTrue(instance.containsKey(2));
    assertFalse(instance.containsKey(3));
  }

  @Test
  public void testContainsValue() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertTrue(instance.containsValue(1.0));
    assertTrue(instance.containsValue(2.0));
    assertTrue(instance.containsValue(3.0));
    assertFalse(instance.containsValue(4.0));
  }

  @Test
  public void testEntrySet() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
    assertEquals(3, entrySet.size());
  }

  @Test
  public void testGet() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertEquals(1.0, instance.get(0), 0.001);
    assertEquals(2.0, instance.get(1), 0.001);
    assertEquals(3.0, instance.get(2), 0.001);
  }

  @Test
  public void testIsEmpty() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertFalse(instance.isEmpty());
  }

  @Test
  public void testKeySet() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    SortedSet<Integer> keySet = instance.keySet();
    assertEquals(3, keySet.size());
  }

  @Test
  public void testPut() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertEquals(1.0, instance.put(0, 4.0), 0.001);
    assertEquals(2.0, instance.put(1, 5.0), 0.001);
    assertEquals(3.0, instance.put(2, 6.0), 0.001);
  }

  @Test
  public void testPutAll() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    Map<Integer, Double> map = new HashMap<>();
    map.put(0, 4.0);
    map.put(1, 5.0);
    map.put(2, 6.0);
    instance.putAll(map);
    assertEquals(4.0, instance.get(0), 0.001);
    assertEquals(5.0, instance.get(1), 0.001);
    assertEquals(6.0, instance.get(2), 0.001);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    instance.remove(0);
  }

  @Test
  public void testSize() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertEquals(3, instance.size());
  }

  @Test
  public void testValues() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    Collection<Double> values = instance.values();
    assertEquals(3, values.size());
  }

  @Test
  public void testNoAttributes() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertEquals(3, instance.noAttributes());
  }

  @Test
  public void testToString() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
  }

  @Test
  public void testRemoveAttribute() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    instance.removeAttribute(1);
    assertEquals(2, instance.noAttributes());
    assertEquals(1.0, instance.get(0), 0.001);
    assertEquals(3.0, instance.get(1), 0.001);
  }

  @Test
  public void testHashCode() {
    double[] att1 = {1.0, 2.0, 3.0};
    DenseInstance instance1 = new DenseInstance(att1);
    double[] att2 = {1.0, 2.0, 3.0};
    DenseInstance instance2 = new DenseInstance(att2);
    assertEquals(instance1.hashCode(), instance2.hashCode());
  }

  @Test
  public void testEquals() {
    double[] att1 = {1.0, 2.0, 3.0};
    DenseInstance instance1 = new DenseInstance(att1);
    double[] att2 = {1.0, 2.0, 3.0};
    DenseInstance instance2 = new DenseInstance(att2);
    assertTrue(instance1.equals(instance2));
  }

  @Test
  public void testCopy() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    DenseInstance copy = (DenseInstance) instance.copy();
    assertEquals(instance.toString(), copy.toString());
  }

  @Test
  public void testRemoveAttributes() {
    double[] att = {1.0, 2.0, 3.0};
    DenseInstance instance = new DenseInstance(att);
    Set<Integer> indices = new HashSet<>();
    indices.add(0);
    instance.removeAttributes(indices);
    assertEquals(2, instance.noAttributes());
    assertEquals(2.0, instance.get(0), 0.001);
    assertEquals(3.0, instance.get(1), 0.001);
  }
}