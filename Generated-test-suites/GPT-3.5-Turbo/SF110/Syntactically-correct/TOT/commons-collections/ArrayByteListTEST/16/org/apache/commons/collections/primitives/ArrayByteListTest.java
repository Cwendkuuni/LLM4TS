package org.apache.commons.collections.primitives;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testArrayByteList() {
        // Test constructor with default initial capacity
        ArrayByteList list1 = new ArrayByteList();
        assertEquals(0, list1.size());
        
        // Test constructor with specified initial capacity
        ArrayByteList list2 = new ArrayByteList(10);
        assertEquals(0, list2.size());
    }
    
    @Test
    public void testArrayByteList(ByteCollection byteCollection) {
        // Test constructor with ByteCollection
        ByteCollection coll = new ArrayByteCollection();
        ArrayByteList list = new ArrayByteList(coll);
        assertEquals(coll.size(), list.size());
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        // Test getting element at specific index
        assertEquals(20, list.get(1));
        
        // Test getting element at index out of range
        try {
            list.get(3);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        
        list.add(10);
        assertEquals(1, list.size());
        
        list.add(20);
        assertEquals(2, list.size());
        
        list.removeElementAt(0);
        assertEquals(1, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        // Test removing element at specific index
        byte removed = list.removeElementAt(1);
        assertEquals(2, list.size());
        assertEquals(20, removed);
        
        // Test removing element at index out of range
        try {
            list.removeElementAt(3);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        // Test setting element at specific index
        byte oldValue = list.set(1, 25);
        assertEquals(3, list.size());
        assertEquals(20, oldValue);
        assertEquals(25, list.get(1));
        
        // Test setting element at index out of range
        try {
            list.set(3, 35);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        // Test adding element at specific index
        list.add(1, 15);
        assertEquals(4, list.size());
        assertEquals(10, list.get(0));
        assertEquals(15, list.get(1));
        assertEquals(20, list.get(2));
        assertEquals(30, list.get(3));
        
        // Test adding element at index out of range
        try {
            list.add(5, 35);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        // Test ensuring capacity for additional elements
        list.ensureCapacity(10);
        assertTrue(list.size() <= 10);
        assertTrue(list.size() <= list.capacity());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        // Test trimming capacity to current size
        list.trimToSize();
        assertTrue(list.size() == list.capacity());
    }
    
    @Test
    public void testSerialization() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        // Test serialization and deserialization
        ArrayByteList clonedList = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(list);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedList = (ArrayByteList) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertNotNull(clonedList);
        assertEquals(list.size(), clonedList.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), clonedList.get(i));
        }
    }
    
    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        
        // Test checking range of index within range
        try {
            list.checkRange(0);
        } catch (IndexOutOfBoundsException e) {
            fail("Unexpected IndexOutOfBoundsException thrown");
        }
        
        // Test checking range of index out of range
        try {
            list.checkRange(1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        
        // Test checking range including endpoint of index within range
        try {
            list.checkRangeIncludingEndpoint(0);
        } catch (IndexOutOfBoundsException e) {
            fail("Unexpected IndexOutOfBoundsException thrown");
        }
        
        // Test checking range including endpoint of index out of range
        try {
            list.checkRangeIncludingEndpoint(1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }
}