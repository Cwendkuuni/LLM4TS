import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListConstructorWithCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListConstructorWithByteCollection() {
        ArrayByteList list = new ArrayByteList(new ArrayByteList());
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
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
        list.add(0, (byte) 5);
        assertEquals(5, list.removeElementAt(0));
        assertEquals(0, list.size());
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        assertEquals(5, list.get(0));
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
        list.add(0, (byte) 5);
        list.trimToSize();
        assertEquals(1, list._data.length);
    }

    // Add more test cases to cover edge cases and error scenarios

}