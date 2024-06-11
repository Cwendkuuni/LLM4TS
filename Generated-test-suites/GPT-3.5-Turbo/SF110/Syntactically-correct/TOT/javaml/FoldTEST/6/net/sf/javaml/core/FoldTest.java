
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.*;

public class FoldTest {
    private Fold fold;
    private Instance instance1;
    private Instance instance2;
    
    @Before
    public void setUp() {
        int[] indices = {0, 1};
        Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[]{1.2, 2.3, 3.4}));
        parent.add(new DenseInstance(new double[]{4.5, 5.6, 6.7}));

        fold = new Fold(parent, indices);
        instance1 = new DenseInstance(new double[]{1.2, 2.3, 3.4});
        instance2 = new DenseInstance(new double[]{4.5, 5.6, 6.7});
    }
    
    @Test
    public void testAdd() {
        try {
            fold.add(instance1);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> classes = fold.classes();
        assertNotNull(classes);
        assertTrue(classes.isEmpty());
    }
    
    @Test
    public void testFolds() {
        try {
            int numFolds = 5;
            Random rg = new Random();
            Dataset[] folds = fold.folds(numFolds, rg);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testInstance() {
        Instance instance = fold.instance(0);
        assertNotNull(instance);
        assertEquals(instance1, instance);
    }
    
    @Test
    public void testKNearest() {
        try {
            int k = 3;
            DistanceMeasure dm = new EuclideanDistance();
            Set<Instance> nearestInstances = fold.kNearest(k, instance1, dm);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testAddByIndex() {
        try {
            fold.add(0, instance1);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testAddAll() {
        try {
            Collection<Instance> collection = new ArrayList<>();
            collection.add(instance1);
            collection.add(instance2);
            fold.addAll(collection);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testAddAllByIndex() {
        try {
            Collection<Instance> collection = new ArrayList<>();
            collection.add(instance1);
            collection.add(instance2);
            fold.addAll(0, collection);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClear() {
        try {
            fold.clear();
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testContains() {
        try {
            boolean containsInstance = fold.contains(instance1);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAll() {
        try {
            Collection<Instance> collection = new ArrayList<>();
            collection.add(instance1);
            collection.add(instance2);
            boolean containsAllInstances = fold.containsAll(collection);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        Instance instance = fold.get(0);
        assertNotNull(instance);
        assertEquals(instance1, instance);
    }
    
    @Test
    public void testIndexOf() {
        try {
            int index = fold.indexOf(instance1);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testIsEmpty() {
        boolean isEmpty = fold.isEmpty();
        assertFalse(isEmpty);
    }
    
    @Test
    public void testIterator() {
        Iterator<Instance> iterator = fold.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(instance1, iterator.next());
        assertEquals(instance2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testLastIndexOf() {
        try {
            int lastIndex = fold.lastIndexOf(instance1);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testListIterator() {
        ListIterator<Instance> listIterator = fold.listIterator();
        assertNotNull(listIterator);
        assertTrue(listIterator.hasNext());
        assertFalse(listIterator.hasPrevious());
        assertEquals(instance1, listIterator.next());
        assertEquals(instance2, listIterator.next());
        assertEquals(instance1, listIterator.previous());
        assertFalse(listIterator.hasPrevious());
        assertEquals(0, listIterator.previousIndex());
        assertEquals(1, listIterator.nextIndex());
    }
    
    @Test
    public void testListIteratorWithIndex() {
        int index = 1;
        ListIterator<Instance> listIterator = fold.listIterator(index);
        assertNotNull(listIterator);
        assertTrue(listIterator.hasNext());
        assertTrue(listIterator.hasPrevious());
        assertEquals(instance2, listIterator.next());
        assertEquals(instance1, listIterator.previous());
        assertEquals(index, listIterator.previousIndex());
        assertEquals(index + 1, listIterator.nextIndex());
    }
    
    @Test
    public void testRemoveObject() {
        try {
            boolean removed = fold.remove(instance1);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveByIndex() {
        try {
            Instance removedInstance = fold.remove(0);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveAll() {
        try {
            Collection<Instance> collection = new ArrayList<>();
            collection.add(instance1);
            collection.add(instance2);
            boolean removedAll = fold.removeAll(collection);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRetainAll() {
        try {
            Collection<Instance> collection = new ArrayList<>();
            collection.add(instance1);
            collection.add(instance2);
            boolean retainedAll = fold.retainAll(collection);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        try {
            Instance replacedInstance = fold.set(0, instance1);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testSize() {
        int size = fold.size();
        assertEquals(2, size);
    }
    
    @Test
    public void testSubList() {
        try {
            int fromIndex = 0;
            int toIndex = 1;
            List<Instance> subList = fold.subList(fromIndex, toIndex);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testToArray() {
        Object[] array = fold.toArray();
        assertNotNull(array);
        assertEquals(instance1, array[0]);
        assertEquals(instance2, array[1]);
    }
    
    @Test
    public void testToArrayWithArrayParameter() {
        Object[] array = fold.toArray(new Object[0]);
        assertNotNull(array);
        assertEquals(instance1, array[0]);
        assertEquals(instance2, array[1]);
    }
    
    @Test
    public void testNoAttributes() {
        int numAttributes = fold.noAttributes();
        assertEquals(3, numAttributes);
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = fold.classValue(0);
        assertNotNull(clazz);
        assertEquals(instance1.classValue(), clazz);
    }
    
    @Test
    public void testClassValue() {
        int index = 0;
        Object clazz = fold.classValue(index);
        assertNotNull(clazz);
        assertEquals(instance1.classValue(), clazz);
    }
    
    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        assertNotNull(copy);
        assertEquals(fold.size(), copy.size());
        assertEquals(fold.get(0), copy.get(0));
        assertEquals(fold.get(1), copy.get(1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorNextOnEmptyFold() {
        List<Instance> emptyIndices = new ArrayList<>();
        Fold emptyFold = new Fold(fold, emptyIndices);
        FoldIterator foldIterator = new FoldIterator(emptyFold);
        
        assertFalse(foldIterator.hasNext());
        foldIterator.next();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorPreviousOnEmptyFold() {
        List<Instance> emptyIndices = new ArrayList<>();
        Fold emptyFold = new Fold(fold, emptyIndices);
        FoldIterator foldIterator = new FoldIterator(emptyFold);
        
        assertFalse(foldIterator.hasPrevious());
        foldIterator.previous();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        FoldIterator foldIterator = new FoldIterator(fold);
        foldIterator.remove();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        FoldIterator foldIterator = new FoldIterator(fold);
        foldIterator.add(new DenseInstance(new double[]{7.8, 8.9, 9.0}));
    }
    
    @Test
    public void testFoldIteratorNextIndexAndPreviousIndex() {
        FoldIterator foldIterator = new FoldIterator(fold);
        assertEquals(0, foldIterator.previousIndex());
        assertEquals(0, foldIterator.nextIndex());
        
        foldIterator.next();
        assertEquals(0, foldIterator.previousIndex());
        assertEquals(1, foldIterator.nextIndex());
        
        foldIterator.next();
        assertEquals(1, foldIterator.previousIndex());
        assertEquals(2, foldIterator.nextIndex());
        
        foldIterator.previous();
        assertEquals(0, foldIterator.previousIndex());
        assertEquals(1, foldIterator.nextIndex());
    }
    
    @Test
    public void testFoldIteratorSet() {
        FoldIterator foldIterator = new FoldIterator(fold);
        Instance newInstance = new DenseInstance(new double[]{7.8, 8.9, 9.0});
        foldIterator.next();
        foldIterator.set(newInstance);
        assertEquals(newInstance, foldIterator.previous());
    }
}

