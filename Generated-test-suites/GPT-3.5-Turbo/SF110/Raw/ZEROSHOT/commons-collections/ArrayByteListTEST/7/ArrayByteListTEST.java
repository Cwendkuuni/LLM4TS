import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    
    private ArrayByteList list;
    
    @Before
    public void setUp() {
        list = new ArrayByteList();
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructor2() {
        ArrayByteList newList = new ArrayByteList(10);
        assertEquals(0, newList.size());
    }
    
    @Test
    public void testConstructor3() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        ArrayByteList newList = new ArrayByteList(byteCollection);
        assertEquals(2, newList.size());
        assertEquals((byte) 1, newList.get(0));
        assertEquals((byte) 2, newList.get(1));
    }
    
    @Test
    public void testAddAndGet() {
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }
    
    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        byte removed = list.removeElementAt(0);
        assertEquals(1, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 1, removed);
    }
    
    @Test
    public void testSet() {
        list.add((byte) 1);
        byte oldval = list.set(0, (byte) 2);
        assertEquals(1, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 1, oldval);
    }
    
    @Test
    public void testAddIndex() {
        list.add((byte) 1);
        list.add((byte) 3);
        list.add(1, (byte) 2);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }
    
    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(5);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.trimToSize();
        assertEquals(3, list.size());
    }
    
}