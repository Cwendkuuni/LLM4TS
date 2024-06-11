package org.apache.commons.collections.primitives;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArrayByteListTest {
    private static final int INITIAL_CAPACITY = 8;
    private static final int MAX_CAPACITY = 100;

    @Test
    public void testArrayByteListDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();

        assertEquals(INITIAL_CAPACITY, list._data.length);
        assertEquals(0, list._size);
    }

    @Test
    public void testArrayByteListConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        ArrayByteList list = new ArrayByteList(initialCapacity);

        assertEquals(initialCapacity, list._data.length);
        assertEquals(0, list._size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListConstructorWithInvalidInitialCapacity() {
        int invalidInitialCapacity = -1;
        new ArrayByteList(invalidInitialCapacity);
    }

    @Test
    public void testArrayByteListConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);

        ArrayByteList list = new ArrayByteList(byteCollection);

        assertEquals(3, list._size);
        assertEquals((byte) 1, list._data[0]);
        assertEquals((byte) 2, list._data[1]);
        assertEquals((byte) 3, list._data[2]);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2, 3};
        list._size = 3;

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(10);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list._size = 5;

        assertEquals(5, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2, 3};
        list._size = 3;

        byte removedElement = list.removeElementAt(1);

        assertEquals((byte) 2, removedElement);
        assertEquals(2, list._size);
        assertEquals((byte) 1, list._data[0]);
        assertEquals((byte) 3, list._data[1]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(10);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2, 3};
        list._size = 3;

        byte oldElement = list.set(1, (byte) 5);

        assertEquals((byte) 2, oldElement);
        assertEquals((byte) 5, list._data[1]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(10, (byte) 5);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[INITIAL_CAPACITY];
        list._size = 3;
        byte newElement = 10;

        list.add(1, newElement);

        assertEquals(4, list._size);
        assertEquals((byte) 10, list._data[1]);
        assertEquals((byte) 2, list._data[2]);
        assertEquals((byte) 3, list._data[3]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(10, (byte) 5);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[INITIAL_CAPACITY];
        list._size = 5;
        int newCapacity = 10;

        list.ensureCapacity(newCapacity);

        assertTrue(Arrays.equals(new byte[newCapacity], list._data));
    }

    @Test
    public void testEnsureCapacityWithLessThanCurrentCapacity() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[INITIAL_CAPACITY];
        list._size = 5;
        int newCapacity = 3;

        list.ensureCapacity(newCapacity);

        assertEquals(INITIAL_CAPACITY, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[INITIAL_CAPACITY];
        list._size = 5;

        list.trimToSize();

        assertEquals(5, list._data.length);
    }

    @Test
    public void testTrimToSizeWhenSizeIsLessThanArrayLength() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[INITIAL_CAPACITY];
        list._size = 3;

        list.trimToSize();

        assertEquals(3, list._data.length);
    }

    @Test
    public void testWriteObject() throws IOException {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2, 3};
        list._size = 3;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();

        assertTrue(outputStream.toByteArray().length > 0);
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list._size = 3;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();

        ArrayByteList deserializedList = (ArrayByteList) Util.deserializeObject(outputStream.toByteArray());

        assertEquals(list._size, deserializedList._size);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list._size = 5;

        list.checkRange(list._size + 1);
    }

    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list._size = 5;

        list.checkRange(list._size - 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list._size = 5;

        list.checkRangeIncludingEndpoint(list._size + 1);
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        list._size = 5;

        list.checkRangeIncludingEndpoint(list._size);
    }

    @Test
    public void testPerformance() {
        ArrayByteList list = new ArrayByteList(MAX_CAPACITY);
        Random random = new Random();

        for (int i = 0; i < MAX_CAPACITY; i++) {
            list.add((byte) random.nextInt());
        }

        assertEquals(MAX_CAPACITY, list._size);
        assertFalse(Arrays.equals(new byte[MAX_CAPACITY], list._data));
    }
}