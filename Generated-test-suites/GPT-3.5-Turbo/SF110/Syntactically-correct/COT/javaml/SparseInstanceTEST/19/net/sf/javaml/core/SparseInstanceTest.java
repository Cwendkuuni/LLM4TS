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
        assertNotNull(instance);
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        assertEquals(0.0, instance.get(0), 0.001);
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 0.0, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector, 0.0);
        assertEquals(0.0, instance.get(0), 0.001);
    }

    @Test
    public void testSparseInstanceWithDataVectorAndClassValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(dataVector, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDataVectorDefaultValueAndClassValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(dataVector, 0.0, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testInitiate() {
        SparseInstance instance = new SparseInstance();
        double[] dataVector = {1.0, 2.0, 3.0};
        instance.initiate(dataVector);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.value(0), 0.001);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(instance.entrySet().size(), 1);
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0.0, instance.get(1), 0.001);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(instance.keySet().size(), 1);
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0), 0.001);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.remove(0);
        assertNull(instance.get(0));
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(instance.values().size(), 1);
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(3.0, instance.get(1), 0.001);
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.setClassValue(3.0);
        assertEquals("{0=1.0, 1=2.0;3.0}", instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = (SparseInstance)instance1.copy();

        assertEquals(instance1.get(0), instance2.get(0));
        assertEquals(instance1.classValue(), instance2.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 2)));
        assertEquals(1, instance.noAttributes());
        assertEquals(2.0, instance.get(0), 0.001);
    }
}

