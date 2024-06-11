import org.junit.Test;
import static org.junit.Assert.*;



public class FoldTest {
    @Test
    public void testAddUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.add(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testClasses() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        SortedSet<Object> result = fold.classes();
        assertNotNull(result);
    }
    
    @Test
    public void testFoldsUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.folds(0, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testInstance() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Instance result = fold.instance(0);
        assertNotNull(result);
    }
    
    @Test
    public void testKNearestUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.kNearest(0, null, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testAddIndexUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.add(0, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testAddAllCollectionUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.addAll(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testAddAllIndexCollectionUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.addAll(0, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testClearUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.clear();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testContains() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        boolean result = fold.contains(null);
        assertFalse(result);
    }
    
    @Test
    public void testContainsAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        boolean result = fold.containsAll(null);
        assertFalse(result);
    }
    
    @Test
    public void testGet() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Instance result = fold.get(0);
        assertNotNull(result);
    }
    
    @Test
    public void testIndexOf() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        int result = fold.indexOf(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        boolean result = fold.isEmpty();
        assertFalse(result);
    }
    
    @Test
    public void testIterator() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Iterator<Instance> result = fold.iterator();
        assertNotNull(result);
    }
    
    @Test
    public void testLastIndexOf() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        int result = fold.lastIndexOf(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testListIterator() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        ListIterator<Instance> result = fold.listIterator();
        assertNotNull(result);
    }
    
    @Test
    public void testListIteratorIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        ListIterator<Instance> result = fold.listIterator(0);
        assertNotNull(result);
    }
    
    @Test
    public void testRemoveUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.remove(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testRemoveIndexUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.remove(0);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testRemoveAllUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.removeAll(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testRetainAllUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.retainAll(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testSetUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.set(0, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testSize() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        int result = fold.size();
        assertEquals(0, result);
    }
    
    @Test
    public void testSubListUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        try {
            fold.subList(0, 0);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testToArray() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Object[] result = fold.toArray();
        assertNotNull(result);
    }
    
    @Test
    public void testToArrayGeneric() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        String[] result = fold.toArray(new String[]{});
        assertNotNull(result);
    }
    
    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        int result = fold.noAttributes();
        assertEquals(0, result);
    }
    
    @Test
    public void testClassIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        int result = fold.classIndex(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testClassValue() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Object result = fold.classValue(0);
        assertNotNull(result);
    }
    
    @Test
    public void testCopy() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Dataset result = fold.copy();
        assertNotNull(result);
    }
    
    @Test
    public void testFoldIteratorConstructorIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        assertNotNull(iterator);
    }
    
    @Test
    public void testFoldIteratorConstructorFold() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        assertNotNull(iterator);
    }
    
    @Test
    public void testFoldIteratorHasNext() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        boolean result = iterator.hasNext();
        assertTrue(result);
    }
    
    @Test
    public void testFoldIteratorNext() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        Instance result = iterator.next();
        assertNotNull(result);
    }
    
    @Test
    public void testFoldIteratorRemoveUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testFoldIteratorAddUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        try {
            iterator.add(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
    
    @Test
    public void testFoldIteratorHasPrevious() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        boolean result = iterator.hasPrevious();
        assertTrue(result);
    }
    
    @Test
    public void testFoldIteratorNextIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        int result = iterator.nextIndex();
        assertEquals(0, result);
    }
    
    @Test
    public void testFoldIteratorPrevious() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        Instance result = iterator.previous();
        assertNotNull(result);
    }
    
    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        int result = iterator.previousIndex();
        assertEquals(-1, result);
    }
    
    @Test
    public void testFoldIteratorSetUnsupportedOperationException() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Fold.FoldIterator iterator = fold.new FoldIterator();
        try {
            iterator.set(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Passed the test
        }
    }
}

