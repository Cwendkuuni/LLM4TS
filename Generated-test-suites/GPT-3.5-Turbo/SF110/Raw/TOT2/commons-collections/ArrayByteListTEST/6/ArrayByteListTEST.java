import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListInitialCapacityConstructor() {
        int initialCapacity = 5;
        ArrayByteList list = new ArrayByteList(initialCapacity);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListByteCollectionConstructor() {
        ByteList byteList = new ArrayByteList();
        byteList.add((byte) 5);
        ArrayByteList list = new ArrayByteList(byteList);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(5, list.get(0));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        assertEquals(20, list.get(1));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        assertEquals(1, list.size());
        assertEquals(5, list.get(0));
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        assertEquals(5, list.set(0, (byte) 10));
        assertEquals(10, list.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 5);
        list.add((byte) 10);
        assertEquals(10, list.removeElementAt(1));
        assertEquals(1, list.size());
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
        list.add((byte) 5);
        list.add((byte) 10);
        list.trimToSize();
        assertEquals(2, list._data.length);
    }

    // Add more test cases for edge cases and error scenarios
    
}