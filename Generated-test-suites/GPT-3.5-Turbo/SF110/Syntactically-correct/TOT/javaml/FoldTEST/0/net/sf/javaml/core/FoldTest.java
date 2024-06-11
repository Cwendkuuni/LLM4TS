
package net.sf.javaml.core;

import net.sf.javaml.distance.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAddThrowsException() {
        Fold fold = new Fold(null, null);
        fold.add(null);
    }
    
    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        SortedSet<Object> classes = fold.classes();
        
        assertTrue(classes.isEmpty());
        
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        
        classes = fold.classes();
        
        assertTrue(classes.isEmpty());
        
        parent.get(0).setClassValue(1);
        parent.get(1).setClassValue(2);
        
        classes = fold.classes();
        
        assertFalse(classes.isEmpty());
        assertEquals(2, classes.size());
        assertTrue(classes.contains(1));
        assertTrue(classes.contains(2));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldsThrowsException() {
        Fold fold = new Fold(null, null);
        fold.folds(3, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testInstanceThrowsException() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        fold.instance(0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testKNearestThrowsException() {
        Fold fold = new Fold(null, null);
        fold.kNearest(3, null, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIndexThrowsException() {
        Fold fold = new Fold(null, null);
        fold.add(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllThrowsException() {
        Fold fold = new Fold(null, null);
        fold.addAll(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithIndexThrowsException() {
        Fold fold = new Fold(null, null);
        fold.addAll(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testClearThrowsException() {
        Fold fold = new Fold(null, null);
        fold.clear();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testContainsThrowsException() {
        Fold fold = new Fold(null, null);
        fold.contains(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAllThrowsException() {
        Fold fold = new Fold(null, null);
        fold.containsAll(null);
    }
    
    @Test
    public void testGet() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        assertNull(fold.get(0));
        
        Instance instance = new Instance();
        parent.add(instance);
        
        assertEquals(instance, fold.get(0));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOfThrowsException() {
        Fold fold = new Fold(null, null);
        fold.indexOf(null);
    }
    
    @Test
    public void testIsEmpty() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0});
        
        assertTrue(fold.isEmpty());
        
        parent.add(new Instance());
        
        assertFalse(fold.isEmpty());
    }
    
    @Test
    public void testIterator() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        Iterator<Instance> iterator = fold.iterator();
        
        assertFalse(iterator.hasNext());
        
        parent.add(new Instance());
        
        iterator = fold.iterator();
        
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOfThrowsException() {
        Fold fold = new Fold(null, null);
        fold.lastIndexOf(null);
    }
    
    @Test
    public void testListIteratorWithoutArguments() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        ListIterator<Instance> iterator = fold.listIterator();
        
        assertFalse(iterator.hasNext());
        
        parent.add(new Instance());
        
        iterator = fold.listIterator();
        
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
    }
    
    @Test
    public void testListIteratorWithIndex() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        ListIterator<Instance> iterator = fold.listIterator(0);
        
        assertFalse(iterator.hasNext());
        
        parent.add(new Instance());
        
        iterator = fold.listIterator(0);
        
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveThrowsException() {
        Fold fold = new Fold(null, null);
        fold.remove(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIndexThrowsException() {
        Fold fold = new Fold(null, null);
        fold.remove(0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllThrowsException() {
        Fold fold = new Fold(null, null);
        fold.removeAll(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllThrowsException() {
        Fold fold = new Fold(null, null);
        fold.retainAll(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsException() {
        Fold fold = new Fold(null, null);
        fold.set(0, null);
    }
    
    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        assertEquals(3, fold.size());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSubListThrowsException() {
        Fold fold = new Fold(null, null);
        fold.subList(0, 3);
    }
    
    @Test
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        Object[] array = fold.toArray();
        
        assertNotNull(array);
        assertEquals(3, array.length);
        
        Instance[] instances = Arrays.copyOf(array, array.length, Instance[].class);
        
        assertNotNull(instances[0]);
    }
    
    @Test
    public void testToArrayWithArgument() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        Instance[] array = fold.toArray(new Instance[0]);
        
        assertNotNull(array);
        assertEquals(3, array.length);
        
        assertNotNull(array[0]);
    }
    
    @Test
    public void testNoAttributes() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        assertEquals(0, fold.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        assertEquals(-1, fold.classIndex(null));
    }
    
    @Test
    public void testClassValue() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        assertNull(fold.classValue(0));
    }
    
    @Test
    public void testCopy() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        Dataset copy = fold.copy();
        
        assertEquals(fold.size(), copy.size());
        
        for (int i = 0; i < fold.size(); i++) {
            assertNotSame(fold.get(i), copy.get(i));
            assertEquals(fold.get(i).getClass(), copy.get(i).getClass());
        }
    }
    
}
