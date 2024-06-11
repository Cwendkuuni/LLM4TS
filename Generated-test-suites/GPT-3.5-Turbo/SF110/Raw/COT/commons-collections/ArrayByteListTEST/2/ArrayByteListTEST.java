
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    
    @Test
    public void testConstructorDefault() {
        ArrayByteList byteList = new ArrayByteList();
        assertNotNull(byteList);
        assertEquals(0, byteList.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList byteList = new ArrayByteList(10);
        assertNotNull(byteList);
        assertEquals(0, byteList.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        ArrayByteList byteList = new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ByteCollectionImpl();
        byteCollection.add((byte)1);
        byteCollection.add((byte)2);
        ArrayByteList byteList = new ArrayByteList(byteCollection);
        assertNotNull(byteList);
        assertEquals(2, byteList.size());
        assertEquals((byte)1, byteList.get(0));
        assertEquals((byte)2, byteList.get(1));
    }
    
    @Test
    public void testGet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(1);
        byteList.add(2);
        byteList.add(3);
        assertEquals((byte)2, byteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexGreaterThanSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.get(0);
    }
    
    @Test
    public void testSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(1);
        byteList.add(2);
        byteList.add(3);
        assertEquals(3, byteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(1);
        byteList.add(2);
        byteList.add(3);
        byte removed = byteList.removeElementAt(1);
        assertEquals((byte)2, removed);
        assertEquals(2, byteList.size());
        assertEquals((byte)1, byteList.get(0));
        assertEquals((byte)3, byteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.removeElementAt(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexGreaterThanSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(1);
        byteList.add(2);
        byteList.add(3);
        byte oldVal = byteList.set(1, (byte)5);
        assertEquals((byte)2, oldVal);
        assertEquals((byte)5, byteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.set(-1, (byte)5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexGreaterThanSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.set(0, (byte)5);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        assertEquals(1, byteList.size());
        assertEquals((byte)1, byteList.get(0));
    }
    
    @Test
    public void testAddAtIndex() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        byteList.add((byte)3);
        byteList.add(1, (byte)2);
        assertEquals(3, byteList.size());
        assertEquals((byte)1, byteList.get(0));
        assertEquals((byte)2, byteList.get(1));
        assertEquals((byte)3, byteList.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(-1, (byte)2);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexGreaterThanSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(1, (byte)2);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.ensureCapacity(10);
        assertEquals(0, byteList.size());
        assertTrue(byteList instanceof ArrayByteList);
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(1);
        byteList.add(2);
        byteList.add(3);
        byteList.trimToSize();
        assertEquals(3, byteList.size());
        assertTrue(byteList instanceof ArrayByteList);
    }
}
