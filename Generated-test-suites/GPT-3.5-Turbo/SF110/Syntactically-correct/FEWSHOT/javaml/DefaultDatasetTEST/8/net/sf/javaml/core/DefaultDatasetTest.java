package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.distance.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.List;
import java.util.Random;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    
    @Before
    public void setup() {
        dataset = new DefaultDataset();
    }
    /*
     * Test Constructor
     */
    @Test
    public void testDefaultDataset() {
      assertEquals(0, dataset.maxAttributes);
      assertNotNull(dataset.classes);
      assertEquals(0, dataset.classes.size());
    }
    
    /*
     * Test Constructor with Collection parameter
     */
    @Test
    public void testDefaultDatasetWithCollection() {
      Collection<Instance> instances = new Vector<Instance>();
      instances.add(new Instance());
      instances.add(new Instance());
      dataset = new DefaultDataset(instances);
      assertEquals(instances.size(), dataset.size());
      assertTrue(dataset.classes.isEmpty());
    }
    
    /*
     * Test addAll method with Collection parameter
     */
     @Test
     public void testAddAll() {
       Collection<Instance> instances = new Vector<Instance>();
       instances.add(new Instance());
       instances.add(new Instance());
       assertTrue(dataset.addAll(instances));
       assertEquals(instances.size(), dataset.size());
     }
     
     /*
     * Test addAll method with index parameter
     */
     @Test
     public void testAddAllWithIndex() {
       Collection<Instance> instances = new Vector<Instance>();
       instances.add(new Instance());
       instances.add(new Instance());
       assertTrue(dataset.addAll(0, instances));
       assertEquals(instances.size(), dataset.size());
     }
     
     /*
     * Test add method
     */
     @Test
     public void testAdd() {
       Instance instance = new Instance();
       assertTrue(dataset.add(instance));
       assertEquals(1, dataset.size());
     }
     
     /*
     * Test addElement method
     */
     @Test
     public void testAddElement() {
       Instance instance = new Instance();
       dataset.addElement(instance);
       assertEquals(1, dataset.size());
     }
     
     /*
     * Test insertElementAt method
     */
     @Test
     public void testInsertElementAt() {
       Instance instance = new Instance();
       dataset.insertElementAt(instance, 0);
       assertEquals(1, dataset.size());
     }
     
     /*
     * Test setElementAt method
     */
     @Test
     public void testSetElementAt() {
       Instance instance = new Instance();
       dataset.add(new Instance());
       dataset.setElementAt(instance, 0);
       assertEquals(instance, dataset.get(0));
     }
     
     /*
     * Test classIndex method with valid class
     */
     @Test
     public void testClassIndexValidClass() {
       dataset.classes.add("class1");
       dataset.classes.add("class2");
       assertEquals(0, dataset.classIndex("class1"));
       assertEquals(1, dataset.classIndex("class2"));
     }
     
    /*
     * Test classIndex method with invalid class
     */
     @Test
     public void testClassIndexInvalidClass() {
       dataset.classes.add("class1");
       dataset.classes.add("class2");
       assertEquals(-1, dataset.classIndex("class3"));
     }
     
     /*
     * Test classValue method
     */
     @Test
     public void testClassValue() {
       dataset.classes.add("class1");
       dataset.classes.add("class2");
       assertEquals("class1", dataset.classValue(0));
       assertEquals("class2", dataset.classValue(1));
       assertNull(dataset.classValue(2));
     }
     
     /*
     * Test copy method
     */
     @Test
     public void testCopy() {
       Instance instance1 = new Instance();
       Instance instance2 = new Instance();
       dataset.add(instance1);
       dataset.add(instance2);
       Dataset copy = dataset.copy();
       assertNotSame(dataset, copy);
       assertEquals(dataset.size(), copy.size());
       assertNotSame(dataset.get(0), copy.instance(0));
       assertNotSame(dataset.get(1), copy.instance(1));
     }
     
     /*
     * Test clear method
     */
     @Test
     public void testClear() {
       dataset.add(new Instance());
       dataset.add(new Instance());
       dataset.clear();
       assertTrue(dataset.isEmpty());
       assertTrue(dataset.classes.isEmpty());
     }
     
     /*
     * Test noAttributes method with empty dataset
     */
     @Test
     public void testNoAttributesEmptyDataset() {
       assertEquals(0, dataset.noAttributes());
     }
     
     /*
     * Test noAttributes method with non-empty dataset
     */
     @Test
     public void testNoAttributesNonEmptyDataset() {
       Instance instance = new Instance();
       instance.put(1, 2);
       dataset.add(instance);
       assertEquals(1, dataset.noAttributes());
     }
     
     /*
     * Test kNearest method
     */
     @Test
     public void testKNearest() {
       Instance inst1 = new Instance();
       Instance inst2 = new Instance();
       Instance inst3 = new Instance();
       inst1.put(1, 2);
       inst2.put(2, 3);
       inst3.put(3, 4);
       dataset.add(inst1);
       dataset.add(inst2);
       dataset.add(inst3);
       DistanceMeasure dm = new EuclideanDistance();
       Set<Instance> closest = dataset.kNearest(2, inst1, dm);
       assertTrue(closest.contains(inst2));
       assertTrue(closest.contains(inst3));
       assertFalse(closest.contains(inst1));
     }
     
     /*
     * Test folds method
     */
     @Test
     public void testFolds() {
       Random rg = new Random();
       Dataset[] folds = dataset.folds(3, rg);
       assertEquals(3, folds.length);
       for (Dataset fold : folds) {
         assertEquals(dataset.size() / 3 + 1, fold.size());
       }
     }
     
}