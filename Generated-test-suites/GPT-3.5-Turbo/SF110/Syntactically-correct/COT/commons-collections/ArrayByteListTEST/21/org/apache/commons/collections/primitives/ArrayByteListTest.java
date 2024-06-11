
/* Generated test cases for ArrayByteList class */
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    
    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }
    
    // Test constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }
    
    // Test constructor with ByteCollection
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        ByteList list = new ArrayByteList(collection);
        assertEquals(0, list.size());
    }
    
    // Test get method
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        assertEquals(1, list.get(0));
    }
    
    // Test size method
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add(0, (byte) 1);
        assertEquals(1, list.size());
    }
    
    // Test removeElementAt method
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        byte removed = list.removeElementAt(0);
        assertEquals(1, removed);
        assertEquals(0, list.size());
    }
    
    // Test set method
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        byte oldValue = list.set(0, (byte) 2);
        assertEquals(1, oldValue);
        assertEquals(2, list.get(0));
    }
    
    // Test add method
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }
    
    // Test ensureCapacity method
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(0, list.size());
    }
    
    // Test trimToSize method
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        list.trimToSize();
        assertEquals(1, list.size());
    }
}
