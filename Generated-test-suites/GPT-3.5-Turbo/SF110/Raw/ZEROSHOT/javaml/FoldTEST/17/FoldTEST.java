package net.sf.javaml.core;

import net.sf.javaml.distance.DistanceMeasure;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    @Test
    public void testAdd() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        try {
            fold.add(new Instance());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }

    @Test
    public void testClasses() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        SortedSet<Object> result = fold.classes();
        
        assertEquals(parent.classes(), result);
    }

    @Test
    public void testFolds() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        try {
            fold.folds(5, new Random());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testInstance() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        Instance result = fold.instance(1);
        
        assertEquals(parent.instance(indices[1]), result);
    }

    @Test
    public void testKNearest() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        try {
            fold.kNearest(5, new Instance(), new DistanceMeasure());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testAddWithIndex() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        try {
            fold.add(1, new Instance());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }

    @Test
    public void testAddAll() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        try {
            fold.addAll(Arrays.asList(new Instance()));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }

    @Test
    public void testAddAllWithIndex() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        try {
            fold.addAll(1, Arrays.asList(new Instance()));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }

    @Test
    public void testClear() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.clear();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }

    @Test
    public void testContains() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.contains(new Object());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testContainsAll() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.containsAll(Arrays.asList(new Object()));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testGet() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        Instance result = fold.get(1);
        
        assertEquals(parent.instance(indices[1]), result);
    }

    @Test
    public void testIndexOf() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.indexOf(new Object());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testIsEmpty() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        boolean result = fold.isEmpty();
        
        assertFalse(result);
    }

    @Test
    public void testIterator() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        Iterator<Instance> result = fold.iterator();
        
        assertNotNull(result);
    }

    @Test
    public void testLastIndexOf() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.lastIndexOf(new Object());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testListIterator() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        ListIterator<Instance> result = fold.listIterator();
        
        assertNotNull(result);
    }

    @Test
    public void testListIteratorWithIndex() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        ListIterator<Instance> result = fold.listIterator(1);

        assertNotNull(result);
    }

    @Test
    public void testRemove() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.remove(new Object());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }

    @Test
    public void testRemoveWithIndex() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.remove(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }

    @Test
    public void testRemoveAll() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.removeAll(Arrays.asList(new Object()));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }

    @Test
    public void testRetainAll() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.retainAll(Arrays.asList(new Object()));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }

    @Test
    public void testSet() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.set(1, new Instance());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }

    @Test
    public void testSize() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        int result = fold.size();
        
        assertEquals(indices.length, result);
    }

    @Test
    public void testSubList() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        try {
            fold.subList(0, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }

    @Test
    public void testToArray() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        Object[] result = fold.toArray();
        
        for (int i = 0; i < result.length; i++) {
            assertEquals(parent.instance(indices[i]), result[i]);
        }
    }

    @Test
    public void testToArrayWithParameter() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        Object[] array = new Object[indices.length];
        Object[] result = fold.toArray(array);

        assertSame(array, result);

        for (int i = 0; i < result.length; i++) {
            assertEquals(parent.instance(indices[i]), result[i]);
        }
    }

    @Test
    public void testNoAttributes() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        int result = fold.noAttributes();
        
        assertEquals(parent.noAttributes(), result);
    }

    @Test
    public void testClassIndex() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        int result = fold.classIndex(new Object());
        
        assertEquals(parent.classIndex(new Object()), result);
    }

    @Test
    public void testClassValue() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        Object result = fold.classValue(1);
        
        assertEquals(parent.classValue(1), result);
    }

    @Test
    public void testCopy() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        
        Dataset result = fold.copy();
        
        assertEquals(DefaultDataset.class, result.getClass());
        
        for (int i = 0; i < fold.size(); i++) {
            assertEquals(fold.instance(i).copy(), result.instance(i));
        }
    }

    @Test
    public void testFoldIterator() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);

        Fold.FoldIterator iterator = fold.new FoldIterator(0);

        assertNotNull(iterator);
    }

}