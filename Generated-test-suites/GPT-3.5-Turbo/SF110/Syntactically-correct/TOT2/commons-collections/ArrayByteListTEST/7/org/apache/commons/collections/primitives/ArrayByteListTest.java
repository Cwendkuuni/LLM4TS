package org.apache.commons.collections.primitives;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        byte removedElement = arrayByteList.removeElementAt(0);
        assertEquals(1, removedElement);
        assertEquals(1, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
    }

    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 1);
        byte oldElement = arrayByteList.set(0, (byte) 5);
        assertEquals(1, oldElement);
        assertEquals(5, arrayByteList.get(0));
    }
    
    // Add more test cases for the remaining methods

}