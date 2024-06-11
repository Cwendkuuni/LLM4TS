import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());

        ArrayByteList list2 = new ArrayByteList(5);
        assertNotNull(list2);
        assertEquals(0, list2.size());
    }

    @Test
    public void testGetSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        list.add(1);
        list.add(2);
        assertEquals(2, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        assertEquals(2, list.removeElementAt(1));
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(4, list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        assertEquals(2, list.set(1, 5));
        assertEquals(1, list.get(0));
        assertEquals(5, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);

        list.ensureCapacity(10);

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);

        list.trimToSize();

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(3, list._data.length);
    }

}