import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList arrayByteList;
    
    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList arrayByteListWithCapacity = new ArrayByteList(10);
        assertEquals(0, arrayByteListWithCapacity.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        ArrayByteList arrayByteListWithNegativeCapacity = new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ArrayByteList arrayByteListWithCollection = new ArrayByteList(new ArrayByteList());
        assertEquals(0, arrayByteListWithCollection.size());
    }
    
    @Test
    public void testGet() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        arrayByteList.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithOutOfRangeIndex() {
        arrayByteList.get(0);
    }
    
    @Test
    public void testSize() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.removeElementAt(0));
        assertEquals(0, arrayByteList.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        arrayByteList.removeElementAt(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithOutOfRangeIndex() {
        arrayByteList.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.set(0, (byte) 2));
        assertEquals(2, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        arrayByteList.set(-1, (byte) 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithOutOfRangeIndex() {
        arrayByteList.set(0, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(1, arrayByteList.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        arrayByteList.add(-1, (byte) 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithOutOfRangeIndex() {
        arrayByteList.add(1, (byte) 1);
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.trimToSize();
        assertEquals(1, arrayByteList.size());
    }
    
    @Test
    public void testSerializationAndDeserialization() throws Exception {
        arrayByteList.add(0, (byte) 1);
        
        // Serialize the object to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(arrayByteList);
        
        // Deserialize the byte array to an object
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(inputStream);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        
        assertEquals(1, deserializedList.get(0));
        assertEquals(1, deserializedList.size());
    }
}