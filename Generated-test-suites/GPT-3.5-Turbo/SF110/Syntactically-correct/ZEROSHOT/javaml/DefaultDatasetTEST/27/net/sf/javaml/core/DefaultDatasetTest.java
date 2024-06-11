package net.sf.javaml.core;

import org.junit.Test;
import org.junit.Assert;
import net.sf.javaml.distance.*;

import java.util.*;

public class DefaultDatasetTest {

  @Test
  public void testAddAll() {
    List<Instance> instances = new ArrayList<>();
    DefaultDataset dataset = new DefaultDataset();

    Instance instance1 = new Instance();
    Instance instance2 = new Instance();

    instances.add(instance1);
    instances.add(instance2);

    boolean result = dataset.addAll(instances);

    Assert.assertTrue(result);
    Assert.assertEquals(2, dataset.size());
    Assert.assertTrue(dataset.contains(instance1));
    Assert.assertTrue(dataset.contains(instance2));
  }

  @Test
  public void testAddAllAtIndex() {
    List<Instance> instances = new ArrayList<>();
    DefaultDataset dataset = new DefaultDataset();

    Instance instance1 = new Instance();
    Instance instance2 = new Instance();

    instances.add(instance1);
    instances.add(instance2);

    boolean result = dataset.addAll(0, instances);

    Assert.assertTrue(result);
    Assert.assertEquals(2, dataset.size());
    Assert.assertTrue(dataset.contains(instance1));
    Assert.assertTrue(dataset.contains(instance2));
    Assert.assertEquals(instance1, dataset.get(0));
    Assert.assertEquals(instance2, dataset.get(1));
  }

  @Test
  public void testClear() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance1 = new Instance();
    Instance instance2 = new Instance();

    dataset.add(instance1);
    dataset.add(instance2);

    dataset.clear();

    Assert.assertEquals(0, dataset.size());
    Assert.assertFalse(dataset.contains(instance1));
    Assert.assertFalse(dataset.contains(instance2));
  }

  @Test
  public void testAdd() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();

    boolean result = dataset.add(instance);

    Assert.assertTrue(result);
    Assert.assertEquals(1, dataset.size());
    Assert.assertTrue(dataset.contains(instance));
    Assert.assertEquals(instance, dataset.get(0));
  }

  @Test
  public void testAddAtIndex() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();

    dataset.add(0, instance);

    Assert.assertEquals(1, dataset.size());
    Assert.assertTrue(dataset.contains(instance));
    Assert.assertEquals(instance, dataset.get(0));
  }

  @Test
  public void testAddElement() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();

    dataset.addElement(instance);

    Assert.assertEquals(1, dataset.size());
    Assert.assertTrue(dataset.contains(instance));
    Assert.assertEquals(instance, dataset.get(0));
  }

  @Test
  public void testInsertElementAt() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();

    dataset.insertElementAt(instance, 0);

    Assert.assertEquals(1, dataset.size());
    Assert.assertTrue(dataset.contains(instance));
    Assert.assertEquals(instance, dataset.get(0));
  }
  
  @Test
  public void testSetElementAt() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance1 = new Instance();
    Instance instance2 = new Instance();

    dataset.add(instance1);

    dataset.setElementAt(instance2, 0);

    Assert.assertEquals(1, dataset.size());
    Assert.assertFalse(dataset.contains(instance1));
    Assert.assertTrue(dataset.contains(instance2));
    Assert.assertEquals(instance2, dataset.get(0));
  }

  @Test
  public void testInstance() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();

    dataset.add(instance);

    Instance result = dataset.instance(0);

    Assert.assertEquals(instance, result);
  }

  @Test
  public void testClasses() {
    DefaultDataset dataset = new DefaultDataset();
    TreeSet<Object> classes = new TreeSet<>();
    classes.add("A");
    classes.add("B");

    dataset.classes = classes;

    SortedSet<Object> result = dataset.classes();

    Assert.assertEquals(classes, result);
  }

  @Test
  public void testKNearest() {
    DefaultDataset dataset = new DefaultDataset();

    Instance instance1 = new Instance();
    Instance instance2 = new Instance();
    Instance instance3 = new Instance();

    dataset.add(instance1);
    dataset.add(instance2);
    dataset.add(instance3);

    DistanceMeasure dm = new DistanceMeasure();

    Set<Instance> result = dataset.kNearest(2, instance1, dm);

    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.contains(instance2));
    Assert.assertTrue(result.contains(instance3));
  }

  @Test
  public void testRemoveFarthest() {
    DefaultDataset dataset = new DefaultDataset();
    Map<Instance, Double> vector = new HashMap<>();
    Instance instance1 = new Instance();
    Instance instance2 = new Instance();
    Instance instance3 = new Instance();

    vector.put(instance1, 1.0);
    vector.put(instance2, 2.0);
    vector.put(instance3, 3.0);
    
    double result = dataset.removeFarthest(vector);

    Assert.assertEquals(3.0, result, 0.0);
    Assert.assertEquals(2, vector.size());
    Assert.assertTrue(vector.containsKey(instance1));
    Assert.assertTrue(vector.containsKey(instance2));
    Assert.assertFalse(vector.containsKey(instance3));
  }

  @Test
  public void testFolds() {
    DefaultDataset dataset = new DefaultDataset();
    Random rg = new Random();

    dataset.add(new Instance());

    Dataset[] result = dataset.folds(2, rg);

    Assert.assertEquals(2, result.length);
    Assert.assertEquals(Fold.class, result[0].getClass());
    Assert.assertEquals(Fold.class, result[1].getClass());
  }

  @Test
  public void testNoAttributes() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();

    dataset.add(instance);

    int result = dataset.noAttributes();

    Assert.assertEquals(0, result);
  }

  @Test
  public void testClassIndex() {
    DefaultDataset dataset = new DefaultDataset();
    TreeSet<Object> classes = new TreeSet<>();
    classes.add("A");
    classes.add("B");

    dataset.classes = classes;

    int result1 = dataset.classIndex("A");
    int result2 = dataset.classIndex("B");
    int result3 = dataset.classIndex("C");

    Assert.assertEquals(0, result1);
    Assert.assertEquals(1, result2);
    Assert.assertEquals(-1, result3);
  }

  @Test
  public void testClassValue() {
    DefaultDataset dataset = new DefaultDataset();
    TreeSet<Object> classes = new TreeSet<>();
    classes.add("A");
    classes.add("B");

    dataset.classes = classes;

    Object result1 = dataset.classValue(0);
    Object result2 = dataset.classValue(1);
    Object result3 = dataset.classValue(2);

    Assert.assertEquals("A", result1);
    Assert.assertEquals("B", result2);
    Assert.assertNull(result3);
  }

  @Test
  public void testCopy() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance1 = new Instance();
    Instance instance2 = new Instance();

    dataset.add(instance1);
    dataset.add(instance2);

    Dataset result = dataset.copy();

    Assert.assertEquals(DefaultDataset.class, result.getClass());
    Assert.assertNotSame(dataset, result);
    Assert.assertEquals(dataset.size(), result.size());
    Assert.assertTrue(result.contains(instance1));
    Assert.assertTrue(result.contains(instance2));
  }
}