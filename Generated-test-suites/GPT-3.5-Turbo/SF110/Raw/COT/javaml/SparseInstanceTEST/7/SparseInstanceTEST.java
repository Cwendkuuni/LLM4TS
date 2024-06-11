
import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        int expected = 5;
        int actual = instance.noAttributes();
        assertEquals(expected, actual);
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        int expected = -1;
        int actual = instance.noAttributes();
        assertEquals(expected, actual);
    }

    @Test
    public void testSparseInstance_addDefaultValue() {
        SparseInstance instance = new SparseInstance(5);
        double expected = 0.0;
        double actual = instance.get(0);
        assertEquals(expected, actual, 0.001);
    }
    
    // Potential edge case: noAttributes = 0
    @Test
    public void testSparseInstance_noAttributesZero() {
        SparseInstance instance = new SparseInstance(0);
        int expected = 0;
        int actual = instance.noAttributes();
        assertEquals(expected, actual);
    }
    
    // Potential edge case: defaultValue = 0.0
    @Test
    public void testSparseInstance_defaultValueZero() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        double expected = 0.0;
        double actual = instance.get(0);
        assertEquals(expected, actual, 0.001);
    }
    
    // Potential edge case: classValue = null
    @Test
    public void testSparseInstance_nullClassValue() {
        SparseInstance instance = new SparseInstance(5, 0.0, null);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstance_doubleArray() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        double expected = 1.0;
        double actual = instance.get(0);
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testSparseInstance_doubleArray_addDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        double expected = 1.0;
        double actual = instance.get(0);
        assertEquals(expected, actual, 0.001);
    }
    
    // Potential edge case: datavector = empty array
    @Test
    public void testSparseInstance_doubleArray_emptyDatavector() {
        double[] datavector = {};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        int expected = 0;
        int actual = instance.noAttributes();
        assertEquals(expected, actual);
    }
    
    // Potential edge case: classValue = null
    @Test
    public void testSparseInstance_doubleArray_nullClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, null);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        double expected = 1.0;
        double actual = instance.value(0);
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.clear();
        boolean expected = true;
        boolean actual = instance.isEmpty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        boolean expected = true;
        boolean actual = instance.containsKey(0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        boolean expected = true;
        boolean actual = instance.containsValue(1.0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        int expected = 1;
        int actual = instance.entrySet().size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        double expected = 1.0;
        double actual = instance.get(0);
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        boolean expected = true;
        boolean actual = instance.isEmpty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        int expected = 1;
        int actual = instance.keySet().size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        double expected = 1.0;
        double actual = instance.get(0);
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        int expected = 2;
        int actual = instance.size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.remove(0);
        boolean expected = true;
        boolean actual = instance.isEmpty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        int expected = 1;
        int actual = instance.size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        int expected = 1;
        int actual = instance.values().size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        int expected = 1;
        int actual = instance.noAttributes();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.removeAttribute(0);
        boolean expected = true;
        boolean actual = instance.isEmpty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        String expected = "{0=1.0;null}";
        String actual = instance.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        boolean expected = true;
        boolean actual = instance1.equals(instance2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = (SparseInstance) instance1.copy();
        boolean expected = true;
        boolean actual = instance1.equals(instance2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(Set.of(0, 2));
        int expected = 1;
        int actual = instance.size();
        assertEquals(expected, actual);
    }

}
