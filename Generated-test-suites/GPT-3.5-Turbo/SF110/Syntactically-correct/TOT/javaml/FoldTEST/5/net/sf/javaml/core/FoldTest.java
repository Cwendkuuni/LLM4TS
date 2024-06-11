import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoldTest {
    
    private Dataset parent;
    private Fold fold;
    private Instance instance;
    
    @Before
    public void setup() {
        parent = new DefaultDataset();
        fold = new Fold(parent, new int[]{0, 1, 2});
        instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        parent.add(instance);
    }
    
    @Test
    public void testAdd() {
        try {
            fold.add(instance);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> classes = fold.classes();
        assertTrue(classes.contains("A"));
    }
    
    @Test
    public void testFolds() {
        try {
            fold.folds(2, new Random());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testInstance() {
        Instance result = fold.instance(0);
        assertSame(instance, result);
    }
    
    @Test
    public void testKNearest() {
        try {
            fold.kNearest(3, instance, new EuclideanDistance());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testAddByIndex() {
        try {
            fold.add(0, instance);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testAddAll() {
        try {
            fold.addAll(new ArrayList<Instance>());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testAddAllByIndex() {
        try {
            fold.addAll(0, new ArrayList<Instance>());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClear() {
        try {
            fold.clear();
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testContains() {
        try {
            fold.contains(instance);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAll() {
        try {
            fold.containsAll(new ArrayList<Instance>());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        Instance result = fold.get(0);
        assertSame(instance, result);
    }
    
    @Test
    public void testIndexOf() {
        try {
            fold.indexOf(instance);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(fold.isEmpty());
    }
    
    @Test
    public void testIterator() {
        Iterator<Instance> iterator = fold.iterator();
        assertTrue(iterator.hasNext());
        assertSame(instance, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testLastIndexOf() {
        try {
            fold.lastIndexOf(instance);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testListIterator() {
        ListIterator<Instance> iterator = fold.listIterator();
        assertTrue(iterator.hasNext());
        assertSame(instance, iterator.next());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }
    
    @Test
    public void testListIteratorWithIndex() {
        ListIterator<Instance> iterator = fold.listIterator(0);
        assertTrue(iterator.hasNext());
        assertSame(instance, iterator.next());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }
    
    @Test
    public void testRemoveByObject() {
        try {
            fold.remove(instance);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveByIndex() {
        try {
            fold.remove(0);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveAll() {
        try {
            fold.removeAll(new ArrayList<Instance>());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRetainAll() {
        try {
            fold.retainAll(new ArrayList<Instance>());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        try {
            fold.set(0, instance);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testSize() {
        int size = fold.size();
        assertEquals(3, size);
    }
    
    @Test
    public void testSubList() {
        try {
            fold.subList(0, 2);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testToArray() {
        Object[] array = fold.toArray();
        assertSame(instance, array[0]);
        assertEquals(null, array[1]);
        assertEquals(null, array[2]);
    }
    
    @Test
    public void testToArrayWithArguments() {
        Double[] array = new Double[3];
        Double[] result = fold.toArray(array);
        assertSame(instance, result[0]);
        assertEquals(null, result[1]);
        assertEquals(null, result[2]);
    }
    
    @Test
    public void testNoAttributes() {
        int noAttributes = fold.noAttributes();
        assertEquals(0, noAttributes);
    }
    
    @Test
    public void testClassIndex() {
        int classIndex = fold.classIndex("A");
        assertEquals(-1, classIndex);
    }
    
    @Test
    public void testClassValue() {
        Object classValue = fold.classValue(0);
        assertEquals("A", classValue);
    }
    
    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        assertEquals(fold.size(), copy.size());
        assertNotSame(fold, copy);
        assertNotSame(fold.iterator().next(), copy.iterator().next());
    }
    
}