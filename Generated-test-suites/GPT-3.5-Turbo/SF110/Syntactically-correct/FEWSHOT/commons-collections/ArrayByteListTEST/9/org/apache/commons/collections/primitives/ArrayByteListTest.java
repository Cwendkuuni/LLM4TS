package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    private ArrayByteList arrayByteList;
    
    @Before
    public void setup() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        ArrayByteList abc = new ArrayByteList();
        assertEquals(8, abc.size());
        
        ArrayByteList xyz = new ArrayByteList(10);
        assertEquals(10, xyz.size());

        assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-1));
    }

    @Test
    public void testGet() {
        arrayByteList.add(6, (byte) 5);
        arrayByteList.add(3, (byte) 8);
        arrayByteList.add(0, (byte) 1);

        assertEquals(1, arrayByteList.get(0));
        assertEquals(8, arrayByteList.get(3));
        assertEquals(5, arrayByteList.get(6));

        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.get(arrayByteList.size()));
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
        
        arrayByteList.add(6, (byte) 5);
        arrayByteList.add(3, (byte) 8);
        arrayByteList.add(0, (byte) 1);

        assertEquals(3, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(6, (byte) 5);
        arrayByteList.add(3, (byte) 8);
        arrayByteList.add(0, (byte) 1);

        assertEquals(1, arrayByteList.removeElementAt(0));
        assertEquals(8, arrayByteList.removeElementAt(2));
        assertEquals(5, arrayByteList.removeElementAt(1));

        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.removeElementAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.removeElementAt(arrayByteList.size()));
    }

    @Test
    public void testSet() {
        arrayByteList.add(6, (byte) 5);
        arrayByteList.add(3, (byte) 8);
        arrayByteList.add(0, (byte) 1);

        assertEquals(1, arrayByteList.set(0, (byte) 4));
        assertEquals(8, arrayByteList.set(2, (byte) 7));
        assertEquals(5, arrayByteList.set(1, (byte) 3));

        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.set(-1, (byte) 5));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.set(arrayByteList.size(), (byte) 5));
    }

    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 7);
        arrayByteList.add(1, (byte) 3);
        arrayByteList.add(2, (byte) 5);

        assertEquals(7, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
        assertEquals(5, arrayByteList.get(2));

        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.add(-1, (byte) 5));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.add(arrayByteList.size() + 1, (byte) 5));
    }

    @Test
    public void testEnsureCapacity() {
        assertEquals(8, arrayByteList.size());

        arrayByteList.ensureCapacity(20);
        assertEquals(20, arrayByteList._data.length);

        arrayByteList.ensureCapacity(5);
        assertEquals(20, arrayByteList._data.length); // Capacity should not change if it's already greater than or equal to the new capacity
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.trimToSize();
        assertEquals(0, arrayByteList._data.length);

        arrayByteList.ensureCapacity(20);
        arrayByteList.trimToSize();
        assertEquals(0, arrayByteList._data.length); // Size should be updated to match the number of elements

        arrayByteList.add(0, (byte) 7);
        arrayByteList.add(1, (byte) 3);
        arrayByteList.add(2, (byte) 5);
        arrayByteList.trimToSize();
        assertEquals(3, arrayByteList._data.length);
    }

    @Test
    public void testChecks() {
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.checkRange(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.checkRange(arrayByteList.size()));

        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.checkRangeIncludingEndpoint(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.checkRangeIncludingEndpoint(arrayByteList.size() + 1));
    }
}