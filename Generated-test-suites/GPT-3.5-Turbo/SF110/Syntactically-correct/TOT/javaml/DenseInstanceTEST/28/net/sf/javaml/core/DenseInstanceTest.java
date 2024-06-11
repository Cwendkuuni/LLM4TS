
import org.junit.*;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance denseInstance;

    @Before
    public void setUp() {
        denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
    }

    @After
    public void tearDown() {
        denseInstance = null;
    }

    // Test case for DenseInstance constructor with attribute values
    @Test
    public void testDenseInstanceConstructorWithAttributes() {
        double[] attributes = denseInstance.attributes;
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, attributes, 0.0);
    }

    // Test case for DenseInstance constructor with attribute values and class value
    @Test
    public void testDenseInstanceConstructorWithAttributesAndClassValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class");
        double[] attributes = instance.attributes;
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, attributes, 0.0);
        Assert.assertEquals("class", instance.classValue());
    }

    // Test case for DenseInstance constructor with size
    @Test
    public void testDenseInstanceConstructorWithSize() {
        DenseInstance instance = new DenseInstance(3);
        double[] attributes = instance.attributes;
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, attributes, 0.0);
    }

    // Test case for value() method
    @Test
    public void testValue() {
        double value = denseInstance.value(1);
        Assert.assertEquals(2.0, value, 0.0);
    }

    // Test case for clear() method
    @Test
    public void testClear() {
        denseInstance.clear();
        double[] attributes = denseInstance.attributes;
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, attributes, 0.0);
    }

    // Test case for containsKey() method with valid key
    @Test
    public void testContainsKeyWithValidKey() {
        boolean containsKey = denseInstance.containsKey(1);
        Assert.assertTrue(containsKey);
    }

    // Test case for containsKey() method with invalid key
    @Test
    public void testContainsKeyWithInvalidKey() {
        boolean containsKey = denseInstance.containsKey(5);
        Assert.assertFalse(containsKey);
    }

    // Test case for containsValue() method with valid value
    @Test
    public void testContainsValueWithValidValue() {
        boolean containsValue = denseInstance.containsValue(2.0);
        Assert.assertTrue(containsValue);
    }

    // Test case for containsValue() method with invalid value
    @Test
    public void testContainsValueWithInvalidValue() {
        boolean containsValue = denseInstance.containsValue(4.0);
        Assert.assertFalse(containsValue);
    }

    // Test case for entrySet() method
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Assert.assertEquals(3, entrySet.size());
    }

    // Test case for get() method
    @Test
    public void testGet() {
        Double attribute = denseInstance.get(1);
        Assert.assertEquals(2.0, attribute, 0.0);
    }

    // Test case for isEmpty() method
    @Test
    public void testIsEmpty() {
        boolean isEmpty = denseInstance.isEmpty();
        Assert.assertFalse(isEmpty);
    }

    // Test case for keySet() method
    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = denseInstance.keySet();
        Assert.assertEquals(3, keySet.size());
        Assert.assertEquals(new TreeSet<Integer>(Arrays.asList(0, 1, 2)), keySet);
    }

    // Test case for put() method
    @Test
    public void testPut() {
        Double previousValue = denseInstance.put(1, 4.0);
        Assert.assertEquals(2.0, previousValue, 0.0);
        Assert.assertEquals(4.0, denseInstance.value(1), 0.0);
    }

    // Test case for putAll() method
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        denseInstance.putAll(map);
        Assert.assertEquals(4.0, denseInstance.value(0), 0.0);
        Assert.assertEquals(5.0, denseInstance.value(1), 0.0);
        Assert.assertEquals(6.0, denseInstance.value(2), 0.0);
    }

    // Test case for remove() method
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        denseInstance.remove(1);
    }

    // Test case for size() method
    @Test
    public void testSize() {
        int size = denseInstance.size();
        Assert.assertEquals(3, size);
    }

    // Test case for values() method
    @Test
    public void testValues() {
        Collection<Double> values = denseInstance.values();
        Assert.assertEquals(3, values.size());
        Assert.assertFalse(values.isEmpty());
        Assert.assertTrue(values.contains(1.0));
        Assert.assertTrue(values.contains(2.0));
        Assert.assertTrue(values.contains(3.0));
    }

    // Test case for noAttributes() method
    @Test
    public void testNoAttributes() {
        int noAttributes = denseInstance.noAttributes();
        Assert.assertEquals(3, noAttributes);
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0];null}";
        String actual = denseInstance.toString();
        Assert.assertEquals(expected, actual);
    }

    // Test case for removeAttribute() method
    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        Assert.assertEquals(2, denseInstance.size());
        Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
        Assert.assertEquals(3.0, denseInstance.value(1), 0.0);
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        int hashCode = denseInstance.hashCode();
        Assert.assertNotEquals(0, hashCode);
    }

    // Test case for equals() method with different instance
    @Test
    public void testEqualsWithDifferentInstance() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        boolean isEqual = denseInstance.equals(instance);
        Assert.assertTrue(isEqual);
    }

    // Test case for equals() method with different attributes
    @Test
    public void testEqualsWithDifferentAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 4.0, 3.0});
        boolean isEqual = denseInstance.equals(instance);
        Assert.assertFalse(isEqual);
    }

    // Test case for copy() method
    @Test
    public void testCopy() {
        DenseInstance copyInstance = (DenseInstance) denseInstance.copy();
        double[] attributes = copyInstance.attributes;
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0},attributes, 0.0);
        Assert.assertEquals(denseInstance.classValue(), copyInstance.classValue());
    }

    // Test case for removeAttributes() method
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        denseInstance.removeAttributes(indices);
        Assert.assertEquals(1, denseInstance.size());
        Assert.assertEquals(2.0, denseInstance.value(0), 0.0);
    }

    // Edge case for DenseInstance constructor with attribute values
    @Test
    public void testDenseInstanceConstructorWithEmptyAttributes() {
        double[] attributes = new double[0];
        DenseInstance instance = new DenseInstance(attributes);
        Assert.assertArrayEquals(attributes, instance.attributes, 0.0);
    }

    // Edge case for containsKey() method with negative key
    @Test
    public void testContainsKeyWithNegativeKey() {
        boolean containsKey = denseInstance.containsKey(-1);
        Assert.assertFalse(containsKey);
    }

    // Edge case for containsValue() method with non-double value
    @Test
    public void testContainsValueWithNonDoubleValue() {
        boolean containsValue = denseInstance.containsValue(1);
        Assert.assertFalse(containsValue);
    }

    // Edge case for get() method with negative key
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithNegativeKey() {
        denseInstance.get(-1);
    }

    // Error scenario for put() method with invalid key
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testPutWithInvalidKey() {
        denseInstance.put(3, 4.0);
    }

    // Error scenario for removeAttribute() method with invalid index
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemoveAttributeWithInvalidIndex() {
        denseInstance.removeAttribute(3);
    }

    // Error scenario for removeAttributes() method with invalid indices
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemoveAttributesWithInvalidIndices() {
        Set<Integer> indices = new HashSet<>();
        indices.add(3);
        denseInstance.removeAttributes(indices);
    }
}
