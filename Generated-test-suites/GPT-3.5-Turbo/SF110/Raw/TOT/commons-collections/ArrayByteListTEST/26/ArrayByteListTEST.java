import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList list;
    
    @Before
    public void setUp() {
        list = new ArrayByteList();
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(list);
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructor_withInitialCapacity() {
        ArrayByteList listWithCapacity = new ArrayByteList(10);
        assertNotNull(listWithCapacity);
        assertTrue(listWithCapacity.isEmpty());
        assertEquals(0, listWithCapacity.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNegativeCapacity() {
        ArrayByteList listWithNegativeCapacity = new ArrayByteList(-5);
    }
    
    @Test
    public void testConstructor_withByteCollection() {
        ByteCollection byteCollection = new ArrayByteCollection();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        
        ArrayByteList listFromCollection = new ArrayByteList(byteCollection);
        assertNotNull(listFromCollection);
        assertFalse(listFromCollection.isEmpty());
        assertEquals(2, listFromCollection.size());
        assertEquals(1, listFromCollection.get(0));
        assertEquals(2, listFromCollection.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_IndexOutOfBounds() {
        list.get(0);
    }
    
    @Test
    public void testGet() {
        list.add((byte) 10);
        assertEquals(10, list.get(0));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, list.size());
        
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_IndexOutOfBounds() {
        list.removeElementAt(0);
    }
    
    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        
        byte removed = list.removeElementAt(0);
        assertEquals(1, removed);
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_IndexOutOfBounds() {
        list.set(0, (byte) 1);
    }
    
    @Test
    public void testSet() {
        list.add((byte) 1);
        list.add((byte) 2);
        
        byte previous = list.set(1, (byte) 10);
        assertEquals(2, previous);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(10, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_IndexOutOfBounds() {
        list.add(1, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        list.add((byte) 1);
        list.add(0, (byte) 10);
        list.add(1, (byte) 5);
        
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(5, list.get(1));
        assertEquals(1, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertEquals(8, list._data.length);
    }
    
    @Test
    public void testEnsureCapacity_Negative() {
        list.ensureCapacity(-5);
        assertEquals(8, list._data.length);
    }
    
    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        list.trimToSize();
        assertEquals(3, list._data.length);
    }
    
    @Test
    public void testSerialization() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        // Serialize and then deserialize list
        ArrayByteList deserializedList = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(list);
            out.flush();
            out.close();
            
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            deserializedList = (ArrayByteList) in.readObject();
            in.close();
        } catch (Exception e) {
            fail("Exception occurred during serialization/deserialization: " + e.getMessage());
        }
        
        // Test deserialized list
        assertNotNull(deserializedList);
        assertEquals(list.size(), deserializedList.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), deserializedList.get(i));
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange_IndexOutOfBounds() {
        list.checkRange(0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint_IndexOutOfBounds() {
        list.checkRangeIncludingEndpoint(0);
    }
    
}