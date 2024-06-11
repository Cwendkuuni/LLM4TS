import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    @Test
    public void testAdd() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertFalse(fold.add(new Instance()));
    }

    @Test
    public void testClasses() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.classes());
    }

    @Test
    public void testFolds() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.folds(5, new Random());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testInstance() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.instance(0));

        // Edge case - index out of bounds
        try {
            fold.instance(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Success
        }
    }

    @Test
    public void testKNearest() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.kNearest(3, new Instance(), new DistanceMeasure());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testAddAtIndex() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.add(0, new Instance());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testAddAll() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.addAll(new ArrayList<>());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testAddAllAtIndex() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.addAll(0, new ArrayList<>());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testClear() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.clear();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testContains() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertFalse(fold.contains(new Object()));
    }

    @Test
    public void testContainsAll() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertFalse(fold.containsAll(new ArrayList<>()));
    }

    @Test
    public void testGet() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.get(0));

        // Edge case - index out of bounds
        try {
            fold.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Success
        }
    }

    @Test
    public void testIndexOf() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertEquals(0, fold.indexOf(new Object()));
    }

    @Test
    public void testIsEmpty() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.iterator());
    }

    @Test
    public void testLastIndexOf() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertEquals(0, fold.lastIndexOf(new Object()));
    }

    @Test
    public void testListIterator() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.listIterator());

        // Typical use case with index
        assertNotNull(fold.listIterator(0));
    }

    @Test
    public void testRemoveObject() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.remove(new Object());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testRemoveAtIndex() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.remove(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testRemoveAll() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.removeAll(new ArrayList<>());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testRetainAll() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.retainAll(new ArrayList<>());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testSet() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.set(0, new Instance());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testSize() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertEquals(3, fold.size());
    }

    @Test
    public void testSubList() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        try {
            fold.subList(0, 2);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    @Test
    public void testToArray() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.toArray());
    }

    @Test
    public void testToArrayWithArgument() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.toArray(new Object[0]));
    }

    @Test
    public void testNoAttributes() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertEquals(0, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertEquals(0, fold.classIndex(new Object()));
    }

    @Test
    public void testClassValue() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.classValue(0));
    }

    @Test
    public void testCopy() {
        // Typical use case
        Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
        assertNotNull(fold.copy());
    }

    @Test
    public void testFoldIterator() {
        // Typical use case
        Fold.FoldIterator iterator = new Fold(new Dataset(), new int[]{1, 2, 3}).new FoldIterator(0);
        assertNotNull(iterator);

        // Typical use case with index
        Fold.FoldIterator iteratorWithIndex = new Fold(new Dataset(), new int[]{1, 2, 3}).new FoldIterator(1);
        assertNotNull(iteratorWithIndex);

        // Typical use case for previous and next
        iterator.next();
        assertNotNull(iterator.previous());
    }

}