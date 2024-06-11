import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList list;
    
    @Before
    public void setUp() {
        list = new ArrayByteList();
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(8, list._data.length);
        assertEquals(0, list._size);
    }
    
    @Test
    public void testConstructor2() {
        ArrayByteList list2 = new ArrayByteList(10);
        assertEquals(10, list2._data.length);
        assertEquals(0, list2._size);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor2_InvalidCapacity() {
        ArrayByteList list3 = new ArrayByteList(-5);
    }
    
    @Test
    public void testConstructor3() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);
        ArrayByteList list4 = new ArrayByteList(byteCollection);
        assertEquals(3, list4._size);
        assertEquals(3, list4._data.length);
        assertEquals(1, list4._data[0]);
        assertEquals(2, list4._data[1]);
        assertEquals(3, list4._data[2]);
    }
    
    @Test
    public void testGet() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        assertEquals(2, list.get(1));
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testGet_InvalidIndex() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.get(5);
    }
    
    @Test
    public void testSize() {
        list._size = 5;
        assertEquals(5, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        assertEquals(2, list.removeElementAt(1));
        assertEquals(2, list._size);
        assertEquals(1, list._data[0]);
        assertEquals(3, list._data[1]);
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_InvalidIndex() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.removeElementAt(5);
    }
    
    @Test
    public void testSet() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        assertEquals(2, list.set(1, (byte) 5));
        assertEquals(1, list._data[0]);
        assertEquals(5, list._data[1]);
        assertEquals(3, list._data[2]);
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testSet_InvalidIndex() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.set(5, (byte) 5);
    }
    
    @Test
    public void testAdd() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.add(1, (byte) 5);
        assertEquals(4, list._size);
        assertEquals(1, list._data[0]);
        assertEquals(5, list._data[1]);
        assertEquals(2, list._data[2]);
        assertEquals(3, list._data[3]);
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testAdd_InvalidIndex() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.add(5, (byte) 5);
    }
    
    @Test
    public void testEnsureCapacity() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.ensureCapacity(5);
        assertEquals(5, list._data.length);
        assertEquals(3, list._size);
        assertEquals(1, list._data[0]);
        assertEquals(2, list._data[1]);
        assertEquals(3, list._data[2]);
    }
    
    @Test
    public void testEnsureCapacity_NoChange() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.ensureCapacity(3);
        assertEquals(3, list._data.length);
        assertEquals(3, list._size);
        assertEquals(1, list._data[0]);
        assertEquals(2, list._data[1]);
        assertEquals(3, list._data[2]);
    }
    
    @Test
    public void testTrimToSize() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.trimToSize();
        assertEquals(3, list._data.length);
        assertEquals(3, list._size);
        assertEquals(1, list._data[0]);
        assertEquals(2, list._data[1]);
        assertEquals(3, list._data[2]);
    }
    
    @Test
    public void testTrimToSize_NoChange() {
        list._data = new byte[]{1, 2, 3};
        list._size = 3;
        list.trimToSize();
        assertEquals(3, list._data.length);
        assertEquals(3, list._size);
        assertEquals(1, list._data[0]);
        assertEquals(2, list._data[1]);
        assertEquals(3, list._data[2]);
    }
    
    @Test
    public void testCheckRange_ValidIndex() {
        list._size = 5;
        list.checkRange(3);
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCheckRange_InvalidIndex() {
        list._size = 5;
        list.checkRange(7);
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint_ValidIndex() {
        list._size = 5;
        list.checkRangeIncludingEndpoint(3);
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint_InvalidIndex() {
        list._size = 5;
        list.checkRangeIncludingEndpoint(9);
    }
}