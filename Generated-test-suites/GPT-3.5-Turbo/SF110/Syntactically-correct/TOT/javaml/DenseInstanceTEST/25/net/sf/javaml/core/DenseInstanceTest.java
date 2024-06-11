import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    private DenseInstance denseInstance;
    
    @Before
    public void setUp() {
        denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
    }
    
    @Test
    public void testConstructor1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.getAttributes(), 0.001);
    }
    
    @Test
    public void testConstructor2() {
        double[] att = {4.0, 5.0, 6.0};
        Object classValue = "Class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.getAttributes(), 0.001);
        assertEquals(classValue, instance.getClassValue());
    }
    
    @Test
    public void testValue() {
        assertEquals(2.0, denseInstance.value(1), 0.001);
    }
    
    @Test
    public void testClear() {
        denseInstance.clear();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, denseInstance.getAttributes(), 0.001);
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(2.0));
        assertFalse(denseInstance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        assertEquals(3, denseInstance.entrySet().size());
        assertTrue(denseInstance.entrySet().contains(0, 1.0));
        assertFalse(denseInstance.entrySet().contains(3, 4.0));
    }
    
    @Test
    public void testGet() {
        assertEquals(1.0, denseInstance.get(0), 0.001);
        assertNull(denseInstance.get(3));
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        assertEquals(3, denseInstance.keySet().size());
        assertTrue(denseInstance.keySet().contains(0));
        assertFalse(denseInstance.keySet().contains(3));
    }
    
    @Test
    public void testPut() {
        assertEquals(1.0, denseInstance.put(0, 4.0), 0.001);
        assertEquals(4.0, denseInstance.value(0), 0.001);
    }
    
    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(3);
        instance.put(0, 4.0);
        instance.put(1, 5.0);
        instance.put(2, 6.0);
        
        denseInstance.putAll(instance);
        assertEquals(4.0, denseInstance.value(0), 0.001);
        assertEquals(5.0, denseInstance.value(1), 0.001);
        assertEquals(6.0, denseInstance.value(2), 0.001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        denseInstance.remove(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(3, denseInstance.size());
    }
    
    @Test
    public void testValues() {
        assertEquals(3, denseInstance.values().size());
        assertTrue(denseInstance.values().contains(1.0));
        assertFalse(denseInstance.values().contains(4.0));
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, denseInstance.noAttributes());
    }
    
    @Test
    public void testToString() {
        assertEquals("{[1.0, 2.0, 3.0];null}", denseInstance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        assertEquals("{[1.0, 3.0];null}", denseInstance.toString());
    }
    
    @Test
    public void testHashCode() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(denseInstance.hashCode(), instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(denseInstance.equals(instance));
        
        DenseInstance differentInstance = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        assertFalse(denseInstance.equals(differentInstance));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = denseInstance.copy();
        assertArrayEquals(denseInstance.getAttributes(), copy.getAttributes(), 0.001);
        assertEquals(denseInstance.getClassValue(), copy.getClassValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{0.0, 1.0, 2.0});
        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 1)));
        assertEquals("{[2.0];null}", instance.toString());
    }
}