import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    
    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertNotNull(list);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ByteArrayList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);
        
        ArrayByteList list = new ArrayByteList(collection);
        
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.get(1);
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        
        assertEquals(0, list.size());
        
        list.add((byte) 1);
        list.add((byte) 2);
        
        assertEquals(2, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        byte removedElement = list.removeElementAt(0);
        
        assertEquals(1, list.size());
        assertEquals((byte) 1, removedElement);
        assertEquals((byte) 2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.removeElementAt(1);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        
        byte oldVal = list.set(1, (byte) 3);
        
        assertEquals(2, list.size());
        assertEquals((byte) 2, oldVal);
        assertEquals((byte) 3, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.set(1, (byte) 2);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        
        list.add(0, (byte) 1);
        assertEquals(1, list.size());
        assertEquals((byte) 1, list.get(0));
        
        list.add(1, (byte) 2);
        assertEquals(2, list.size());
        assertEquals((byte) 2, list.get(1));
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
        list.ensureCapacity(10);
        list.trimToSize();
        assertEquals(0, list.size());
    }
}