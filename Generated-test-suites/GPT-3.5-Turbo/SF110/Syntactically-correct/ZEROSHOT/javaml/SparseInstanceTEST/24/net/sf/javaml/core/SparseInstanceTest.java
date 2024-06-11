import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assert(instance.noAttributes == 5);
    }

    @Test
    public void testSparseInstance1() {
        SparseInstance instance = new SparseInstance();
        assert(instance.noAttributes == -1);
        assert(instance.defaultValue == 0.0);
        assert(instance.data.isEmpty());
    }

    @Test
    public void testSparseInstance2() {
        SparseInstance instance = new SparseInstance(5);
        assert(instance.noAttributes == 5);
        assert(instance.defaultValue == 0.0);
        assert(instance.data.isEmpty());
    }

    @Test
    public void testSparseInstance3() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assert(instance.noAttributes == 5);
        assert(instance.defaultValue == 1.0);
        assert(instance.data.isEmpty());
    }

    @Test
    public void testSparseInstance4() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assert(instance.noAttributes == 5);
        assert(instance.defaultValue == 0.0);
        assert(instance.data.isEmpty());
        assert(instance.classValue().equals(classValue));
    }

    @Test
    public void testSparseInstance5() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assert(instance.noAttributes == 5);
        assert(instance.defaultValue == 1.0);
        assert(instance.data.isEmpty());
        assert(instance.classValue().equals(classValue));
    }

    @Test
    public void testSparseInstance6() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assert(instance.noAttributes == 3);
        assert(instance.defaultValue == 0.0);
        assert(instance.data.size() == 3);
        assert(instance.data.get(0) == 1.0);
        assert(instance.data.get(1) == 2.0);
        assert(instance.data.get(2) == 3.0);
    }

    @Test
    public void testSparseInstance7() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 1.0);
        assert(instance.noAttributes == 3);
        assert(instance.defaultValue == 1.0);
        assert(instance.data.size() == 3);
        assert(instance.data.get(0) == 1.0);
        assert(instance.data.get(1) == 2.0);
        assert(instance.data.get(2) == 3.0);
    }

    @Test
    public void testSparseInstance8() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assert(instance.noAttributes == 3);
        assert(instance.defaultValue == 0.0);
        assert(instance.data.size() == 3);
        assert(instance.data.get(0) == 1.0);
        assert(instance.data.get(1) == 2.0);
        assert(instance.data.get(2) == 3.0);
        assert(instance.classValue().equals(classValue));
    }

    @Test
    public void testSparseInstance9() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 1.0, classValue);
        assert(instance.noAttributes == 3);
        assert(instance.defaultValue == 1.0);
        assert(instance.data.size() == 3);
        assert(instance.data.get(0) == 1.0);
        assert(instance.data.get(1) == 2.0);
        assert(instance.data.get(2) == 3.0);
        assert(instance.classValue().equals(classValue));
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 0.0, 3.0};
        SparseInstance instance = new SparseInstance();
        instance.initiate(datavector);
        assert(instance.noAttributes == 3);
        assert(instance.defaultValue == 0.0);
        assert(instance.data.size() == 2);
        assert(instance.data.get(0) == 1.0);
        assert(instance.data.get(2) == 3.0);
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assert(instance.value(0) == 1.0);
        assert(instance.value(1) == 2.0);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assert(!instance.data.isEmpty());
        instance.clear();
        assert(instance.data.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assert(instance.containsKey(0));
        assert(!instance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assert(instance.containsValue(1.0));
        assert(!instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assert(instance.entrySet().size() == 2);
        assert(instance.entrySet().contains(new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0)));
        assert(instance.entrySet().contains(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0)));
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assert(instance.get(0) == 1.0);
        assert(instance.get(1) == 2.0);
        assert(instance.get(2) == instance.defaultValue);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assert(instance.isEmpty());
        instance.put(0, 1.0);
        assert(!instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Integer> keySet = instance.keySet();
        assert(keySet.size() == 2);
        assert(keySet.contains(0));
        assert(keySet.contains(1));
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assert(instance.data.isEmpty());
        instance.put(0, 1.0);
        assert(instance.data.size() == 1);
        assert(instance.data.get(0) == 1.0);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assert(instance.data.size() == 2);
        assert(instance.data.get(0) == 1.0);
        assert(instance.data.get(1) == 2.0);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assert(instance.data.size() == 1);
        instance.remove(0);
        assert(instance.data.isEmpty());
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assert(instance.size() == instance.data.size());
        instance.put(0, 1.0);
        assert(instance.size() == instance.data.size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Collection<Double> values = instance.values();
        assert(values.size() == 2);
        assert(values.contains(1.0));
        assert(values.contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assert(instance.noAttributes() == instance.noAttributes);
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assert(instance.noAttributes() == 2);
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assert(instance.data.size() == 1);
        assert(instance.data.get(0) == 2.0);
        assert(instance.noAttributes == 1);
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assert(instance.toString().equals("{0=1.0;2.0}"));
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);

        assert(instance1.hashCode() == instance2.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);

        assert(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        SparseInstance copy = (SparseInstance) instance.copy();
        assert(instance.data.equals(copy.data));
        assert(instance.defaultValue == copy.defaultValue);
        assert(instance.noAttributes == copy.noAttributes);
        assert(instance.classValue().equals(copy.classValue()));
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
        assert(instance.data.size() == 1);
        assert(instance.data.get(0) == 3.0);
        assert(instance.noAttributes == 1);
    }
}