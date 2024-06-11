import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        
        assertEquals(0.0, instance.value(0), 0.0);
        assertEquals(0.0, instance.value(1), 0.0);
        assertEquals(0.0, instance.value(2), 0.0);
    }
    
    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
        assertFalse(instance.containsKey(-1));
    }
    
    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
        assertFalse(instance.containsValue(-1.0));
    }
    
    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        assertEquals(3, entrySet.size());
        for (int i = 0; i < 3; i++) {
            assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(i, (double)(i + 1))));
        }
    }
    
    @Test
    public void testGet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertFalse(instance.isEmpty());
        
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        SortedSet<Integer> keySet = instance.keySet();
        
        assertEquals(3, keySet.size());
        for (int i = 0; i < 3; i++) {
            assertTrue(keySet.contains(i));
        }
    }
    
    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertEquals(1.0, instance.put(0, 4.0), 0.0);
        assertEquals(2.0, instance.put(1, 5.0), 0.0);
        assertEquals(3.0, instance.put(2, 6.0), 0.0);
        
        assertEquals(4.0, instance.value(0), 0.0);
        assertEquals(5.0, instance.value(1), 0.0);
        assertEquals(6.0, instance.value(2), 0.0);
    }
    
    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        
        instance.putAll(map);
        
        assertEquals(4.0, instance.value(0), 0.0);
        assertEquals(5.0, instance.value(1), 0.0);
        assertEquals(6.0, instance.value(2), 0.0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Collection<Double> values = instance.values();
        
        assertEquals(3, values.size());
        for (int i = 0; i < 3; i++) {
            assertTrue(values.contains((double)(i + 1)));
        }
    }
    
    @Test
    public void testNoAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        String expectedString = "{[1.0, 2.0, 3.0]; null}";
        
        assertEquals(expectedString, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        int sizeBeforeRemove = instance.size();
        
        instance.removeAttribute(1);
        
        assertEquals(sizeBeforeRemove - 1, instance.size());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(3.0, instance.value(1), 0.0);
    }
    
    @Test
    public void testHashCode() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance copy = (DenseInstance)instance.copy();
        
        assertNotSame(instance, copy);
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 0.0);
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        int sizeBeforeRemove = instance.size();
        
        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 2)));
        
        assertEquals(sizeBeforeRemove - 2, instance.size());
        assertEquals(2.0, instance.value(0), 0.0);
    }
}