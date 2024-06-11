import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    // Test add method
    @Test (expected = UnsupportedOperationException.class)
    public void testAdd() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.add(new Instance());
    }

    // Test addAll method
    @Test (expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.addAll(new ArrayList<Instance>());
    }

    // Test clear method
    @Test (expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.clear();
    }

    // Test contains method
    @Test (expected = UnsupportedOperationException.class)
    public void testContains() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.contains(new Object());
    }

    // Test containsAll method
    @Test (expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.containsAll(new ArrayList<Object>());
    }

    // Test get method
    @Test
    public void testGet() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertEquals(fold.instance(1), fold.get(1));
        assertEquals(fold.instance(2), fold.get(2));
        assertEquals(fold.instance(3), fold.get(3));
    }

    // Test indexOf method
    @Test (expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.indexOf(new Object());
    }

    // Test isEmpty method
    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertFalse(fold.isEmpty());
    }

    // Test iterator method
    @Test
    public void testIterator() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertNotNull(fold.iterator());
    }

    // Test lastIndexOf method
    @Test (expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.lastIndexOf(new Object());
    }

    // Test listIterator method
    @Test
    public void testListIterator() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertNotNull(fold.listIterator());
    }

    // Test listIterator with index method
    @Test
    public void testListIteratorWithIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertNotNull(fold.listIterator(1));
    }

    // Test remove method
    @Test (expected = UnsupportedOperationException.class)
    public void testRemove() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.remove(new Object());
    }

    // Test remove with index method
    @Test (expected = UnsupportedOperationException.class)
    public void testRemoveWithIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.remove(1);
    }

    // Test removeAll method
    @Test (expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.removeAll(new ArrayList<Object>());
    }

    // Test retainAll method
    @Test (expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.retainAll(new ArrayList<Object>());
    }

    // Test set method
    @Test (expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.set(1, new Instance());
    }

    // Test size method
    @Test
    public void testSize() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertEquals(3, fold.size());
    }

    // Test subList method
    @Test (expected = UnsupportedOperationException.class)
    public void testSubList() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.subList(1, 2);
    }

    // Test toArray method
    @Test
    public void testToArray() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Object[] array = fold.toArray();
        assertEquals(fold.instance(1), array[0]);
        assertEquals(fold.instance(2), array[1]);
        assertEquals(fold.instance(3), array[2]);
    }

    // Test toArray with array parameter method
    @Test
    public void testToArrayWithArray() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Instance[] array = fold.toArray(new Instance[3]);
        assertEquals(fold.instance(1), array[0]);
        assertEquals(fold.instance(2), array[1]);
        assertEquals(fold.instance(3), array[2]);
    }

    // Test noAttributes method
    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertEquals(fold.parent.noAttributes(), fold.noAttributes());
    }

    // Test classIndex method
    @Test
    public void testClassIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertEquals(fold.parent.classIndex(new Object()), fold.classIndex(new Object()));
    }

    // Test classValue method
    @Test
    public void testClassValue() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertEquals(fold.parent.classValue(1), fold.classValue(1));
    }

    // Test copy method
    @Test
    public void testCopy() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Dataset copy = fold.copy();
        assertNotSame(fold, copy);
        assertNotSame(fold.instance(1), copy.instance(1));
        assertNotSame(fold.instance(2), copy.instance(2));
        assertNotSame(fold.instance(3), copy.instance(3));
    }

    // Test instance method
    @Test
    public void testInstance() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertEquals(fold.parent.instance(fold.indices[1]), fold.instance(1));
        assertEquals(fold.parent.instance(fold.indices[2]), fold.instance(2));
        assertEquals(fold.parent.instance(fold.indices[3]), fold.instance(3));
    }
    
    // Test classes method
    @Test
    public void testClasses() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        assertEquals(fold.parent.classes(), fold.classes());
    }
    
    // Test folds method
    @Test (expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.folds(3, new Random());
    }
    
    // Test kNearest method
    @Test (expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.kNearest(5, new Instance(), new DistanceMeasure());
    }

    // Test FoldIterator hasNext method
    @Test
    public void testFoldIteratorHasNext() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        assertTrue(iterator.hasNext());
    }

    // Test FoldIterator next method
    @Test
    public void testFoldIteratorNext() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        assertEquals(fold.instance(2), iterator.next());
    }

    // Test FoldIterator remove method
    @Test (expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        iterator.remove();
    }

    // Test FoldIterator add method
    @Test (expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        iterator.add(new Instance());
    }

    // Test FoldIterator hasPrevious method
    @Test
    public void testFoldIteratorHasPrevious() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        assertTrue(iterator.hasPrevious());
    }

    // Test FoldIterator nextIndex method
    @Test
    public void testFoldIteratorNextIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        assertEquals(3, iterator.nextIndex());
    }

    // Test FoldIterator previous method
    @Test
    public void testFoldIteratorPrevious() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        assertEquals(fold.instance(2), iterator.previous());
    }

    // Test FoldIterator previousIndex method
    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        assertEquals(2, iterator.previousIndex());
    }

    // Test FoldIterator set method
    @Test (expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        FoldIterator iterator = fold.new FoldIterator(3);
        iterator.set(new Instance());
    }

}