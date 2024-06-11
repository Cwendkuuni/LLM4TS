
package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;

public class FoldTest {
    private Dataset parent;
    private int[] indices;
    private Fold fold;

    @Before
    public void setUp() {
        parent = new Dataset() {
            @Override
            public boolean add(Instance i) {
                return false;
            }

            @Override
            public SortedSet<Object> classes() {
                return null;
            }

            @Override
            public Dataset[] folds(int numFolds, Random rg) {
                return null;
            }

            @Override
            public Instance instance(int index) {
                return null;
            }

            @Override
            public Set<Instance> kNearest(int k, Instance inst, DistanceMeasure dm) {
                return null;
            }

            @Override
            public void add(int index, Instance element) {
            }

            @Override
            public boolean addAll(Collection<? extends Instance> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends Instance> c) {
                return false;
            }

            @Override
            public void clear() {
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public Instance get(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Iterator<Instance> iterator() {
                return null;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Instance> listIterator() {
                return null;
            }

            @Override
            public ListIterator<Instance> listIterator(int index) {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public Instance remove(int index) {
                return null;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public Instance set(int index, Instance element) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public List<Instance> subList(int fromIndex, int toIndex) {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public int noAttributes() {
                return 0;
            }

            @Override
            public int classIndex(Object clazz) {
                return 0;
            }

            @Override
            public Object classValue(int index) {
                return null;
            }

            @Override
            public Dataset copy() {
                return null;
            }
        };
        indices = new int[]{1, 2, 3};
        fold = new Fold(parent, indices);
    }

    @Test
    public void testAdd() {
        assertFalse(fold.add(null));
    }

    @Test
    public void testClasses() {
        assertNull(fold.classes());
    }

    @Test
    public void testFolds() {
        try {
            fold.folds(2, new Random());
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testInstance() {
        assertNull(fold.instance(0));
    }

    @Test
    public void testKNearest() {
        try {
            fold.kNearest(3, null, null);
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddIndex() {
        fold.add(1, null);
    }

    @Test
    public void testAddAll() {
        assertFalse(fold.addAll(null));
    }

    @Test
    public void testAddAllIndex() {
        assertFalse(fold.addAll(1, null));
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

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        fold.indexOf(null);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        assertNotNull(fold.iterator());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        fold.lastIndexOf(null);
    }

    @Test
    public void testListIterator() {
        assertNotNull(fold.listIterator());
    }

    @Test
    public void testListIteratorIndex() {
        assertNotNull(fold.listIterator(0));
    }

    @Test
    public void testRemove() {
        assertFalse(fold.remove(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        fold.remove(0);
    }

    @Test
    public void testRemoveAll() {
        assertFalse(fold.removeAll(null));
    }

    @Test
    public void testRetainAll() {
        assertFalse(fold.retainAll(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        fold.set(0, null);
    }

    @Test
    public void testSize() {
        assertEquals(3, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        fold.subList(0, 1);
    }

    @Test
    public void testToArray() {
        Object[] expected = new Object[3];

        for (int i = 0; i < expected.length; i++) {
            expected[i] = fold.instance(i);
        }

        assertArrayEquals(expected, fold.toArray());
    }

    @Test
    public void testToArrayGeneric() {
        Object[] expected = new Object[3];

        for (int i = 0; i < expected.length; i++) {
            expected[i] = fold.instance(i);
        }

        assertArrayEquals(expected, fold.toArray(new Object[3]));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        assertEquals(0, fold.classIndex(null));
    }

    @Test
    public void testClassValue() {
        assertNull(fold.classValue(0));
    }

    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        assertNotNull(copy);
        assertEquals(3, copy.size());
    }

    @Test
    public void testFoldIteratorConstructor() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(0);
        assertNotNull(iterator);
        assertEquals(0, iterator.currentIndex);
    }

    @Test
    public void testFoldIteratorFoldConstructor() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        assertNotNull(iterator);
        assertEquals(0, iterator.currentIndex);
    }

    @Test
    public void testHasNext() {
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testNext() {
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertNotNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIterator() {
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddIterator() {
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.add(null);
    }

    @Test
    public void testHasPrevious() {
        Fold.FoldIterator iterator = fold.new FoldIterator(1);
        assertTrue(iterator.hasPrevious());
    }

    @Test
    public void testNextIndex() {
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertEquals(1, iterator.nextIndex());
    }

    @Test
    public void testPrevious() {
        Fold.FoldIterator iterator = fold.new FoldIterator(1);
        assertNotNull(iterator.previous());
    }

    @Test
    public void testPreviousIndex() {
        Fold.FoldIterator iterator = fold.new FoldIterator(1);
        assertEquals(0, iterator.previousIndex());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetIterator() {
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.set(null);
    }
}

