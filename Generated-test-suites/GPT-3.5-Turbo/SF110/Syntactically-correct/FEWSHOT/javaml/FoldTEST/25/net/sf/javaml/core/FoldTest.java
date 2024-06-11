package net.sf.javaml.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;

public class FoldTest {

    private Dataset parent;
    private int[] indices;
    private Fold fold;

    @Before
    public void setUp() {
        parent = new DefaultDataset();
        fold = new Fold(parent, indices);
    }

    @Test
    public void testAdd() {
        assertFalse(fold.add(null));
    }

    @Test
    public void testClasses() {
        assertEquals(parent.classes(), fold.classes());
    }

    @Test
    public void testFolds() {
        assertNull(fold.folds(3, new Random()));
    }

    @Test
    public void testInstance() {
        assertNull(fold.instance(0));
    }

    @Test
    public void testKNearest() {
        assertNull(fold.kNearest(3, null, new EuclideanDistance()));
    }

    @Test
    public void testAddIndexElement() {
        assertFalse(fold.add(0, null));
    }

    @Test
    public void testAddAll() {
        assertFalse(fold.addAll(null));
    }

    @Test
    public void testAddAllIndex() {
        assertFalse(fold.addAll(0, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @Test
    public void testContains() {
        assertFalse(fold.contains(null));
    }

    @Test
    public void testContainsAll() {
        assertFalse(fold.containsAll(null));
    }

    @Test
    public void testGet() {
        assertNull(fold.get(0));
    }

    @Test
    public void testIndexOf() {
        assertEquals(-1, fold.indexOf(null));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        assertTrue(fold.iterator().hasNext());
    }

    @Test
    public void testLastIndexOf() {
        assertEquals(-1, fold.lastIndexOf(null));
    }

    @Test
    public void testListIterator() {
        assertTrue(fold.listIterator().hasNext());
    }

    @Test
    public void testListIteratorIndex() {
        assertTrue(fold.listIterator(0).hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        fold.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        fold.removeAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        fold.retainAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        fold.set(0, null);
    }

    @Test
    public void testSize() {
        assertEquals(0, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        fold.subList(0, 1);
    }

    @Test
    public void testToArray() {
        assertEquals(0, fold.toArray().length);
    }

    @Test
    public void testToArrayGeneric() {
        assertEquals(0, fold.toArray(new Object[0]).length);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        assertEquals(parent.classIndex(null), fold.classIndex(null));
    }

    @Test
    public void testClassValue() {
        assertEquals(parent.classValue(0), fold.classValue(0));
    }

    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        assertNotNull(copy);
        assertEquals(0, copy.size());
    }

    @Test
    public void testFoldIterator() {
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }
}