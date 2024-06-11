
import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        byte removed = list.removeElementAt(0);
        assertEquals(10, removed);
        assertEquals(0, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        byte oldValue = list.set(0, (byte) 20);
        assertEquals(10, oldValue);
        assertEquals(20, list.get(0));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.get(0));
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
        list.add(0, (byte) 10);
        list.trimToSize();
        assertEquals(1, list._data.length);
    }

    // TODO: Add additional tests for edge cases and exception handling scenarios

}
