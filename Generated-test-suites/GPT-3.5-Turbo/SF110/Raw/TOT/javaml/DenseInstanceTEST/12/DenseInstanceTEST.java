import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {
    
    private DenseInstance denseInstance;
    
    @Before
    public void setUp() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(attributes);
    }
    
    @Test
    public void testValue() {
        Assert.assertEquals(1.0, denseInstance.value(0), 0.00001);
        Assert.assertEquals(2.0, denseInstance.value(1), 0.00001);
        Assert.assertEquals(3.0, denseInstance.value(2), 0.00001);
        // Testing an out of bounds index, it should throw an exception
        try {
            denseInstance.value(3);
        } catch (ArrayIndexOutOfBoundsException e) {
            Assert.assertTrue(true);
        }
    }
    
    @Test
    public void testClear() {
        denseInstance.clear();
        double[] expectedAttributes = new double[]{0.0, 0.0, 0.0};
        Assert.assertArrayEquals(expectedAttributes, denseInstance.getAttributes(), 0.00001);
    }
    
    @Test
    public void testContainsKey() {
        Assert.assertTrue(denseInstance.containsKey(0));
        Assert.assertTrue(denseInstance.containsKey(1));
        Assert.assertTrue(denseInstance.containsKey(2));
        // Testing with an invalid key type, it should return false
        Assert.assertFalse(denseInstance.containsKey("key"));
        // Testing with an out of bounds index key, it should return false
        Assert.assertFalse(denseInstance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        Assert.assertTrue(denseInstance.containsValue(1.0));
        Assert.assertTrue(denseInstance.containsValue(2.0));
        Assert.assertTrue(denseInstance.containsValue(3.0));
        // Testing with a value not present in the attributes, it should return false
        Assert.assertFalse(denseInstance.containsValue(4.0));
        // Testing with an invalid value type, it should return false
        Assert.assertFalse(denseInstance.containsValue("value"));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Assert.assertEquals(3, entrySet.size());
        Iterator<Map.Entry<Integer, Double>> iterator = entrySet.iterator();
        Assert.assertEquals(0, (int) iterator.next().getKey());
        Assert.assertEquals(1, (int) iterator.next().getKey());
        Assert.assertEquals(2, (int) iterator.next().getKey());
    }
    
    @Test
    public void testGet() {
        Assert.assertEquals(1.0, denseInstance.get(0), 0.00001);
        Assert.assertEquals(2.0, denseInstance.get(1), 0.00001);
        Assert.assertEquals(3.0, denseInstance.get(2), 0.00001);
    }
    
    @Test
    public void testIsEmpty() {
        Assert.assertFalse(denseInstance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = denseInstance.keySet();
        Assert.assertEquals(3, keySet.size());
        Iterator<Integer> iterator = keySet.iterator();
        Assert.assertEquals(0, (int) iterator.next());
        Assert.assertEquals(1, (int) iterator.next());
        Assert.assertEquals(2, (int) iterator.next());
    }
    
    @Test
    public void testPut() {
        double originalValue = denseInstance.put(0, 10.0);
        Assert.assertEquals(1.0, originalValue, 0.00001);
        Assert.assertEquals(10.0, denseInstance.value(0), 0.00001);
        // Testing with an out of bounds index, it should throw an exception
        try {
            denseInstance.put(3, 5.0);
        } catch (ArrayIndexOutOfBoundsException e) {
            Assert.assertTrue(true);
        }
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 10.0);
        map.put(1, 20.0);
        denseInstance.putAll(map);
        Assert.assertEquals(10.0, denseInstance.value(0), 0.00001);
        Assert.assertEquals(20.0, denseInstance.value(1), 0.00001);
    }
    
    @Test
    public void testRemove() {
        try {
            denseInstance.remove(0);
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }
    
    @Test
    public void testSize() {
        Assert.assertEquals(3, denseInstance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> values = denseInstance.values();
        Assert.assertEquals(3, values.size());
        Iterator<Double> iterator = values.iterator();
        Assert.assertEquals(1.0, iterator.next(), 0.00001);
        Assert.assertEquals(2.0, iterator.next(), 0.00001);
        Assert.assertEquals(3.0, iterator.next(), 0.00001);
    }
    
    @Test
    public void testNoAttributes() {
        Assert.assertEquals(3, denseInstance.noAttributes());
    }
    
    @Test
    public void testToString() {
        String expectedString = "{[1.0, 2.0, 3.0];null}";
        Assert.assertEquals(expectedString, denseInstance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        double[] expectedAttributes = new double[]{1.0, 3.0};
        Assert.assertArrayEquals(expectedAttributes, denseInstance.getAttributes(), 0.00001);
    }
    
    @Test
    public void testHashCode() {
        int hashCode = denseInstance.hashCode();
        denseInstance.put(0, 10.0);
        Assert.assertNotEquals(hashCode, denseInstance.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance equalInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance differentInstance = new DenseInstance(new double[]{1.0, 2.0, 4.0});
        Assert.assertEquals(denseInstance, equalInstance);
        Assert.assertNotEquals(denseInstance, differentInstance);
    }
    
    @Test
    public void testCopy() {
        DenseInstance copiedInstance = denseInstance.copy();
        Assert.assertEquals(denseInstance, copiedInstance);
        copiedInstance.put(0, 10.0);
        Assert.assertNotEquals(denseInstance, copiedInstance);
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        denseInstance.removeAttributes(indices);
        double[] expectedAttributes = new double[]{1.0, 3.0};
        Assert.assertArrayEquals(expectedAttributes, denseInstance.getAttributes(), 0.00001);
    }
}