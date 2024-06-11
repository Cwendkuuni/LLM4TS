package org.apache.commons.collections.primitives;

import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());

        ArrayByteList listWithCapacity = new ArrayByteList(10);
        assertNotNull(listWithCapacity);
        assertEquals(0, listWithCapacity.size());
    }

    @Test
    public void testConstructorWithCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);

        ArrayByteList list = new ArrayByteList(collection);
        assertNotNull(list);
        assertEquals(collection.size(), list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        list.add((byte) 1);
        assertEquals(1, list.size());

        list.add((byte) 2);
        assertEquals(2, list.size());

        list.removeElementAt(0);
        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte removed = list.removeElementAt(1);
        assertEquals(2, list.size());
        assertEquals((byte) 2, removed);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte oldValue = list.set(1, (byte) 5);
        assertEquals((byte) 2, oldValue);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 5, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 3);
        list.add((byte) 4);

        list.add(1, (byte) 2);
        assertEquals(4, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(0, list.size());

        list.add((byte) 1);
        list.ensureCapacity(10);
        assertEquals(1, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.trimToSize();
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    // Some test cases for the private methods are not provided as they are
    // indirectly tested by other public methods.

    @Test
    public void testWriteObject() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        // TODO: Add test case for writeObject method
    }

    @Test
    public void testReadObject() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        // TODO: Add test case for readObject method
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRange(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRangeIncludingEndpoint(1);
    }

}