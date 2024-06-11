import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {
    
    @Test
    public void testConstructor1() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructor2() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testAddAndGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(1, list.size());
        assertEquals(10, list.get(0));
    }
    
    @Test
    public void testRemove() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        assertEquals(10, list.removeElementAt(0));
        assertEquals(1, list.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 10);
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
    
    @Test
    public void testSerialization() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        list.add(1, (byte) 20);
        
        ArrayByteList deserializedList = (ArrayByteList) deserialize(serialize(list));
        assertEquals(2, deserializedList.size());
        assertEquals(10, deserializedList.get(0));
        assertEquals(20, deserializedList.get(1));
    }

    // Helper methods for serialization
    private byte[] serialize(Object obj) {
        // Implement serialization logic here
        return null;
    }

    private Object deserialize(byte[] data) {
        // Implement deserialization logic here
        return null;
    }
}