import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList arrayByteList;
    
    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructor1() {
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testConstructor2() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructor3() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals((byte) 1, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        arrayByteList.get(0);
    }
    
    @Test
    public void testSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        byte removed = arrayByteList.removeElementAt(0);
        assertEquals((byte) 1, removed);
        assertEquals(1, arrayByteList.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        arrayByteList.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        byte oldval = arrayByteList.set(0, (byte) 2);
        assertEquals((byte) 1, oldval);
        assertEquals((byte) 2, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        arrayByteList.set(0, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 1);
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals(1, arrayByteList.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        arrayByteList.add(1, (byte) 1);
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
    }
}