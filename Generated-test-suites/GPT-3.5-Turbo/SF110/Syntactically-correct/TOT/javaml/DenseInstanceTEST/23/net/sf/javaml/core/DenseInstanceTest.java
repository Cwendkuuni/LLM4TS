import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {
    
    private DenseInstance instance;
    
    @Before
    public void setup() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
    }
    
    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> expected = new HashSet<>();
        expected.add(new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0));
        expected.add(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0));
        expected.add(new AbstractMap.SimpleEntry<Integer, Double>(2, 3.0));
        
        Set<Map.Entry<Integer, Double>> actual = instance.entrySet();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> expected = new TreeSet<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        
        SortedSet<Integer> actual = instance.keySet();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPut() {
        Double previousValue = instance.put(0, 100.0);
        assertEquals(1.0, previousValue, 0.0001);
        assertEquals(100.0, instance.get(0), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> values = new HashMap<>();
        values.put(0, 10.0);
        values.put(1, 20.0);
        values.put(2, 30.0);
        
        instance.putAll(values);
        
        assertEquals(10.0, instance.get(0), 0.0001);
        assertEquals(20.0, instance.get(1), 0.0001);
        assertEquals(30.0, instance.get(2), 0.0001);
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
        Collection<Double> expected = new ArrayList<>();
        expected.add(1.0);
        expected.add(2.0);
        expected.add(3.0);
        
        Collection<Double> actual = instance.values();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0]; null}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        int expected = Arrays.hashCode(instance.getAttributes());
        assertEquals(expected, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance otherInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.equals(otherInstance));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance)instance.copy();
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 0.0001);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        
        instance.removeAttributes(indices);
        
        double[] expected = {2.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }
}