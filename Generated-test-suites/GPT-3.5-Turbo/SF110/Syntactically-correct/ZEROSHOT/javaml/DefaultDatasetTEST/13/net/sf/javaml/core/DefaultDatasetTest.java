
package net.sf.javaml.core;
import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testAddAll() {
        Collection<Instance> collection = new ArrayList<>();
        collection.add(new Instance());
        collection.add(new Instance());
        assertTrue(dataset.addAll(collection));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllIndex() {
        Collection<Instance> collection = new ArrayList<>();
        collection.add(new Instance());
        collection.add(new Instance());
        dataset.add(new Instance());
        assertTrue(dataset.addAll(1, collection));
        assertEquals(3, dataset.size());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testSetElementAt() {
        dataset.add(new Instance());
        Instance instance = new Instance();
        dataset.setElementAt(instance, 0);
        assertEquals(1,dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        TreeSet<Object> classes = new TreeSet<>();
        classes.add("class1");
        classes.add("class2");
        dataset.add(new Instance("class1"));
        dataset.add(new Instance("class2"));
        assertEquals(classes, dataset.classes());
    }

    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        Set<Instance> nearest = dataset.kNearest(2, inst, dm);
        assertNotNull(nearest);
        assertEquals(0, nearest.size());
    }

    @Test
    public void testFolds() {
        Random rg = new Random();
        int numFolds = 2;
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        dataset.add(new Instance("class1"));
        dataset.add(new Instance("class2"));
        int index = dataset.classIndex("class2");
        assertEquals(1, index);
    }

    @Test
    public void testClassValue() {
        dataset.add(new Instance("class1"));
        dataset.add(new Instance("class2"));
        Object classValue = dataset.classValue(0);
        assertNotNull(classValue);
        assertEquals("class1", classValue);
    }

    @Test
    public void testCopy() {
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        assertNotNull(copy);
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.classes(), copy.classes());
    }

}
