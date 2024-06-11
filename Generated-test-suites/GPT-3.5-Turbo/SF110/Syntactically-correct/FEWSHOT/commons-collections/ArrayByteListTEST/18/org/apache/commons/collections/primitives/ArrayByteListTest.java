package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        arrayByteList = new ArrayByteList(10);
        assertEquals(0, arrayByteList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        arrayByteList = new ArrayByteList(-10);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        byteList.add((byte) 2);
        arrayByteList = new ArrayByteList(byteList);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        arrayByteList.get(0);
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        assertEquals(1, arrayByteList.get(0));
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
        arrayByteList.add((byte) 1);
        assertEquals(1, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        assertEquals(1, arrayByteList.removeElementAt(0));
        assertEquals(0, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        arrayByteList.set(0, (byte) 1);
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        assertEquals(1, arrayByteList.set(0, (byte) 2));
        assertEquals(2, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        arrayByteList.add(1, (byte) 1);
    }

    @Test
    public void testAdd() {
        arrayByteList.add((byte) 1);
        arrayByteList.add(0, (byte) 2);
        assertEquals(2, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
        assertEquals(1, arrayByteList.get(1));
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList._data.length);
    }

    // Additional tests could be added to verify serialization and deserialization
}