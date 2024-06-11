import org.junit.Test;
import org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        
        list.add((byte) 1);
        assertEquals(1, list.size());
        
        list.add((byte) 2);
        assertEquals(2, list.size());
        
        list.removeElementAt(0);
        assertEquals(1, list.size());
        
        list.removeElementAt(0);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        
        try {
            list.get(0);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        list.add((byte) 1);
        assertEquals(1, list.get(0));
        
        list.add((byte) 2);
        assertEquals(2, list.get(1));
        
        try {
            list.get(2);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        
        try {
            list.removeElementAt(0);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        assertEquals(2, list.removeElementAt(1));
        
        try {
            list.removeElementAt(2);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        
        try {
            list.set(0, (byte) 1);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.set(1, (byte) 3);
        
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(2);
        
        assertEquals(2, list.getCapacity());
        
        list.add((byte) 1);
        
        assertEquals(2, list.getCapacity());
        
        list.add((byte) 2);
        
        assertEquals(2, list.getCapacity());
        
        list.add((byte) 3);
        
        assertEquals(4, list.getCapacity());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(4);
        
        assertEquals(4, list.getCapacity());
        
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        
        assertEquals(4, list.getCapacity());
        
        list.removeElementAt(0);
        
        assertEquals(3, list.getCapacity());
        
        list.trimToSize();
        
        assertEquals(3, list.getCapacity());
    }
}