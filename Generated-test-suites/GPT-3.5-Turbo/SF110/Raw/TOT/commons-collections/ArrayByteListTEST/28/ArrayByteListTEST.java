import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor1() {
        ArrayByteList list = new ArrayByteList();
        assertTrue(list.size() == 0);
    }
    
    @Test
    public void testConstructor2() {
        ArrayByteList list = new ArrayByteList(10);
        assertTrue(list.size() == 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2_InvalidCapacity() {
        ArrayByteList list = new ArrayByteList(-10);
    }
    
    @Test
    public void testConstructor3() {
        ByteCollection collection = new ArrayByteList(5);
        collection.add((byte)1);
        collection.add((byte)2);
        collection.add((byte)3);
        ArrayByteList list = new ArrayByteList(collection);
        assertTrue(list.size() == 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(5);
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.add(1, (byte)2);
        list.add(2, (byte)3);
        byte removed = list.removeElementAt(1);
        assertEquals(2, removed);
        assertEquals(2, list.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(5);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.add(1, (byte)2);
        list.add(2, (byte)3);
        byte oldValue = list.set(1, (byte)4);
        assertEquals(2, oldValue);
        assertEquals(3, list.size());
        assertEquals((byte)4, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(5, (byte)1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.add(1, (byte)2);
        list.add(2, (byte)3);
        list.add(1, (byte)4);
        assertEquals(4, list.size());
        assertEquals((byte)4, list.get(1));
        assertEquals((byte)2, list.get(2));
        assertEquals((byte)3, list.get(3));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_InvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(5, (byte)1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_InvalidIndex2() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, (byte)1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testEnsureCapacity_Enlarge() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.ensureCapacity(5);
        assertEquals(1, list.size());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.add(1, (byte)2);
        list.add(2, (byte)3);
        list.trimToSize();
        assertEquals(3, list.size());
        assertEquals(3, list.get(2));
    }
    
    @Test
    public void testWriteObject() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.add(1, (byte)2);
        list.add(2, (byte)3);
        // TODO: Write test for writeObject
    }
    
    @Test
    public void testReadObject() {
        // TODO: Write test for readObject
    }
}