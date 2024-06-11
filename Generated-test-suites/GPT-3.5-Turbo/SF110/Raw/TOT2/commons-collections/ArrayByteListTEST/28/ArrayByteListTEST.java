import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        byte element = 5;
        arrayByteList.add(0, element);
        assertEquals(element, arrayByteList.get(0));
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        byte element = 5;
        arrayByteList.add(0, element);
        assertEquals(element, arrayByteList.removeElementAt(0));
    }
    
    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        byte element = 5;
        arrayByteList.add(0, 10);
        arrayByteList.set(0, element);
        assertEquals(element, arrayByteList.get(0));
    }
    
    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        byte element = 5;
        arrayByteList.add(0, element);
        assertEquals(element, arrayByteList.get(0));
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList._data.length);
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, 5);
        arrayByteList.trimToSize();
        assertEquals(1, arrayByteList._data.length);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.checkRange(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithIndexGreaterThanSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, 5);
        arrayByteList.checkRange(1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.checkRangeIncludingEndpoint(1);
    }
}