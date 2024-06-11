
package net.sf.javaml.core.test;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset parent = new DefaultDataset();
        int[] indices = {1, 2, 3};
        Fold fold = new Fold(parent, indices);
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0});
        fold.add(instance);
    }
    
    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.classes(), fold.classes());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        Random rg = new Random();
        int numFolds = 5;
        fold.folds(numFolds, rg);
    }
    
    @Test
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0});
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {1};
        Fold fold = new Fold(parent, indices);
        assertEquals(instance1, fold.instance(0));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        DistanceMeasure dm = new EuclideanDistance();
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0});
        int k = 3;
        fold.kNearest(k, instance, dm);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddIndexElement() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0});
        int index = 0;
        fold.add(index, instance);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        fold.addAll(parent);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        fold.addAll(0, parent);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        Object obj = new Object();
        fold.contains(obj);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        fold.containsAll(parent);
    }
    
    @Test
    public void testGet() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0});
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {1};
        Fold fold = new Fold(parent, indices);
        assertEquals(instance1, fold.get(0));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        Object obj = new Object();
        fold.indexOf(obj);
    }
    
    @Test
    public void testIsEmpty() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        assertTrue(fold.isEmpty());
    }
    
    @Test
    public void testIterator() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0});
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {1};
        Fold fold = new Fold(parent, indices);
        int index = 0;
        for (Instance instance : fold) {
            assertEquals(parent.get(indices[index]), instance);
            index++;
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        Object obj = new Object();
        fold.lastIndexOf(obj);
    }
    
    @Test
    public void testListIterator() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0});
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {1};
        Fold fold = new Fold(parent, indices);
        int index = 0;
        ListIterator<Instance> it = fold.listIterator();
        while (it.hasNext()) {
            assertEquals(parent.get(indices[index]), it.next());
            index++;
        }
    }
    
    @Test
    public void testListIteratorWithIndex() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0});
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {1};
        Fold fold = new Fold(parent, indices);
        int index = 0;
        ListIterator<Instance> it = fold.listIterator(0);
        while (it.hasNext()) {
            assertEquals(parent.get(indices[index]), it.next());
            index++;
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithObject() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        Object obj = new Object();
        fold.remove(obj);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        int index = 0;
        fold.remove(index);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        fold.removeAll(parent);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        fold.retainAll(parent);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0});
        int index = 0;
        fold.set(index, instance);
    }
    
    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        int[] indices = {1, 2, 3};
        Fold fold = new Fold(parent, indices);
        assertEquals(indices.length, fold.size());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Dataset parent = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parent, indices);
        int fromIndex = 0;
        int toIndex = 2;
        fold.subList(fromIndex, toIndex);
    }
    
    @Test
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0});
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {1};
        Fold fold = new Fold(parent, indices);
        Object[] arr = fold.toArray();
        assertEquals(instance1, arr[0]);
    }
    
    @Test
    public void testToArrayWithType() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0});
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {1};
        Fold fold = new Fold(parent, indices);
        Instance[] arr = new Instance[1];
        arr = fold.toArray(arr);
        assertEquals(instance1, arr[0]);
    }
    
    @Test
    public void testNoAttributes() {
        Dataset parent = new DefaultDataset();
        int[] indices = {1, 2, 3};
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = {1, 2, 3};
        Fold fold = new Fold(parent, indices);
        Object clazz = new Object();
        assertEquals(parent.classIndex(clazz), fold.classIndex(clazz));
    }
    
    @Test
    public void testClassValue() {
        Dataset parent = new DefaultDataset();
        int[] indices = {1, 2, 3};
        Fold fold = new Fold(parent, indices);
        int index = 0;
        assertEquals(parent.classValue(index), fold.classValue(index));
    }
    
    @Test
    public void testCopy() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0});
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {1};
        Fold fold = new Fold(parent, indices);
        Dataset copy = fold.copy();
        assertEquals(1, copy.size());
        assertEquals(instance1, copy.get(0));
    }
    
}
