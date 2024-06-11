import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    
    private ArrayByteList arrayByteList;
    
    @Before
    public void setup() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testParameterizedConstructor() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(-5);
    }
    
    @Test
    public void testByteCollectionConstructor() {
        ByteCollection collection = new ByteCollection();
        collection.add((byte) 1);
        collection.add((byte) 2);
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test
    public void testGet() {
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        assertEquals(5, arrayByteList.get(0));
        assertEquals(10, arrayByteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        arrayByteList.get(0);
    }
    
    @Test
    public void testSize() {
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        assertEquals(2, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        byte removed = arrayByteList.removeElementAt(0);
        assertEquals(5, removed);
        assertEquals(1, arrayByteList.size());
        assertEquals(10, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtInvalidIndex() {
        arrayByteList.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        byte oldVal = arrayByteList.set(0, (byte) 15);
        assertEquals(5, oldVal);
        assertEquals(15, arrayByteList.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidIndex() {
        arrayByteList.set(0, (byte) 5);
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        arrayByteList.add(1, (byte) 15);
        assertEquals(3, arrayByteList.size());
        assertEquals(5, arrayByteList.get(0));
        assertEquals(15, arrayByteList.get(1));
        assertEquals(10, arrayByteList.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalidIndex() {
        arrayByteList.add(1, (byte) 5);
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
    }
    
    @Test
    public void testSerialization() {
        arrayByteList.add(0, (byte) 5);
        arrayByteList.add(1, (byte) 10);
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(arrayByteList);
            
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            ArrayByteList serializedList = (ArrayByteList) ois.readObject();
            
            assertEquals(arrayByteList.size(), serializedList.size());
            assertEquals(arrayByteList.get(0), serializedList.get(0));
            assertEquals(arrayByteList.get(1), serializedList.get(1));
        } catch (Exception e) {
            fail("Serialization error: " + e.getMessage());
        }
    }
}