import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList list;
    
    @Before
    public void setUp() {
        list = new ArrayByteList();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        list = new ArrayByteList(-10);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte) 10);
        byteList.add((byte) 20);
        list = new ArrayByteList(byteList);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }
    
    @Test
    public void testGet() {
        list.add((byte) 10);
        list.add((byte) 20);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        list.get(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(0, list.size());
        list.add((byte) 10);
        assertEquals(1, list.size());
        list.add((byte) 20);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        list.add((byte) 10);
        list.add((byte) 20);
        list.add((byte) 30);
        byte removedElement = list.removeElementAt(1);
        assertEquals(3, list.size());
        assertEquals(20, removedElement);
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        list.add((byte) 10);
        list.add((byte) 20);
        byte oldValue = list.set(1, (byte) 30);
        assertEquals(2, list.size());
        assertEquals(20, oldValue);
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        list.set(0, (byte) 10);
    }
    
    @Test
    public void testAdd() {
        list.add(0, (byte) 10);
        assertEquals(1, list.size());
        assertEquals(10, list.get(0));
        list.add(1, (byte) 20);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        list.add(1, (byte) 10);
    }
    
    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertEquals(0, list.size());
        list.add((byte) 10);
        list.ensureCapacity(50);
        assertTrue(list.size() <= 50);
        assertEquals(10, list.get(0));
    }
    
    @Test
    public void testTrimToSize() {
        list.add((byte) 10);
        list.trimToSize();
        assertEquals(1, list.size());
        byte[] expectedData = {10};
        assertArrayEquals(expectedData, list.toArray());
    }
    
    @Test
    public void testSerialization() {
        list.add((byte) 10);
        list.add((byte) 20);
        
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("test.ser"));
            out.writeObject(list);
            out.close();
            
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("test.ser"));
            list = (ArrayByteList) in.readObject();
            in.close();
            
            assertEquals(2, list.size());
            assertEquals(10, list.get(0));
            assertEquals(20, list.get(1));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithInvalidIndex() {
        list.checkRange(0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        list.checkRangeIncludingEndpoint(0);
    }
}