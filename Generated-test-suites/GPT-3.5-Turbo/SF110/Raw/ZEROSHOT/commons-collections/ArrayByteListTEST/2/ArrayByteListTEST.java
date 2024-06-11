package org.apache.commons.collections.primitives;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor_default() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testConstructor_withInitialCapacity() {
        int initialCapacity = 8;
        ArrayByteList arrayByteList = new ArrayByteList(initialCapacity);
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testConstructor_withByteCollection() {
        ByteCollection byteCollection = new ArrayByteCollection();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);
        
        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);
        assertNotNull(arrayByteList);
        assertEquals(3, arrayByteList.size());
        assertEquals((byte)1, arrayByteList.get(0));
        assertEquals((byte)2, arrayByteList.get(1));
        assertEquals((byte)3, arrayByteList.get(2));
    }
    
    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        
        assertEquals((byte)1, arrayByteList.get(0));
        assertEquals((byte)2, arrayByteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_withInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.get(0);
    }
    
    @Test
    public void testSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertEquals(0, arrayByteList.size());
        
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        
        assertEquals((byte)2, arrayByteList.removeElementAt(1));
        assertEquals(1, arrayByteList.size());
        assertEquals((byte)1, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_withInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        
        assertEquals((byte)2, arrayByteList.set(1, (byte)3));
        assertEquals(2, arrayByteList.size());
        assertEquals((byte)1, arrayByteList.get(0));
        assertEquals((byte)3, arrayByteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_withInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.set(0, (byte)1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 3);
        
        arrayByteList.add(1, (byte) 2);
        assertEquals(3, arrayByteList.size());
        assertEquals((byte)1, arrayByteList.get(0));
        assertEquals((byte)2, arrayByteList.get(1));
        assertEquals((byte)3, arrayByteList.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_withInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(1, (byte)1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(4);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        
        arrayByteList.ensureCapacity(5);
        assertEquals(2, arrayByteList.size());
        
        arrayByteList.ensureCapacity(10);
        assertEquals(2, arrayByteList.size());
        
        arrayByteList.ensureCapacity(12);
        assertEquals(2, arrayByteList.size());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList(10);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
        assertEquals(2, arrayByteList._data.length);
    }
    
    @Test
    public void testWriteObject() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        
        // TODO: Write test case to verify the content of the ObjectOutputStream
    }
    
    @Test
    public void testReadObject() {
        // TODO: Write test case to verify the reading of ObjectInputStream
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange_withInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.checkRange(-1);
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.checkRangeIncludingEndpoint(0);
        arrayByteList.checkRangeIncludingEndpoint(1);
        
        // TODO: Add test cases to verify valid index range
    }
}