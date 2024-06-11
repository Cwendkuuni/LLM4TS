package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
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
    public void testConstructor() {
        assertEquals(8, arrayByteList.size());

        ArrayByteList listWithInitialCapacity = new ArrayByteList(10);
        assertEquals(10, listWithInitialCapacity.size());

        assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-1));
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());

        addElementsToByteList(arrayByteList, 5);
        assertEquals(5, arrayByteList.size());

        removeElementsFromByteList(arrayByteList, 3);
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));

        arrayByteList.add(1, (byte) 2);
        assertEquals(2, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));

        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.add(3, (byte) 3));
    }

    @Test
    public void testRemoveElementAt() {
        addElementsToByteList(arrayByteList, 5);
        assertEquals((byte) 1, arrayByteList.removeElementAt(0));
        assertEquals(4, arrayByteList.size());
        assertEquals((byte) 2, arrayByteList.get(0));

        assertEquals((byte) 3, arrayByteList.removeElementAt(1));
        assertEquals(3, arrayByteList.size());
        assertEquals((byte) 2, arrayByteList.get(0));
        assertEquals((byte) 4, arrayByteList.get(1));
    }

    @Test
    public void testSet() {
        addElementsToByteList(arrayByteList, 3);
        assertEquals((byte) 1, arrayByteList.set(0, (byte) 10));
        assertEquals((byte) 10, arrayByteList.get(0));

        assertEquals((byte) 2, arrayByteList.set(1, (byte) 20));
        assertEquals((byte) 20, arrayByteList.get(1));
    }

    @Test
    public void testGet() {
        addElementsToByteList(arrayByteList, 3);
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));

        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.get(3));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList listWithInitialCapacity = new ArrayByteList(2);
        listWithInitialCapacity.ensureCapacity(5);
        assertEquals(5, getArrayCapacity(listWithInitialCapacity));

        ArrayByteList listWithoutInitialCapacity = new ArrayByteList();
        listWithoutInitialCapacity.ensureCapacity(8);
        assertEquals(8, getArrayCapacity(listWithoutInitialCapacity));

        ArrayByteList listWithoutInitialCapacity2 = new ArrayByteList();
        listWithoutInitialCapacity2.ensureCapacity(3);
        assertEquals(3, getArrayCapacity(listWithoutInitialCapacity2));
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList listWithInitialCapacity = new ArrayByteList(5);
        listWithInitialCapacity.trimToSize();
        assertEquals(0, getArrayCapacity(listWithInitialCapacity));

        ArrayByteList listWithoutInitialCapacity = new ArrayByteList();
        listWithoutInitialCapacity.trimToSize();
        assertEquals(0, getArrayCapacity(listWithoutInitialCapacity));

        addElementsToByteList(arrayByteList, 3);
        arrayByteList.trimToSize();
        assertEquals(3, getArrayCapacity(arrayByteList));
    }

    private void addElementsToByteList(ArrayByteList list, int numElements) {
        for (int i = 1; i <= numElements; i++) {
            list.add((byte) i);
        }
    }

    private void removeElementsFromByteList(ArrayByteList list, int numElements) {
        for (int i = 0; i < numElements; i++) {
            list.removeElementAt(0);
        }
    }

    private int getArrayCapacity(ArrayByteList list) {
        try {
            Object[] array = ((org.apache.commons.collections.primitives.ArrayByteList) list).getClass()
                    .getDeclaredField("_data").get(list);
            return array.length;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
}