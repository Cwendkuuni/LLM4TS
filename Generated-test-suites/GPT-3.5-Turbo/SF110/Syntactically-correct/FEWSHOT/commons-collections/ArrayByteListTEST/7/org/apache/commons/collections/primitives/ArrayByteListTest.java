
package org.apache.commons.collections.primitives;

import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testConstructorWithDefaultCapacity() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        Assert.assertEquals(0, list.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidCapacity() {
        new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        ArrayByteList list = new ArrayByteList(collection);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        byte removed = list.removeElementAt(0);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 1, removed);
        Assert.assertEquals((byte) 2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        byte oldVal = list.set(0, (byte) 2);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
        Assert.assertEquals((byte) 1, oldVal);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(2);
        list.add((byte) 1);
        list.ensureCapacity(10);
        Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 1);
        list.trimToSize();
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(1, list.capacity());
    }
}

