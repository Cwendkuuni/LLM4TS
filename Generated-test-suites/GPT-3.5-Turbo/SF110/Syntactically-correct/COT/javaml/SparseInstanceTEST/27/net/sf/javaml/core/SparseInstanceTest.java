
import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertEquals(1.0, instance.get(0), 0.01);
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithAllParameters() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.01);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDoubleArray() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.01);
        assertEquals(2.0, instance.get(1), 0.01);
        assertEquals(3.0, instance.get(2), 0.01);
    }

    @Test
    public void testSparseInstanceWithDoubleArrayAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        assertEquals(0.0, instance.get(3), 0.01);
    }

    @Test
    public void testSparseInstanceWithDoubleArrayAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithAllParametersUsingDoubleArray() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.0, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.01);
        assertEquals(2.0, instance.get(1), 0.01);
        assertEquals(3.0, instance.get(2), 0.01);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(1.0, instance.value(0), 0.01);
    }

    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(2.0, instance.get(1), 0.01);
        assertEquals(0.0, instance.get(3), 0.01);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.keySet().size());
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.01);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0), 0.01);
        assertEquals(2.0, instance.get(1), 0.01);
    }

    @Test
    public void testRemove() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        instance.remove(1);
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testSize() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.values().size());
    }

    @Test
    public void testNoAttributes() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        instance.removeAttribute(1);
        assertFalse(instance.containsKey(1));
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.0, classValue);
        assertEquals("{0=1.0, 1=2.0, 2=3.0;" + classValue.toString() + "}", instance.toString());
    }

    @Test
    public void testHashCode() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance1 = new SparseInstance(datavector, 0.0, classValue);
        SparseInstance instance2 = new SparseInstance(datavector, 0.0, classValue);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance1 = new SparseInstance(datavector, 0.0, classValue);
        SparseInstance instance2 = new SparseInstance(datavector, 0.0, classValue);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.0, classValue);
        Instance copy = instance.copy();
        assertTrue(copy instanceof SparseInstance);
        assertEquals(instance.get(0), copy.get(0));
        assertEquals(instance.get(1), copy.get(1));
        assertEquals(instance.get(2), copy.get(2));
        assertEquals(instance.classValue(), copy.classValue());
        assertEquals(instance.noAttributes(), copy.noAttributes());
    }

    @Test
    public void testRemoveAttributes() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        HashSet<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(2);
        instance.removeAttributes(indices);
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertEquals(1, instance.size());
    }

}
