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
        sparseInstance.setNoAttributes(10);
        Assert.assertEquals(10, sparseInstance.noAttributes);
    }

    @Test
    public void testSparseInstance1() {
        SparseInstance instance = new SparseInstance();
        Assert.assertEquals(-1, instance.noAttributes);
    }

    @Test
    public void testSparseInstance2() {
        SparseInstance instance = new SparseInstance(10);
        Assert.assertEquals(10, instance.noAttributes);
    }

    @Test
    public void testSparseInstance3() {
        SparseInstance instance = new SparseInstance(10, 0.5);
        Assert.assertEquals(10, instance.noAttributes);
        Assert.assertEquals(0.5, instance.defaultValue, 1e-9);
    }

    @Test
    public void testSparseInstance4() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(10, classValue);
        Assert.assertEquals(10, instance.noAttributes);
        Assert.assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstance5() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(10, 0.5, classValue);
        Assert.assertEquals(10, instance.noAttributes);
        Assert.assertEquals(0.5, instance.defaultValue, 1e-9);
        Assert.assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstance6() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        Assert.assertEquals(3, instance.noAttributes);
    }

    @Test
    public void testSparseInstance7() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.5);
        Assert.assertEquals(3, instance.noAttributes);
        Assert.assertEquals(0.5, instance.defaultValue, 1e-9);
    }

    @Test
    public void testSparseInstance8() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        Assert.assertEquals(3, instance.noAttributes);
        Assert.assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstance9() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.5, classValue);
        Assert.assertEquals(3, instance.noAttributes);
        Assert.assertEquals(0.5, instance.defaultValue, 1e-9);
        Assert.assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testValue() {
        sparseInstance.put(0, 1.0);
        Assert.assertEquals(1.0, sparseInstance.value(0), 1e-9);
    }

    @Test
    public void testClear() {
        sparseInstance.put(0, 1.0);
        sparseInstance.clear();
        Assert.assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        sparseInstance.put(0, 1.0);
        Assert.assertTrue(sparseInstance.containsKey(0));
        Assert.assertFalse(sparseInstance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        sparseInstance.put(0, 1.0);
        Assert.assertTrue(sparseInstance.containsValue(1.0));
        Assert.assertFalse(sparseInstance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        Map.Entry<Integer, Double> entry = sparseInstance.entrySet().iterator().next();
        Assert.assertEquals(0, (int) entry.getKey());
        Assert.assertEquals(1.0, (double) entry.getValue(), 1e-9);
    }

    @Test
    public void testGet() {
        sparseInstance.put(0, 1.0);
        Assert.assertEquals(1.0, sparseInstance.get(0), 1e-9);
        Assert.assertEquals(0.0, sparseInstance.get(1), 1e-9);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(sparseInstance.isEmpty());
        sparseInstance.put(0, 1.0);
        Assert.assertFalse(sparseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        Assert.assertEquals(2, sparseInstance.keySet().size());
        Assert.assertTrue(sparseInstance.keySet().contains(0));
        Assert.assertTrue(sparseInstance.keySet().contains(1));
    }

    @Test
    public void testPut() {
        sparseInstance.put(0, 1.0);
        Assert.assertEquals(1.0, sparseInstance.get(0), 1e-9);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        sparseInstance.putAll(map);
        Assert.assertEquals(1.0, sparseInstance.get(0), 1e-9);
        Assert.assertEquals(2.0, sparseInstance.get(1), 1e-9);
    }

    @Test
    public void testRemove() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        sparseInstance.remove(0);
        Assert.assertFalse(sparseInstance.containsKey(0));
        Assert.assertEquals(2.0, sparseInstance.get(1), 1e-9);
    }

    @Test
    public void testSize() {
        sparseInstance.put(0, 1.0);
        Assert.assertEquals(1, sparseInstance.size());
        sparseInstance.put(1, 2.0);
        Assert.assertEquals(2, sparseInstance.size());
    }

    @Test
    public void testValues() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        Assert.assertEquals(2, sparseInstance.values().size());
        Assert.assertTrue(sparseInstance.values().contains(1.0));
        Assert.assertTrue(sparseInstance.values().contains(2.0));
    }

    @Test
    public void testNoAttributes1() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        Assert.assertEquals(2, sparseInstance.noAttributes());
    }

    @Test
    public void testNoAttributes2() {
        Assert.assertEquals(0, sparseInstance.noAttributes());
    }

    @Test
    public void testNoAttributes3() {
        sparseInstance.put(5, 1.0);
        Assert.assertEquals(6, sparseInstance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        sparseInstance.removeAttribute(0);
        Assert.assertFalse(sparseInstance.containsKey(0));
        Assert.assertEquals(1.0, sparseInstance.get(1), 1e-9);
        Assert.assertEquals(1, sparseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        Assert.assertEquals("{0=1.0;null}", sparseInstance.toString());
    }

    @Test
    public void testHashCode() {
        HashMap<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        sparseInstance.data = data;
        sparseInstance.defaultValue = 0.5;
        int expectedHashCode = 31 * data.hashCode() + Double.hashCode(0.5);
        Assert.assertEquals(expectedHashCode, sparseInstance.hashCode());
    }

    @Test
    public void testEquals() {
        HashMap<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        SparseInstance instance1 = new SparseInstance();
        instance1.data = data;
        instance1.defaultValue = 0.5;
        SparseInstance instance2 = new SparseInstance();
        instance2.data = data;
        instance2.defaultValue = 0.5;
        Assert.assertEquals(instance1, instance2);
    }

    @Test
    public void testCopy() {
        sparseInstance.put(0, 1.0);
        SparseInstance copy = (SparseInstance) sparseInstance.copy();
        Assert.assertEquals(sparseInstance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        sparseInstance.removeAttributes(Set.of(0, 1));
        Assert.assertTrue(sparseInstance.isEmpty());
    }

}