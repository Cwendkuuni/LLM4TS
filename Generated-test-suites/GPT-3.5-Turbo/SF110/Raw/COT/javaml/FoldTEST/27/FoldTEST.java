
import static org.junit.Assert.*;
import org.junit.Test;

public class FoldTest {

    @Test
    public void testAdd() {
        Fold fold = new Fold(null, null);
        Instance instance = new Instance();
        assertFalse(fold.add(instance));
    }
    
    @Test
    public void testClasses() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.classes());
    }
    
    @Test
    public void testFolds() {
        Fold fold = new Fold(null, null);
        try {
            fold.folds(0, null);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testInstance() {
        Fold fold = new Fold(null, null);
        assertNull(fold.instance(0));
    }
    
    @Test
    public void testKNearest() {
        Fold fold = new Fold(null, null);
        try {
            fold.kNearest(0, null, null);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testAddIndexElement() {
        Fold fold = new Fold(null, null);
        Instance instance = new Instance();
        try {
            fold.add(0, instance);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testAddAllC() {
        Fold fold = new Fold(null, null);
        Collection<Instance> collection = new ArrayList<>();
        assertFalse(fold.addAll(collection));
    }
    
    @Test
    public void testAddAllIndexC() {
        Fold fold = new Fold(null, null);
        Collection<Instance> collection = new ArrayList<>();
        try {
            fold.addAll(0, collection);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testClear() {
        Fold fold = new Fold(null, null);
        try {
            fold.clear();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testContains() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.contains(null));
    }
    
    @Test
    public void testContainsAll() {
        Fold fold = new Fold(null, null);
        Collection<Object> collection = new ArrayList<>();
        assertFalse(fold.containsAll(collection));
    }
    
    @Test
    public void testGet() {
        Fold fold = new Fold(null, null);
        assertNull(fold.get(0));
    }
    
    @Test
    public void testIndexOf() {
        Fold fold = new Fold(null, null);
        try {
            fold.indexOf(null);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(null, null);
        assertFalse(fold.isEmpty());
    }
    
    @Test
    public void testIterator() {
        Fold fold = new Fold(null, null);
        assertNotNull(fold.iterator());
    }
    
    @Test
    public void testLastIndexOf() {
        Fold fold = new Fold(null, null);
        try {
            fold.lastIndexOf(null);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
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
        try {
            fold.remove(0);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testRemoveAll() {
        Fold fold = new Fold(null, null);
        Collection<Object> collection = new ArrayList<>();
        assertFalse(fold.removeAll(collection));
    }
    
    @Test
    public void testRetainAll() {
        Fold fold = new Fold(null, null);
        Collection<Object> collection = new ArrayList<>();
        assertFalse(fold.retainAll(collection));
    }
    
    @Test
    public void testSet() {
        Fold fold = new Fold(null, null);
        Instance instance = new Instance();
        try {
            fold.set(0, instance);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testSize() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.size());
    }
    
    @Test
    public void testSubList() {
        Fold fold = new Fold(null, null);
        try {
            fold.subList(0, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testToArray() {
        Fold fold = new Fold(null, null);
        assertArrayEquals(new Object[0], fold.toArray());
    }
    
    @Test
    public void testToArray() {
        Fold fold = new Fold(null, null);
        assertArrayEquals(new Object[0], fold.toArray(new Object[0]));
    }
    
    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        Fold fold = new Fold(null, null);
        assertEquals(0, fold.classIndex(null));
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
    public void testFoldIteratorHasNext() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testFoldIteratorNext() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertNull(iterator.next());
    }
    
    @Test
    public void testFoldIteratorRemove() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        try {
            iterator.remove();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testFoldIteratorAdd() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        try {
            iterator.add(null);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testFoldIteratorHasPrevious() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertFalse(iterator.hasPrevious());
    }
    
    @Test
    public void testFoldIteratorNextIndex() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertEquals(0, iterator.nextIndex());
    }
    
    @Test
    public void testFoldIteratorPrevious() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertNull(iterator.previous());
    }
    
    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertEquals(0, iterator.previousIndex());
    }
    
    @Test
    public void testFoldIteratorSet() {
        Fold fold = new Fold(null, null);
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        try {
            iterator.set(null);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
}

