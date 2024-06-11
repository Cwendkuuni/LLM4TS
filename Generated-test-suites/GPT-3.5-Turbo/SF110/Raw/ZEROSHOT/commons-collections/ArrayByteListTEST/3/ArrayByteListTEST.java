package org.apache.commons.collections.primitives;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        byte element1 = list.get(0);
        byte element2 = list.get(1);

        assertEquals((byte) 1, element1);
        assertEquals((byte) 2, element2);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        int size = list.size();

        assertEquals(3, size);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte removedElement = list.removeElementAt(1);

        assertEquals((byte) 2, removedElement);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        byte oldval = list.set(1, (byte) 3);

        assertEquals((byte) 2, oldval);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 3);

        list.add(1, (byte) 2);

        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(2);
        list.add((byte) 1);
        list.add((byte) 2);

        list.ensureCapacity(4);

        assertTrue(list.size() >= 4);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(4);
        list.add((byte) 1);
        list.add((byte) 2);

        list.trimToSize();

        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testSerialization() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        // Serialize the list
        try {
            FileOutputStream fileOut = new FileOutputStream("list.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(list);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize the list
        ArrayByteList deserializedList = null;
        try {
            FileInputStream fileIn = new FileInputStream("list.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            deserializedList = (ArrayByteList) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            System.out.println("ArrayByteList class not found");
            e.printStackTrace();
            return;
        }

        assertNotNull(deserializedList);
        assertEquals(2, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);

        list.get(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);

        list.add(2, (byte) 2);
    }

}