package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {
    
    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        
        ArrayByteList list2 = new ArrayByteList(10);
        assertEquals(0, list2.size());
        
        try {
            ArrayByteList list3 = new ArrayByteList(-5);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        
        try {
            byte value = list.get(2);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        
        list.add((byte) 1);
        assertEquals(1, list.size());
        
        list.add((byte) 2);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        byte removed = list.removeElementAt(1);
        assertEquals(2, list.size());
        assertEquals((byte) 2, removed);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
        
        try {
            byte removed2 = list.removeElementAt(2);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        byte oldVal = list.set(1, (byte) 3);
        assertEquals(2, list.size());
        assertEquals((byte) 2, oldVal);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
        
        try {
            byte oldVal2 = list.set(2, (byte) 4);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        
        list.add(0, (byte) 2);
        assertEquals(2, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 1, list.get(1));
        
        list.add(1, (byte) 3);
        assertEquals(3, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertEquals((byte) 1, list.get(2));
        
        try {
            list.add(-1, (byte) 4);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        try {
            list.add(4, (byte) 5);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.ensureCapacity(20);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        
        list.ensureCapacity(1);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(20);
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.trimToSize();
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        
        ArrayByteList list2 = new ArrayByteList();
        list2.trimToSize();
        assertEquals(0, list2.size());
    }
    
    // Additional test cases can be added for writeObject and readObject methods
    
    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        try {
            list.checkRange(-1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        try {
            list.checkRange(3);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        try {
            list.checkRangeIncludingEndpoint(-1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        try {
            list.checkRangeIncludingEndpoint(4);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

}