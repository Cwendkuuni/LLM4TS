import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FoldTest {

    private Dataset parent;
    private int[] indices;

    @Before
    public void setUp() {
        parent = new DefaultDataset();
        parent.add(new Instance(Arrays.asList(1, 2, 3), "A"));
        parent.add(new Instance(Arrays.asList(4, 5, 6), "B"));
        parent.add(new Instance(Arrays.asList(7, 8, 9), "A"));
        indices = new int[]{0, 1};
    }

    @Test
    public void testConstructor() {
        Fold fold = new Fold(parent, indices);
        assertNotNull(fold);
    }

    @Test
    public void testAdd() {
        Fold fold = new Fold(parent, indices);
        Instance instance = new Instance(Arrays.asList(10, 11, 12), "C");
        assertFalse(fold.add(instance));
    }

    @Test
    public void testClasses() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(parent, indices);
        fold.folds(2, new Random());
    }

    @Test
    public void testInstance() {
        Fold fold = new Fold(parent, indices);
        Instance instance1 = parent.instance(0);
        Instance instance2 = fold.instance(0);
        assertEquals(instance1, instance2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(parent, indices);
        fold.kNearest(3, parent.instance(0), new EuclideanDistance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtIndex() {
        Fold fold = new Fold(parent, indices);
        Instance instance = new Instance(Arrays.asList(10, 11, 12), "C");
        fold.add(1, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Fold fold = new Fold(parent, indices);
        Collection collection = Arrays.asList(new Instance(Arrays.asList(10, 11, 12), "C"));
        fold.addAll(collection);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllAtIndex() {
        Fold fold = new Fold(parent, indices);
        Collection collection = Arrays.asList(new Instance(Arrays.asList(10, 11, 12), "C"));
        fold.addAll(1, collection);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }

    @Test
    public void testContains() {
        Fold fold = new Fold(parent, indices);
        assertTrue(fold.contains(parent.instance(0)));
        assertFalse(fold.contains(parent.instance(2)));
    }

    @Test
    public void testContainsAll() {
        Fold fold = new Fold(parent, indices);
        Collection collection1 = Arrays.asList(parent.instance(0), parent.instance(1));
        assertTrue(fold.containsAll(collection1));
        Collection collection2 = Arrays.asList(parent.instance(0), parent.instance(2));
        assertFalse(fold.containsAll(collection2));
    }

    @Test
    public void testGet() {
        Fold fold = new Fold(parent, indices);
        Instance instance1 = parent.instance(0);
        Instance instance2 = fold.get(0);
        assertEquals(instance1, instance2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Fold fold = new Fold(parent, indices);
        fold.indexOf(parent.instance(0));
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(parent, new int[]{});
        assertTrue(fold.isEmpty());
        fold = new Fold(parent, indices);
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Fold fold = new Fold(parent, indices);
        assertNotNull(fold.iterator());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Fold fold = new Fold(parent, indices);
        fold.lastIndexOf(parent.instance(0));
    }

    @Test
    public void testListIterator() {
        Fold fold = new Fold(parent, indices);
        assertNotNull(fold.listIterator());
    }

    @Test
    public void testListIteratorWithIndex() {
        Fold fold = new Fold(parent, indices);
        assertNotNull(fold.listIterator(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveObject() {
        Fold fold = new Fold(parent, indices);
        fold.remove(parent.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtIndex() {
        Fold fold = new Fold(parent, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Fold fold = new Fold(parent, indices);
        Collection collection = Arrays.asList(parent.instance(0), parent.instance(1));
        fold.removeAll(collection);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(parent, indices);
        Collection collection = Arrays.asList(parent.instance(0), parent.instance(1));
        fold.retainAll(collection);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(parent, indices);
        Instance instance = new Instance(Arrays.asList(10, 11, 12), "C");
        fold.set(0, instance);
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(parent, indices);
        assertEquals(indices.length, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Fold fold = new Fold(parent, indices);
        fold.subList(0, 1);
    }

    @Test
    public void testToArray() {
        Fold fold = new Fold(parent, indices);
        Object[] array = fold.toArray();
        assertTrue(Arrays.deepEquals(new Object[]{parent.instance(0), parent.instance(1)}, array));
    }

    @Test
    public void testToArrayWithGenericArray() {
        Fold fold = new Fold(parent, indices);
        Instance[] array = fold.toArray(new Instance[2]);
        assertTrue(Arrays.deepEquals(new Instance[]{parent.instance(0), parent.instance(1)}, array));
    }

    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Fold fold = new Fold(parent, indices);
        assertEquals(parent.classIndex("A"), fold.classIndex("A"));
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
        for (int i = 0; i < fold.size(); i++) {
            assertEquals(fold.get(i), copy.get(i));
        }
    }

    @Test
    public void testFoldIteratorHasNext() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(0);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testFoldIteratorNext() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(0);
        assertEquals(parent.instance(0), iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(0);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(0);
        iterator.add(new Instance(Arrays.asList(10, 11, 12), "C"));
    }

    @Test
    public void testFoldIteratorHasPrevious() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(1);
        assertTrue(iterator.hasPrevious());
    }

    @Test
    public void testFoldIteratorNextIndex() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(0);
        assertEquals(1, iterator.nextIndex());
    }

    @Test
    public void testFoldIteratorPrevious() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(1);
        assertEquals(parent.instance(0), iterator.previous());
    }

    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(1);
        assertEquals(0, iterator.previousIndex());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.new FoldIterator(0);
        iterator.set(new Instance(Arrays.asList(10, 11, 12), "C"));
    }
}