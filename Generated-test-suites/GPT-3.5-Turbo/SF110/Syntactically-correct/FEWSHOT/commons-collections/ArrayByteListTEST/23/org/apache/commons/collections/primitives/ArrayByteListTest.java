package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals(1, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfRange() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(1, list.removeElementAt(0));
        assertEquals(1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfRange() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals(1, list.set(0, (byte) 2));
        assertEquals(2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfRange() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        assertEquals(1, list.get(0));
        list.add(1, (byte) 2);
        assertEquals(2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfRange() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        // No testable behavior, just checking if it does not throw an exception
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        list.trimToSize();
        // No testable behavior, just checking if it does not throw an exception
    }
}