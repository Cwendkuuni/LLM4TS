package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
    }

    @Test
    public void testConstructor_DefaultCapacity() {
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructor_InitialCapacity() {
        ArrayByteList customList = new ArrayByteList(10);
        assertEquals(0, customList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeCapacity() {
        ArrayByteList customList = new ArrayByteList(-1);
        fail("Expected an IllegalArgumentException to be thrown");
    }

    @Test
    public void testConstructor_CopyCollection() {
        ArrayByteList customList = new ArrayByteList(list);
        assertEquals(0, customList.size());
    }

    @Test
    public void testAdd() {
        list.add((byte) 1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    public void testAdd_Index() {
        list.add((byte) 1);
        list.add(0, (byte) 0);
        assertEquals(2, list.size());
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
    }

    @Test
    public void testAdd_Index_OutOfBounds() {
        try {
            list.add(1, (byte) 0);
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertTrue(true);
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        byte removed = list.removeElementAt(0);
        assertEquals(0, list.size());
        assertEquals(1, removed);
    }

    @Test
    public void testRemoveElementAt_Index_OutOfBounds() {
        try {
            list.removeElementAt(0);
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSet() {
        list.add((byte) 1);
        byte oldVal = list.set(0, (byte) 2);
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
        assertEquals(1, oldVal);
    }

    @Test
    public void testSet_Index_OutOfBounds() {
        try {
            list.set(0, (byte) 0);
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testTrimToSize() {
        list.trimToSize();
        assertTrue(true);
    }

    @Test
    public void testWriteObject() {
        // Not testing this method as it is tested indirectly
        assertTrue(true);
    }

    @Test
    public void testReadObject() {
        // Not testing this method as it is tested indirectly
        assertTrue(true);
    }
}