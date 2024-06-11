package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class FoldTest {
    private Dataset parent;
    private Fold fold;

    @Before
    public void setUp() {
        parent = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        parent.add(instance1);
        parent.add(instance2);
        int[] indices = {0, 1};
        fold = new Fold(parent, indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddThrowsException() {
        Instance instance = new Instance();
        fold.add(instance);
    }

    @Test
    public void testClasses() {
        SortedSet<Object> classes = fold.classes();
        assertEquals(parent.classes(), classes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldsThrowsException() {
        int numFolds = 5;
        Random rg = new Random();
        fold.folds(numFolds, rg);
    }

    @Test
    public void testInstance() {
        Instance instance = fold.instance(1);
        assertEquals(parent.instance(1), instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearestThrowsException() {
        int k = 3;
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        fold.kNearest(k, inst, dm);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIndexThrowsException() {
        int index = 0;
        Instance element = new Instance();
        fold.add(index, element);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllCollectionThrowsException() {
        Collection<Instance> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        fold.addAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithIndexCollectionThrowsException() {
        int index = 0;
        Collection<Instance> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        fold.addAll(index, c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClearThrowsException() {
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsThrowsException() {
        Object o = new Object();
        fold.contains(o);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAllThrowsException() {
        Collection<Object> c = new ArrayList<>();
        c.add(new Object());
        c.add(new Object());
        fold.containsAll(c);
    }

    @Test
    public void testGet() {
        Instance instance = fold.get(0);
        assertEquals(parent.instance(0), instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOfThrowsException() {
        Object o = new Object();
        fold.indexOf(o);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Iterator<Instance> iterator = fold.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOfThrowsException() {
        Object o = new Object();
        fold.lastIndexOf(o);
    }

    @Test
    public void testListIterator() {
        ListIterator<Instance> listIterator = fold.listIterator();
        assertTrue(listIterator.hasNext());
    }

    @Test
    public void testListIteratorWithIndex() {
        int index = 1;
        ListIterator<Instance> listIterator = fold.listIterator(index);
        assertTrue(listIterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveThrowsException() {
        Object o = new Object();
        fold.remove(o);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIndexThrowsException() {
        int index = 0;
        fold.remove(index);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllThrowsException() {
        Collection<Object> c = new ArrayList<>();
        c.add(new Object());
        c.add(new Object());
        fold.removeAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllThrowsException() {
        Collection<Object> c = new ArrayList<>();
        c.add(new Object());
        c.add(new Object());
        fold.retainAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsException() {
        int index = 0;
        Instance element = new Instance();
        fold.set(index, element);
    }

    @Test
    public void testSize() {
        assertEquals(2, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubListThrowsException() {
        int fromIndex = 0;
        int toIndex = 1;
        fold.subList(fromIndex, toIndex);
    }

    @Test
    public void testToArray() {
        Object[] expected = {parent.instance(0), parent.instance(1)};
        Object[] actual = fold.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToArrayWithParameter() {
        Instance[] expected = {parent.instance(0), parent.instance(1)};
        Instance[] actual = new Instance[2];
        actual = fold.toArray(actual);
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
        for (Instance i : parent) {
            expected.add(i.copy());
        }
        Dataset actual = fold.copy();
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void testFoldIteratorHasNext() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testFoldIteratorNext() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        Instance expected = parent.instance(0);
        Instance actual = iterator.next();
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemoveThrowsException() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAddThrowsException() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        iterator.add(new Instance());
    }

    @Test
    public void testFoldIteratorHasPrevious() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testFoldIteratorNextIndex() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        int expected = 0;
        int actual = iterator.nextIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testFoldIteratorPrevious() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        iterator.next();
        Instance expected = parent.instance(0);
        Instance actual = iterator.previous();
        assertEquals(expected, actual);
    }

    @Test
    public void testFoldIteratorPreviousIndex() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        int expected = -1;
        int actual = iterator.previousIndex();
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSetThrowsException() {
        ListIterator<Instance> iterator = new Fold.FoldIterator(fold);
        iterator.set(new Instance());
    }
}