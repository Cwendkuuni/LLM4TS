import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class FoldTest {
    private Dataset parent;
    private int[] indices;
    private Fold fold;
    
    @Before
    public void setUp() {
        parent = new DefaultDataset();
        indices = new int[3];
        for(int i=0; i<3; i++){
            Instance instance = new Instance();
            parent.add(instance);
            indices[i] = i;
        }
        fold = new Fold(parent, indices);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Instance instance = new Instance();
        fold.add(instance);
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> result = fold.classes();
        assertEquals(parent.classes(), result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Random rg = new Random();
        fold.folds(3, rg);
    }
    
    @Test
    public void testInstance() {
        Instance result = fold.instance(0);
        assertEquals(parent.instance(indices[0]), result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        fold.kNearest(3, inst, dm);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddIndexElement() {
        int index = 0;
        Instance element = new Instance();
        fold.add(index, element);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllCollection() {
        Collection<? extends Instance> c = new ArrayList<Instance>();
        fold.addAll(c);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndexCollection() {
        int index = 0;
        Collection<? extends Instance> c = new ArrayList<Instance>();
        fold.addAll(index, c);
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
        Collection<?> c = new ArrayList<Object>();
        fold.containsAll(c);
    }
    
    @Test
    public void testGet() {
        Instance result = fold.get(0);
        assertEquals(fold.instance(0), result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Object o = new Object();
        fold.indexOf(o);
    }
    
    @Test
    public void testIsEmpty() {
        boolean result = fold.isEmpty();
        assertFalse(result);
    }
    
    @Test
    public void testIterator() {
        Iterator<Instance> result = fold.iterator();
        assertNotNull(result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Object o = new Object();
        fold.lastIndexOf(o);
    }
    
    @Test
    public void testListIterator() {
        ListIterator<Instance> result = fold.listIterator();
        assertNotNull(result);
    }
    
    @Test
    public void testListIteratorIndex() {
        int index = 0;
        ListIterator<Instance> result = fold.listIterator(index);
        assertNotNull(result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveObject() {
        Object o = new Object();
        fold.remove(o);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        int index = 0;
        fold.remove(index);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Collection<?> c = new ArrayList<Object>();
        fold.removeAll(c);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Collection<?> c = new ArrayList<Object>();
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
        int result = fold.size();
        assertEquals(indices.length, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        int fromIndex = 0;
        int toIndex = 1;
        fold.subList(fromIndex, toIndex);
    }
    
    @Test
    public void testToArray() {
        Object[] result = fold.toArray();
        Object[] expected = new Object[indices.length];
        for(int i=0; i<indices.length; i++){
            expected[i] = fold.instance(i);
        }
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testToArrayArray() {
        Integer[] array = new Integer[indices.length];
        Integer[] result = fold.toArray(array);
        Integer[] expected = new Integer[indices.length];
        for(int i=0; i<indices.length; i++){
            expected[i] = (Integer) fold.instance(i);
        }
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testNoAttributes() {
        int result = fold.noAttributes();
        assertEquals(parent.noAttributes(), result);
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        int result = fold.classIndex(clazz);
        assertEquals(parent.classIndex(clazz), result);
    }
    
    @Test
    public void testClassValue() {
        int index = 0;
        Object result = fold.classValue(index);
        assertEquals(parent.classValue(index), result);
    }
    
    @Test
    public void testCopy() {
        Dataset result = fold.copy();
        assertNotNull(result);
        assertNotSame(fold, result);
        assertEquals(parent.noAttributes(), result.noAttributes());
        assertEquals(fold.size(), result.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorHasNext() {
        FoldIterator iterator = new FoldIterator(fold);
        boolean result = iterator.hasNext();
        assertTrue(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorNext() {
        FoldIterator iterator = new FoldIterator(fold);
        Instance result = iterator.next();
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        FoldIterator iterator = new FoldIterator(fold);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        FoldIterator iterator = new FoldIterator(fold);
        Instance arg0 = new Instance();
        iterator.add(arg0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorHasPrevious() {
        FoldIterator iterator = new FoldIterator(fold);
        boolean result = iterator.hasPrevious();
        assertFalse(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorNextIndex() {
        FoldIterator iterator = new FoldIterator(fold);
        int result = iterator.nextIndex();
        assertEquals(0, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorPrevious() {
        FoldIterator iterator = new FoldIterator(fold);
        Instance result = iterator.previous();
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorPreviousIndex() {
        FoldIterator iterator = new FoldIterator(fold);
        int result = iterator.previousIndex();
        assertEquals(-1, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        FoldIterator iterator = new FoldIterator(fold);
        Instance arg0 = new Instance();
        iterator.set(arg0);
    }    
}