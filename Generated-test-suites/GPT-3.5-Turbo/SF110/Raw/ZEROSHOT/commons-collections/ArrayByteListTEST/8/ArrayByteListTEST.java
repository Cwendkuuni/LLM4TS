package org.apache.commons.collections.primitives;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testConstructorWithInitialCapacity() {
        list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        list = new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);
        list = new ArrayByteList(collection);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testGet() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfRange() {
        list.get(0);
    }

    @Test
    public void testSize() {
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
        list.add((byte) 2);
        assertEquals(2, list.size());
        list.add((byte) 3);
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(2, list.removeElementAt(1));
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfRange() {
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(2, list.set(1, (byte) 4));
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(4, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        list.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexOutOfRange() {
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        list.add(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfRange() {
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.ensureCapacity(5);
        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testEnsureCapacityWithMincapLessThanDataLength() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.ensureCapacity(2);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.trimToSize();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testSerialization() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(list);
            out.close();

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            ArrayByteList newList = (ArrayByteList) in.readObject();
            in.close();

            assertEquals(list.size(), newList.size());
            for (int i = 0; i < list.size(); i++) {
                assertEquals(list.get(i), newList.get(i));
            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithIndexOutOfRange() {
        list.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithIndexOutOfRange() {
        list.checkRangeIncludingEndpoint(1);
    }
}