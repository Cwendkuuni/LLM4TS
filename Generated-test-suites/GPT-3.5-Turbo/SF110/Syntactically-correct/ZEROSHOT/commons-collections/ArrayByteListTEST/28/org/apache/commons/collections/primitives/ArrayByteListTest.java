import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testGetSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }
    
    @Test
    public void testAddElement() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        assertEquals(1, list.size());
        assertEquals((byte)1, list.get(0));
    }
    
    @Test
    public void testRemoveElement() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.add(1, (byte)2);
        list.add(2, (byte)3);
        assertEquals(3, list.size());
        
        byte removedElement = list.removeElementAt(1);
        assertEquals(2, list.size());
        assertEquals((byte)2, removedElement);
        assertEquals((byte)1, list.get(0));
        assertEquals((byte)3, list.get(1));
    }
    
    @Test
    public void testSetElement() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.add(1, (byte)2);
        list.add(2, (byte)3);
        assertEquals(3, list.size());
        
        byte replacedElement = list.set(1, (byte)4);
        assertEquals((byte)2, replacedElement);
        assertEquals((byte)1, list.get(0));
        assertEquals((byte)4, list.get(1));
        assertEquals((byte)3, list.get(2));
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(0, list.size());
        assertTrue(list._data.length >= 10);
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        list.trimToSize();
        assertEquals(0, list.size());
        assertEquals(0, list._data.length);
    }
    
    @Test
    public void testSerialization() throws Exception {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte)1);
        list.add(1, (byte)2);
        list.add(2, (byte)3);
        assertEquals(3, list.size());
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        out.close();
        
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        in.close();
        
        assertEquals(3, deserializedList.size());
        assertEquals((byte)1, deserializedList.get(0));
        assertEquals((byte)2, deserializedList.get(1));
        assertEquals((byte)3, deserializedList.get(2));
    }
}