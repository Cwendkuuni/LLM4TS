package org.apache.commons.collections.primitives;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testArrayByteList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListWithByteCollection() {
        ByteCollection collection = new ByteArrayList();
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        assertEquals(10, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_IndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        byte removed = list.removeElementAt(0);
        assertEquals(10, removed);
        assertEquals(0, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_IndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        byte oldValue = list.set(0, 20);
        assertEquals(10, oldValue);
        assertEquals(20, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_IndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, 20);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        assertEquals(10, list.get(0));
        assertEquals(1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_IndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, 10);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        // No assertion as it's an internal method
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.trimToSize();
        // No assertion as it's an internal method
    }

}