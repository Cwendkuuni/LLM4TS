package net.sf.javaml.core;

import org.junit.Test;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assert(instance.noAttributes() == 5);
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assert(instance.noAttributes() == -1);
        assert(instance.defaultValue == 0.0);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testSparseInstanceWithAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assert(instance.noAttributes() == 5);
        assert(instance.defaultValue == 0.0);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testSparseInstanceWithAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assert(instance.noAttributes() == 5);
        assert(instance.defaultValue == 1.0);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testSparseInstanceWithAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assert(instance.noAttributes() == 5);
        assert(instance.defaultValue == 0.0);
        assert(instance.getClassValue() == classValue);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testSparseInstanceWithAttributesDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assert(instance.noAttributes() == 5);
        assert(instance.defaultValue == 1.0);
        assert(instance.getClassValue() == classValue);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assert(instance.noAttributes() == 3);
        assert(instance.defaultValue == 0.0);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 1.0);
        assert(instance.noAttributes() == 3);
        assert(instance.defaultValue == 1.0);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testSparseInstanceWithDataVectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assert(instance.noAttributes() == 3);
        assert(instance.defaultValue == 0.0);
        assert(instance.getClassValue() == classValue);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testSparseInstanceWithDataVectorDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 1.0, classValue);
        assert(instance.noAttributes() == 3);
        assert(instance.defaultValue == 1.0);
        assert(instance.getClassValue() == classValue);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testInitiate() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assert(instance.noAttributes() == 3);
        assert(instance.defaultValue == 0.0);
        assert(instance.data instanceof HashMap);
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assert(instance.value(1) == 2.0);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        instance.clear();
        assert(instance.size() == 0);
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assert(instance.containsKey(1));
        assert(!instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assert(instance.containsValue(2.0));
        assert(!instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assert(entrySet.size() == 1);
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assert(entry.getKey() == 1);
            assert(entry.getValue() == 2.0);
        }
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assert(instance.get(1) == 2.0);
        assert(instance.get(2) == 0.0);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assert(instance.isEmpty());
        instance.put(1, 2.0);
        assert(!instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        Set<Integer> keySet = instance.keySet();
        assert(keySet.size() == 1);
        for (Integer key : keySet) {
            assert(key == 1);
        }
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assert(instance.size() == 1);
        assert(instance.get(1) == 2.0);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 2.0);
        instance.putAll(map);
        assert(instance.size() == 1);
        assert(instance.get(1) == 2.0);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        instance.remove(1);
        assert(instance.size() == 0);
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assert(instance.size() == instance.entrySet().size());
        instance.put(1, 2.0);
        assert(instance.size() == instance.entrySet().size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        Collection<Double> values = instance.values();
        assert(values.size() == 1);
        for (Double value : values) {
            assert(value == 2.0);
        }
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assert(instance.noAttributes() == -1);
        instance.put(0, 1.0);
        assert(instance.noAttributes() == 1);
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assert(instance.size() == 2);
        assert(instance.containsKey(0));
        assert(instance.containsKey(1));
        assert(!instance.containsKey(2));
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assert(instance.toString().equals("{0=1.0, 1=2.0, 2=3.0; null}"));
    }

    @Test
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assert(instance.hashCode() != 0);
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        assert(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        SparseInstance instance2 = instance1.copy();
        assert(instance1.equals(instance2));
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(2);
        instance.removeAttributes(indices);
        assert(instance.size() == 1);
        assert(instance.containsKey(0));
        assert(!instance.containsKey(1));
        assert(!instance.containsKey(2));
    }
}