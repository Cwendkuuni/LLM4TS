import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.DistanceMeasure;

public class FoldTest {
    private Fold fold;
    private DefaultDataset parent;

    @Before
    public void setUp() {
        // Create a parent dataset
        parent = new DefaultDataset();

        // Add instances to the parent dataset
        parent.add(new Instance(new double[] { 1.0, 2.0 }, "A"));
        parent.add(new Instance(new double[] { 1.5, 3.0 }, "B"));
        parent.add(new Instance(new double[] { 2.0, 2.5 }, "C"));
        parent.add(new Instance(new double[] { 3.0, 4.0 }, "D"));
        parent.add(new Instance(new double[] { 3.5, 4.5 }, "E"));

        // Create the fold using the parent dataset and indices
        int[] indices = { 1, 3, 4 };
        fold = new Fold(parent, indices);
    }

    @Test
    public void testAdd_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to add an instance to the fold
        try {
            fold.add(new Instance(new double[] { 4.0, 5.0 }, "F"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testClasses() {
        // Test that the classes() method returns the classes of the parent dataset
        assertSame(parent.classes(), fold.classes());
    }

    @Test
    public void testFolds_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to generate folds
        try {
            fold.folds(2, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testInstance() {
        // Test that the instance() method returns the correct instance from the parent dataset
        Instance instance = fold.instance(0);
        assertEquals(parent.instance(1), instance);
    }

    @Test
    public void testKNearest_ExceptionThrown(){
        // Test that UnsupportedOperationException is thrown when trying to find k nearest instances
        try {
            fold.kNearest(3, new Instance(new double[] { 2.5, 3.5 }), new DistanceMeasure() {
                public double measure(double[] arg0, double[] arg1) {
                    return 0;
                }
            });
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testAddWithIndex_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to add an instance at a specific index
        try {
            fold.add(2, new Instance(new double[] { 3.5, 4.5 }, "E"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testAddAll_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to add a collection of instances
        try {
            fold.addAll(parent);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testAddAllWithIndex_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to add a collection of instances at a specific index
        try {
            fold.addAll(2, parent);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testClear_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to clear the fold
        try {
            fold.clear();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testContains_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when checking if an instance is contained in the fold
        try {
            fold.contains(new Instance(new double[] { 1.5, 3.0 }));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testContainsAll_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when checking if a collection of instances is contained in the fold
        try {
            fold.containsAll(parent);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testGet() {
        // Test that the get() method returns the correct instance from the parent dataset
        Instance instance = fold.get(2);
        assertEquals(parent.instance(4), instance);
    }

    @Test
    public void testIndexOf_ExceptionThrown(){
        // Test that UnsupportedOperationException is thrown when trying to find the index of an instance
        try {
            fold.indexOf(new Instance(new double[] { 2.0, 2.5 }, "C"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testIsEmpty() {
        // Test that the isEmpty() method returns false
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        // Test that the iterator() method returns a valid iterator for the fold
        assertNotNull(fold.iterator());
    }

    @Test
    public void testLastIndexOf_ExceptionThrown(){
        // Test that UnsupportedOperationException is thrown when trying to find the last index of an instance
        try {
            fold.lastIndexOf(new Instance(new double[] { 2.0, 2.5 }, "C"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testListIterator() {
        // Test that the listIterator() method returns a valid iterator for the fold
        assertNotNull(fold.listIterator());
    }

    @Test
    public void testListIteratorWithIndex() {
        // Test that the listIterator(int) method returns a valid iterator for the fold
        assertNotNull(fold.listIterator(2));
    }

    @Test
    public void testRemove_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to remove an instance from the fold
        try {
            fold.remove(new Instance(new double[] { 1.5, 3.0 }));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testRemoveWithIndex_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to remove an instance at a specific index from the fold
        try {
            fold.remove(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testRemoveAll_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to remove a collection of instances from the fold
        try {
            fold.removeAll(parent);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testRetainAll_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to retain a collection of instances in the fold
        try {
            fold.retainAll(parent);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testSet_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to set an instance at a specific index in the fold
        try {
            fold.set(1, new Instance(new double[] { 3.0, 4.0 }, "D"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testSize() {
        // Test that the size() method returns the correct size of the fold
        assertEquals(3, fold.size());
    }

    @Test
    public void testSubList_ExceptionThrown() {
        // Test that UnsupportedOperationException is thrown when trying to get a sublist of the fold
        try {
            fold.subList(1, 3);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testToArray() {
        // Test that the toArray() method returns an array containing the correct instances from the fold
        Instance[] expected = { parent.instance(1), parent.instance(3), parent.instance(4) };
        assertArrayEquals(expected, fold.toArray());
    }

    @Test
    public void testToArrayWithArgument() {
        // Test that the toArray(T[]) method returns an array containing the correct instances from the fold
        Instance[] expected = { parent.instance(1), parent.instance(3), parent.instance(4) };
        Instance[] result = fold.toArray(new Instance[3]);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNoAttributes() {
        // Test that the noAttributes() method returns the number of attributes of the parent dataset
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        // Test that the classIndex(Object) method returns the class index of the parent dataset
        assertEquals(parent.classIndex("C"), fold.classIndex("C"));
    }

    @Test
    public void testClassValue() {
        // Test that the classValue(int) method returns the correct class value from the parent dataset
        assertEquals(parent.classValue(1), fold.classValue(1));
    }

    @Test
    public void testCopy() {
        // Test that the copy() method returns a copy of the fold with the same instances
        Fold copy = fold.copy();
        assertNotSame(fold, copy);
        assertArrayEquals(fold.toArray(), copy.toArray());
    }
}