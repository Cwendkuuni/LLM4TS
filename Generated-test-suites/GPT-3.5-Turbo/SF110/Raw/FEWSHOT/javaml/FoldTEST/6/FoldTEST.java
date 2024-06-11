package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testAdd() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.add(null));
    }

    @Test
    public void testClasses() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.classes());
    }

    @Test
    public void testFolds() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.folds(0, null));
    }

    @Test
    public void testInstance() {
        Fold fold = new Fold(null, null);
        assertNull(fold.instance(0));
    }

    @Test
    public void testKNearest() {
        Fold fold = new Fold(null, null);
        assertNull(fold.kNearest(0, null, null));
    }

    @Test
    public void testAddIndex() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.add(0, null));
    }

    @Test
    public void testAddAll() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.addAll(null));
    }

    @Test
    public void testAddAllIndex() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.addAll(0, null));
    }

    @Test
    public void testClear() {
        Fold fold = new Fold(null, null);
        fold.clear();
        assertEquals(0, fold.size());
    }

    @Test
    public void testContains() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.contains(null));
    }

    @Test
    public void testContainsAll() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.containsAll(null));
    }

    @Test
    public void testGet() {
        Fold fold = new Fold(null, null);
        assertNull(fold.get(0));
    }

    @Test
    public void testIndexOf() {
        Fold fold = new Fold(null, null);
        assertEquals(-1, fold.indexOf(null));
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(null, null);
        assertTrue(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.iterator());
    }

    @Test
    public void testLastIndexOf() {
        Fold fold = new Fold(null, null);
        assertEquals(-1, fold.lastIndexOf(null));
    }

    @Test
    public void testListIterator() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.listIterator());
    }

    @Test
    public void testListIteratorIndex() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.listIterator(0));
    }

    @Test
    public void testRemove() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.remove(null));
    }

    @Test
    public void testRemoveIndex() {
        Fold fold = new Fold(null, null);
        assertNull(fold.remove(0));
    }

    @Test
    public void testRemoveAll() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.removeAll(null));
    }

    @Test
    public void testRetainAll() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.retainAll(null));
    }

    @Test
    public void testSet() {
        Fold fold = new Fold(null, null);
        assertNull(fold.set(0, null));
    }

    @Test
    public void testSize() {
        int[] indices = {0, 1, 2};
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        Fold fold = new Fold(parent, indices);
        assertEquals(indices.length, fold.size());
    }

    @Test
    public void testSubList() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.subList(0, 0).size());
    }

    @Test
    public void testToArray() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.toArray());
    }

    @Test
    public void testToArrayGeneric() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.toArray(new Instance[0]));
    }

    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Fold fold = new Fold(null, null);
        assertEquals(-1, fold.classIndex(null));
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
        Fold.FoldIterator iter = fold.new FoldIterator(0);
        assertFalse(iter.hasNext());
        assertFalse(iter.hasPrevious());
    }
}