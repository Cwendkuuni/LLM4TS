
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class SparseInstanceTest {
  
  SparseInstance instance;
  
  @Before
  public void setUp() {
    instance = new SparseInstance();
  }
  
  @Test
  public void testSetNoAttributes() {
    instance.setNoAttributes(10);
    assertEquals(10, instance.noAttributes);
  }
  
  @Test
  public void testSparseInstance() {
    SparseInstance instance = new SparseInstance();
    assertNotNull(instance.data);
    assertEquals(-1, instance.noAttributes);
    assertEquals(0.0, instance.defaultValue, 0.01);
  }
  
  @Test
  public void testSparseInstanceWithNoAttributes() {
    SparseInstance instance = new SparseInstance(10);
    assertNotNull(instance.data);
    assertEquals(10, instance.noAttributes);
    assertEquals(0.0, instance.defaultValue, 0.01);
  }
  
  @Test
  public void testSparseInstanceWithDefaultValue() {
    SparseInstance instance = new SparseInstance(10, 2.5);
    assertNotNull(instance.data);
    assertEquals(10, instance.noAttributes);
    assertEquals(2.5, instance.defaultValue, 0.01);
  }
  
  @Test
  public void testSparseInstanceWithClassValue() {
    String classValue = "ClassA";
    SparseInstance instance = new SparseInstance(10, classValue);
    assertNotNull(instance.data);
    assertEquals(10, instance.noAttributes);
    assertEquals(classValue, instance.classValue());
  }
  
  @Test
  public void testSparseInstanceWithAllParameters() {
    String classValue = "ClassA";
    SparseInstance instance = new SparseInstance(10, 2.5, classValue);
    assertNotNull(instance.data);
    assertEquals(10, instance.noAttributes);
    assertEquals(2.5, instance.defaultValue, 0.01);
    assertEquals(classValue, instance.classValue());
  }
  
  @Test
  public void testSparseInstanceWithDataVector() {
    double[] datavector = {1.0, 2.0, 3.0};
    SparseInstance instance = new SparseInstance(datavector);
    assertNotNull(instance.data);
    assertEquals(datavector.length, instance.noAttributes);
    assertEquals(0.0, instance.defaultValue, 0.01);
  }
  
  @Test
  public void testSparseInstanceWithDataVectorAndDefaultValue() {
    double[] datavector = {1.0, 2.0, 3.0};
    SparseInstance instance = new SparseInstance(datavector, 2.5);
    assertNotNull(instance.data);
    assertEquals(datavector.length, instance.noAttributes);
    assertEquals(2.5, instance.defaultValue, 0.01);
  }
  
  @Test
  public void testSparseInstanceWithDataVectorAndClassValue() {
    double[] datavector = {1.0, 2.0, 3.0};
    String classValue = "ClassA";
    SparseInstance instance = new SparseInstance(datavector, classValue);
    assertNotNull(instance.data);
    assertEquals(datavector.length, instance.noAttributes);
    assertEquals(0.0, instance.defaultValue, 0.01);
    assertEquals(classValue, instance.classValue());
  }
  
  @Test
  public void testSparseInstanceWithDataVectorDefaultValueAndClassValue() {
    double[] datavector = {1.0, 2.0, 3.0};
    String classValue = "ClassA";
    SparseInstance instance = new SparseInstance(datavector, 2.5, classValue);
    assertNotNull(instance.data);
    assertEquals(datavector.length, instance.noAttributes);
    assertEquals(2.5, instance.defaultValue, 0.01);
    assertEquals(classValue, instance.classValue());
  }
  
  @Test
  public void testInitiate() {
    double[] datavector = {1.0, 2.0, 0.0, 4.0};
    instance.initiate(datavector);
    assertEquals(4, instance.noAttributes);
    assertEquals(1.0, instance.value(0), 0.01);
    assertEquals(2.0, instance.value(1), 0.01);
    assertEquals(4.0, instance.value(3), 0.01);
  }
  
  @Test
  public void testValue() {
    instance.put(0, 1.5);
    assertEquals(1.5, instance.value(0), 0.01);
  }
  
  @Test
  public void testClear() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    instance.clear();
    assertTrue(instance.isEmpty());
  }
  
  @Test
  public void testContainsKey() {
    instance.put(0, 1.5);
    assertTrue(instance.containsKey(0));
    assertFalse(instance.containsKey(1));
  }
  
  @Test
  public void testContainsValue() {
    instance.put(0, 1.5);
    assertTrue(instance.containsValue(1.5));
    assertFalse(instance.containsValue(2.0));
  }
  
  @Test
  public void testEntrySet() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
    assertEquals(2, entrySet.size());
    for (Map.Entry<Integer, Double> entry : entrySet) {
      assertTrue(instance.containsKey(entry.getKey()));
      assertTrue(instance.containsValue(entry.getValue()));
    }
  }
  
  @Test
  public void testGet() {
    instance.put(0, 1.5);
    assertEquals(1.5, instance.get(0), 0.01);
    assertEquals(instance.defaultValue, instance.get(1), 0.01);
  }
  
  @Test
  public void testIsEmpty() {
    assertTrue(instance.isEmpty());
    instance.put(0, 1.5);
    assertFalse(instance.isEmpty());
  }
  
  @Test
  public void testKeySet() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    TreeSet<Integer> keySet = instance.keySet();
    assertEquals(2, keySet.size());
    for (Integer key : keySet) {
      assertTrue(instance.containsKey(key));
    }
  }
  
  @Test
  public void testPut() {
    double oldValue = instance.put(0, 1.5);
    assertNull(oldValue);
    assertEquals(1.5, instance.value(0), 0.01);
  }
  
  @Test
  public void testPutAll() {
    Map<Integer, Double> map = new HashMap<>();
    map.put(0, 1.5);
    map.put(1, 2.0);
    instance.putAll(map);
    assertEquals(1.5, instance.value(0), 0.01);
    assertEquals(2.0, instance.value(1), 0.01);
  }
  
  @Test
  public void testRemove() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    double removedValue = instance.remove(0);
    assertEquals(1.5, removedValue, 0.01);
    assertFalse(instance.containsKey(0));
  }
  
  @Test
  public void testSize() {
    assertEquals(0, instance.size());
    instance.put(0, 1.5);
    assertEquals(1, instance.size());
  }
  
  @Test
  public void testValues() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    Collection<Double> values = instance.values();
    assertEquals(2, values.size());
    for (double value : values) {
      assertTrue(instance.containsValue(value));
    }
  }
  
  @Test
  public void testNoAttributes() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    assertEquals(2, instance.noAttributes());
  }
  
  @Test
  public void testRemoveAttribute() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    instance.put(2, 3.0);
    instance.put(3, 4.0);
    instance.removeAttribute(2);
    assertEquals(3, instance.noAttributes());
    assertEquals(4.0, instance.value(2), 0.01);
    assertFalse(instance.containsKey(3));
  }
  
  @Test
  public void testToString() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    String expected = "{" + instance.data.toString() + ";" + instance.classValue() + "}";
    assertEquals(expected, instance.toString());
  }
  
  @Test
  public void testHashCode() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    int expected = 31 * instance.data.hashCode() + (int) (Double.doubleToLongBits(instance.defaultValue) ^ (Double.doubleToLongBits(instance.defaultValue) >>> 32));
    assertEquals(expected, instance.hashCode());
  }
  
  @Test
  public void testEquals() {
    SparseInstance other = new SparseInstance();
    other.put(0, 1.5);
    other.put(1, 2.0);
    assertFalse(instance.equals(other));
    
    other.clear();
    other.put(0, 1.5);
    other.put(1, 2.0);
    other.setClassValue("ClassA");
    assertFalse(instance.equals(other));
    
    other.clear();
    other.put(0, 1.5);
    other.put(1, 2.0);
    other.setClassValue(instance.classValue());
    assertTrue(instance.equals(other));
  }
  
  @Test
  public void testCopy() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    instance.setClassValue("ClassA");
    SparseInstance copy = (SparseInstance) instance.copy();
    assertEquals(instance.data, copy.data);
    assertEquals(instance.defaultValue, copy.defaultValue, 0.01);
    assertEquals(instance.noAttributes, copy.noAttributes);
    assertEquals(instance.classValue(), copy.classValue());
  }
  
  @Test
  public void testRemoveAttributes() {
    instance.put(0, 1.5);
    instance.put(1, 2.0);
    instance.put(2, 3.0);
    Set<Integer> indices = new HashSet<>();
    indices.add(0);
    indices.add(2);
    instance.removeAttributes(indices);
    assertFalse(instance.containsKey(0));
    assertTrue(instance.containsKey(1));
    assertFalse(instance.containsKey(2));
  }
}

