
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class ArrayByteListTest {

    private ArrayByteList testList;

    @Before
    public void setUp() {
        testList = new ArrayByteList();
        testList.add((byte) 10);
        testList.add((byte) 20);
        testList.add((byte) 30);
    }

    @Test
    public void testGet() {
        Assert.assertEquals((byte) 10, testList.get(0));
        Assert.assertEquals((byte) 20, testList.get(1));
        Assert.assertEquals((byte) 30, testList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        testList.get(3);
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, testList.size());
        testList.removeElementAt(0);
        Assert.assertEquals(2, testList.size());
    }

    @Test
    public void testRemoveElementAt() {
        byte removedElement = testList.removeElementAt(1);
        Assert.assertEquals((byte) 20, removedElement);
        Assert.assertEquals(2, testList.size());
        Assert.assertEquals((byte) 10, testList.get(0));
        Assert.assertEquals((byte) 30, testList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtInvalidIndex() {
        testList.removeElementAt(3);
    }

    @Test
    public void testSet() {
        byte oldElement = testList.set(1, (byte) 25);
        Assert.assertEquals((byte) 20, oldElement);
        Assert.assertEquals(3, testList.size());
        Assert.assertEquals((byte) 10, testList.get(0));
        Assert.assertEquals((byte) 25, testList.get(1));
        Assert.assertEquals((byte) 30, testList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidIndex() {
        testList.set(3, (byte) 40);
    }

    @Test
    public void testAdd() {
        testList.add(1, (byte) 15);
        Assert.assertEquals(4, testList.size());
        Assert.assertEquals((byte) 10, testList.get(0));
        Assert.assertEquals((byte) 15, testList.get(1));
        Assert.assertEquals((byte) 20, testList.get(2));
        Assert.assertEquals((byte) 30, testList.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalidIndex() {
        testList.add(4, (byte) 35);
    }

    @Test
    public void testEnsureCapacity() {
        testList.ensureCapacity(10);
        Assert.assertTrue(testList.size() <= 10);
    }

    @Test
    public void testTrimToSize() {
        testList.trimToSize();
        Assert.assertEquals(testList.size(), testList.toArray().length);
    }

    @Test
    public void testSerialization() {
        ArrayByteList deserializedList = null;
        try {
            byte[] serializedData = SerializationUtils.serialize(testList);
            deserializedList = (ArrayByteList) SerializationUtils.deserialize(serializedData);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Serialization/Deserialization failed");
        }

        Assert.assertNotNull(deserializedList);
        Assert.assertTrue(Arrays.equals(testList.toArray(), deserializedList.toArray()));
        Assert.assertEquals(testList.size(), deserializedList.size());
    }
}
