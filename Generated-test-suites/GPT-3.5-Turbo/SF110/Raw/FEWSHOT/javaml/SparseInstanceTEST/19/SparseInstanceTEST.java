import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {
    
    private SparseInstance sparseInstance;
    private double[] dataVector;
    
    @Before
    public void setup() {
        sparseInstance = new SparseInstance();
        dataVector = new double[] {1.0, 2.0, 3.0};
    }
    
    @Test
    public void testSetNoAttributes() {
        sparseInstance.setNoAttributes(3);
        int noAttributes = sparseInstance.noAttributes();
        
        assertEquals(3, noAttributes);
    }
    
    @Test
    public void testSparseInstanceConstructorWithNoAttributes() {
        SparseInstance instance = new SparseInstance(3);
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstanceConstructorWithDefaultValue() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstanceConstructorWithClassValue() {
        Object classValue = mock(Object.class);
        
        SparseInstance instance = new SparseInstance(3, classValue);
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testSparseInstanceConstructorWithAllParameters() {
        Object classValue = mock(Object.class);
        
        SparseInstance instance = new SparseInstance(3, 1.0, classValue);
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testSparseInstanceConstructorWithDataVector() {
        SparseInstance instance = new SparseInstance(dataVector);
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
        assertEquals(dataVector[1], instance.value(1), 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstanceConstructorWithDataVectorAndDefaultValue() {
        SparseInstance instance = new SparseInstance(dataVector, 1.0);
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
        assertEquals(dataVector[1], instance.value(1), 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstanceConstructorWithDataVectorAndClassValue() {
        Object classValue = mock(Object.class);
        
        SparseInstance instance = new SparseInstance(dataVector, classValue);
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
        assertEquals(dataVector[1], instance.value(1), 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testSparseInstanceConstructorWithDataVectorAndAllParameters() {
        Object classValue = mock(Object.class);
        
        SparseInstance instance = new SparseInstance(dataVector, 1.0, classValue);
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
        assertEquals(dataVector[1], instance.value(1), 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testInitiate() {
        sparseInstance.initiate(dataVector);
        int noAttributes = sparseInstance.noAttributes();
        Set<Integer> keySet = sparseInstance.keySet();
        
        assertEquals(3, noAttributes);
        assertEquals(3, keySet.size());
        assertEquals(dataVector[0], sparseInstance.get(0), 0.001);
        assertEquals(dataVector[1], sparseInstance.get(1), 0.001);
        assertEquals(dataVector[2], sparseInstance.get(2), 0.001);
    }
    
    @Test
    public void testValue() {
        sparseInstance.initiate(dataVector);
        double value = sparseInstance.value(1);
        
        assertEquals(dataVector[1], value, 0.001);
    }
    
    @Test
    public void testClear() {
        sparseInstance.initiate(dataVector);
        sparseInstance.clear();
        
        assertEquals(0, sparseInstance.size());
        assertTrue(sparseInstance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        sparseInstance.initiate(dataVector);
        boolean containsKey = sparseInstance.containsKey(1);
        
        assertTrue(containsKey);
    }
    
    @Test
    public void testContainsValue() {
        sparseInstance.initiate(dataVector);
        boolean containsValue = sparseInstance.containsValue(1.0);
        
        assertTrue(containsValue);
    }
    
    @Test
    public void testEntrySet() {
        sparseInstance.initiate(dataVector);
        Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
        
        assertEquals(3, entrySet.size());
        assertNotNull(entrySet.iterator().next());
    }
    
    @Test
    public void testGet() {
        sparseInstance.initiate(dataVector);
        Double value = sparseInstance.get(1);
        
        assertEquals(dataVector[1], value, 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        sparseInstance.initiate(dataVector);
        sparseInstance.clear();
        boolean isEmpty = sparseInstance.isEmpty();
        
        assertTrue(isEmpty);
    }
    
    @Test
    public void testKeySet() {
        sparseInstance.initiate(dataVector);
        Set<Integer> keySet = sparseInstance.keySet();
        
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(1));
    }
    
    @Test
    public void testPut() {
        sparseInstance.put(1, 2.0);
        
        assertEquals(2.0, sparseInstance.value(1), 0.001);
    }
    
    @Test
    public void testPutAll() {
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        
        sparseInstance.putAll(map);
        
        assertEquals(2.0, sparseInstance.value(1), 0.001);
        assertEquals(2, sparseInstance.size());
    }
    
    @Test
    public void testRemove() {
        sparseInstance.initiate(dataVector);
        Double removedValue = sparseInstance.remove(1);
        Double value = sparseInstance.get(1);
        
        assertEquals(dataVector[1], removedValue, 0.001);
        assertNull(value);
    }
    
    @Test
    public void testSize() {
        sparseInstance.initiate(dataVector);
        int size = sparseInstance.size();
        
        assertEquals(3, size);
    }
    
    @Test
    public void testValues() {
        sparseInstance.initiate(dataVector);
        Collection<Double> values = sparseInstance.values();
        
        assertEquals(3, values.size());
        assertTrue(values.contains(dataVector[0]));
    }
    
    @Test
    public void testNoAttributes() {
        sparseInstance.setNoAttributes(3);
        
        int noAttributes = sparseInstance.noAttributes();
        
        assertEquals(3, noAttributes);
    }
    
    @Test
    public void testRemoveAttribute() {
        sparseInstance.initiate(dataVector);
        sparseInstance.removeAttribute(1);
        Double value = sparseInstance.get(1);
        
        assertNull(value);
        assertEquals(2, sparseInstance.size());
    }
    
    @Test
    public void testToString() {
        sparseInstance.initiate(dataVector);
        String stringRepresentation = sparseInstance.toString();
        
        assertNotNull(stringRepresentation);
    }
    
    @Test
    public void testHashCode() {
        sparseInstance.initiate(dataVector);
        int hashCode = sparseInstance.hashCode();
        
        assertNotNull(hashCode);
    }
    
    @Test
    public void testEquals() {
        sparseInstance.initiate(dataVector);
        boolean isEquals = sparseInstance.equals(sparseInstance);
        
        assertTrue(isEquals);
    }
    
    @Test
    public void testCopy() {
        sparseInstance.initiate(dataVector);
        SparseInstance copyInstance = (SparseInstance) sparseInstance.copy();
        
        assertEquals(sparseInstance, copyInstance);
    }
    
    @Test
    public void testRemoveAttributes() {
        sparseInstance.initiate(dataVector);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(1);
        
        sparseInstance.removeAttributes(indices);
        
        assertEquals(1, sparseInstance.noAttributes());
    }
}