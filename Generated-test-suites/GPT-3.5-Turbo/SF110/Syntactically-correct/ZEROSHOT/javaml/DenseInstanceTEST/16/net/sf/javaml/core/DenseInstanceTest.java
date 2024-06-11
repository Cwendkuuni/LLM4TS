import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertEquals(1.0, instance.value(0), 0.0001);
        Assert.assertEquals(2.0, instance.value(1), 0.0001);
        Assert.assertEquals(3.0, instance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.clear();

        Assert.assertArrayEquals(new double[3], instance.getAttributes(), 0.0001);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Assert.assertTrue(instance.containsKey(0));
        Assert.assertTrue(instance.containsKey(1));
        Assert.assertTrue(instance.containsKey(2));
        Assert.assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertTrue(instance.containsValue(1.0));
        Assert.assertTrue(instance.containsValue(2.0));
        Assert.assertTrue(instance.containsValue(3.0));
        Assert.assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertEquals(3, instance.entrySet().size());
        
        Map.Entry<Integer, Double> entry1 = instance.entrySet().iterator().next();
        Assert.assertEquals(0, entry1.getKey().intValue());
        Assert.assertEquals(1.0, entry1.getValue(), 0.0001);
        
        Map.Entry<Integer, Double> entry2 = instance.entrySet().iterator().next();
        Assert.assertEquals(1, entry2.getKey().intValue());
        Assert.assertEquals(2.0, entry2.getValue(), 0.0001);
        
        Map.Entry<Integer, Double> entry3 = instance.entrySet().iterator().next();
        Assert.assertEquals(2, entry3.getKey().intValue());
        Assert.assertEquals(3.0, entry3.getValue(), 0.0001);
    }
    
    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertEquals(1.0, instance.get(0), 0.0001);
        Assert.assertEquals(2.0, instance.get(1), 0.0001);
        Assert.assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertEquals(3, instance.keySet().size());
        Assert.assertTrue(instance.keySet().contains(0));
        Assert.assertTrue(instance.keySet().contains(1));
        Assert.assertTrue(instance.keySet().contains(2));
        Assert.assertFalse(instance.keySet().contains(3));
    }
    
    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        instance.put(1, 4.0);
        
        Assert.assertEquals(1.0, instance.get(0), 0.0001);
        Assert.assertEquals(4.0, instance.get(1), 0.0001);
        Assert.assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        
        instance.putAll(map);
        
        Assert.assertEquals(1.0, instance.get(0), 0.0001);
        Assert.assertEquals(4.0, instance.get(1), 0.0001);
        Assert.assertEquals(5.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Double removed = instance.remove(1);
        
        Assert.assertEquals(2.0, removed, 0.0001);
        
        Assert.assertEquals(1.0, instance.get(0), 0.0001);
        Assert.assertNull(instance.get(1));
        Assert.assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        instance.removeAttribute(1);
    }
    
    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertEquals(3, instance.values().size());
        Assert.assertTrue(instance.values().contains(1.0));
        Assert.assertTrue(instance.values().contains(2.0));
        Assert.assertTrue(instance.values().contains(3.0));
    }
    
    @Test
    public void testNoAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Assert.assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.removeAttributes(new HashSet<>(Arrays.asList(1, 2)));

        Assert.assertEquals(1, instance.noAttributes());
        Assert.assertEquals(1.0, instance.get(0), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = new DenseInstance(attributes);
        
        Assert.assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        
        Assert.assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        
        DenseInstance instance2 = instance1.copy();
        
        Assert.assertEquals(instance1, instance2);
    }

}