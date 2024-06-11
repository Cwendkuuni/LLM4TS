import org.junit.Test;
import org.junit.Before;
import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ByteList;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    private ArrayByteList list;
    
    @Before
    public void setUp() {
        list = new ArrayByteList();
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructor2() {
        list = new ArrayByteList(5);
        assertEquals(0, list.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3() {
        list = new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructor4() {
        ByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        byteList.add((byte) 2);
        list = new ArrayByteList(byteList);
        
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test
    public void testGet() {
        list.add((byte) 1);
        list.add((byte) 2);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        list.get(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(0, list.size());
        
        list.add((byte) 1);
        list.add((byte) 2);
        
        assertEquals(2, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        byte removed = list.removeElementAt(1);
        
        assertEquals(2, removed);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtInvalidIndex() {
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        byte replaced = list.set(1, (byte) 4);
        
        assertEquals(2, replaced);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(4, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidIndex() {
        list.set(0, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        list.add((byte) 1);
        list.add((byte) 2);
        
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test
    public void testAddAtIndex() {
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.add(1, (byte) 3);
        
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(2, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalidIndex() {
        list.add(1, (byte) 1);
    }
    
    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        
        assertEquals(0, list.size());
    }
    
    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.trimToSize();
        
        assertEquals(2, list.size());
    }
    
    @Test
    public void testCheckRange() {
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.checkRange(0);
        list.checkRange(1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeInvalidIndex() {
        list.checkRange(-1);
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        list.add((byte) 1);
        list.add((byte) 2);
        
        list.checkRangeIncludingEndpoint(0);
        list.checkRangeIncludingEndpoint(1);
        list.checkRangeIncludingEndpoint(2);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointInvalidIndex() {
        list.checkRangeIncludingEndpoint(-1);
    }
}