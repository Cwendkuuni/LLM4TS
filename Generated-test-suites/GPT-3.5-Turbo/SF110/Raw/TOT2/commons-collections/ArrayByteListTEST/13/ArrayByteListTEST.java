import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteList() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
    }
    
    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertNotNull(list);
    }
    
    @Test
    public void testArrayByteListWithByteCollection() {
        ArrayByteList list = new ArrayByteList(new ByteCollectionImpl());
        assertNotNull(list);
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        assertEquals(1, list.get(0));
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add(1);
        assertEquals(1, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        byte removed = list.removeElementAt(0);
        assertEquals(1, removed);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        assertEquals(1, list.set(0, 2));
        assertEquals(2, list.get(0));
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        assertEquals(1, list.get(0));
        list.add(1, (byte)2);
        assertEquals(2, list.get(1));
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
        list.ensureCapacity(10);
        list.trimToSize();
        assertEquals(0, list._data.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte)1);
    }
}