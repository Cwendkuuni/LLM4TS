package org.apache.commons.collections.primitives;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayByteListTest {
    
    @Test
    public void testConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 5);
        assertEquals(1, arrayByteList.size());
    }
    
    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 5);
        assertEquals(5, arrayByteList.get(0));
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        arrayByteList.removeElementAt(0);
        assertEquals(1, arrayByteList.size());
        assertEquals(10, arrayByteList.get(0));
    }
    
    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 5);
        arrayByteList.set(0, (byte) 10);
        assertEquals(10, arrayByteList.get(0));
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 5);
        arrayByteList.ensureCapacity(10);
        assertEquals(5, arrayByteList.get(0));
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
    }
}