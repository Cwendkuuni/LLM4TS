import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;
    
    @Before
    public void setup() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList byteList = new ArrayByteList(10);
        assertEquals(0, byteList.size());
        assertEquals(10, byteList._data.length);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);
        ArrayByteList byteList = new ArrayByteList(byteCollection);
        assertEquals(3, byteList.size());
        assertEquals(1, byteList.get(0));
        assertEquals(2, byteList.get(1));
        assertEquals(3, byteList.get(2));
    }
    
    @Test
    public void testGet() {
        arrayByteList.add(1);
        assertEquals(1, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        arrayByteList.get(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
        arrayByteList.add(1);
        assertEquals(1, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(1);
        assertEquals(1, arrayByteList.removeElementAt(0));
        assertEquals(0, arrayByteList.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        arrayByteList.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        arrayByteList.add(1);
        assertEquals(1, arrayByteList.set(0, (byte) 2));
        assertEquals(2, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        arrayByteList.set(0, (byte) 2);
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add(1, (byte) 2);
        assertEquals(1, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList._data.length);
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        arrayByteList.trimToSize();
        assertEquals(3, arrayByteList._data.length);
    }
    
}