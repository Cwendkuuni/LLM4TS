
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

public class ArrayByteListTest {
    
    @Test
    public void testConstructorWithoutArguments() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 5;
        ArrayByteList list = new ArrayByteList(initialCapacity);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte)1);
        collection.add((byte)2);
        
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)2);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        
        list.add((byte)1);
        list.add((byte)2);
        assertEquals(2, list.size());
        
        list.removeElementAt(0);
        assertEquals(1, list.size());
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
    public void testRemoveElementAtInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)2);
        
        assertEquals(2, list.set(0, (byte)3));
        assertEquals(3, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte)1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)3);
        
        list.add(1, (byte)2);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalidIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte)1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(5);
        
        assertEquals(0, list.size());
        assertEquals(8, list._data.length);
        
        for (int i = 0; i < 8; i++) {
            list.add((byte)i);
        }
        list.ensureCapacity(10);
        
        assertEquals(8, list.size());
        assertEquals(15, list._data.length);
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        
        assertEquals(0, list.size());
        assertEquals(15, list._data.length);
        
        for (int i = 0; i < 8; i++) {
            list.add((byte)i);
        }
        list.trimToSize();
        
        assertEquals(8, list.size());
        assertEquals(8, list._data.length);
    }
    
    @Test
    public void testWriteObject() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte)1);
        list.add((byte)2);
        
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteStream);
            
            out.writeObject(list);
            
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteStream.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteInStream);
            
            ArrayByteList newList = (ArrayByteList) in.readObject();
            assertEquals(2, newList.size());
            assertEquals(1, newList.get(0));
            assertEquals(2, newList.get(1));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        
        try {
            list.checkRange(-1);
            assertFalse(true);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        
        try {
            list.checkRange(0);
            assertFalse(true);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        
        list.add((byte)1);
        
        try {
            list.checkRange(1);
            assertFalse(true);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        
        try {
            list.checkRange(0);
            assertTrue(true);
        } catch (IndexOutOfBoundsException e) {
            assertFalse(true);
        }
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        
        try {
            list.checkRangeIncludingEndpoint(-1);
            assertFalse(true);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        
        try {
            list.checkRangeIncludingEndpoint(1);
            assertFalse(true);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        
        list.add((byte)1);
        
        try {
            list.checkRangeIncludingEndpoint(1);
            assertTrue(true);
        } catch (IndexOutOfBoundsException e) {
            assertFalse(true);
        }
    }
}

