import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import net.sf.javaml.core.*;

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
    
    @Test
    public void testAdd() {
        Instance instance = new DenseInstance(0);
        try {
            fold.add(instance);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> classes = fold.classes();
        SortedSet<Object> expectedClasses = parent.classes();
        assertEquals(expectedClasses, classes);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        int numFolds = 3;
        Random rg = new Random();
        fold.folds(numFolds, rg);
    }
    
    @Test
    public void testInstance() {
        int index = 1;
        Instance expectedInstance = parent.instance(indices[index]);
        Instance instance = fold.instance(index);
        assertEquals(expectedInstance, instance);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        int k = 3;
        Instance inst = new DenseInstance(0);
        DistanceMeasure dm = new EuclideanDistance();
        fold.kNearest(k, inst, dm);
    }
    
    @Test
    public void testAddIndexElement() {
        int index = 1;
        Instance instance = new DenseInstance(0);
        try {
            fold.add(index, instance);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> c = new ArrayList<>();
        try {
            fold.addAll(c);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testAddAllIndex() {
        int index = 1;
        Collection<Instance> c = new ArrayList<>();
        try {
            fold.addAll(index, c);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClear() {
        try {
            fold.clear();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
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
        int index = 1;
        Instance expectedInstance = fold.instance(index);
        Instance instance = fold.get(index);
        assertEquals(expectedInstance, instance);
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
        assertTrue(iterator.hasNext());
        assertEquals(fold.instance(0), iterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Object o = new Object();
        fold.lastIndexOf(o);
    }
    
    @Test
    public void testListIterator() {
        ListIterator<Instance> listIterator = fold.listIterator();
        assertTrue(listIterator.hasNext());
        assertEquals(fold.instance(0), listIterator.next());
    }
    
    @Test
    public void testListIteratorIndex() {
        int index = 1;
        ListIterator<Instance> listIterator = fold.listIterator(index);
        assertTrue(listIterator.hasNext());
        assertEquals(fold.instance(index), listIterator.next());
    }
    
    @Test
    public void testRemove() {
        Object o = new Object();
        try {
            fold.remove(o);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveIndex() {
        int index = 1;
        try {
            fold.remove(index);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveAll() {
        Collection<?> c = new ArrayList<>();
        try {
            fold.removeAll(c);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRetainAll() {
        Collection<?> c = new ArrayList<>();
        try {
            fold.retainAll(c);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        int index = 1;
        Instance instance = new DenseInstance(0);
        try {
            fold.set(index, instance);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testSize() {
        int expectedSize = indices.length;
        int size = fold.size();
        assertEquals(expectedSize, size);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        int fromIndex = 0;
        int toIndex = 2;
        fold.subList(fromIndex, toIndex);
    }
    
    @Test
    public void testToArray() {
        Object[] expected = new Object[fold.size()];
        for (int i = 0; i < fold.size(); i++) {
            expected[i] = fold.instance(i);
        }
        Object[] array = fold.toArray();
        assertArrayEquals(expected, array);
    }
    
    @Test
    public void testToArrayGeneric() {
        Integer[] expected = new Integer[fold.size()];
        for (int i = 0; i < fold.size(); i++) {
            expected[i] = Integer.valueOf(i);
        }
        Integer[] array = fold.toArray(new Integer[fold.size()]);
        assertArrayEquals(expected, array);
    }
    
    @Test
    public void testNoAttributes() {
        int expectedNoAttributes = parent.noAttributes();
        int noAttributes = fold.noAttributes();
        assertEquals(expectedNoAttributes, noAttributes);
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        int expectedClassIndex = parent.classIndex(clazz);
        int classIndex = fold.classIndex(clazz);
        assertEquals(expectedClassIndex, classIndex);
    }
    
    @Test
    public void testClassValue() {
        int index = 1;
        Object expectedClassValue = parent.classValue(index);
        Object classValue = fold.classValue(index);
        assertEquals(expectedClassValue, classValue);
    }
    
    @Test
    public void testCopy() {
        Dataset expectedCopy = new DefaultDataset();
        for (int i = 0; i < fold.size(); i++) {
            expectedCopy.add(fold.instance(i).copy());
        }
        Dataset copy = fold.copy();
        assertEquals(expectedCopy, copy);
    }
    
    @Test
    public void testFoldIteratorHasNext() {
        Fold.FoldIterator iterator = fold.new FoldIterator();
        assertTrue(iterator.hasNext());
        iterator.next();
    }
    
    @Test
    public void testFoldIteratorNext() {
        Fold.FoldIterator iterator = fold.new FoldIterator();
        assertTrue(iterator.hasNext());
        Instance expectedInstance = fold.instance(0);
        Instance instance = iterator.next();
        assertEquals(expectedInstance, instance);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Fold.FoldIterator iterator = fold.new FoldIterator();
        iterator.remove();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Fold.FoldIterator iterator = fold.new FoldIterator();
        iterator.add(new DenseInstance(0));
    }
    
    @Test
    public void testFoldIteratorHasPrevious() {
        int index = 1;
        Fold.FoldIterator iterator = fold.new FoldIterator(index);
        assertTrue(iterator.hasPrevious());
        iterator.previous();
    }
    
    @Test
    public void testFoldIteratorNextIndex() {
        int index = 1;
        Fold.FoldIterator iterator = fold.new FoldIterator(index);
        assertEquals(index, iterator.nextIndex());
    }
    
    @Test
    public void testFoldIteratorPrevious() {
        int index = 1;
        Fold.FoldIterator iterator = fold.new FoldIterator(index);
        assertTrue(iterator.hasPrevious());
        Instance expectedInstance = fold.instance(index - 1);
        Instance instance = iterator.previous();
        assertEquals(expectedInstance, instance);
    }
    
    @Test
    public void testFoldIteratorPreviousIndex() {
        int index = 1;
        Fold.FoldIterator iterator = fold.new FoldIterator(index);
        assertEquals(index - 1, iterator.previousIndex());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorSet() {
        Fold.FoldIterator iterator = fold.new FoldIterator();
        iterator.set(new DenseInstance(0));
    }
}