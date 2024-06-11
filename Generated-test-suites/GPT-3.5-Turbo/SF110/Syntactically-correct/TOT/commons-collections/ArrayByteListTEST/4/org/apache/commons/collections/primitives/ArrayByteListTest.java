import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor1() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructor2() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3() {
        ArrayByteList list = new ArrayByteList(-1);
    }

    @Test
    public void testConstructor4() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);

        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(-1);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        list.add((byte) 1);
        assertEquals(1, list.size());

        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(3, list.size());

        list.removeElementAt(0);
        assertEquals(2, list.size());
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
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBoundsNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(-1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte previous = list.set(1, (byte) 4);
        assertEquals(2, previous);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(4, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(-1, (byte) 0);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBoundsNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, (byte) 0);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(5);

        assertEquals(0, list.size());
        assertEquals(8, list._data.length);

        list.add((byte) 1);
        list.ensureCapacity(2);

        assertEquals(1, list.size());
        assertEquals(8, list._data.length);

        list.ensureCapacity(10);

        assertEquals(1, list.size());
        assertEquals(16, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.trimToSize();
        assertEquals(0, list._data.length);

        list.add((byte) 1);
        list.add((byte) 2);
        list.trimToSize();
        assertEquals(2, list._size);
        assertEquals(2, list._data.length);
    }
}