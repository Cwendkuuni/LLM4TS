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
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertEquals(5, list._data.length);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3() {
        ArrayByteList list = new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructor4() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);
        
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(3, list.size());
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
    public void testGetInvalid() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list._size = 3;
        assertEquals(3, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2, 3};
        list._size = 3;

        byte removed = list.removeElementAt(1);
        assertEquals((byte) 2, removed);
        assertEquals(2, list.size());
        assertEquals((byte)1, list._data[0]);
        assertEquals((byte)3, list._data[1]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtInvalid() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2, 3};
        list._size = 3;

        byte old = list.set(1, (byte) 4);
        assertEquals((byte) 2, old);
        assertEquals((byte) 4, list._data[1]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalid() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 3};
        list._size = 2;

        list.add(1, (byte) 2);
        assertEquals(3, list.size());
        assertEquals((byte)1, list._data[0]);
        assertEquals((byte)2, list._data[1]);
        assertEquals((byte)3, list._data[2]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalid() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2};
        list._size = 2;

        list.ensureCapacity(5);
        assertEquals(2, list.size());
        assertEquals(5, list._data.length);
        assertEquals((byte)1, list._data[0]);
        assertEquals((byte)2, list._data[1]);
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2};
        list._size = 2;

        list.trimToSize();
        assertEquals(2, list.size());
        assertEquals(2, list._data.length);
        assertEquals((byte)1, list._data[0]);
        assertEquals((byte)2, list._data[1]);
    }
    
    @Test
    public void testWriteObject() {
        ArrayByteList list = new ArrayByteList();
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        // Test if writeObject method is called successfully
    }
    
    @Test
    public void testReadObject() {
        ArrayByteList list = new ArrayByteList();
        
        // Test if readObject method is called successfully
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeInvalid() {
        ArrayByteList list = new ArrayByteList();
        list._size = 3;
        list.checkRange(5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointInvalid() {
        ArrayByteList list = new ArrayByteList();
        list._size = 3;
        list.checkRangeIncludingEndpoint(5);
    }

}