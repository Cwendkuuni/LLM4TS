package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FoldTest {

    private Dataset parent;
    private int[] indices;

    @Before
    public void initialize() {
        parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        indices = new int[]{0, 1, 2};
    }

    @Test
    public void testAdd() {
        Fold fold = new Fold(parent, indices);
        assertFalse(fold.add(new Instance()));
    }

    @Test
    public void testClasses() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.classes(), fold.classes());
    }

    @Test
    public void testFolds() {
        Fold fold = new Fold(parent, indices);
        assertNull(fold.folds(3, null));
    }

    @Test
    public void testInstance() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.instance(0), fold.instance(0));
    }

    @Test
    public void testKNearest() {
        Fold fold = new Fold(parent, indices);
        assertNull(fold.kNearest(2, new Instance(), null));
    }

    @Test
    public void testAddByIndex() {
        Fold fold = new Fold(parent, indices);
        assertFalse(fold.add(0, new Instance()));
    }

    @Test
    public void testAddAll() {
        Fold fold = new Fold(parent, indices);
        assertFalse(fold.addAll(null));
    }

    @Test
    public void testAddAllByIndex() {
        Fold fold = new Fold(parent, indices);
        assertFalse(fold.addAll(0, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }

    @Test
    public void testContains() {
        Fold fold = new Fold(parent, indices);
        assertFalse(fold.contains(null));
    }

    @Test
    public void testContainsAll() {
        Fold fold = new Fold(parent, indices);
        assertFalse(fold.containsAll(null));
    }

    @Test
    public void testGet() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.instance(0), fold.get(0));
    }

    @Test
    public void testIndexOf() {
        Fold fold = new Fold(parent, indices);
        assertEquals(-1, fold.indexOf(null));
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(parent, indices);
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Fold fold = new Fold(parent, indices);
        assertTrue(fold.iterator().hasNext());
    }

    @Test
    public void testLastIndexOf() {
        Fold fold = new Fold(parent, indices);
        assertEquals(-1, fold.lastIndexOf(null));
    }

    @Test
    public void testListIterator() {
        Fold fold = new Fold(parent, indices);
        assertTrue(fold.listIterator().hasNext());
    }

    @Test
    public void testListIteratorByIndex() {
        Fold fold = new Fold(parent, indices);
        assertTrue(fold.listIterator(0).hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveByObject() {
        Fold fold = new Fold(parent, indices);
        fold.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveByIndex() {
        Fold fold = new Fold(parent, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Fold fold = new Fold(parent, indices);
        fold.removeAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(parent, indices);
        fold.retainAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(parent, indices);
        fold.set(0, new Instance());
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.size(), fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Fold fold = new Fold(parent, indices);
        fold.subList(0, 1);
    }

    @Test
    public void testToArray() {
        Fold fold = new Fold(parent, indices);
        Object[] expected = { parent.instance(0), parent.instance(1), parent.instance(2) };
        assertArrayEquals(expected, fold.toArray());
    }

    @Test
    public void testToArrayWithType() {
        Fold fold = new Fold(parent, indices);
        Object[] expected = { parent.instance(0), parent.instance(1), parent.instance(2) };
        Instance[] array = new Instance[3];
        assertArrayEquals(expected, fold.toArray(array));
    }

    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.classIndex(null), fold.classIndex(null));
    }

    @Test
    public void testClassValue() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.classValue(0), fold.classValue(0));
    }

    @Test
    public void testCopy() {
        Fold fold = new Fold(parent, indices);
        Dataset copy = fold.copy();
        assertEquals(fold.size(), copy.size());
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private class Instance {}
}