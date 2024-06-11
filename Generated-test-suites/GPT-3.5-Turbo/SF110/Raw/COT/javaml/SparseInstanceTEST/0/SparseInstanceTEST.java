import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {
    
    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        int expected = 5;
        int actual = instance.noAttributes;
        assertEquals("SetNoAttributes Test", expected, actual);
    }
    
    @Test
    public void testSparseInstance1() {
        SparseInstance instance = new SparseInstance();
        int expected = -1;
        int actual = instance.noAttributes;
        assertEquals("SparseInstance1 Test", expected, actual);
    }
    
    @Test
    public void testSparseInstance2() {
        SparseInstance instance = new SparseInstance(10);
        int expected = 10;
        int actual = instance.noAttributes;
        assertEquals("SparseInstance2 Test", expected, actual);
    }
    
    @Test
    public void testSparseInstance3() {
        SparseInstance instance = new SparseInstance(10, 0.5);
        double expected = 0.5;
        double actual = instance.defaultValue;
        assertEquals("SparseInstance3 Test", expected, actual, 0.001);
    }
    
    @Test
    public void testSparseInstance4() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(10, 0.5, classValue);
        Object expected = classValue;
        Object actual = instance.classValue();
        assertEquals("SparseInstance4 Test", expected, actual);
    }
    
    @Test
    public void testSparseInstance5() {
        double[] dataVector = {1.0, 2.0, 3.0, 0.5};
        SparseInstance instance = new SparseInstance(dataVector);
        int expected = dataVector.length;
        int actual = instance.noAttributes;
        assertEquals("SparseInstance5 Test", expected, actual);
    }
    
    @Test
    public void testSparseInstance6() {
        double[] dataVector = {1.0, 2.0, 3.0, 0.5};
        double defaultValue = 0.5;
        SparseInstance instance = new SparseInstance(dataVector, defaultValue);
        double[] expected = dataVector;
        double[] actual = new double[instance.noAttributes];
        for (int i = 0; i < instance.noAttributes; i++) {
            actual[i] = instance.value(i);
        }
        assertArrayEquals("SparseInstance6 Test", expected, actual, 0.001);
    }
    
    @Test
    public void testSparseInstance7() {
        double[] dataVector = {1.0, 2.0, 3.0, 0.5};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(dataVector, classValue);
        Object expected = classValue;
        Object actual = instance.classValue();
        assertEquals("SparseInstance7 Test", expected, actual);
    }
    
    @Test
    public void testSparseInstance8() {
        double[] dataVector = {1.0, 2.0, 3.0, 0.5};
        double defaultValue = 0.5;
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(dataVector, defaultValue, classValue);
        double[] expected = dataVector;
        double[] actual = new double[instance.noAttributes];
        for (int i = 0; i < instance.noAttributes; i++) {
            actual[i] = instance.value(i);
        }
        assertArrayEquals("SparseInstance8 Test", expected, actual, 0.001);
    }
    
    @Test
    public void testValue() {
        double[] dataVector = {1.0, 2.0, 3.0, 0.5};
        SparseInstance instance = new SparseInstance(dataVector);
        double expected = 2.0;
        double actual = instance.value(1);
        assertEquals("Value Test", expected, actual, 0.001);
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 0.5);
        instance.clear();
        boolean expected = true;
        boolean actual = instance.isEmpty();
        assertEquals("Clear Test", expected, actual);
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        boolean expected = true;
        boolean actual = instance.containsKey(1);
        assertEquals("ContainsKey Test", expected, actual);
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        boolean expected = true;
        boolean actual = instance.containsValue(2.0);
        assertEquals("ContainsValue Test", expected, actual);
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Set<Map.Entry<Integer, Double>> expected = instance.data.entrySet();
        Set<Map.Entry<Integer, Double>> actual = instance.entrySet();
        assertEquals("EntrySet Test", expected, actual);
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Double expected = 2.0;
        Double actual = instance.get(1);
        assertEquals("Get Test", expected, actual);
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        boolean expected = true;
        boolean actual = instance.isEmpty();
        assertEquals("IsEmpty Test", expected, actual);
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        TreeSet<Integer> expected = new TreeSet<Integer>(instance.data.keySet());
        TreeSet<Integer> actual = instance.keySet();
        assertEquals("KeySet Test", expected, actual);
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        Double expected = 1.0;
        Double actual = instance.put(0, 1.0);
        assertEquals("Put Test", expected, actual);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        map.put(2, 3.0);
        instance.putAll(map);
        Double expected = 2.0;
        Double actual = instance.get(1);
        assertEquals("PutAll Test", expected, actual);
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Double expected = 2.0;
        Double actual = instance.remove(1);
        assertEquals("Remove Test", expected, actual);
    }
    
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        int expected = instance.data.size();
        int actual = instance.size();
        assertEquals("Size Test", expected, actual);
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Collection<Double> expected = instance.data.values();
        Collection<Double> actual = instance.values();
        assertEquals("Values Test", expected, actual);
    }
    
    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        int expected = instance.noAttributes;
        int actual = instance.noAttributes();
        assertEquals("NoAttributes Test", expected, actual);
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        int expected = 2;
        int actual = instance.noAttributes;
        assertEquals("RemoveAttribute Test", expected, actual);
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        String expected = "{0=1.0;1=2.0;2=3.0;null}";
        String actual = instance.toString();
        assertEquals("ToString Test", expected, actual);
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        int expected = instance.data.hashCode();
        int actual = instance.hashCode();
        assertEquals("HashCode Test", expected, actual);
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        instance1.put(2, 3.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);
        instance2.put(2, 3.0);
        boolean expected = true;
        boolean actual = instance1.equals(instance2);
        assertEquals("Equals Test", expected, actual);
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Instance copyInstance = instance.copy();
        boolean expected = true;
        boolean actual = instance.equals(copyInstance);
        assertEquals("Copy Test", expected, actual);
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);
        instance.removeAttributes(indices);
        int expected = 1;
        int actual = instance.noAttributes;
        assertEquals("RemoveAttributes Test", expected, actual);
    }
    
}