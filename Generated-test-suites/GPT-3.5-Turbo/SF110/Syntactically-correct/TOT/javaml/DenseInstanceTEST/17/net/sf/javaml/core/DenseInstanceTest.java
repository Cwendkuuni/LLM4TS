import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    DenseInstance denseInstance;

    @Before
    public void setUp() {
        denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
    }

    @Test
    public void testValue() {
        Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
        Assert.assertEquals(2.0, denseInstance.value(1), 0.0);
        Assert.assertEquals(3.0, denseInstance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        double[] expected = new double[]{0.0, 0.0, 0.0};
        Assert.assertArrayEquals(expected, denseInstance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        Assert.assertTrue(denseInstance.containsKey(0));
        Assert.assertTrue(denseInstance.containsKey(1));
        Assert.assertTrue(denseInstance.containsKey(2));
        Assert.assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        Assert.assertTrue(denseInstance.containsValue(1.0));
        Assert.assertTrue(denseInstance.containsValue(2.0));
        Assert.assertTrue(denseInstance.containsValue(3.0));
        Assert.assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Assert.assertEquals(3, entrySet.size());
        Map.Entry<Integer, Double> entry1 = new AbstractMap.SimpleEntry<>(0, 1.0);
        Map.Entry<Integer, Double> entry2 = new AbstractMap.SimpleEntry<>(1, 2.0);
        Map.Entry<Integer, Double> entry3 = new AbstractMap.SimpleEntry<>(2, 3.0);
        Assert.assertTrue(entrySet.contains(entry1));
        Assert.assertTrue(entrySet.contains(entry2));
        Assert.assertTrue(entrySet.contains(entry3));
    }

    @Test
    public void testGet() {
        Assert.assertEquals(1.0, denseInstance.get(0), 0.0);
        Assert.assertEquals(2.0, denseInstance.get(1), 0.0);
        Assert.assertEquals(3.0, denseInstance.get(2), 0.0);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = denseInstance.keySet();
        Assert.assertEquals(3, keySet.size());
        Assert.assertTrue(keySet.contains(0));
        Assert.assertTrue(keySet.contains(1));
        Assert.assertTrue(keySet.contains(2));
    }

    @Test
    public void testPut() {
        double oldValue = denseInstance.put(0, 4.0);
        Assert.assertEquals(1.0, oldValue, 0.0);
        Assert.assertEquals(4.0, denseInstance.attributes[0], 0.0);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        denseInstance.putAll(map);
        Assert.assertEquals(4.0, denseInstance.attributes[0], 0.0);
        Assert.assertEquals(5.0, denseInstance.attributes[1], 0.0);
        Assert.assertEquals(6.0, denseInstance.attributes[2], 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        denseInstance.remove(0);
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> values = denseInstance.values();
        Assert.assertEquals(3, values.size());
        Assert.assertTrue(values.contains(1.0));
        Assert.assertTrue(values.contains(2.0));
        Assert.assertTrue(values.contains(3.0));
    }

    @Test
    public void testNoAttributes() {
        Assert.assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0]; null}";
        Assert.assertEquals(expected, denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(0);
        double[] expected = new double[]{2.0, 3.0};
        Assert.assertArrayEquals(expected, denseInstance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(961697080, denseInstance.hashCode()); 
    }

    @Test
    public void testEquals() {
        DenseInstance anotherInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Assert.assertTrue(denseInstance.equals(anotherInstance));
    }

    @Test
    public void testCopy() {
        DenseInstance copiedInstance = denseInstance.copy();
        Assert.assertEquals(denseInstance.toString(), copiedInstance.toString());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);
        denseInstance.removeAttributes(indices);
        double[] expected = new double[]{3.0};
        Assert.assertArrayEquals(expected, denseInstance.attributes, 0.0);
    }

}