
package org.apache.commons.collections.primitives;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList byteList;

    @Before
    public void setUp() {
        byteList = new ArrayByteList();
    }

    // Tests for the constructor
    @Test
    public void testConstructorWithNoArgument() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        ArrayByteList list = new ArrayByteList(-10);
    }

    // Test for the add method
    @Test
    public void testAdd() {
        byteList.add(0, (byte) 1);
        assertEquals(1, byteList.size());
        assertEquals(1, byteList.get(0));
    }

    // Test for the ensureCapacity method
    @Test
    public void testEnsureCapacity() {
        byteList.ensureCapacity(10);
        assertEquals(0, byteList.size());
        assertEquals(10, byteList._data.length);
    }

    // Test for the get method
    @Test
    public void testGet() {
        byteList.add(0, (byte) 1);
        byteList.add(1, (byte) 2);
        assertEquals(1, byteList.get(0));
        assertEquals(2, byteList.get(1));
    }

    // Test for the removeElementAt method
    @Test
    public void testRemoveElementAt() {
        byteList.add(0, (byte) 1);
        byteList.add(1, (byte) 2);
        assertEquals(2, byteList.size());
        byte removedByte = byteList.removeElementAt(0);
        assertEquals(2, byteList.size());
        assertEquals(1, removedByte);
        assertEquals(2, byteList.get(0));
    }

    // Test for the set method
    @Test
    public void testSet() {
        byteList.add(0, (byte) 1);
        byteList.add(1, (byte) 2);
        byteList.set(0, (byte) 3);
        assertEquals(2, byteList.size());
        assertEquals(3, byteList.get(0));
    }

    // Test for the size method
    @Test
    public void testSize() {
        assertEquals(0, byteList.size());
        byteList.add(0, (byte) 1);
        assertEquals(1, byteList.size());
    }

    // Test for the trimToSize method
    @Test
    public void testTrimToSize() {
        byteList.add(0, (byte) 1);
        byteList.trimToSize();
        assertEquals(1, byteList.size());
        assertEquals(1, byteList._data.length);
    }
}
