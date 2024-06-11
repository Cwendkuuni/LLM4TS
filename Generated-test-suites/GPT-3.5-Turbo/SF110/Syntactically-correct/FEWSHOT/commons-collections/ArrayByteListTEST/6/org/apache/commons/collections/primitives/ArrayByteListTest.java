package org.apache.commons.collections.primitives;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {
    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
    }

    @Test
    public void testConstructor_Default() {
        ArrayByteList newList = new ArrayByteList();
        assertNotNull(newList);
        assertEquals(0, newList.size());
    }

    @Test
    public void testConstructor_WithInitialCapacity() {
        ArrayByteList newList = new ArrayByteList(10);
        assertNotNull(newList);
        assertEquals(0, newList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testConstructor_WithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add(4);
        collection.add(5);
        ArrayByteList newList = new ArrayByteList(collection);
        assertNotNull(newList);
        assertEquals(2, newList.size());
        assertEquals(4, newList.get(0));
        assertEquals(5, newList.get(1));
    }

    @Test
    public void testGet() {
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_InvalidIndex() {
        list.get(3);
    }

    @Test
    public void testSize() {
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        assertEquals(1, list.removeElementAt(0));
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_InvalidIndex() {
        list.removeElementAt(3);
    }

    @Test
    public void testSet() {
        assertEquals(1, list.set(0, (byte) 10));
        assertEquals(10, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_InvalidIndex() {
        list.set(3, (byte) 10);
    }

    @Test
    public void testAdd() {
        list.add(0, (byte) 10);
        assertEquals(4, list.size());
        assertEquals(10, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_InvalidIndex() {
        list.add(4, (byte) 10);
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertEquals(3, list.size());
        list.ensureCapacity(2);
        assertEquals(3, list.size());
    }

    @Test
    public void testTrimToSize() {
        list.trimToSize();
        assertEquals(3, list.size());
        assertTrue(list._data.length >= list.size());
    }
    
    // Additional Test Cases
    
    @Test
    public void testAddToEnd() {
        list.add(4);
        assertEquals(4, list.size());
        assertEquals(4, list.get(3));
    }

    @Test
    public void testAddToMiddle() {
        list.add(1, (byte) 10);
        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
    }
    
    @Test
    public void testSetToEnd() {
        list.set(2, (byte) 10);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(10, list.get(2));
    }
    
    @Test
    public void testSetToMiddle() {
        list.set(1, (byte) 10);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(3, list.get(2));
    }
}