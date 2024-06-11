import org.junit.Test;
import static org.junit.Assert.*;

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
  
    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListWithInvalidInitialCapacity() {
        ArrayByteList list = new ArrayByteList(-1);
    }
  
    @Test
    public void testArrayByteListCopy() {
        ArrayByteList list1 = new ArrayByteList();
        list1.add((byte) 1);
        list1.add((byte) 2);
        ArrayByteList list2 = new ArrayByteList(list1);
        assertEquals(2, list2.size());
    }
  
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
  
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
        list.add((byte) 2);
        assertEquals(2, list.size());
    }
  
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        byte removed = list.removeElementAt(0);
        assertEquals(1, removed);
        assertEquals(1, list.size());
    }
  
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals(1, list.set(0, (byte) 2));
        assertEquals(2, list.get(0));
    }
  
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals(1, list.get(0));
        list.add((byte) 2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
  
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
        list.ensureCapacity(5);
        assertEquals(10, list._data.length);
    }
  
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.trimToSize();
        assertEquals(2, list._data.length);
    }
}