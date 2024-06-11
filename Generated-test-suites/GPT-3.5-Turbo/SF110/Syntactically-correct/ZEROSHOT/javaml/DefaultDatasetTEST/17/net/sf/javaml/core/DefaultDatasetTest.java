import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

  @Test
  public void testAddAll() {
    DefaultDataset dataset = new DefaultDataset();
    List<Instance> instances = new ArrayList<>();
    instances.add(new Instance());
    instances.add(new Instance());
    boolean result = dataset.addAll(instances);
    assertTrue(result);
    assertEquals(2, dataset.size());
  }

  @Test
  public void testAddAllAtIndex() {
    DefaultDataset dataset = new DefaultDataset();
    List<Instance> instances = new ArrayList<>();
    instances.add(new Instance());
    instances.add(new Instance());
    dataset.addAll(instances);
    Instance newInstance = new Instance();
    boolean result = dataset.addAll(1, Collections.singletonList(newInstance));
    assertTrue(result);
    assertEquals(newInstance, dataset.get(1));
  }

  @Test
  public void testClear() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.add(new Instance());
    dataset.add(new Instance());
    dataset.clear();
    assertTrue(dataset.isEmpty());
  }

  @Test
  public void testAdd() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    boolean result = dataset.add(instance);
    assertTrue(result);
    assertEquals(1, dataset.size());
    assertEquals(instance, dataset.get(0));
  }

  @Test
  public void testAddAtIndex() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.add(new Instance());
    dataset.add(new Instance());
    Instance newInstance = new Instance();
    dataset.add(1, newInstance);
    assertEquals(newInstance, dataset.get(1));
  }

  @Test
  public void testAddElement() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    dataset.addElement(instance);
    assertEquals(1, dataset.size());
    assertEquals(instance, dataset.get(0));
  }

  @Test
  public void testInsertElementAt() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.add(new Instance());
    dataset.add(new Instance());
    Instance newInstance = new Instance();
    dataset.insertElementAt(newInstance, 1);
    assertEquals(newInstance, dataset.get(1));
  }

  @Test
  public void testSetElementAt() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.add(new Instance());
    dataset.add(new Instance());
    Instance newInstance = new Instance();
    dataset.setElementAt(newInstance, 1);
    assertEquals(newInstance, dataset.get(1));
  }

  @Test
  public void testInstance() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    dataset.add(instance);
    Instance retrievedInstance = dataset.instance(0);
    assertEquals(instance, retrievedInstance);
  }

  @Test
  public void testClasses() {
    DefaultDataset dataset = new DefaultDataset();
    TreeSet<Object> classes = new TreeSet<>();
    classes.add("class1");
    classes.add("class2");
    dataset.classes = classes;
    SortedSet<Object> retrievedClasses = dataset.classes();
    assertEquals(classes, retrievedClasses);
  }

  @Test
  public void testKNearest() {
    DefaultDataset dataset = new DefaultDataset();
    Instance inst = new Instance();
    DistanceMeasure dm = new DistanceMeasureImpl();
    dataset.add(new Instance());
    dataset.add(new Instance());
    Set<Instance> nearest = dataset.kNearest(1, inst, dm);
    assertEquals(1, nearest.size());
  }

  @Test
  public void testRemoveFarthest() {
    DefaultDataset dataset = new DefaultDataset();
    Map<Instance, Double> vector = new HashMap<>();
    Instance inst1 = new Instance();
    instance1.setValue("attr1", 1);
    Instance inst2 = new Instance();
    instance2.setValue("attr1", 2);
    double max = dataset.removeFarthest(vector);
    assertEquals(0.0, max, 0.001);
    vector.put(inst1, 0.5);
    vector.put(inst2, 0.7);
    max = dataset.removeFarthest(vector);
    assertEquals(0.7, max, 0.001);
    assertEquals(1, vector.size());
    assertFalse(vector.containsKey(inst2));
  }

  @Test
  public void testFolds() {
    DefaultDataset dataset = new DefaultDataset();
    Random rg = new Random();
    Dataset[] folds = dataset.folds(5, rg);
    assertEquals(5, folds.length);
    for (Dataset fold : folds) {
      assertTrue(fold instanceof Fold);
    }
  }

  @Test
  public void testNoAttributes() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.add(new Instance());
    dataset.add(new Instance());
    int numAttributes = dataset.noAttributes();
    assertEquals(0, numAttributes);
  }

  @Test
  public void testClassIndex() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.classes = new TreeSet<>();
    dataset.classes.add("class1");
    dataset.classes.add("class2");
    int classIndex1 = dataset.classIndex("class1");
    int classIndex2 = dataset.classIndex("class2");
    assertEquals(0, classIndex1);
    assertEquals(1, classIndex2);
  }

  @Test
  public void testClassValue() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.classes = new TreeSet<>();
    dataset.classes.add("class1");
    dataset.classes.add("class2");
    Object classValue1 = dataset.classValue(0);
    Object classValue2 = dataset.classValue(1);
    assertEquals("class1", classValue1);
    assertEquals("class2", classValue2);
  }

  @Test
  public void testCopy() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.add(new Instance());
    dataset.add(new Instance());
    Dataset copy = dataset.copy();
    assertEquals(dataset.size(), copy.size());
    for (int i = 0; i < dataset.size(); i++) {
      assertEquals(dataset.get(i), copy.get(i));
    }
  }
}