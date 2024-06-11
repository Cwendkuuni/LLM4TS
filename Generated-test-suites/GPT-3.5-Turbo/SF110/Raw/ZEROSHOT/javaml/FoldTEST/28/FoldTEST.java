import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.core.Instance;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    private Dataset parent;
    private Fold fold;

    @Before
    public void setUp() {
        // Create a parent Dataset and set the indices for the Fold
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
        int[] indices = {0, 1, 2};
        fold = new Fold(parent, indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        fold.add(null);
    }

    @Test
    public void testClasses() {
        assertNull(fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        fold.folds(0, null);
    }

    @Test
    public void testInstance() {
        assertNull(fold.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        fold.kNearest(0, null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIndex() {
        fold.add(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        fold.addAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithIndex() {
        fold.addAll(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        fold.contains(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        fold.containsAll(null);
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

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator() {
        fold.iterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        fold.lastIndexOf(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        fold.listIterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorWithIndex() {
        fold.listIterator(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        fold.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIndex() {
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
        assertEquals(3, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        fold.subList(0, 1);
    }

    @Test
    public void testToArray() {
        assertArrayEquals(new Object[] {null, null, null}, fold.toArray());
    }

    @Test
    public void testToArrayWithType() {
        assertArrayEquals(new Object[] {null, null, null}, fold.toArray(new Object[3]));
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
        assertNotEquals(fold, copy);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorHasNext() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testFoldIteratorNext() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        iterator.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorHasPrevious() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testFoldIteratorNextIndex() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        assertEquals(0, iterator.nextIndex());
    }

    @Test
    public void testFoldIteratorPrevious() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        assertEquals(-1, iterator.previousIndex());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold.FoldIterator iterator = new Fold.FoldIterator(fold);
        iterator.set(null);
    }

}