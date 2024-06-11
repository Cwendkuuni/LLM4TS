import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    
    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testArrayByteListConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testArrayByteListConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteCollection();
        collection.add((byte) 1);
        ArrayByteList list = new ArrayByteList(collection);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.get(0));
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        byte removed = list.removeElementAt(0);
        assertEquals(5, removed);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 5);
        byte oldVal = list.set(0, (byte) 10);
        assertEquals(5, oldVal);
        assertEquals(10, list.get(0));
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        list.add(1, (byte) 2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        for (int i = 0; i < 10; i++) {
            list.add(i, (byte) i);
        }
        list.ensureCapacity(20);
        assertEquals(10, list.size());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        list.add(1, (byte) 2);
        list.trimToSize();
        assertEquals(2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeIndexGet() {
        ArrayByteList list = new ArrayByteList();
        list.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testOutOfRangeIndexGet() {
        ArrayByteList list = new ArrayByteList();
        list.get(1);
    }
}