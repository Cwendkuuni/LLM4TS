import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList list;
    
    @Before
    public void setUp() {
        list = new ArrayByteList();
    }
    
    @Test
    public void testArrayByteListConstructorDefault() {
        assertEquals(0, list.size());
    }
    
    @Test
    public void testArrayByteListConstructorWithInitialCapacity() {
        ArrayByteList listWithCapacity = new ArrayByteList(10);
        assertEquals(0, listWithCapacity.size());
    }
    
    @Test
    public void testGet() {
        list.add(0, (byte) 10);
        assertEquals(10, list.get(0));
    }
    
    @Test
    public void testRemoveElementAt() {
        list.add(0, (byte) 5);
        list.add(1, (byte) 10);
        assertEquals(10, list.removeElementAt(1));
        assertEquals(5, list.get(0));
    }
    
    @Test
    public void testSet() {
        list.add(0, (byte) 5);
        assertEquals(5, list.set(0, (byte) 10));
        assertEquals(10, list.get(0));
    }
    
    @Test
    public void testAdd() {
        list.add(0, (byte) 5);
        list.add(1, (byte) 10);
        assertEquals(5, list.get(0));
        assertEquals(10, list.get(1));
    }
    
    @Test
    public void testEnsureCapacity() {
        for (int i = 0; i < 11; i++) {
            list.add(i, (byte) i);
        }
        assertEquals(11, list.size());
    }
    
    @Test
    public void testTrimToSize() {
        for (int i = 0; i < 10; i++) {
            list.add(i, (byte) i);
        }
        list.trimToSize();
        assertEquals(10, list.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        list.checkRange(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithOutOfRangeIndex() {
        list.add(0, (byte) 5);
        list.checkRange(1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        list.checkRangeIncludingEndpoint(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithOutOfRangeIndex() {
        list.add(0, (byte) 5);
        list.checkRangeIncludingEndpoint(2);
    }
}