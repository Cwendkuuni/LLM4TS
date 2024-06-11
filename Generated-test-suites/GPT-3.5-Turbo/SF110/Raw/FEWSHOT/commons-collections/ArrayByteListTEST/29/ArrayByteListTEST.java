
package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructor1() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testConstructor2() {
        arrayByteList = new ArrayByteList(10);
        assertEquals(0, arrayByteList.size());
        assertTrue(arrayByteList.toArray().length >= 10);
    }

    @Test
    public void testConstructor3() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);
        arrayByteList = new ArrayByteList(byteCollection);
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        arrayByteList.get(0);
    }

    @Test
    public void testSize() {
        arrayByteList.add((byte) 1);
        assertEquals(1, arrayByteList.size());
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        byte removedElement = arrayByteList.removeElementAt(1);
        assertEquals(2, removedElement);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        byte oldElement = arrayByteList.set(0, (byte) 2);
        assertEquals(1, oldElement);
        assertEquals(1, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        arrayByteList.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 2);
        assertEquals(1, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
        arrayByteList.add(arrayByteList.size(), (byte) 3);
        assertEquals(2, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        arrayByteList.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(5);
        assertTrue(arrayByteList.toArray().length >= 5);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.toArray().length);
    }
}
