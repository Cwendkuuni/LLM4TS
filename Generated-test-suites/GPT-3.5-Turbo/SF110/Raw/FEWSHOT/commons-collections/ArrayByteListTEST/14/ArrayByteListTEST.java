
package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrayByteListTest {
    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        list = new ArrayByteList(10);
        assertEquals(0, list.size());

        try {
            list = new ArrayByteList(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("capacity -1", e.getMessage());
        }
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();

        try {
            list.get(0);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 0, found 0", e.getMessage());
        }

        list.add(10);

        try {
            list.get(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 1, found 1", e.getMessage());
        }

        assertEquals(10, list.get(0));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();

        assertEquals(0, list.size());

        list.add(10);

        assertEquals(1, list.size());

        list.removeElementAt(0);

        assertEquals(0, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();

        try {
            list.removeElementAt(0);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 0, found 0", e.getMessage());
        }

        list.add(10);
        list.add(20);
        list.add(30);

        byte removed = list.removeElementAt(1);
        assertEquals(20, removed);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();

        try {
            list.set(0, (byte) 10);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 0, found 0", e.getMessage());
        }

        list.add(20);

        byte oldVal = list.set(0, (byte) 10);

        assertEquals(20, oldVal);
        assertEquals(10, list.get(0));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();

        try {
            list.add(1, (byte) 10);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and at most 0, found 1", e.getMessage());
        }

        list.add(0, (byte) 10);

        assertEquals(1, list.size());
        assertEquals(10, list.get(0));

        list.add(0, (byte) 20);

        assertEquals(2, list.size());
        assertEquals(20, list.get(0));
        assertEquals(10, list.get(1));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();

        list.ensureCapacity(10);

        assertEquals(0, list.size());

        list.add((byte) 10);
        list.ensureCapacity(20);

        assertEquals(1, list.size());

        list.ensureCapacity(5);

        assertEquals(1, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();

        list.trimToSize();

        assertEquals(0, list.size());

        list.add((byte) 10);
        list.trimToSize();

        assertEquals(1, list.size());

        list.removeElementAt(0);
        list.trimToSize();

        assertEquals(0, list.size());
    }
}
