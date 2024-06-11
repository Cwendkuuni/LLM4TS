import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class ArrayByteListTest {
    
    private ArrayByteList arrayByteList;
    
    @Before
    public void setup() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testConstructorWithNoArguments() {
        Assert.assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        arrayByteList = new ArrayByteList(10);
        Assert.assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        arrayByteList = new ArrayByteList(byteCollection);
        Assert.assertEquals(1, arrayByteList.size());
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
    }
    
    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
        Assert.assertEquals((byte) 2, arrayByteList.get(1));
        Assert.assertEquals((byte) 3, arrayByteList.get(2));
    }
    
    @Test
    public void testSize() {
        Assert.assertEquals(0, arrayByteList.size());
        arrayByteList.add((byte) 1);
        Assert.assertEquals(1, arrayByteList.size());
        arrayByteList.add((byte) 2);
        Assert.assertEquals(2, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        Assert.assertEquals((byte) 2, arrayByteList.removeElementAt(1));
        Assert.assertEquals(2, arrayByteList.size());
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
        Assert.assertEquals((byte) 3, arrayByteList.get(1));
    }
    
    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        Assert.assertEquals((byte) 2, arrayByteList.set(1, (byte) 4));
        Assert.assertEquals(3, arrayByteList.size());
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
        Assert.assertEquals((byte) 4, arrayByteList.get(1));
        Assert.assertEquals((byte) 3, arrayByteList.get(2));
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 3);
        arrayByteList.add(1, (byte) 2);
        Assert.assertEquals(3, arrayByteList.size());
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
        Assert.assertEquals((byte) 2, arrayByteList.get(1));
        Assert.assertEquals((byte) 3, arrayByteList.get(2));
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        Assert.assertEquals(0, arrayByteList.size());
        arrayByteList = new ArrayByteList(5);
        arrayByteList.ensureCapacity(10);
        Assert.assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.ensureCapacity(10);
        arrayByteList.trimToSize();
        Assert.assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testSerialization() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(arrayByteList);
            
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
            arrayByteList = (ArrayByteList) in.readObject();
            
            Assert.assertEquals(3, arrayByteList.size());
            Assert.assertEquals((byte) 1, arrayByteList.get(0));
            Assert.assertEquals((byte) 2, arrayByteList.get(1));
            Assert.assertEquals((byte) 3, arrayByteList.get(2));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        arrayByteList = new ArrayByteList(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        arrayByteList.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexGreaterThanSize() {
        arrayByteList.get(1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        arrayByteList.removeElementAt(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexGreaterThanSize() {
        arrayByteList.removeElementAt(1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        arrayByteList.set(-1, (byte) 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexGreaterThanSize() {
        arrayByteList.set(1, (byte) 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        arrayByteList.add(-1, (byte) 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexGreaterThanSize() {
        arrayByteList.add(1, (byte) 1);
    }
}