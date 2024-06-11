import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {
    
    @Test
    public void testArrayByteListDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }
    
    @Test
    public void testArrayByteListInitialCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testArrayByteListByteCollectionConstructor() {
        ArrayByteList list = new ArrayByteList(new ArrayByteList());
        assertEquals(0, list.size());
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        assertEquals(5, list.get(0));
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        assertEquals(5, list.removeElementAt(0));
        assertEquals(0, list.size());
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        assertEquals(5, list.set(0, (byte) 10));
        assertEquals(10, list.get(0));
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        list.add(1, (byte) 10);
        assertEquals(5, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(2, list.size());
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        list.ensureCapacity(10);
        assertEquals(5, list.get(0));
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        list.ensureCapacity(10);
        list.trimToSize();
        assertEquals(5, list.get(0));
        assertEquals(1, list.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeException() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointException() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(1);
    }
}