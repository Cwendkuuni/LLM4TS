package org.apache.commons.collections.primitives;

import junit.framework.TestCase.*;

public class ArrayByteListTest {
    private ArrayByteList byteList;
    
    public void setUp() {
        byteList = new ArrayByteList();
    }
    
    public void testConstructor() {
        assertEquals(8, byteList._data.length);
        assertEquals(0, byteList._size);
    }
    
    public void testConstructorWithCapacity() {
        ArrayByteList byteList = new ArrayByteList(10);
        assertEquals(10, byteList._data.length);
        assertEquals(0, byteList._size);
    }
    
    public void testConstructorWithCollection() {
        ByteCollection col = new ByteCollection();
        col.add((byte)1);
        col.add((byte)2);
        
        ArrayByteList byteList = new ArrayByteList(col);
        assertEquals(2, byteList._size);
        assertEquals(1, byteList._data[0]);
        assertEquals(2, byteList._data[1]);
    }
    
    public void testGet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList._data[0] = 1;
        byteList._data[1] = 2;
        byteList._data[2] = 3;
        
        assertEquals(1, byteList.get(0));
        assertEquals(2, byteList.get(1));
        assertEquals(3, byteList.get(2));
    }
    
    public void testSize() {
        ArrayByteList byteList = new ArrayByteList();
        assertEquals(0, byteList.size());
        
        byteList._size = 3;
        assertEquals(3, byteList.size());
    }
    
    public void testRemoveElementAt() {
        ArrayByteList byteList = new ArrayByteList();
        byteList._data[0] = 1;
        byteList._data[1] = 2;
        byteList._data[2] = 3;
        byteList._size = 3;
        
        byte removedByte = byteList.removeElementAt(1);
        assertEquals(2, removedByte);
        assertEquals(2, byteList._size);
        assertEquals(1, byteList._data[0]);
        assertEquals(3, byteList._data[1]);
    }
    
    public void testSet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList._data[0] = 1;
        byteList._data[1] = 2;
        
        byte oldByte = byteList.set(1, (byte)3);
        assertEquals(2, oldByte);
        assertEquals(1, byteList._data[0]);
        assertEquals(3, byteList._data[1]);
    }
    
    public void testAdd() {
        ArrayByteList byteList = new ArrayByteList();
        byteList._data[0] = 1;
        byteList._size = 1;
        
        byteList.add(1, (byte)2);
        assertEquals(2, byteList._size);
        assertEquals(1, byteList._data[0]);
        assertEquals(2, byteList._data[1]);
    }
    
    public void testEnsureCapacity() {
        ArrayByteList byteList = new ArrayByteList();
        byteList._data[0] = 1;
        byteList._data[1] = 2;
        byteList._data[2] = 3;
        
        byteList.ensureCapacity(5);
        assertEquals(5, byteList._data.length);
        assertEquals(1, byteList._data[0]);
        assertEquals(2, byteList._data[1]);
        assertEquals(3, byteList._data[2]);
    }
    
    public void testTrimToSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList._data[0] = 1;
        byteList._data[1] = 2;
        byteList._data[2] = 3;
        byteList._size = 3;
        
        byteList.trimToSize();
        assertEquals(3, byteList._data.length);
        assertEquals(3, byteList._size);
        assertEquals(1, byteList._data[0]);
        assertEquals(2, byteList._data[1]);
        assertEquals(3, byteList._data[2]);
    }
    
    //Additional test cases can be added to cover all methods in ArrayByteList
    
}