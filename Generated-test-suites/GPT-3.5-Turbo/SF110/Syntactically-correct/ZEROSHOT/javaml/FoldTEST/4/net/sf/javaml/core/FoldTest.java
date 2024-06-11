package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.distance.*;
import java.util.*;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.add(new Instance());
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        SortedSet<Object> classes = fold.classes();
        assertEquals(parent.classes(), classes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int numFolds = 5;
        Random rg = new Random();
        fold.folds(numFolds, rg);
    }

    @Test
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance instance = fold.instance(1);
        assertEquals(parent.instance(1), instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int k = 3;
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        fold.kNearest(k, inst, dm);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddIndexElement() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int index = 1;
        Instance element = new Instance();
        fold.add(index, element);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllC() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Collection<Instance> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        fold.addAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndexC() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int index = 1;
        Collection<Instance> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        fold.addAll(index, c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Object o = new Instance();
        fold.contains(o);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Collection<?> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        fold.containsAll(c);
    }

    @Test
    public void testGet() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance instance = fold.get(1);
        assertEquals(parent.instance(1), instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Object o = new Instance();
        fold.indexOf(o);
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        assertTrue(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Iterator<Instance> iterator = fold.iterator();
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Object o = new Instance();
        fold.lastIndexOf(o);
    }

    @Test
    public void testListIterator() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        ListIterator<Instance> iterator = fold.listIterator();
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testListIteratorIndex() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        ListIterator<Instance> iterator = fold.listIterator(1);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveObject() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Object o = new Instance();
        fold.remove(o);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int index = 1;
        fold.remove(index);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Collection<?> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        fold.removeAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        Collection<?> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        fold.retainAll(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int index = 1;
        Instance element = new Instance();
        fold.set(index, element);
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int expectedSize = 3;
        int actualSize = fold.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{1, 2, 3});
        int fromIndex = 0;
        int toIndex = 2;
        fold.subList(fromIndex, toIndex);
    }

    @Test
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Object[] expectedArray = {parent.instance(0), parent.instance(1), parent.instance(2)};
        Object[] actualArray = fold.toArray();
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testToArrayT() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance[] expectedArray = {parent.instance(0), parent.instance(1), parent.instance(2)};
        Instance[] actualArray = fold.toArray(new Instance[0]);
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testNoAttributes() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        int expectedAttributes = parent.noAttributes();
        int actualAttributes = fold.noAttributes();
        assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testClassIndex() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Object clazz = parent.classValue(1);
        int expectedClassIndex = parent.classIndex(clazz);
        int actualClassIndex = fold.classIndex(clazz);
        assertEquals(expectedClassIndex, actualClassIndex);
    }

    @Test
    public void testClassValue() {
        Dataset parent = new DefaultDataset();
        Object expectedClassValue = new Object();
        parent.setClassValue(1, expectedClassValue);
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Object actualClassValue = fold.classValue(1);
        assertEquals(expectedClassValue, actualClassValue);
    }

    @Test
    public void testCopy() {
        Dataset parent = new DefaultDataset();
        Instance inst1 = new Instance();
        inst1.put(1, 2);
        parent.add(inst1);
        Instance inst2 = new Instance();
        inst2.put(3, 4);
        parent.add(inst2);
        int[] indices = {0, 1};
        Fold fold = new Fold(parent, indices);
        Dataset copy = fold.copy();
        assertEquals(parent.size(), copy.size());
        Instance copyInst1 = copy.instance(0);
        assertEquals(inst1.get(1), copyInst1.get(1));
        Instance copyInst2 = copy.instance(1);
        assertEquals(inst2.get(3), copyInst2.get(3));
    }

    @Test
    public void testFoldIterator() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());
        parent.add(new Instance());
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        ListIterator<Instance> iterator1 = fold.new FoldIterator(1);
        assertTrue(iterator1.hasNext());
        assertNotNull(iterator1.next());
        assertTrue(iterator1.hasNext());
        assertNotNull(iterator1.next());
        assertFalse(iterator1.hasNext());

        ListIterator<Instance> iterator2 = fold.new FoldIterator(fold);
        assertTrue(iterator2.hasNext());
        assertNotNull(iterator2.next());
        assertTrue(iterator2.hasNext());
        assertNotNull(iterator2.next());
        assertTrue(iterator2.hasNext());
        assertNotNull(iterator2.next());
        assertFalse(iterator2.hasNext());
    }
}