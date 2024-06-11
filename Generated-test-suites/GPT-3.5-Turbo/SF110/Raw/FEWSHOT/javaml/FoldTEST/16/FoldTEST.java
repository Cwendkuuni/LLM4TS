package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public class FoldTest {
    private Fold fold;
    private Dataset parent;
    private int[] indices;
    
    @Before
    public void setUp() {
        parent = new DefaultDataset();
        indices = new int[]{0, 1, 2, 3};
        fold = new Fold(parent, indices);
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        instance.put("feature", 10);
        instance.put("label", "positive");
        
        boolean result = fold.add(instance);
        
        assertFalse(result);
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> classes = fold.classes();
        
        assertNotNull(classes);
    }
    
    @Test
    public void testFolds() {
        int numFolds = 5;
        Random rg = new Random();
        
        Dataset[] folds = fold.folds(numFolds, rg);
        
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
    }
    
    @Test
    public void testInstance() {
        int index = 2;
        Instance instance = new Instance();
        instance.put("feature", 10);
        instance.put("label", "positive");
        parent.add(instance);
        
        Instance retrievedInstance = fold.instance(index);
        
        assertNotNull(retrievedInstance);
        assertEquals(instance, retrievedInstance);
    }
    
    @Test
    public void testKNearest() {
        int k = 3;
        Instance instance = new Instance();
        instance.put("feature", 10);
        instance.put("label", "positive");
        DistanceMeasure dm = new EuclideanDistance();
        
        Set<Instance> nearestInstances = fold.kNearest(k, instance, dm);
        
        assertNotNull(nearestInstances);
        assertEquals(k, nearestInstances.size());
    }
    
    @Test
    public void testAddAtIndex() {
        int index = 2;
        Instance instance = new Instance();
        instance.put("feature", 10);
        instance.put("label", "positive");
        
        try {
            fold.add(index, instance);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }
    
    @Test
    public void testAddAll() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        instances.add(new Instance());
        
        boolean result = fold.addAll(instances);
        
        assertFalse(result);
    }
    
    @Test
    public void testAddAllAtIndex() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        instances.add(new Instance());
        int index = 2;
        
        boolean result = fold.addAll(index, instances);
        
        assertFalse(result);
    }
    
    @Test
    public void testClear() {
        try {
            fold.clear();
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }
    
    @Test
    public void testContains() {
        Object object = new Object();
        
        boolean result = fold.contains(object);
        
        assertFalse(result);
    }
    
    @Test
    public void testContainsAll() {
        Collection<Object> collection = new ArrayList<>();
        collection.add(new Object());
        collection.add(new Object());
        
        boolean result = fold.containsAll(collection);
        
        assertFalse(result);
    }
    
    @Test
    public void testGet() {
        int index = 1;
        Instance instance = new Instance();
        instance.put("feature", 10);
        instance.put("label", "positive");
        parent.add(instance);
        
        Instance retrievedInstance = fold.get(index);
        
        assertNotNull(retrievedInstance);
        assertEquals(instance, retrievedInstance);
    }
    
    @Test
    public void testIndexOf() {
        Object object = new Object();
        
        int index = fold.indexOf(object);
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIsEmpty() {
        boolean isEmpty = fold.isEmpty();
        
        assertFalse(isEmpty);
    }
    
    @Test
    public void testIterator() {
        Iterator<Instance> iterator = fold.iterator();
        
        assertNotNull(iterator);
    }
    
    @Test
    public void testLastIndexOf() {
        Object object = new Object();
        
        int index = fold.lastIndexOf(object);
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testListIterator() {
        ListIterator<Instance> listIterator = fold.listIterator();
        
        assertNotNull(listIterator);
    }
    
    @Test
    public void testListIteratorWithIndex() {
        int index = 2;
        
        ListIterator<Instance> listIterator = fold.listIterator(index);
        
        assertNotNull(listIterator);
    }
    
    @Test
    public void testRemove() {
        Object object = new Object();
        
        boolean result = fold.remove(object);
        
        assertFalse(result);
    }
    
    @Test
    public void testRemoveAtIndex() {
        int index = 2;
        
        try {
            Instance removedInstance = fold.remove(index);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }
    
    @Test
    public void testRemoveAll() {
        Collection<Object> collection = new ArrayList<>();
        collection.add(new Object());
        collection.add(new Object());
        
        boolean result = fold.removeAll(collection);
        
        assertFalse(result);
    }
    
    @Test
    public void testRetainAll() {
        Collection<Object> collection = new ArrayList<>();
        collection.add(new Object());
        collection.add(new Object());
        
        boolean result = fold.retainAll(collection);
        
        assertFalse(result);
    }
    
    @Test
    public void testSet() {
        int index = 2;
        Instance instance = new Instance();
        instance.put("feature", 10);
        instance.put("label", "positive");
        
        try {
            Instance replacedInstance = fold.set(index, instance);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }
    
    @Test
    public void testSize() {
        int size = fold.size();
        
        assertEquals(indices.length, size);
    }
    
    @Test
    public void testSubList() {
        int fromIndex = 1;
        int toIndex = 3;
        
        try {
            List<Instance> subList = fold.subList(fromIndex, toIndex);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }
    
    @Test
    public void testToArray() {
        Object[] array = fold.toArray();
        
        assertNotNull(array);
        assertEquals(indices.length, array.length);
        assertEquals(indices[0], ((Instance)array[0]).getIndex());
        assertEquals(indices[1], ((Instance)array[1]).getIndex());
        assertEquals(indices[2], ((Instance)array[2]).getIndex());
        assertEquals(indices[3], ((Instance)array[3]).getIndex());
    }
    
    @Test
    public void testToArrayWithParameter() {
        Object[] array = new Object[fold.size()];
        
        Object[] result = fold.toArray(array);
        
        assertNotNull(result);
        assertEquals(fold.size(), result.length);
    }
    
    @Test
    public void testNoAttributes() {
        int noAttributes = fold.noAttributes();
        
        assertEquals(0, noAttributes);
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        
        int classIndex = fold.classIndex(clazz);
        
        assertEquals(-1, classIndex);
    }
    
    @Test
    public void testClassValue() {
        int index = 2;
        Object expectedValue = new Object();
        
        Object classValue = fold.classValue(index);
        
        assertEquals(expectedValue, classValue);
    }
    
    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        
        assertNotNull(copy);
        assertFalse(copy == fold);
        assertEquals(fold.size(), copy.size());
    }
    
    @Test
    public void testFoldIteratorHasNext() {
        FoldIterator foldIterator = fold.new FoldIterator(0);
        
        boolean hasNext = foldIterator.hasNext();
        
        assertTrue(hasNext);
    }
    
    @Test
    public void testFoldIteratorNext() {
        int index = 1;
        FoldIterator foldIterator = fold.new FoldIterator(index);
        
        Instance nextInstance = foldIterator.next();
        
        assertEquals(fold.instance(index), nextInstance);
    }
    
    @Test
    public void testFoldIteratorRemove() {
        int index = 1;
        FoldIterator foldIterator = fold.new FoldIterator(index);
        
        try {
            foldIterator.remove();
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }
    
    @Test
    public void testFoldIteratorAdd() {
        int index = 1;
        FoldIterator foldIterator = fold.new FoldIterator(index);
        
        try {
            foldIterator.add(new Instance());
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }
    
    @Test
    public void testFoldIteratorHasPrevious() {
        int index = 1;
        FoldIterator foldIterator = fold.new FoldIterator(index);
        
        boolean hasPrevious = foldIterator.hasPrevious();
        
        assertTrue(hasPrevious);
    }
    
    @Test
    public void testFoldIteratorNextIndex() {
        int index = 1;
        FoldIterator foldIterator = fold.new FoldIterator(index);
        
        int nextIndex = foldIterator.nextIndex();
        
        assertEquals(index, nextIndex);
    }
    
    @Test
    public void testFoldIteratorPrevious() {
        int index = 1;
        FoldIterator foldIterator = fold.new FoldIterator(index);
        
        Instance previousInstance = foldIterator.previous();
        
        assertEquals(fold.instance(index - 1), previousInstance);
    }
    
    @Test
    public void testFoldIteratorPreviousIndex() {
        int index = 1;
        FoldIterator foldIterator = fold.new FoldIterator(index);
        
        int previousIndex = foldIterator.previousIndex();
        
        assertEquals(index - 1, previousIndex);
    }
    
    @Test
    public void testFoldIteratorSet() {
        int index = 1;
        Instance instance = new Instance();
        instance.put("feature", 10);
        instance.put("label", "positive");
        FoldIterator foldIterator = fold.new FoldIterator(index);
        
        try {
            foldIterator.set(instance);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }
}