package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    private ArrayByteList arrayByteList;
    
    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        arrayByteList.add((byte) 4);
    }
    
    @Test
    public void testGet() {
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));
        assertEquals((byte) 4, arrayByteList.get(3));
    }
    
    @Test
    public void testSize() {
        assertEquals(4, arrayByteList.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_IndexOutOfBounds() {
        arrayByteList.get(4);
    }
    
    @Test
    public void testRemoveElementAt() {
        byte removed = arrayByteList.removeElementAt(2);
        assertEquals((byte) 3, removed);
        assertEquals(3, arrayByteList.size());
        assertArrayEquals(new byte[]{1, 2, 4}, arrayByteList.toArray());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_IndexOutOfBounds() {
        arrayByteList.removeElementAt(4);
    }
    
    @Test
    public void testSet() {
        byte old = arrayByteList.set(1, (byte) 5);
        assertEquals((byte) 2, old);
        assertEquals((byte) 5, arrayByteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_IndexOutOfBounds() {
        arrayByteList.set(4, (byte) 5);
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add(2, (byte) 6);
        assertEquals(5, arrayByteList.size());
        assertArrayEquals(new byte[]{1, 2, 6, 3, 4}, arrayByteList.toArray());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_IndexOutOfBounds() {
        arrayByteList.add(5, (byte) 6);
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        byte[] expected = new byte[]{1, 2, 3, 4};
        assertArrayEquals(expected, arrayByteList.toArray());
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.trimToSize();
        byte[] expected = new byte[]{1, 2, 3, 4};
        assertArrayEquals(expected, arrayByteList.toArray());
    }
    
    @Test
    public void testWriteObject() {
        // TODO: Implement test case
    }
    
    @Test
    public void testReadObject() {
        // TODO: Implement test case
    }
    
    @Test
    public void testCheckRange() {
        // TODO: Implement test case
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        // TODO: Implement test case
    }
}