package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.*;

public class FoldTest {

    private Dataset parent;
    private int[] indices;

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Fold fold = new Fold(parent, indices);
        fold.add(new Instance());
    }

    @Test
    public void testClasses() {
        Fold fold = new Fold(parent, indices);
        SortedSet<Object> expected = parent.classes();
        SortedSet<Object> actual = fold.classes();
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(parent, indices);
        fold.folds(5, new Random());
    }

    @Test
    public void testInstance() {
        Fold fold = new Fold(parent, indices);
        Instance expected = parent.instance(indices[0]);
        Instance actual = fold.instance(0);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(parent, indices);
        fold.kNearest(5, new Instance(), new DistanceMeasure());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIndex() {
        Fold fold = new Fold(parent, indices);
        fold.add(0, new Instance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Fold fold = new Fold(parent, indices);
        fold.addAll(new ArrayList<Instance>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithIndex() {
        Fold fold = new Fold(parent, indices);
        fold.addAll(0, new ArrayList<Instance>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Fold fold = new Fold(parent, indices);
        fold.contains(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Fold fold = new Fold(parent, indices);
        fold.containsAll(new ArrayList<Object>());
    }

    @Test
    public void testGet() {
        Fold fold = new Fold(parent, indices);
        Instance expected = fold.instance(0);
        Instance actual = fold.get(0);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Fold fold = new Fold(parent, indices);
        fold.indexOf(new Object());
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(parent, indices);
        boolean expected = false;
        boolean actual = fold.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        Fold fold = new Fold(parent, indices);
        Iterator<Instance> iterator = fold.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(fold.instance(0), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(fold.instance(1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(fold.instance(2), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(fold.instance(3), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(fold.instance(4), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Fold fold = new Fold(parent, indices);
        fold.lastIndexOf(new Object());
    }

    @Test
    public void testListIterator() {
        Fold fold = new Fold(parent, indices);
        ListIterator<Instance> iterator = fold.listIterator();
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertEquals(fold.instance(0), iterator.next());
        assertTrue(iterator.hasPrevious());
        assertEquals(fold.instance(0), iterator.previous());
        assertTrue(iterator.hasNext());
        assertEquals(fold.instance(0), iterator.next());
        assertEquals(0, iterator.previousIndex());
        assertEquals(1, iterator.nextIndex());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testListIteratorWithIndex() {
        Fold fold = new Fold(parent, indices);
        ListIterator<Instance> iterator = fold.listIterator(2);
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(fold.instance(2), iterator.next());
        assertEquals(fold.instance(2), iterator.previous());
        assertTrue(iterator.hasNext());
        assertEquals(fold.instance(2), iterator.next());
        assertEquals(2, iterator.previousIndex());
        assertEquals(3, iterator.nextIndex());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Fold fold = new Fold(parent, indices);
        fold.remove(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIndex() {
        Fold fold = new Fold(parent, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Fold fold = new Fold(parent, indices);
        fold.removeAll(new ArrayList<Object>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(parent, indices);
        fold.retainAll(new ArrayList<Object>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(parent, indices);
        fold.set(0, new Instance());
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(parent, indices);
        int expected = indices.length;
        int actual = fold.size();
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Fold fold = new Fold(parent, indices);
        fold.subList(0, 2);
    }

    @Test
    public void testToArray() {
        Fold fold = new Fold(parent, indices);
        Object[] expected = new Object[fold.size()];
        for (int i = 0; i < fold.size(); i++) {
            expected[i] = fold.instance(i);
        }
        Object[] actual = fold.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToArrayWithArray() {
        Fold fold = new Fold(parent, indices);
        Object[] expected = new Object[fold.size()];
        for (int i = 0; i < fold.size(); i++) {
            expected[i] = fold.instance(i);
        }
        Object[] actual = fold.toArray(new Object[fold.size()]);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(parent, indices);
        int expected = parent.noAttributes();
        int actual = fold.noAttributes();
        assertEquals(expected, actual);
    }

    @Test
    public void testClassIndex() {
        Fold fold = new Fold(parent, indices);
        int expected = parent.classIndex(new Object());
        int actual = fold.classIndex(new Object());
        assertEquals(expected, actual);
    }

    @Test
    public void testClassValue() {
        Fold fold = new Fold(parent, indices);
        Object expected = parent.classValue(0);
        Object actual = fold.classValue(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopy() {
        Fold fold = new Fold(parent, indices);
        Dataset expected = parent.copy();
        Dataset actual = fold.copy();
        assertEquals(expected, actual);
    }

}