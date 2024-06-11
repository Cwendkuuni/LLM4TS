package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testAdd() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.add(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(null, null);
        fold.folds(0, null);
    }

    @Test
    public void testInstance() {
        Fold fold = new Fold(null, null);
        assertNull(fold.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(null, null);
        fold.kNearest(0, null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtIndex() {
        Fold fold = new Fold(null, null);
        fold.add(0, null);
    }

    @Test
    public void testAddAll() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.addAll(null));
    }

    @Test
    public void testAddAllAtIndex() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.addAll(0, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(null, null);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Fold fold = new Fold(null, null);
        fold.contains(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Fold fold = new Fold(null, null);
        fold.containsAll(null);
    }

    @Test
    public void testGet() {
        Fold fold = new Fold(null, null);
        assertNull(fold.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Fold fold = new Fold(null, null);
        fold.indexOf(null);
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(null, null);
        assertTrue(fold.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator() {
        Fold fold = new Fold(null, null);
        fold.iterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Fold fold = new Fold(null, null);
        fold.lastIndexOf(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        Fold fold = new Fold(null, null);
        fold.listIterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorWithIndex() {
        Fold fold = new Fold(null, null);
        fold.listIterator(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveObject() {
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
        fold.removeAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(null, null);
        fold.retainAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(null, null);
        fold.set(0, null);
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Fold fold = new Fold(null, null);
        fold.subList(0, 0);
    }

    @Test
    public void testToArray() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.toArray().length);
    }

    @Test
    public void testToArrayWithParameter() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.toArray(new Object[0]).length);
    }

    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.classIndex(null));
    }

    @Test
    public void testClassValue() {
        Fold fold = new Fold(null, null);
        assertNull(fold.classValue(0));
    }

    @Test
    public void testCopy() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.copy());
    }

    @Test
    public void testFoldIterator() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.new FoldIterator());
    }
}