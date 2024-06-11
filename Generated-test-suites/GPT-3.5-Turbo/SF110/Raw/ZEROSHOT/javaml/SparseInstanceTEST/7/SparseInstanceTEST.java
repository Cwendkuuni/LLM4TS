package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {
  
  @Test
  public void testSetNoAttributes() {
    SparseInstance instance = new SparseInstance();
    instance.setNoAttributes(5);
    int result = instance.noAttributes();
    assertEquals(5, result);
  }
  
  @Test
  public void testSparseInstance() {
    SparseInstance instance = new SparseInstance();
    int result = instance.noAttributes();
    assertEquals(-1, result);
  }
  
  @Test
  public void testSparseInstanceWithAttributes() {
    SparseInstance instance = new SparseInstance(5);
    int result = instance.noAttributes();
    assertEquals(5, result);
  }
  
  @Test
  public void testSparseInstanceWithAttributesAndDefaultValue() {
    SparseInstance instance = new SparseInstance(5, 0.0);
    int result = instance.noAttributes();
    assertEquals(5, result);
  }
  
  @Test
  public void testSparseInstanceWithAttributesAndClassValue() {
    Object classValue = new Object();
    SparseInstance instance = new SparseInstance(5, classValue);
    int result = instance.noAttributes();
    assertEquals(5, result);
    assertSame(classValue, instance.classValue());
  }
  
  @Test
  public void testSparseInstanceWithAttributesAndDefaultValueAndClassValue() {
    Object classValue = new Object();
    SparseInstance instance = new SparseInstance(5, 0.0, classValue);
    int result = instance.noAttributes();
    assertEquals(5, result);
    assertSame(classValue, instance.classValue());
  }
  
  @Test
  public void testSparseInstanceWithDataVector() {
    double[] datavector = {1.0, 2.0, 3.0};
    SparseInstance instance = new SparseInstance(datavector);
    int result = instance.noAttributes();
    assertEquals(3, result);
    assertEquals(1.0, instance.get(0), 0.0);
    assertEquals(2.0, instance.get(1), 0.0);
    assertEquals(3.0, instance.get(2), 0.0);
  }
  
  @Test
  public void testSparseInstanceWithDataVectorAndDefaultValue() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector, 0.0);
    int result = instance.noAttributes();
    assertEquals(2, result);
    assertEquals(1.0, instance.get(0), 0.0);
    assertEquals(2.0, instance.get(1), 0.0);
  }
  
  @Test
  public void testSparseInstanceWithDataVectorAndClassValue() {
    double[] datavector = {1.0, 2.0};
    Object classValue = new Object();
    SparseInstance instance = new SparseInstance(datavector, classValue);
    int result = instance.noAttributes();
    assertEquals(2, result);
    assertEquals(1.0, instance.get(0), 0.0);
    assertEquals(2.0, instance.get(1), 0.0);
    assertSame(classValue, instance.classValue());
  }
  
  @Test
  public void testSparseInstanceWithDataVectorAndDefaultValueAndClassValue() {
    double[] datavector = {1.0, 2.0, 0.0};
    Object classValue = new Object();
    SparseInstance instance = new SparseInstance(datavector, 0.0, classValue);
    int result = instance.noAttributes();
    assertEquals(2, result);
    assertEquals(1.0, instance.get(0), 0.0);
    assertEquals(2.0, instance.get(1), 0.0);
    assertSame(classValue, instance.classValue());
  }
  
  @Test
  public void testInitiate() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance();
    instance.initiate(datavector);
    int result = instance.noAttributes();
    assertEquals(2, result);
    assertEquals(1.0, instance.get(0), 0.0);
    assertEquals(2.0, instance.get(1), 0.0);
  }
  
  @Test
  public void testValue() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    double result = instance.value(1);
    assertEquals(2.0, result, 0.0);
  }
  
  @Test
  public void testClear() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    instance.clear();
    assertTrue(instance.isEmpty());
  }
  
  @Test
  public void testContainsKey() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    boolean result = instance.containsKey(1);
    assertTrue(result);
  }
  
  @Test
  public void testContainsValue() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    boolean result = instance.containsValue(2.0);
    assertTrue(result);
  }
  
  @Test
  public void testEntrySet() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    int result = instance.entrySet().size();
    assertEquals(2, result);
  }
  
  @Test
  public void testGet() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    Double result = instance.get(1);
    assertEquals(2.0, result, 0.0);
  }
  
  @Test
  public void testIsEmptyForEmptyInstance() {
    SparseInstance instance = new SparseInstance();
    boolean result = instance.isEmpty();
    assertTrue(result);
  }
  
  @Test
  public void testIsEmptyForNonEmptyInstance() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    boolean result = instance.isEmpty();
    assertFalse(result);
  }
  
  @Test
  public void testKeySet() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    int result = instance.keySet().size();
    assertEquals(2, result);
  }
  
  @Test
  public void testPut() {
    SparseInstance instance = new SparseInstance();
    Double result = instance.put(1, 2.0);
    assertNull(result);
    assertEquals(2.0, instance.get(1), 0.0);
  }
  
  @Test
  public void testPutAll() {
    SparseInstance instance = new SparseInstance();
    HashMap<Integer, Double> map = new HashMap<Integer, Double>();
    map.put(1, 2.0);
    map.put(2, 3.0);
    instance.putAll(map);
    int result = instance.noAttributes();
    assertEquals(2, result);
    assertEquals(2.0, instance.get(1), 0.0);
    assertEquals(3.0, instance.get(2), 0.0);
  }
  
  @Test
  public void testRemove() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    Double result = instance.remove(1);
    assertEquals(2.0, result, 0.0);
    assertFalse(instance.containsKey(1));
  }
  
  @Test
  public void testSize() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    int result = instance.size();
    assertEquals(2, result);
  }
  
  @Test
  public void testValues() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    int result = instance.values().size();
    assertEquals(2, result);
  }
  
  @Test
  public void testNoAttributesWithExplicitNoAttribute() {
    SparseInstance instance = new SparseInstance(5);
    int result = instance.noAttributes();
    assertEquals(5, result);
  }
  
  @Test
  public void testNoAttributesWithEmptyInstance() {
    SparseInstance instance = new SparseInstance();
    int result = instance.noAttributes();
    assertEquals(0, result);
  }
  
  @Test
  public void testNoAttributesWithNonEmptyInstance() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    int result = instance.noAttributes();
    assertEquals(3, result);
  }
  
  @Test
  public void testRemoveAttribute() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    instance.removeAttribute(1);
    int result = instance.noAttributes();
    assertEquals(1, result);
    assertEquals(1.0, instance.get(0), 0.0);
  }
  
  @Test
  public void testToString() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    String result = instance.toString();
    assertEquals("{0=1.0;1=2.0;}", result);
  }
  
  @Test
  public void testHashCode() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    int result = instance.hashCode();
    assertEquals(93695704, result);
  }
  
  @Test
  public void testEqualsForSameInstance() {
    SparseInstance instance = new SparseInstance();
    boolean result = instance.equals(instance);
    assertTrue(result);
  }
  
  @Test
  public void testEqualsForNull() {
    SparseInstance instance = new SparseInstance();
    boolean result = instance.equals(null);
    assertFalse(result);
  }
  
  @Test
  public void testEqualsForDifferentClass() {
    SparseInstance instance = new SparseInstance();
    boolean result = instance.equals(new Object());
    assertFalse(result);
  }
  
  @Test
  public void testEqualsForDifferentData() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance1 = new SparseInstance(datavector);
    SparseInstance instance2 = new SparseInstance(datavector);
    boolean result = instance1.equals(instance2);
    assertTrue(result);
  }
  
  @Test
  public void testCopy() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance1 = new SparseInstance(datavector);
    instance1.setClassValue(new Object());
    SparseInstance instance2 = (SparseInstance)instance1.copy();
    boolean result = instance1.equals(instance2);
    assertTrue(result);
    assertNotSame(instance1, instance2);
    assertSame(instance1.classValue(), instance2.classValue());
  }
  
  @Test
  public void testRemoveAttributes() {
    double[] datavector = {1.0, 2.0, 0.0};
    SparseInstance instance = new SparseInstance(datavector);
    instance.removeAttributes(new HashSet<Integer>(){{add(1);}});
    int result = instance.noAttributes();
    assertEquals(1, result);
    assertEquals(1.0, instance.get(0), 0.0);
  }
}