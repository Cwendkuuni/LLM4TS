import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testInitialCapacityConstructor() {
        int initialCapacity = 5;
        ArrayByteList list = new ArrayByteList(initialCapacity);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testByteCollectionConstructor() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        ArrayByteList newList = new ArrayByteList(list);
        assertNotNull(newList);
        assertEquals(2, newList.size());
        assertEquals(1, newList.get(0));
        assertEquals(2, newList.get(1));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(5);
        list.add(10);
        assertEquals(5, list.get(0));
        assertEquals(10, list.get(1));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(2, list.removeElementAt(1));
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        assertEquals(1, list.set(0, (byte) 5));
        assertEquals(5, list.get(0));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        assertEquals(1, list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.ensureCapacity(5);
        assertEquals(5, list._data.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithOutOfBoundsIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(5);
        list.checkRange(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithOutOfBoundsIndex() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.checkRangeIncludingEndpoint(1);
    }

    @Test
    public void testSerializableOperations() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        assertEquals(2, list.size());
    }
}