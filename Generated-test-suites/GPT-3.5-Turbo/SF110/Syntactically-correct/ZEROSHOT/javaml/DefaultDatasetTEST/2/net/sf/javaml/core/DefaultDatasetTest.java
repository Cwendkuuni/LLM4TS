import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

  @Test
  public void testAddAll() {
    Collection<Instance> coll = new ArrayList<Instance>();
    coll.add(new Instance());
    DefaultDataset dataset = new DefaultDataset();
    assertTrue(dataset.addAll(coll));
  }

  @Test
  public void testAddAllAtIndex() {
    Collection<Instance> coll = new ArrayList<Instance>();
    coll.add(new Instance());
    DefaultDataset dataset = new DefaultDataset();
    assertTrue(dataset.addAll(0, coll));
  }

  @Test
  public void testClear() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.clear();
    assertEquals(dataset.size(), 0);
  }

  @Test
  public void testAdd() {
    Instance instance = new Instance();
    DefaultDataset dataset = new DefaultDataset();
    assertTrue(dataset.add(instance));
  }

  @Test
  public void testAddAtIndex() {
    Instance instance = new Instance();
    DefaultDataset dataset = new DefaultDataset();
    dataset.add(0, instance);
    assertEquals(dataset.get(0), instance);
  }

  @Test
  public void testAddElement() {
    Instance instance = new Instance();
    DefaultDataset dataset = new DefaultDataset();
    dataset.addElement(instance);
    assertEquals(dataset.lastElement(), instance);
  }

  @Test
  public void testInsertElementAt() {
    Instance instance = new Instance();
    DefaultDataset dataset = new DefaultDataset();
    dataset.insertElementAt(instance, 0);
    assertEquals(dataset.firstElement(), instance);
  }

  @Test
  public void testSetElementAt() {
    Instance instance = new Instance();
    DefaultDataset dataset = new DefaultDataset();
    dataset.addElement(new Instance());
    dataset.setElementAt(instance, 0);
    assertEquals(dataset.get(0), instance);
  }

  @Test
  public void testInstance() {
    Instance instance = new Instance();
    DefaultDataset dataset = new DefaultDataset();
    dataset.add(instance);
    assertEquals(dataset.instance(0), instance);
  }

  @Test
  public void testClasses() {
    DefaultDataset dataset = new DefaultDataset();
    assertNotNull(dataset.classes());
  }

  @Test
  public void testKNearest() {
    int k = 3;
    Instance instance = new Instance();
    DistanceMeasure dm = new EuclideanDistance();
    DefaultDataset dataset = new DefaultDataset();
    Set<Instance> nearest = dataset.kNearest(k, instance, dm);
    assertNotNull(nearest);
  }

  @Test
  public void testRemoveFarthest() {
    Map<Instance, Double> vector = new HashMap<Instance, Double>();
    vector.put(new Instance(), 1.0);
    vector.put(new Instance(), 2.0);
    DefaultDataset dataset = new DefaultDataset();
    double max = dataset.removeFarthest(vector);
    assertEquals(max, 2.0, 0.001);
  }

  @Test
  public void testFolds() {
    int numFolds = 5;
    Random rg = new Random();
    DefaultDataset dataset = new DefaultDataset();
    Dataset[] folds = dataset.folds(numFolds, rg);
    assertEquals(folds.length, numFolds);
  }

  @Test
  public void testNoAttributes() {
    DefaultDataset dataset = new DefaultDataset();
    assertEquals(dataset.noAttributes(), 0);
  }

  @Test
  public void testClassIndex() {
    Object clazz = new Object();
    DefaultDataset dataset = new DefaultDataset();
    int classIndex = dataset.classIndex(clazz);
    assertEquals(classIndex, -1);
  }

  @Test
  public void testClassValue() {
    int index = 0;
    DefaultDataset dataset = new DefaultDataset();
    Object classValue = dataset.classValue(index);
    assertNull(classValue);
  }

  @Test
  public void testCopy() {
    DefaultDataset dataset = new DefaultDataset();
    Dataset copy = dataset.copy();
    assertNotNull(copy);
    assertEquals(copy.size(), dataset.size());
  }
}