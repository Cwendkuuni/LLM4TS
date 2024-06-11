import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assert(instance.value(0) == 1.0);
        assert(instance.value(1) == 2.0);
        assert(instance.value(2) == 3.0);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assert(Arrays.equals(instance.attributes, expected));
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assert(instance.containsKey(0));
        assert(instance.containsKey(1));
        assert(instance.containsKey(2));
        assert(!instance.containsKey(3));
        assert(!instance.containsKey(-1));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assert(instance.containsValue(1.0));
        assert(instance.containsValue(2.0));
        assert(instance.containsValue(3.0));
        assert(!instance.containsValue(4.0));
        assert(!instance.containsValue(0.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assert(entrySet.size() == 3);
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assert(att[key] == value);
        }
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assert(instance.get(0) == 1.0);
        assert(instance.get(1) == 2.0);
        assert(instance.get(2) == 3.0);
    }

    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assert(!instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        SortedSet<Integer> keySet = instance.keySet();
        assert(keySet.size() == 3);
        for (int key : keySet) {
            assert(att[key] == instance.get(key));
        }
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Double previous = instance.put(0, 10.0);
        assert(previous == 1.0);
        assert(instance.get(0) == 10.0);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 10.0);
        map.put(1, 20.0);
        instance.putAll(map);
        assert(instance.get(0) == 10.0);
        assert(instance.get(1) == 20.0);
        assert(instance.get(2) == 3.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assert(instance.size() == 3);
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Collection<Double> values = instance.values();
        assert(values.size() == 3);
        for (double value : values) {
            assert(instance.containsValue(value));
        }
    }

    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assert(instance.noAttributes() == 3);
    }

    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        String expected = "{[1.0, 2.0, 3.0];null}";
        assert(instance.toString().equals(expected));
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        assert(instance.size() == 2);
        assert(instance.get(0) == 1.0);
        assert(instance.get(1) == 3.0);
    }

    @Test
    public void testHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {1.0, 2.0, 4.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        assert(instance1.hashCode() == instance2.hashCode());
        assert(instance1.hashCode() != instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {1.0, 2.0, 4.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        assert(instance1.equals(instance1));
        assert(instance1.equals(instance2));
        assert(!instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = (DenseInstance)instance.copy();
        assert(instance != copy);
        assert(instance.equals(copy));
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assert(instance.size() == 1);
        assert(instance.get(0) == 2.0);
    }
}