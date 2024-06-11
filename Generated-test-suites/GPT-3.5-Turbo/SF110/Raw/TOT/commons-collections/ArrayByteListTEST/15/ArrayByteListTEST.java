package org.apache.commons.collections.primitives;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor1() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructor2() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
        assertEquals(10, list.capacity());
    }

    @Test
    public void testConstructor3() {
        ByteList collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);
        
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(3, list.size());
        assertEquals(3, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(1, list.get(2));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_OutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        
        assertEquals(0, list.size());
        
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        byte removed = list.removeElementAt(1);
        
        assertEquals(2, list.size());
        assertEquals(2, removed);
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_OutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        byte oldVal = list.set(1, (byte) 4);
        
        assertEquals(3, list.size());
        assertEquals(2, oldVal);
        assertEquals(1, list.get(0));
        assertEquals(4, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_OutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.add(1, (byte) 3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(2, list.get(2));
        
        list.add(0, (byte) 4);
        assertEquals(4, list.size());
        assertEquals(4, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(2, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_OutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(10, list.capacity());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        list.trimToSize();
        assertEquals(3, list.capacity());
    }

    @Test
    public void testSerialization() throws Exception {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();
        
        assertEquals(3, deserializedList.size());
        assertEquals(1, deserializedList.get(0));
        assertEquals(2, deserializedList.get(1));
        assertEquals(3, deserializedList.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentConstructor2() {
        new ArrayByteList(-1);
    }
}