import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.001);
    }
    
    @Test
    public void testClear() {
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.001);
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> expected = new HashSet<Map.Entry<Integer, Double>>();
        expected.add(new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0));
        expected.add(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0));
        expected.add(new AbstractMap.SimpleEntry<Integer, Double>(2, 3.0));
        
        assertEquals(expected, instance.entrySet());
    }
    
    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> expected = new TreeSet<Integer>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        
        assertEquals(expected, instance.keySet());
    }
    
    @Test
    public void testPut() {
        assertEquals(1.0, instance.put(0, 0.5), 0.001);
        assertEquals(0.5, instance.get(0), 0.001);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 0.5);
        map.put(1, 1.5);
        map.put(2, 2.5);
        
        instance.putAll(map);
        
        assertEquals(0.5, instance.get(0), 0.001);
        assertEquals(1.5, instance.get(1), 0.001);
        assertEquals(2.5, instance.get(2), 0.001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> expected = new ArrayList<Double>();
        expected.add(1.0);
        expected.add(2.0);
        expected.add(3.0);
        
        assertEquals(expected, instance.values());
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        DenseInstance other = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance other = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 0.001);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        
        instance.removeAttributes(indices);
        
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.001);
    }
}