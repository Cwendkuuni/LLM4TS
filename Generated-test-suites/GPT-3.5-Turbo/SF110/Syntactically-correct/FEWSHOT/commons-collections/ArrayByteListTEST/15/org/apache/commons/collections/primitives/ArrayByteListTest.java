
package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Before
    public void setup() {
        list = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList newList = new ArrayByteList(5);
        assertEquals(0, newList.size());
    }

    @Test
    public void testConstructorWithNegativeInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-1));
    }

    @Test
    public void testAdd() {
        byte element = 1;
        list.add(element);
        assertEquals(1, list.size());
        assertEquals(element, list.get(0));
    }

    @Test
    public void testAddAtIndex() {
        byte element = 1;
        list.add(0, element);
        assertEquals(1, list.size());
        assertEquals(element, list.get(0));
    }

    @Test
    public void testAddAtIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, (byte) 1));
    }

    @Test
    public void testGet() {
        byte element = 1;
        list.add(element);
        assertEquals(element, list.get(0));
    }

    @Test
    public void testGetOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        byte element = 1;
        list.add(element);
        byte removedElement = list.removeElementAt(0);
        assertEquals(element, removedElement);
        assertEquals(0, list.size());
    }

    @Test
    public void testRemoveElementAtOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(0));
    }

    @Test
    public void testSet() {
        byte element = 1;
        list.add(element);
        byte newElement = 2;
        byte oldElement = list.set(0, newElement);
        assertEquals(element, oldElement);
        assertEquals(newElement, list.get(0));
    }

    @Test
    public void testSetOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, (byte) 1));
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(5);
        // No assertion for this method as it does not change the size or contents of the list
    }

    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.trimToSize();
        // No assertion for this method as it does not change the size or contents of the list
    }
}
