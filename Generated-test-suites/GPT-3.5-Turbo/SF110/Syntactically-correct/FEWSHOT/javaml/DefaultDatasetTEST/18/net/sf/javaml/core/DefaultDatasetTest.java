import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.Set;
import java.util.Vector;
import java.util.List;
import java.util.Random;

public class DefaultDatasetTest {
  
  @Test
  public void testDefaultDataset_Constructor_Collection() {
    Collection<Instance> coll = new Vector<Instance>();
    DefaultDataset dataset = new DefaultDataset(coll);
    assertEquals(0, dataset.maxAttributes);
    assertNotNull(dataset.classes);
  }
  
  @Test
  public void testDefaultDataset_Constructor() {
    DefaultDataset dataset = new DefaultDataset();
    assertEquals(0, dataset.maxAttributes);
    assertNotNull(dataset.classes);
  }
  
  @Test
  public void testDefaultDataset_check_Collection() {
    DefaultDataset dataset = new DefaultDataset();
    Collection<Instance> coll = new Vector<Instance>();
    coll.add(new Instance());
    dataset.check(coll);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_check_Instance() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    dataset.check(instance);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_addAll_Collection() {
    DefaultDataset dataset = new DefaultDataset();
    Collection<Instance> coll = new Vector<Instance>();
    coll.add(new Instance());
    boolean result = dataset.addAll(coll);
    assertTrue(result);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_addAll_Collection_Index() {
    DefaultDataset dataset = new DefaultDataset();
    Collection<Instance> coll = new Vector<Instance>();
    coll.add(new Instance());
    boolean result = dataset.addAll(0, coll);
    assertTrue(result);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_clear() {
    DefaultDataset dataset = new DefaultDataset();
    dataset.classes.add("class1");
    dataset.add(new Instance());
    dataset.clear();
    assertEquals(0, dataset.classes.size());
    assertEquals(0, dataset.size());
  }
  
  @Test
  public void testDefaultDataset_add_Instance() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    boolean result = dataset.add(instance);
    assertTrue(result);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_add_Index_Instance() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    dataset.add(0, instance);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_addElement() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    dataset.addElement(instance);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_insertElementAt_Instance_Index() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    dataset.insertElementAt(instance, 0);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_setElementAt_Instance_Index() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    dataset.setElementAt(instance, 0);
    assertEquals(0, dataset.maxAttributes);
    assertEquals(0, dataset.classes.size());
  }
  
  @Test
  public void testDefaultDataset_instance_Index() {
    DefaultDataset dataset = new DefaultDataset();
    Instance instance = new Instance();
    dataset.add(instance);
    Instance result = dataset.instance(0);
    assertEquals(instance, result);
  }
  
  @Test
  public void testDefaultDataset_classes() {
    DefaultDataset dataset = new DefaultDataset();
    TreeSet<Object> result = dataset.classes();
    assertNotNull(result);
  }
  
  @Test
  public void testDefaultDataset_kNearest() {
    DefaultDataset dataset = new DefaultDataset();
    Instance inst = new Instance();
    DistanceMeasure dm = new DistanceMeasureImpl();
    Set<Instance> result = dataset.kNearest(3, inst, dm);
    assertNotNull(result);
  }
  
  @Test
  public void testDefaultDataset_removeFarthest() {
    DefaultDataset dataset = new DefaultDataset();
    Map<Instance, Double> vector = new HashMap<Instance, Double>();
    double result = dataset.removeFarthest(vector);
    assertEquals(0.0, result, 0.0001);
  }
  
  @Test
  public void testDefaultDataset_folds() {
    DefaultDataset dataset = new DefaultDataset();
    Random rg = new Random();
    Dataset[] result = dataset.folds(5, rg);
    assertNotNull(result);
    assertEquals(5, result.length);
  }
  
  @Test
  public void testDefaultDataset_noAttributes() {
    DefaultDataset dataset = new DefaultDataset();
    int result = dataset.noAttributes();
    assertEquals(0, result);
  }
  
  @Test
  public void testDefaultDataset_classIndex() {
    DefaultDataset dataset = new DefaultDataset();
    int result = dataset.classIndex("class1");
    assertEquals(-1, result);
  }
  
  @Test
  public void testDefaultDataset_classValue() {
    DefaultDataset dataset = new DefaultDataset();
    Object result = dataset.classValue(0);
    assertNull(result);
  }
  
  @Test
  public void testDefaultDataset_copy() {
    DefaultDataset dataset = new DefaultDataset();
    Dataset result = dataset.copy();
    assertNotNull(result);
  }
}