package org.apache.commons.collections.primitives;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertNotNull(arrayByteList);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(10);
        assertNotNull(arrayByteList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ByteCollectionImpl();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.get(0);
    }

    @Test
    public void testSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        assertEquals(3, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(1, arrayByteList.removeElementAt(0));
        assertEquals(1, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(1, arrayByteList.set(0, (byte) 3));
        assertEquals(3, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.ensureCapacity(10);
        assertTrue(arrayByteList._data.length >= 10);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.trimToSize();
        assertEquals(1, arrayByteList._data.length);
    }

    // TODO: Write tests for private methods

}