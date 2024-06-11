import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(0, (byte) 10);
        assertEquals((byte) 10, list.get(0));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        assertEquals((byte) 10, list.removeElementAt(0));
        assertEquals(1, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals((byte) 10, list.set(0, (byte) 20));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        assertEquals(2, list.size());
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        list.ensureCapacity(4);
        assertEquals(2, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        list.trimToSize();
        assertEquals(2, list.size());
    }

    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, (byte) 10));
    }

}