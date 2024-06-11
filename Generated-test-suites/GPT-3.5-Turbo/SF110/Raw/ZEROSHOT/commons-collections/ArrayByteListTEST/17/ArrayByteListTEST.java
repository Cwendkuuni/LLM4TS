package org.apache.commons.collections.primitives;

import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor1() {
        ArrayByteList arr = new ArrayByteList();
        assertEquals(0, arr.size());
    }
    
    @Test
    public void testConstructor2() {
        ArrayByteList arr = new ArrayByteList(10);
        assertEquals(0, arr.size());
        assertEquals(10, arr._data.length);
    }
    
    @Test
    public void testConstructor3() {
        ByteCollection col = new ByteCollection();
        col.add((byte) 1);
        col.add((byte) 2);
        col.add((byte) 3);
        ArrayByteList arr = new ArrayByteList(col);
        assertEquals(3, arr.size());
        assertEquals(3, arr._data.length);
        assertEquals((byte) 1, arr._data[0]);
        assertEquals((byte) 2, arr._data[1]);
        assertEquals((byte) 3, arr._data[2]);
    }
    
    @Test
    public void testGet() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.add((byte) 1);
        arr.add((byte) 2);
        arr.add((byte) 3);
        assertEquals((byte) 1, arr.get(0));
        assertEquals((byte) 2, arr.get(1));
        assertEquals((byte) 3, arr.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.get(5);
    }
    
    @Test
    public void testSize() {
        ArrayByteList arr = new ArrayByteList(5);
        assertEquals(0, arr.size());
        arr.add((byte) 1);
        arr.add((byte) 2);
        assertEquals(2, arr.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.add((byte) 1);
        arr.add((byte) 2);
        arr.add((byte) 3);
        assertEquals((byte) 2, arr.removeElementAt(1));
        assertEquals(2, arr.size());
        assertEquals((byte) 1, arr.get(0));
        assertEquals((byte) 3, arr.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtInvalidIndex() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.add((byte) 1);
        arr.add((byte) 2);
        arr.add((byte) 3);
        assertEquals((byte) 2, arr.set(1, (byte) 4));
        assertEquals(3, arr.size());
        assertEquals((byte) 1, arr.get(0));
        assertEquals((byte) 4, arr.get(1));
        assertEquals((byte) 3, arr.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidIndex() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.set(0, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.add((byte) 1);
        arr.add((byte) 2);
        assertEquals(2, arr.size());
        assertEquals((byte) 1, arr.get(0));
        assertEquals((byte) 2, arr.get(1));
    }
    
    @Test
    public void testAddAtIndex() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.add((byte) 1);
        arr.add((byte) 3);
        arr.add(1, (byte) 2);
        assertEquals(3, arr.size());
        assertEquals((byte) 1, arr.get(0));
        assertEquals((byte) 2, arr.get(1));
        assertEquals((byte) 3, arr.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtInvalidIndex() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.add(1, (byte) 1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.ensureCapacity(10);
        assertEquals(5, arr._data.length);
        arr.ensureCapacity(20);
        assertEquals(30, arr._data.length);
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList arr = new ArrayByteList(30);
        arr.ensureCapacity(10);
        assertEquals(30, arr._data.length);
        arr.trimToSize();
        assertEquals(10, arr._data.length);
    }
    
    @Test
    public void testWriteObject() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.add((byte) 1);
        arr.add((byte) 2);
        arr.add((byte) 3);
        byte[] expected = new byte[] {1, 2, 3};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(baos);
            arr.writeObject(out);
            out.close();
            byte[] actual = baos.toByteArray();
            assertArrayEquals(expected, actual);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testReadObject() {
        byte[] data = new byte[] {1, 2, 3};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(bais);
            ArrayByteList arr = new ArrayByteList();
            arr.readObject(in);
            in.close();
            assertEquals(3, arr.size());
            assertEquals((byte) 1, arr.get(0));
            assertEquals((byte) 2, arr.get(1));
            assertEquals((byte) 3, arr.get(2));
        } catch (IOException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange1() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.checkRange(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange2() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.checkRange(5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint1() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.checkRangeIncludingEndpoint(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint2() {
        ArrayByteList arr = new ArrayByteList(5);
        arr.checkRangeIncludingEndpoint(6);
    }
}