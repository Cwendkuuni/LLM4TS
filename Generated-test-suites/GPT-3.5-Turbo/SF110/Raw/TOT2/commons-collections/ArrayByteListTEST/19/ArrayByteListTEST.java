import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testArrayByteList_DefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteList_InitialCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteList_CollectionConstructor() {
        // Test with empty collection
        ArrayByteList list = new ArrayByteList(new ArrayByteCollection());
        assertEquals(0, list.size());

        // Test with non-empty collection
        ArrayByteCollection col = new ArrayByteCollection();
        col.add(10);
        col.add(20);

        list = new ArrayByteList(col);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
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
        list.add(5);
        list.add(10);

        assertEquals(10, list.removeElementAt(1));
        assertEquals(1, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(5);

        assertEquals(5, list.set(0, 10));
        assertEquals(10, list.get(0));
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
        ArrayByteList list = new ArrayByteList(2);
        list.add((byte) 1);
        list.add((byte) 2);

        list.ensureCapacity(5);
        assertEquals(5, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.trimToSize();
        assertEquals(2, list._data.length);
    }

    // Additional edge case and error scenario tests can be added here

}