package org.apache.commons.collections.primitives;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        ArrayByteList arrayByteList = new ArrayByteList(initialCapacity);
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        int initialCapacity = -1;
        ArrayByteList arrayByteList = new ArrayByteList(initialCapacity);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList(10);
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);

        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);
        assertNotNull(arrayByteList);
        assertEquals(2, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
    }

    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);

        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithOutOfRangeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.get(10);
    }

    @Test
    public void testSize() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        assertEquals(0, arrayByteList.size());

        arrayByteList.add((byte) 1);
        assertEquals(1, arrayByteList.size());

        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());

        arrayByteList.removeElementAt(0);
        assertEquals(1, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);

        byte removedElement = arrayByteList.removeElementAt(1);
        assertEquals((byte) 2, removedElement);
        assertEquals(2, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 3, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithOutOfRangeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.removeElementAt(10);
    }

    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);

        byte oldValue = arrayByteList.set(1, (byte) 5);
        assertEquals((byte) 2, oldValue);
        assertEquals(3, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 5, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithOutOfRangeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.set(10, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 3);

        arrayByteList.add(1, (byte) 2);
        assertEquals(3, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithOutOfRangeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add(10, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        arrayByteList.ensureCapacity(10);
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testEnsureCapacityWhenMinCapIsLessThanCurrentCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(10);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        arrayByteList.ensureCapacity(5);
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testTrimToSizeWhenSizeIsEqualToCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testWriteObject() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        // TODO: Write test case for writeObject
    }

    @Test
    public void testReadObject() {
        // TODO: Write test case for readObject
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithOutOfRangeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.checkRange(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithOutOfRangeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        arrayByteList.checkRangeIncludingEndpoint(10);
    }

}