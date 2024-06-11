
package net.sf.javaml.core;

import net.sf.javaml.distance.*;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoldTest {
    private Dataset parent;
    private int[] indices;
    private Fold fold;
    
    @Before
    public void setUp() {
        parent = new DefaultDataset();
        indices = new int[]{0, 1, 2, 3, 4};
        fold = new Fold(parent, indices);
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        try {
            fold.add(instance);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> expected = parent.classes();
        SortedSet<Object> actual = fold.classes();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFolds() {
        int numFolds = 5;
        Random rg = new Random();
        try {
            fold.folds(numFolds, rg);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testInstance() {
        Instance expected = parent.instance(indices[0]);
        Instance actual = fold.instance(0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testKNearest() {
        int k = 3;
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        try {
            fold.kNearest(k, inst, dm);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAddAtIndex() {
        int index = 0;
        Instance element = new Instance();
        try {
            fold.add(index, element);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> collection = new ArrayList<>();
        try {
            fold.addAll(collection);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAddAllAtIndex() {
        int index = 0;
        Collection<Instance> collection = new ArrayList<>();
        try {
            fold.addAll(index, collection);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testClear() {
        try {
            fold.clear();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testContains() {
        Object o = new Object();
        try {
            fold.contains(o);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testContainsAll() {
        Collection<Object> collection = new ArrayList<>();
        try {
            fold.containsAll(collection);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGet() {
        Instance expected = fold.instance(0);
        Instance actual = fold.get(0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf() {
        Object o = new Object();
        try {
            fold.indexOf(o);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsEmpty() {
        boolean expected = false;
        boolean actual = fold.isEmpty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIterator() {
        Iterator<Instance> expected = new Fold.FoldIterator();
        Iterator<Instance> actual = fold.iterator();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLastIndexOf() {
        Object o = new Object();
        try {
            fold.lastIndexOf(o);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testListIterator() {
        ListIterator<Instance> expected = new Fold.FoldIterator();
        ListIterator<Instance> actual = fold.listIterator();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testListIteratorWithIndex() {
        int index = 0;
        ListIterator<Instance> expected = new Fold.FoldIterator(index);
        ListIterator<Instance> actual = fold.listIterator(index);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoveObject() {
        Object o = new Object();
        try {
            fold.remove(o);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testRemoveAtIndex() {
        int index = 0;
        try {
            fold.remove(index);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testRemoveAll() {
        Collection<Object> collection = new ArrayList<>();
        try {
            fold.removeAll(collection);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testRetainAll() {
        Collection<Object> collection = new ArrayList<>();
        try {
            fold.retainAll(collection);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSet() {
        int index = 0;
        Instance instance = new Instance();
        try {
            fold.set(index, instance);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSize() {
        int expected = indices.length;
        int actual = fold.size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSubList() {
        int fromIndex = 0;
        int toIndex = 2;
        try {
            fold.subList(fromIndex, toIndex);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testToArray() {
        Object[] expected = new Object[indices.length];
        for (int i = 0; i < fold.size(); i++) {
            expected[i] = fold.instance(i);
        }
        Object[] actual = fold.toArray();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testToArrayWithGenericArray() {
        Instance[] a = new Instance[indices.length];
        Instance[] expected = fold.toArray(a);
        Instance[] actual = new Instance[indices.length];
        for (int i = 0; i < indices.length; i++) {
            actual[i] = fold.instance(i);
        }
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testNoAttributes() {
        int expected = parent.noAttributes();
        int actual = fold.noAttributes();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        int expected = parent.classIndex(clazz);
        int actual = fold.classIndex(clazz);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testClassValue() {
        int index = 0;
        Object expected = parent.classValue(index);
        Object actual = fold.classValue(index);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopy() {
        Dataset expected = new DefaultDataset();
        for (int i = 0; i < indices.length; i++) {
            expected.add(fold.instance(i).copy());
        }
        Dataset actual = fold.copy();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFoldIteratorConstructorWithIndex() {
        int index = 0;
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(index);
        assertEquals(index, foldIterator.currentIndex);
    }
    
    @Test
    public void testFoldIteratorConstructorWithFold() {
        Fold fold = new Fold(parent, indices);
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        assertEquals(0, foldIterator.currentIndex);
    }
    
    @Test
    public void testFoldIteratorHasNext() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator();
        assertTrue(foldIterator.hasNext());
    }
    
    @Test
    public void testFoldIteratorNext() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator();
        Instance expected = fold.instance(foldIterator.currentIndex);
        Instance actual = foldIterator.next();
        assertEquals(expected, actual);
        assertEquals(1, foldIterator.currentIndex);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator();
        foldIterator.remove();
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator();
        foldIterator.add(new Instance());
    }
    
    @Test
    public void testFoldIteratorHasPrevious() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(1);
        assertTrue(foldIterator.hasPrevious());
    }
    
    @Test
    public void testFoldIteratorNextIndex() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator();
        int expected = foldIterator.currentIndex;
        int actual = foldIterator.nextIndex();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFoldIteratorPrevious() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(1);
        Instance expected = fold.instance(foldIterator.currentIndex);
        Instance actual = foldIterator.previous();
        assertEquals(expected, actual);
        assertEquals(0, foldIterator.currentIndex);
    }
    
    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator();
        int expected = foldIterator.currentIndex;
        int actual = foldIterator.previousIndex();
        assertEquals(expected, actual);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator();
        foldIterator.set(new Instance());
    }

}
