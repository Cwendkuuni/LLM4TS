import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.DistanceMeasure;
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
                throw new UnsupportedOperationException("Cannot do this on a fold of a dataset");
            }

            @Override
            public SortedSet<Object> classes() {
                return Collections.emptySortedSet();
            }

            @Override
            public Dataset[] folds(int numFolds, Random rg) {
                throw new UnsupportedOperationException("Method is not yet implemented");
            }

            @Override
            public Instance instance(int index) {
                return null;
            }

            @Override
            public Set<Instance> kNearest(int k, Instance inst, DistanceMeasure dm) {
                throw new UnsupportedOperationException("Method is not yet implemented");
            }

            @Override
            public void add(int index, Instance element) {
                throw new UnsupportedOperationException("Cannot do this on a fold of a dataset");
            }

            @Override
            public boolean addAll(Collection<? extends Instance> c) {
                throw new UnsupportedOperationException("Cannot do this on a fold of a dataset");
            }

            @Override
            public boolean addAll(int index, Collection<? extends Instance> c) {
                throw new UnsupportedOperationException("Cannot do this on a fold of a dataset");
            }

            @Override
            public void clear() {
                throw new UnsupportedOperationException("Cannot do this on a fold of a dataset");
            }

            @Override
            public boolean contains(Object o) {
                throw new UnsupportedOperationException("Method is not yet implemented");
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                throw new UnsupportedOperationException("Method is not yet implemented");
            }

            @Override
            public Instance get(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                throw new UnsupportedOperationException("Method is not yet implemented");
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
                throw new UnsupportedOperationException("Method is not yet implemented");
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
                throw new UnsupportedOperationException("You cannot do this on a fold.");
            }

            @Override
            public Instance remove(int index) {
                throw new UnsupportedOperationException("You cannot do this on a fold.");
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException("You cannot do this on a fold.");
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException("You cannot do this on a fold.");
            }

            @Override
            public Instance set(int index, Instance element) {
                throw new UnsupportedOperationException("You cannot do this on a fold.");
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public List<Instance> subList(int fromIndex, int toIndex) {
                throw new UnsupportedOperationException("You cannot do this on a fold.");
            }

            @Override
            public Object[] toArray() {
                return null;
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
        indices = new int[5];
        fold = new Fold(parent, indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        fold.add(null);
    }

    @Test
    public void testClasses() {
        SortedSet<Object> result = fold.classes();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        fold.folds(0, null);
    }

    @Test
    public void testInstance() {
        Instance result = fold.instance(0);
        assertNull(result);
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
        Instance result = fold.get(0);
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        fold.indexOf(null);
    }

    @Test
    public void testIsEmpty() {
        boolean result = fold.isEmpty();
        assertFalse(result);
    }

    @Test
    public void testIterator() {
        Iterator<Instance> result = fold.iterator();
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        fold.lastIndexOf(null);
    }

    @Test
    public void testListIterator() {
        ListIterator<Instance> result = fold.listIterator();
        assertNull(result);
    }

    @Test
    public void testListIteratorWithIndex() {
        ListIterator<Instance> result = fold.listIterator(0);
        assertNull(result);
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
        int result = fold.size();
        assertEquals(5, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        fold.subList(0, 0);
    }

    @Test
    public void testToArray() {
        Object[] result = fold.toArray();
        assertNotNull(result);
        assertEquals(5, result.length);
    }

    @Test
    public void testToArrayWithArgument() {
        Object[] a = new Object[5];
        Object[] result = fold.toArray(a);
        assertNotNull(result);
        assertEquals(5, result.length);
    }

    @Test
    public void testNoAttributes() {
        int result = fold.noAttributes();
        assertEquals(0, result);
    }

    @Test
    public void testClassIndex() {
        int result = fold.classIndex(null);
        assertEquals(0, result);
    }

    @Test
    public void testClassValue() {
        Object result = fold.classValue(0);
        assertNull(result);
    }

    @Test
    public void testCopy() {
        Dataset result = fold.copy();
        assertNull(result);
    }

    @Test
    public void testFoldIteratorHasNext() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(0);
        boolean result = foldIterator.hasNext();
        assertTrue(result);
    }

    @Test
    public void testFoldIteratorNext() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(0);
        Instance result = foldIterator.next();
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(0);
        foldIterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(0);
        foldIterator.add(null);
    }

    @Test
    public void testFoldIteratorHasPrevious() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(1);
        boolean result = foldIterator.hasPrevious();
        assertTrue(result);
    }

    @Test
    public void testFoldIteratorNextIndex() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(0);
        int result = foldIterator.nextIndex();
        assertEquals(0, result);
    }

    @Test
    public void testFoldIteratorPrevious() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(1);
        Instance result = foldIterator.previous();
        assertNull(result);
    }

    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(1);
        int result = foldIterator.previousIndex();
        assertEquals(1, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(0);
        foldIterator.set(null);
    }
}