
import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {
    
    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }
    
    @Test
    public void testArrayByteListConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testArrayByteListConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        ArrayByteList list = new ArrayByteList(collection);
        
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListConstructorWithIllegalCapacity() {
        ArrayByteList list = new ArrayByteList(-1);
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        byte result = list.get(0);
        
        assertEquals(1, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        
        byte result = list.get(1);
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        
        int result = list.size();
        
        assertEquals(1, result);
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        byte removed = list.removeElementAt(0);
        
        assertEquals(1, removed);
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        
        byte oldValue = list.set(0, (byte) 2);
        
        assertEquals(1, oldValue);
        assertEquals(2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        
        list.add(0, (byte) 1);
        
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        
        list.ensureCapacity(10);
        
        assertEquals(0, list.size());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.trimToSize();
        
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
}
