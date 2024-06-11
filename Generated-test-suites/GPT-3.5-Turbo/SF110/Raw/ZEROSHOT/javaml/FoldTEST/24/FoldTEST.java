import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class FoldTest {

    private Fold fold;
    private Dataset parent;
    private int[] indices;

    @Before
    public void setUp() {
        parent = new DefaultDataset();
        indices = new int[]{0, 1, 2};
        fold = new Fold(parent, indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Instance instance = new Instance();
        fold.add(instance);
    }

    @Test
    public void testClasses() {
        SortedSet<Object> classes = fold.classes();
        assertEquals(parent.classes(), classes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        int numFolds = 10;
        Random rg = new Random();
        fold.folds(numFolds, rg);
    }

    @Test
    public void testInstance() {
        Instance instance = parent.instance(indices[0]);
        assertEquals(instance, fold.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        int k = 5;
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        fold.kNearest(k, inst, dm);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtIndex() {
        int index = 0;
        Instance element = new Instance();
        fold.add(index, element);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Collection<Instance> collection = new ArrayList<Instance>();
        fold.addAll(collection);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllAtIndex() {
        int index = 0;
        Collection<Instance> collection = new ArrayList<Instance>();
        fold.addAll(index, collection);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Object o = new Object();
        fold.contains(o);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Collection<?> c = new ArrayList<>();
        fold.containsAll(c);
    }

    @Test
    public void testGet() {
        Instance instance = fold.instance(0);
        assertEquals(instance, fold.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
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
        assertNotNull(iterator);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Object o = new Object();
        fold.lastIndexOf(o);
    }

    @Test
    public void testListIterator() {
        ListIterator<Instance> listIterator = fold.listIterator();
        assertNotNull(listIterator);
    }

    @Test
    public void testListIteratorWithIndex() {
        int index = 0;
        ListIterator<Instance> listIterator = fold.listIterator(index);
        assertNotNull(listIterator);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveByObject() {
        Object o = new Object();
        fold.remove(o);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveByIndex() {
        int index = 0;
        fold.remove(index);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Collection<?> c = new ArrayList<>();
        fold.removeAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Collection<?> c = new ArrayList<>();
        fold.retainAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        int index = 0;
        Instance element = new Instance();
        fold.set(index, element);
    }

    @Test
    public void testSize() {
        int expectedSize = indices.length;
        assertEquals(expectedSize, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        int fromIndex = 0;
        int toIndex = 1;
        fold.subList(fromIndex, toIndex);
    }

    @Test
    public void testToArray() {
        Object[] expectedArray = new Object[indices.length];
        for (int i = 0; i < indices.length; i++) {
            expectedArray[i] = parent.instance(indices[i]);
        }
        assertArrayEquals(expectedArray, fold.toArray());
    }

    @Test
    public void testToArrayWithType() {
        Object[] expectedArray = fold.toArray(new Object[indices.length]);
        assertArrayEquals(expectedArray, fold.toArray());
    }

    @Test
    public void testNoAttributes() {
        int expectedNoAttributes = parent.noAttributes();
        assertEquals(expectedNoAttributes, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        int expectedClassIndex = parent.classIndex(clazz);
        assertEquals(expectedClassIndex, fold.classIndex(clazz));
    }

    @Test
    public void testClassValue() {
        int index = 0;
        Object expectedClassValue = parent.classValue(index);
        assertEquals(expectedClassValue, fold.classValue(index));
    }

    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        assertEquals(parent.size(), copy.size());
        for (int i = 0; i < parent.size(); i++) {
            assertEquals(parent.instance(i), copy.instance(i));
        }
    }

    @Test
    public void testFoldIteratorHasNext() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        assertTrue(foldIterator.hasNext());
    }

    @Test
    public void testFoldIteratorNext() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        assertEquals(parent.instance(indices[0]), foldIterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        foldIterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        foldIterator.add(new Instance());
    }

    @Test
    public void testFoldIteratorHasPrevious() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        assertFalse(foldIterator.hasPrevious());
    }

    @Test
    public void testFoldIteratorNextIndex() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        assertEquals(0, foldIterator.nextIndex());
    }

    @Test
    public void testFoldIteratorPrevious() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        assertNull(foldIterator.previous());
    }

    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        assertEquals(-1, foldIterator.previousIndex());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(fold);
        foldIterator.set(new Instance());
    }
}