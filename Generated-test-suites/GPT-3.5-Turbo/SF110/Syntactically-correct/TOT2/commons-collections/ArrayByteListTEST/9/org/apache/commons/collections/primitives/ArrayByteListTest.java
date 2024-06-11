package org.apache.commons.collections.primitives;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {
    
    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testAddElement() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals((byte) 10, list.get(0));
        assertEquals(1, list.size());
    }
    
    @Test
    public void testRemoveElement() {
        ArrayByteList list = new ArrayByteList();
    	list.add(0, (byte) 5);
        assertEquals((byte) 5, list.removeElementAt(0));
        assertEquals(0, list.size());
    }
    
    @Test
    public void testGetElement() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 7);
        assertEquals((byte) 7, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacityConstructor() {
        new ArrayByteList(-1);
    }
    
    // Add more test cases for the remaining methods
    
}