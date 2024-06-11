import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.value(0), 0.00001);
        assertEquals(2.0, instance.value(1), 0.00001);
        assertEquals(3.0, instance.value(2), 0.00001);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        assertArrayEquals(new double[3], instance.attributes, 0.00001);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        assertEquals(3, entrySet.size());
        
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            
            assertTrue(key >= 0 && key < att.length);
            assertEquals(att[key], value, 0.00001);
        }
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.get(0), 0.00001);
        assertEquals(2.0, instance.get(1), 0.00001);
        assertEquals(3.0, instance.get(2), 0.00001);
    }
    
    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        SortedSet<Integer> keySet = instance.keySet();
        
        assertEquals(3, keySet.size());
        
        int index = 0;
        for (int key : keySet) {
            assertEquals(index++, key);
        }
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Double previousValue = instance.put(1, 4.0);
        
        assertEquals(2.0, previousValue, 0.00001);
        assertEquals(4.0, instance.get(1), 0.00001);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        
        instance.putAll(map);
        
        assertEquals(4.0, instance.get(1), 0.00001);
        assertEquals(5.0, instance.get(2), 0.00001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.remove(1);
    }
    
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Collection<Double> values = instance.values();
        
        assertEquals(3, values.size());
        
        int index = 1;
        for (double value : values) {
            assertEquals(index++, value, 0.00001);
        }
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        String expected = "{[1.0, 2.0, 3.0];null}";
        String actual = instance.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(1);
        
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.00001);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        int expectedHashCode = Arrays.hashCode(att);
        int actualHashCode = instance.hashCode();
        
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testEquals() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Instance copy = instance.copy();
        
        assertNotSame(instance, copy);
        assertArrayEquals(att, ((DenseInstance)copy).attributes, 0.00001);
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        
        instance.removeAttributes(indices);
        
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.00001);
    }

}