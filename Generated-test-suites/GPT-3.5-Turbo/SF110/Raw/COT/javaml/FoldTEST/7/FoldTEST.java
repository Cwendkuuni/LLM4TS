import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    private Dataset parent;
    private Fold fold;

    @Before
    public void setup() {
        parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = new int[]{0, 1, 2};
        fold = new Fold(parent, indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        fold.add(new Instance());
    }

    @Test
    public void testClasses() {
        assertEquals(parent.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        fold.folds(5, new Random());
    }

    @Test
    public void testInstance() {
        assertNotNull(fold.instance(0));
        assertNotNull(fold.instance(1));
        assertNotNull(fold.instance(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        fold.kNearest(3, new Instance(), new DistanceMeasure());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIndex() {
        fold.add(0, new Instance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        fold.addAll(new ArrayList<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithIndex() {
        fold.addAll(0, new ArrayList<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        fold.contains(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        fold.containsAll(new ArrayList<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGet() {
        assertNotNull(fold.get(0));
        assertNotNull(fold.get(1));
        assertNotNull(fold.get(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        fold.indexOf(new Object());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(fold.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator() {
        assertNotNull(fold.iterator());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        fold.lastIndexOf(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        assertNotNull(fold.listIterator());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorWithIndex() {
        assertNotNull(fold.listIterator(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        fold.remove(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIndex() {
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        fold.removeAll(new ArrayList<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        fold.retainAll(new ArrayList<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        fold.set(0, new Instance());
    }

    @Test
    public void testSize() {
        assertEquals(3, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        fold.subList(0, 2);
    }

    @Test
    public void testToArray() {
        Object[] array = fold.toArray();
        assertNotNull(array);
        assertEquals(3, array.length);
        assertTrue(array[0] instanceof Instance);
        assertTrue(array[1] instanceof Instance);
        assertTrue(array[2] instanceof Instance);
    }

    @Test
    public void testToArrayWithGenericArray() {
        Instance[] array = fold.toArray(new Instance[0]);
        assertNotNull(array);
        assertEquals(3, array.length);
        assertNotNull(array[0]);
        assertNotNull(array[1]);
        assertNotNull(array[2]);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        assertEquals(parent.classIndex(new Object()), fold.classIndex(new Object()));
    }

    @Test
    public void testClassValue() {
        assertEquals(parent.classValue(0), fold.classValue(0));
    }

    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        assertNotNull(copy);
        assertEquals(fold.size(), copy.size());
        for (int i = 0; i < fold.size(); i++) {
            assertNotSame(fold.instance(i), copy.instance(i));
            assertEquals(fold.instance(i), copy.instance(i));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorHasNext() {
        assertTrue(fold.iterator().hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorNext() {
        assertNotNull(fold.iterator().next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        fold.iterator().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorAdd() {
        fold.iterator().add(new Instance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorHasPrevious() {
        assertFalse(fold.listIterator().hasPrevious());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorNextIndex() {
        assertEquals(0, fold.listIterator().nextIndex());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorPrevious() {
        assertNotNull(fold.listIterator().previous());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorPreviousIndex() {
        assertEquals(-1, fold.listIterator().previousIndex());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorSet() {
        fold.listIterator().set(new Instance());
    }
    
    @Test
    public void testFoldIteratorHasNext() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(0);
        assertTrue(foldIterator.hasNext());
        foldIterator = fold.new FoldIterator(1);
        assertTrue(foldIterator.hasNext());
        foldIterator = fold.new FoldIterator(2);
        assertTrue(foldIterator.hasNext());
    }
    
    @Test
    public void testFoldIteratorNext() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(0);
        assertNotNull(foldIterator.next());
        foldIterator = fold.new FoldIterator(1);
        assertNotNull(foldIterator.next());
        foldIterator = fold.new FoldIterator(2);
        assertNotNull(foldIterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(0);
        foldIterator.remove();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(0);
        foldIterator.add(new Instance());
    }
    
    @Test
    public void testFoldIteratorHasPrevious() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(0);
        assertFalse(foldIterator.hasPrevious());
        foldIterator = fold.new FoldIterator(1);
        assertTrue(foldIterator.hasPrevious());
        foldIterator = fold.new FoldIterator(2);
        assertTrue(foldIterator.hasPrevious());
    }
    
    @Test
    public void testFoldIteratorNextIndex() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(0);
        assertEquals(0, foldIterator.nextIndex());
        foldIterator = fold.new FoldIterator(1);
        assertEquals(1, foldIterator.nextIndex());
        foldIterator = fold.new FoldIterator(2);
        assertEquals(2, foldIterator.nextIndex());
    }
    
    @Test
    public void testFoldIteratorPrevious() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(1);
        assertNotNull(foldIterator.previous());
        foldIterator = fold.new FoldIterator(2);
        assertNotNull(foldIterator.previous());
    }
    
    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(1);
        assertEquals(0, foldIterator.previousIndex());
        foldIterator = fold.new FoldIterator(2);
        assertEquals(1, foldIterator.previousIndex());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold.FoldIterator foldIterator = fold.new FoldIterator(0);
        foldIterator.set(new Instance());
    }
}