import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListInitialCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListByteCollectionConstructor() {
        ArrayByteList list = new ArrayByteList(new ArrayByteList());
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.get(0));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.removeElementAt(0));
        assertEquals(0, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.set(0, (byte) 20));
        assertEquals(20, list.get(0));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(20);
        assertEquals(20, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(20);
        list.trimToSize();
        assertEquals(0, list._data.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.checkRange(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.checkRangeIncludingEndpoint(2);
    }
}