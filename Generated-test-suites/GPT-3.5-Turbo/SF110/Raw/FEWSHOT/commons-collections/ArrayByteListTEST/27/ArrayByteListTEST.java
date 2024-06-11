
package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, list.size());
    }

    @Test
    public void testAdd() {
        list.add((byte) 1);
        assertEquals(1, list.size());
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testAddAtIndex() {
        list.add((byte) 1);
        list.add(0, (byte) 2);
        assertEquals(2, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 1, list.get(1));
    }

    @Test
    public void testAddAtIndexWithInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, (byte) 1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, (byte) 1));
    }

    @Test
    public void testGet() {
        list.add((byte) 1);
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testGetWithInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    public void testSet() {
        list.add((byte) 1);
        list.set(0, (byte) 2);
        assertEquals((byte) 2, list.get(0));
    }

    @Test
    public void testSetWithInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, (byte) 1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, (byte) 1));
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        byte removed = list.removeElementAt(1);
        assertEquals(1, list.size());
        assertEquals((byte) 2, removed);
    }

    @Test
    public void testRemoveElementAtWithInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(0));
    }

    @Test
    public void testSize() {
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertTrue(list.size() >= 10);
    }

    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.trimToSize();
        assertEquals(1, list.size());
    }
}
