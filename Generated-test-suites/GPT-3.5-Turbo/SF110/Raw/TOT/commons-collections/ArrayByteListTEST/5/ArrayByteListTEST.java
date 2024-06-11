import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructorWithDefaultCapacity() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        byteList.add((byte)2);
        
        ArrayByteList list = new ArrayByteList(byteList);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidInitialCapacity() {
        ArrayByteList list = new ArrayByteList(-5);
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)3);
        
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        
        list.add((byte)1);
        assertEquals(1, list.size());
        
        list.add((byte)2);
        assertEquals(2, list.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)2);
        list.add((byte)3);
        
        assertEquals(2, list.removeElementAt(1));
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        
        assertEquals(1, list.set(0, (byte)2));
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte)1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }
    
    @Test
    public void testAddWithInsertion() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)3);
        list.add((byte)4);
        list.add(1, (byte)2);
        
        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte)1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte)1);
        list.add((byte)2);
        list.ensureCapacity(5);
        
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte)1);
        list.add((byte)2);
        list.add((byte)3);
        list.trimToSize();
        
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test
    public void testSerialization() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)2);
        list.add((byte)3);
        
        ArrayByteList deserializedList = null;
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(list);
            oos.close();
            
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            deserializedList = (ArrayByteList) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            fail("Serialization failed: " + e.getMessage());
        }
        
        assertNotNull(deserializedList);
        assertEquals(3, deserializedList.size());
        assertEquals(1, deserializedList.get(0));
        assertEquals(2, deserializedList.get(1));
        assertEquals(3, deserializedList.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(1);
    }
}