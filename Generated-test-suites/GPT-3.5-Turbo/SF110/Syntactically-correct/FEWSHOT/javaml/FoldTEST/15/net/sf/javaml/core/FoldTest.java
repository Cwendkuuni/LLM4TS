package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.ManhattanDistance;
import org.junit.Test;

import java.util.*;

public class FoldTest {
    private static final double DELTA = 1e-15;

    @Test
    public void testAdd() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        assertFalse(fold.add(new Instance()));
    }

    @Test
    public void testClasses() {
        DefaultDataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        assertEquals(parent.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.folds(3, new Random());
    }

    @Test
    public void testInstance() {
        DefaultDataset parent = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        parent.add(instance1);
        parent.add(instance2);
        Fold fold = new Fold(parent, new int[]{1});
        assertEquals(instance2, fold.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.kNearest(5, new Instance(), new ManhattanDistance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.add(0, new Instance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.addAll(Collections.singletonList(new Instance()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.addAll(0, Collections.singletonList(new Instance()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.clear();
    }

    @Test
    public void testContains() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        Instance instance = new Instance();
        assertFalse(fold.contains(instance));
        DefaultDataset parent = new DefaultDataset();
        parent.add(instance);
        fold = new Fold(parent, new int[]{0});
        assertTrue(fold.contains(instance));
    }

    @Test
    public void testContainsAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        assertFalse(fold.containsAll(Collections.singletonList(new Instance())));
        DefaultDataset parent = new DefaultDataset();
        parent.add(new Instance());
        fold = new Fold(parent, new int[]{0});
        assertTrue(fold.containsAll(Collections.singletonList(new Instance())));
    }

    @Test
    public void testGet() {
        DefaultDataset parent = new DefaultDataset();
        Instance instance = new Instance();
        parent.add(instance);
        parent.add(new Instance());
        Fold fold = new Fold(parent, new int[]{0});
        assertEquals(instance, fold.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.indexOf(new Instance());
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        assertFalse(fold.isEmpty());
        DefaultDataset parent = new DefaultDataset();
        Fold emptyFold = new Fold(parent, new int[]{});
        assertTrue(emptyFold.isEmpty());
    }

    @Test
    public void testIterator() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        int count = 0;
        for (Instance instance : fold) {
            count++;
        }
        assertEquals(fold.size(), count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.lastIndexOf(new Instance());
    }

    @Test
    public void testListIterator() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        int count = 0;
        for (ListIterator<Instance> it = fold.listIterator(); it.hasNext(); ) {
            it.next();
            count++;
        }
        assertEquals(fold.size(), count);
    }

    @Test
    public void testListIteratorWithIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        int count = 0;
        for (ListIterator<Instance> it = fold.listIterator(1); it.hasNext(); ) {
            it.next();
            count++;
        }
        assertEquals(fold.size() - 1, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.remove(new Instance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.removeAll(Collections.singletonList(new Instance()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.retainAll(Collections.singletonList(new Instance()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.set(0, new Instance());
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        assertEquals(3, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.subList(0, 1);
    }

    @Test
    public void testToArray() {
        DefaultDataset parent = new DefaultDataset();
        Instance instance = new Instance();
        parent.add(instance);
        Fold fold = new Fold(parent, new int[]{0});
        Object[] expected = {instance};
        assertArrayEquals(expected, fold.toArray());
    }

    @Test
    public void testToArrayWithGenericArray() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        Object[] expected = new Object[fold.size()];
        for (int i = 0; i < fold.size(); i++) {
            expected[i] = fold.instance(i);
        }
        assertArrayEquals(expected, fold.toArray(new Object[fold.size()]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNoAttributes() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.noAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClassIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.classIndex(new Instance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClassValue() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        fold.classValue(0);
    }

    @Test
    public void testCopy() {
        DefaultDataset parent = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        parent.add(instance1);
        parent.add(instance2);
        Fold fold = new Fold(parent, new int[]{1});
        Dataset copy = fold.copy();
        assertEquals(fold.size(), copy.size());
        assertEquals(fold.instance(0), copy.instance(0));
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}