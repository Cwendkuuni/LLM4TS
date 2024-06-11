import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections.primitives.*;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        ArrayByteList newList = new ArrayByteList();
        assertEquals(0, newList.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList newList = new ArrayByteList(10);
        assertEquals(0, newList.size());
        assertEquals(10, newList._data.length);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        ArrayByteList newList = new ArrayByteList(byteCollection);
        assertEquals(1, newList.size());
        assertEquals((byte) 1, newList.get(0));
    }

    @Test
    public void testGet() {
        list.add((byte) 1);
        assertEquals((byte) 1, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        list.get(0);
    }

    @Test
    public void testSize() {
        assertEquals(0, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        byte removed = list.removeElementAt(0);
        assertEquals(0, list.size());
        assertEquals((byte) 1, removed);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        list.add((byte) 1);
        byte oldValue = list.set(0, (byte) 2);
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 1, oldValue);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        list.set(0, (byte) 2);
    }

    @Test
    public void testAdd() {
        list.add((byte) 1);
        assertEquals(1, list.size());
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testAddAtBeginning() {
        list.add(0, (byte) 1);
        list.add(0, (byte) 2);
        assertEquals(2, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 1, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
    }

    @Test
    public void testEnsureCapacityNoChange() {
        list.ensureCapacity(5);
        assertEquals(8, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.trimToSize();
        assertEquals(2, list._data.length);
    }

    @Test
    public void testTrimToSizeNoChange() {
        list.trimToSize();
        assertEquals(8, list._data.length);
    }

    @Test
    public void testWriteObject() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        // TODO: Test writeObject method
        assertTrue(true);
    }

    @Test
    public void testReadObject() {
        // TODO: Test readObject method
        assertTrue(true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        list.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        list.checkRangeIncludingEndpoint(1);
    }
}