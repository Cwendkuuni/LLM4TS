package org.apache.commons.collections.primitives;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testInitialCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialCapacityConstructor_NegativeCapacity() {
        ArrayByteList list = new ArrayByteList(-1);
    }

    @Test
    public void testByteCollectionConstructor() {
        ByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        ArrayByteList list = new ArrayByteList(byteList);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals(1, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        byte removedByte = list.removeElementAt(0);
        assertEquals(1, removedByte);
        assertEquals(0, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        byte oldValue = list.set(0, (byte) 2);
        assertEquals(1, oldValue);
        assertEquals(2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        assertEquals(1, list.get(0));
        list.add(1, (byte) 2);
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        list.trimToSize();
        assertEquals(0, list._data.length);
    }

    @Test
    public void testSerialization() {
        // TODO: Write test case to test object serialization
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(1);
    }

}