
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
        assertEquals(10, list._data.length);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        byteList.add((byte)2);
        
        ArrayByteList list = new ArrayByteList(byteList);
        assertEquals(2, list.size());
        assertEquals(2, list._data.length);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)2);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        
        list.add((byte)1);
        list.add((byte)2);
        
        assertEquals(2, list.size());
        list.removeElementAt(0);
        assertEquals(1, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)2);
        
        byte removedElement = list.removeElementAt(0);
        
        assertEquals(1, list.size());
        assertEquals(1, removedElement);
        assertEquals(2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        
        byte oldElement = list.set(0, (byte)2);
        
        assertEquals(1, list.size());
        assertEquals(1, oldElement);
        assertEquals(2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte)1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte)1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(2);
        list.add((byte)1);
        list.add((byte)2);
        
        list.ensureCapacity(10);
        
        assertEquals(2, list.size());
        assertEquals(10, list._data.length);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte)1);
        list.add((byte)2);
        
        list.trimToSize();
        
        assertEquals(2, list.size());
        assertEquals(2, list._data.length);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    // Test cases for writeObject() and readObject() methods are not included as they don't directly affect the functionality of the class.
    
}

