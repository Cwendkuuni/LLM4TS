import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.distance.*;
import java.util.*;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.add(new Instance());
    }
    
    @Test
    public void testClasses() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        SortedSet<Object> classes = parent.classes();
        assertNotNull(classes);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.folds(10, new Random());
    }
    
    @Test
    public void testInstance() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Instance instance = parent.instance(0);
        assertNotNull(instance);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.kNearest(5, new Instance(), new DistanceMeasure());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddIndex() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.add(1, new Instance());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.addAll(new ArrayList<Instance>());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndex() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.addAll(1, new ArrayList<Instance>());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.clear();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.contains(new Object());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.containsAll(new ArrayList<Object>());
    }
    
    @Test
    public void testGet() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Instance instance = parent.get(0);
        assertNotNull(instance);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.indexOf(new Object());
    }
    
    @Test
    public void testIsEmpty() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        boolean isEmpty = parent.isEmpty();
        assertFalse(isEmpty);
    }
    
    @Test
    public void testIterator() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Iterator<Instance> iterator = parent.iterator();
        assertNotNull(iterator);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.lastIndexOf(new Object());
    }
    
    @Test
    public void testListIterator() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        ListIterator<Instance> listIterator = parent.listIterator();
        assertNotNull(listIterator);
    }
    
    @Test
    public void testListIteratorIndex() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        ListIterator<Instance> listIterator = parent.listIterator(0);
        assertNotNull(listIterator);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.remove(new Object());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.remove(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.removeAll(new ArrayList<Object>());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.retainAll(new ArrayList<Object>());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.set(1, new Instance());
    }
    
    @Test
    public void testSize() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int size = parent.size();
        assertEquals(3, size);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        parent.subList(0, 2);
    }
    
    @Test
    public void testToArray() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Object[] array = parent.toArray();
        assertNotNull(array);
    }
    
    @Test
    public void testToArrayTyped() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Integer[] array = new Integer[3];
        Integer[] result = parent.toArray(array);
        assertArrayEquals(array, result);
    }
    
    @Test
    public void testNoAttributes() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int noAttributes = parent.noAttributes();
        assertEquals(0, noAttributes);
    }
    
    @Test
    public void testClassIndex() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int classIndex = parent.classIndex(new Object());
        assertEquals(-1, classIndex);
    }
    
    @Test
    public void testClassValue() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Object classValue = parent.classValue(0);
        assertNull(classValue);
    }
    
    @Test
    public void testCopy() {
        Dataset parent = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Dataset copy = parent.copy();
        assertNotNull(copy);
    }
    
    @Test
    public void testFoldIteratorHasNext() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        boolean hasNext = iterator.hasNext();
        assertTrue(hasNext);
    }
    
    @Test
    public void testFoldIteratorNext() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        Instance next = iterator.next();
        assertNotNull(next);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.remove();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.add(new Instance());
    }
    
    @Test
    public void testFoldIteratorHasPrevious() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(3);
        boolean hasPrevious = iterator.hasPrevious();
        assertTrue(hasPrevious);
    }
    
    @Test
    public void testFoldIteratorNextIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        int nextIndex = iterator.nextIndex();
        assertEquals(0, nextIndex);
    }
    
    @Test
    public void testFoldIteratorPrevious() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(1);
        Instance previous = iterator.previous();
        assertNotNull(previous);
    }
    
    @Test
    public void testFoldIteratorPreviousIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(1);
        int previousIndex = iterator.previousIndex();
        assertEquals(0, previousIndex);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Fold.FoldIterator iterator = fold.new FoldIterator(0);
        iterator.set(new Instance());
    }
}