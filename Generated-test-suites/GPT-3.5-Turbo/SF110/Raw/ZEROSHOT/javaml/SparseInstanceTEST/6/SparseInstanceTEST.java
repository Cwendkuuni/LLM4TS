package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SparseInstanceTest {

    private SparseInstance sparseInstance;

    @Before
    public void setUp() {
        sparseInstance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        sparseInstance.setNoAttributes(5);
        int expected = 5;
        int actual = sparseInstance.noAttributes;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        int expected = -1;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        int expected = 5;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        int expected = 5;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
        double expectedDefaultValue = 0.0;
        double actualDefaultValue = instance.defaultValue;
        Assert.assertEquals(expectedDefaultValue, actualDefaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        int expected = 5;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
        Object expectedClassValue = classValue;
        Object actualClassValue = instance.classValue();
        Assert.assertEquals(expectedClassValue, actualClassValue);
    }

    @Test
    public void testSparseInstanceWithNoAttributesDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 0.0, classValue);
        int expected = 5;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
        double expectedDefaultValue = 0.0;
        double actualDefaultValue = instance.defaultValue;
        Assert.assertEquals(expectedDefaultValue, actualDefaultValue, 0.001);
        Object expectedClassValue = classValue;
        Object actualClassValue = instance.classValue();
        Assert.assertEquals(expectedClassValue, actualClassValue);
    }

    @Test
    public void testSparseInstanceWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        int expected = 3;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        int expected = 3;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
        double expectedDefaultValue = 0.0;
        double actualDefaultValue = instance.defaultValue;
        Assert.assertEquals(expectedDefaultValue, actualDefaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        int expected = 3;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
        Object expectedClassValue = classValue;
        Object actualClassValue = instance.classValue();
        Assert.assertEquals(expectedClassValue, actualClassValue);
    }

    @Test
    public void testSparseInstanceWithDatavectorDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.0, classValue);
        int expected = 3;
        int actual = instance.noAttributes;
        Assert.assertEquals(expected, actual);
        double expectedDefaultValue = 0.0;
        double actualDefaultValue = instance.defaultValue;
        Assert.assertEquals(expectedDefaultValue, actualDefaultValue, 0.001);
        Object expectedClassValue = classValue;
        Object actualClassValue = instance.classValue();
        Assert.assertEquals(expectedClassValue, actualClassValue);
    }

    @Test
    public void testValue() {
        sparseInstance.put(1, 2.0);
        double expected = 2.0;
        double actual = sparseInstance.value(1);
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testClear() {
        sparseInstance.put(1, 2.0);
        sparseInstance.clear();
        boolean expected = true;
        boolean actual = sparseInstance.isEmpty();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsKey() {
        sparseInstance.put(1, 2.0);
        boolean expected = true;
        boolean actual = sparseInstance.containsKey(1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsValue() {
        sparseInstance.put(1, 2.0);
        boolean expected = true;
        boolean actual = sparseInstance.containsValue(2.0);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEntrySet() {
        sparseInstance.put(1, 2.0);
        Map.Entry<Integer, Double> entry = new HashMap.SimpleEntry<>(1, 2.0);
        boolean expected = true;
        boolean actual = sparseInstance.entrySet().contains(entry);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        sparseInstance.put(1, 2.0);
        double expected = 2.0;
        double actual = sparseInstance.get(1);
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testIsEmpty() {
        boolean expected = true;
        boolean actual = sparseInstance.isEmpty();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testKeySet() {
        sparseInstance.put(1, 2.0);
        int expected = 1;
        int actual = sparseInstance.keySet().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPut() {
        Double value = sparseInstance.put(1, 2.0);
        Double expected = null;
        Double actual = value;
        Assert.assertEquals(expected, actual);
        double expectedValue = 2.0;
        double actualValue = sparseInstance.get(1);
        Assert.assertEquals(expectedValue, actualValue, 0.001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 2.0);
        sparseInstance.putAll(map);
        int expected = 1;
        int actual = sparseInstance.keySet().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemove() {
        sparseInstance.put(1, 2.0);
        Double value = sparseInstance.remove(1);
        Double expected = 2.0;
        Double actual = value;
        Assert.assertEquals(expected, actual);
        boolean expectedEmpty = true;
        boolean actualEmpty = sparseInstance.isEmpty();
        Assert.assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testSize() {
        sparseInstance.put(1, 2.0);
        int expected = 1;
        int actual = sparseInstance.size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testValues() {
        sparseInstance.put(1, 2.0);
        double expected = 2.0;
        double actual = sparseInstance.values().iterator().next();
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testNoAttributes() {
        sparseInstance.put(1, 2.0);
        int expected = 2;
        int actual = sparseInstance.noAttributes();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveAttribute() {
        sparseInstance.put(1, 2.0);
        sparseInstance.removeAttribute(1);
        boolean expected = true;
        boolean actual = sparseInstance.isEmpty();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        sparseInstance.put(1, 2.0);
        String expected = "{1=2.0;null}";
        String actual = sparseInstance.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        int expected = -319325995;
        int actual = sparseInstance.hashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        SparseInstance other = new SparseInstance();
        boolean expected = true;
        boolean actual = sparseInstance.equals(other);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCopy() {
        sparseInstance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) sparseInstance.copy();
        boolean expected = true;
        boolean actual = sparseInstance.equals(copy);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveAttributes() {
        sparseInstance.put(1, 2.0);
        sparseInstance.removeAttributes(sparseInstance.keySet());
        boolean expected = true;
        boolean actual = sparseInstance.isEmpty();
        Assert.assertEquals(expected, actual);
    }

}