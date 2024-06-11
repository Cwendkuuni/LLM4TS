import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;

public class ArrayByteListTest {

    // Test case for ArrayByteList()
    @Test
    public void testConstructor1() {
        ArrayByteList byteList = new ArrayByteList();
        Assert.assertNotNull(byteList);
        Assert.assertEquals(0, byteList.size());
    }
    
    // Test case for ArrayByteList(int initialCapacity)
    @Test
    public void testConstructor2() {
        ArrayByteList byteList = new ArrayByteList(10);
        Assert.assertNotNull(byteList);
        Assert.assertEquals(0, byteList.size());
    }
    
    // Test case for ArrayByteList(ByteCollection that)
    @Test
    public void testConstructor3() {
        ArrayByteList byteList = new ArrayByteList(new ByteCollection());
        Assert.assertNotNull(byteList);
        Assert.assertEquals(0, byteList.size());
    }
    
    // Test case for get(int index)
    @Test
    public void testGet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(10);
        byteList.add(20);
        Assert.assertEquals(10, byteList.get(0));
        Assert.assertEquals(20, byteList.get(1));
    }
    
    // Test case for size()
    @Test
    public void testSize() {
        ArrayByteList byteList = new ArrayByteList();
        Assert.assertEquals(0, byteList.size());
        byteList.add(10);
        Assert.assertEquals(1, byteList.size());
        byteList.add(20);
        Assert.assertEquals(2, byteList.size());
    }
    
    // Test case for removeElementAt(int index)
    @Test
    public void testRemoveElementAt() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(10);
        byteList.add(20);
        byteList.add(30);
        byteList.add(40);
        
        byte removed = byteList.removeElementAt(2);
        
        Assert.assertEquals(30, removed);
        Assert.assertEquals(3, byteList.size());
        Assert.assertEquals(10, byteList.get(0));
        Assert.assertEquals(20, byteList.get(1));
        Assert.assertEquals(40, byteList.get(2));
    }
    
    // Test case for set(int index, byte element)
    @Test
    public void testSet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(10);
        
        byte oldValue = byteList.set(0, 20);
        
        Assert.assertEquals(10, oldValue);
        Assert.assertEquals(1, byteList.size());
        Assert.assertEquals(20, byteList.get(0));
    }
    
    // Test case for add(int index, byte element)
    @Test
    public void testAdd() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(10);
        byteList.add(20);
        
        byteList.add(1, 15);
        
        Assert.assertEquals(3, byteList.size());
        Assert.assertEquals(10, byteList.get(0));
        Assert.assertEquals(15, byteList.get(1));
        Assert.assertEquals(20, byteList.get(2));
    }
    
    // Test case for ensureCapacity(int mincap)
    @Test
    public void testEnsureCapacity() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.ensureCapacity(3);
        
        Assert.assertEquals(3, byteList._data.length);
    }
    
    // Test case for trimToSize()
    @Test
    public void testTrimToSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(10);
        byteList.add(20);
        byteList.ensureCapacity(5);
        
        byteList.trimToSize();
        
        Assert.assertEquals(2, byteList._data.length);
    }
    
    // Test case for serialization and deserialization
    @Test
    public void testSerializationDeserialization() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(10);
        byteList.add(20);
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(byteList);

            byte[] serializedBytes = baos.toByteArray();

            ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            ArrayByteList deserializedList = (ArrayByteList) ois.readObject();

            Assert.assertEquals(byteList.size(), deserializedList.size());
            Assert.assertArrayEquals(byteList._data, deserializedList._data);
        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("Exception occurred during serialization/deserialization: " + e.getMessage());
        }
    }
    
    // Test case for checkRange(int index)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange1() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.checkRange(2);
    }
    
    // Test case for checkRangeIncludingEndpoint(int index)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange2() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.checkRangeIncludingEndpoint(2);
    }
}