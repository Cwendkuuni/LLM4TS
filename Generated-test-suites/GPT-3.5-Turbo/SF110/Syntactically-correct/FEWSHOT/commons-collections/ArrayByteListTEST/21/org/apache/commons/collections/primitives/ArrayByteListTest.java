import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrayByteListTest {
    @Test
    public void testConstructorWithoutInitialCapacity() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        byteList.add((byte) 2);
        ArrayByteList list = new ArrayByteList(byteList);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
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
        list.add((byte) 2);
        list.add((byte) 3);
        byte removed = list.removeElementAt(1);
        assertEquals(2, removed);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
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
        list.add((byte) 2);
        byte oldValue = list.set(1, (byte) 3);
        assertEquals(2, oldValue);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testAddAtIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 3);
        list.add(1, (byte) 2);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        // Assert the capacity is increased, further testing required
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.trimToSize();
        // Assert the capacity is trimmed, further testing required
    }

    @Test
    public void testSerialization() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        // Assert serialization and deserialization, further testing required
    }
}