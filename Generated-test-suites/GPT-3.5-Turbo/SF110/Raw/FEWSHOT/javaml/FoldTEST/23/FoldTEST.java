package net.sf.javaml.core;

import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Fold fold = new Fold(null, null);
        fold.add(null);
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, null);
        SortedSet<Object> classes = fold.classes();
        assertNotNull(classes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(null, null);
        Random rg = new Random();
        fold.folds(0, rg);
    }

    @Test
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Instance instance = fold.instance(0);
        assertNotNull(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(null, null);
        Instance inst = null;
        DistanceMeasure dm = null;
        fold.kNearest(0, inst, dm);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddIndexElement() {
        Fold fold = new Fold(null, null);
        fold.add(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Fold fold = new Fold(null, null);
        Collection<Instance> c = null;
        fold.addAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndexCollection() {
        Fold fold = new Fold(null, null);
        Collection<Instance> c = null;
        fold.addAll(0, c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(null, null);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Fold fold = new Fold(null, null);
        Object o = null;
        fold.contains(o);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Fold fold = new Fold(null, null);
        Collection<?> c = null;
        fold.containsAll(c);
    }

    @Test
    public void testGet() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Instance instance = fold.get(0);
        assertNotNull(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Fold fold = new Fold(null, null);
        Object o = null;
        fold.indexOf(o);
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(null, null);
        boolean result = fold.isEmpty();
        assertFalse(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator() {
        Fold fold = new Fold(null, null);
        Iterator<Instance> iterator = fold.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Fold fold = new Fold(null, null);
        Object o = null;
        fold.lastIndexOf(o);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        Fold fold = new Fold(null, null);
        ListIterator<Instance> iterator = fold.listIterator();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorIndex() {
        Fold fold = new Fold(null, null);
        ListIterator<Instance> iterator = fold.listIterator(0);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Fold fold = new Fold(null, null);
        fold.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        Fold fold = new Fold(null, null);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Fold fold = new Fold(null, null);
        Collection<?> c = null;
        fold.removeAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(null, null);
        Collection<?> c = null;
        fold.retainAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(null, null);
        fold.set(0, null);
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(null, new int[]{0, 1, 2});
        int size = fold.size();
        assertEquals(3, size);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Fold fold = new Fold(null, null);
        fold.subList(0, 0);
    }

    @Test
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Object[] array = fold.toArray();
        assertNotNull(array);
    }

    @Test
    public void testToArrayA() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Object[] array = fold.toArray(new Object[0]);
        assertNotNull(array);
    }

    @Test
    public void testNoAttributes() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        int noAttributes = fold.noAttributes();
        assertEquals(0, noAttributes);
    }

    @Test
    public void testClassIndex() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        int classIndex = fold.classIndex(null);
        assertEquals(-1, classIndex);
    }

    @Test
    public void testClassValue() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Object classValue = fold.classValue(0);
        assertNull(classValue);
    }

    @Test
    public void testCopy() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Dataset copy = fold.copy();
        assertNotNull(copy);
    }

    @Test
    public void testFoldIteratorConstructorIndex() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = new Fold.FoldIterator(0);
        assertNotNull(iterator);
    }

    @Test
    public void testFoldIteratorConstructorFold() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        assertNotNull(iterator);
    }

    @Test
    public void testFoldIteratorHasNext() {
        Fold fold = new Fold(null, new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        boolean hasNext = iterator.hasNext();
        assertTrue(hasNext);
    }

    @Test
    public void testFoldIteratorNext() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        Instance next = iterator.next();
        assertNotNull(next);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.add(null);
    }

    @Test
    public void testFoldIteratorHasPrevious() {
        Fold fold = new Fold(null, new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator(1);
        boolean hasPrevious = iterator.hasPrevious();
        assertTrue(hasPrevious);
    }

    @Test
    public void testFoldIteratorNextIndex() {
        Fold fold = new Fold(null, new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        int nextIndex = iterator.nextIndex();
        assertEquals(0, nextIndex);
    }

    @Test
    public void testFoldIteratorPrevious() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0, 1});
        Fold.FoldIterator iterator = fold.new FoldIterator(1);
        Instance previous = iterator.previous();
        assertNotNull(previous);
    }

    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold fold = new Fold(null, new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        int previousIndex = iterator.previousIndex();
        assertEquals(0, previousIndex);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Dataset parent = new DefaultDataset();
        parent.add(null);
        Fold fold = new Fold(parent, new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.set(null);
    }
}