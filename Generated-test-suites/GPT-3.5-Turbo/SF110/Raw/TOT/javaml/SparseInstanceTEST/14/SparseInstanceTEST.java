
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class SparseInstanceTest {

    private SparseInstance instance;
    private HashMap<Integer, Double> data;

    @Before
    public void setUp() {
        instance = new SparseInstance();
        data = new HashMap<Integer, Double>();
    }

    // Test case for setNoAttributes() method
    @Test
    public void testSetNoAttributes() {
        int noAttributes = 5;
        instance.setNoAttributes(noAttributes);
        assertEquals(noAttributes, instance.noAttributes());
    }

    // Test cases for overloaded constructors 
    @Test
    public void testSparseInstance() {
        instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        int noAttributes = 5;
        instance = new SparseInstance(noAttributes);
        assertEquals(noAttributes, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        int noAttributes = 5;
        double defaultValue = 0.0;
        instance = new SparseInstance(noAttributes, defaultValue);
        assertEquals(defaultValue, instance.value(0), 0.001);
        assertEquals(noAttributes, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        int noAttributes = 5;
        Object classValue = "class";
        instance = new SparseInstance(noAttributes, classValue);
        assertEquals(classValue, instance.classValue());
        assertEquals(noAttributes, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithAllParameters() {
        int noAttributes = 5;
        double defaultValue = 0.0;
        Object classValue = "class";
        instance = new SparseInstance(noAttributes, defaultValue, classValue);
        assertEquals(defaultValue, instance.value(0), 0.001);
        assertEquals(classValue, instance.classValue());
        assertEquals(noAttributes, instance.noAttributes());
    }

    // Test cases for overloaded constructors with datavector parameter
    @Test
    public void testSparseInstanceWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i), 0.001);
        }
    }

    @Test
    public void testSparseInstanceWithDatavectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        double defaultValue = 0.0;
        instance = new SparseInstance(datavector, defaultValue);
        assertEquals(datavector.length, instance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i), 0.001);
        }
        assertEquals(defaultValue, instance.value(datavector.length + 1), 0.001);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = "class";
        instance = new SparseInstance(datavector, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i), 0.001);
        }
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithAllParametersAndDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        double defaultValue = 0.0;
        Object classValue = "class";
        instance = new SparseInstance(datavector, defaultValue, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i), 0.001);
        }
        assertEquals(defaultValue, instance.value(datavector.length + 1), 0.001);
        assertEquals(classValue, instance.classValue());
    }

    // Test case for initiate() method
    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0, 0.0, 1.5};
        data.put(0, 1.0);
        data.put(1, 2.0);
        data.put(2, 3.0);
        data.put(4, 1.5);
        instance.initiate(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(data, instance.entrySet());
    }

    // Test case for value() method
    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance = new SparseInstance(datavector);
        assertEquals(datavector[0], instance.value(0), 0.001);
        assertEquals(datavector[1], instance.value(1), 0.001);
        assertEquals(datavector[2], instance.value(2), 0.001);
    }

    // Test case for clear() method
    @Test
    public void testClear() {
        data.put(0, 1.0);
        data.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    // Test case for containsKey() method
    @Test
    public void testContainsKey() {
        data.put(0, 1.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }

    // Test case for containsValue() method
    @Test
    public void testContainsValue() {
        data.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(2.0));
    }

    // Test case for entrySet() method
    @Test
    public void testEntrySet() {
        data.put(0, 1.0);
        data.put(1, 2.0);
        assertEquals(data.entrySet(), instance.entrySet());
    }

    // Test case for get() method
    @Test
    public void testGet() {
        data.put(0, 1.0);
        assertEquals(data.get(0), instance.get(0));
        assertEquals(data.get(1), instance.get(1));
    }

    // Test case for isEmpty() method
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        data.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }

    // Test case for keySet() method
    @Test
    public void testKeySet() {
        data.put(0, 1.0);
        data.put(1, 2.0);
        assertEquals(data.keySet(), instance.keySet());
    }

    // Test cases for put() method
    @Test
    public void testPut() {
        assertEquals(null, instance.put(0, 1.0));
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(1, instance.size());

        assertEquals(1.0, instance.put(0, 2.0), 0.001);
        assertEquals(2.0, instance.value(0), 0.001);
        assertEquals(1, instance.size());
    }

    // Test case for putAll() method
    @Test
    public void testPutAll() {
        HashMap<Integer, Double> newData = new HashMap<Integer, Double>();
        newData.put(0, 1.0);
        newData.put(1, 2.0);
        instance.putAll(newData);
        assertEquals(newData, instance.entrySet());
    }

    // Test case for remove() method
    @Test
    public void testRemove() {
        data.put(0, 1.0);
        assertEquals(1.0, instance.remove(0), 0.001);
        assertFalse(instance.containsKey(0));
    }

    // Test case for size() method
    @Test
    public void testSize() {
        assertEquals(0, instance.size());
        data.put(0, 1.0);
        assertEquals(data.size(), instance.size());
    }

    // Test case for values() method
    @Test
    public void testValues() {
        data.put(0, 1.0);
        data.put(1, 2.0);
        assertEquals(data.values(), instance.values());
    }

    // Test case for noAttributes() method
    @Test
    public void testNoAttributes() {
        assertEquals(-1, instance.noAttributes());
        data.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
        data.put(5, 2.0);
        assertEquals(6, instance.noAttributes());
    }

    // Test case for removeAttribute() method
    @Test
    public void testRemoveAttribute() {
        data.put(0, 1.0);
        data.put(1, 2.0);
        instance.removeAttribute(0);
        assertEquals(1, instance.size());
        assertFalse(instance.containsKey(0));
        assertEquals(1.0, instance.value(0), 0.001);
        instance.removeAttribute(1);
        assertEquals(0, instance.size());
    }

    // Test case for removeAttributes() method
    @Test
    public void testRemoveAttributes() {
        data.put(0, 1.0);
        data.put(1, 2.0);
        Set<Integer> indices = new HashSet<Integer>(Arrays.asList(0, 1));
        instance.removeAttributes(indices);
        assertEquals(0, instance.size());
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        data.put(0, 1.0);
        String expected = "{" + data.toString() + "; null}";
        assertEquals(expected, instance.toString());
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        long temp = Double.doubleToLongBits(0.0);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        assertEquals(result, instance.hashCode());
    }

    // Test case for equals() method
    @Test
    public void testEquals() {
        data.put(0, 1.0);
        SparseInstance otherInstance = new SparseInstance();
        assertFalse(instance.equals(otherInstance));
        otherInstance.put(0, 1.0);
        assertTrue(instance.equals(otherInstance));
    }

    // Test case for copy() method
    @Test
    public void testCopy() {
        data.put(0, 1.0);
        instance.putAll(data);
        SparseInstance copiedInstance = (SparseInstance) instance.copy();
        assertEquals(instance.entrySet(), copiedInstance.entrySet());
        assertEquals(instance.value(0), copiedInstance.value(0), 0.001);
        assertEquals(instance.classValue(), copiedInstance.classValue());
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
    }

    // Test case for compareTo() method
    // ...

    // Test case for serialize() and deserialize() methods
    // ...

}
