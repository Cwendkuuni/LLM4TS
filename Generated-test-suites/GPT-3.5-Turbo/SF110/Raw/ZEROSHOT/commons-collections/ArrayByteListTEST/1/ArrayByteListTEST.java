package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
    }

    @Test
    public void testGet() {
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        arrayByteList.get(-1);
    }

    @Test
    public void testSize() {
        assertEquals(3, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        byte removed = arrayByteList.removeElementAt(1);
        assertEquals(2, arrayByteList.size());
        assertEquals((byte) 2, removed);
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 3, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        arrayByteList.removeElementAt(-1);
    }

    @Test
    public void testSet() {
        byte previous = arrayByteList.set(0, (byte) 10);
        assertEquals(3, arrayByteList.size());
        assertEquals((byte) 10, arrayByteList.get(0));
        assertEquals((byte) 1, previous);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        arrayByteList.set(-1, (byte) 10);
    }

    @Test
    public void testAdd() {
        arrayByteList.add(1, (byte) 10);
        assertEquals(4, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 10, arrayByteList.get(1));
        assertEquals((byte) 2, arrayByteList.get(2));
        assertEquals((byte) 3, arrayByteList.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        arrayByteList.add(-1, (byte) 10);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(3, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.trimToSize();
        assertEquals(3, arrayByteList.size());
    }

    // Additional test cases can be added to cover more scenarios

}