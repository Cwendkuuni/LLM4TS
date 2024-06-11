import org.junit.Test;

import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance.value(0) == 1.0;
        assert instance.value(1) == 2.0;
        assert instance.value(2) == 3.0;
    }

    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assert instance.value(0) == 0.0;
        assert instance.value(1) == 0.0;
        assert instance.value(2) == 0.0;
    }

    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance.containsKey(0);
        assert instance.containsKey(1);
        assert instance.containsKey(2);
        assert !instance.containsKey(3);
    }

    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance.containsValue(1.0);
        assert instance.containsValue(2.0);
        assert instance.containsValue(3.0);
        assert !instance.containsValue(4.0);
    }

    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assert entrySet.size() == 3;
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assert instance.value(key) == value;
        }
    }

    @Test
    public void testGet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance.get(0) == 1.0;
        assert instance.get(1) == 2.0;
        assert instance.get(2) == 3.0;
    }

    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert !instance.isEmpty();
    }

    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        SortedSet<Integer> keySet = instance.keySet();
        assert keySet.size() == 3;
        int index = 0;
        for (int key : keySet) {
            assert key == index++;
        }
    }

    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance.put(0, 5.0) == 1.0;
        assert instance.value(0) == 5.0;
    }

    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 5.0);
        map.put(1, 6.0);
        map.put(2, 7.0);
        instance.putAll(map);
        assert instance.value(0) == 5.0;
        assert instance.value(1) == 6.0;
        assert instance.value(2) == 7.0;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.remove(0);
    }

    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance.size() == 3;
    }

    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Collection<Double> values = instance.values();
        assert values.size() == 3;
        int index = 0;
        for (double value : values) {
            assert value == instance.value(index++);
        }
    }

    @Test
    public void testNoAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance.noAttributes() == 3;
    }

    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance.toString().equals("{[1.0, 2.0, 3.0];null}");
    }

    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.removeAttribute(1);
        assert instance.size() == 2;
        assert instance.value(0) == 1.0;
        assert instance.value(1) == 3.0;
    }

    @Test
    public void testHashCode() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assert instance1.hashCode() == instance2.hashCode();
    }

    @Test
    public void testEquals() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[]{3.0, 2.0, 1.0});
        assert instance1.equals(instance2);
        assert !instance1.equals(instance3);
    }

    @Test
    public void testCopy() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = instance1.copy();
        assert Arrays.equals(instance1.attributes, instance2.attributes);
        assert instance1.classValue().equals(instance2.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        assert instance.size() == 2;
        assert instance.value(0) == 1.0;
        assert instance.value(1) == 3.0;
    }
}