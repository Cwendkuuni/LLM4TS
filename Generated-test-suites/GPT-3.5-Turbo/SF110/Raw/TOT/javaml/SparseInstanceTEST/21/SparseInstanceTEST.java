import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest{

    @Test
    public void testSetNoAttributes(){
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSetNoAttributesNegative(){
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(-5);
        assertEquals(-5, instance.noAttributes());
    }

    @Test
    public void testSetNoAttributesZero(){
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(0);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testSparseInstance(){
        SparseInstance instance = new SparseInstance();
        assertNotNull(instance);
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceNoAttributes(){
        SparseInstance instance = new SparseInstance(5);
        assertNotNull(instance);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceNoAttributesNegative(){
        SparseInstance instance = new SparseInstance(-5);
        assertNotNull(instance);
        assertEquals(-5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceNoAttributesZero(){
        SparseInstance instance = new SparseInstance(0);
        assertNotNull(instance);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceNoAttributesValue(){
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertNotNull(instance);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceNoAttributesValueNegative(){
        SparseInstance instance = new SparseInstance(-5, -1.0);
        assertNotNull(instance);
        assertEquals(-5, instance.noAttributes());
        assertEquals(-1.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceNoAttributesValueZero(){
        SparseInstance instance = new SparseInstance(0, 0.0);
        assertNotNull(instance);
        assertEquals(0, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceNoAttributesValueClass(){
        SparseInstance instance = new SparseInstance(5, "class");
        assertNotNull(instance);
        assertEquals(5, instance.noAttributes());
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testSparseInstanceNoAttributesValueClassNegative(){
        SparseInstance instance = new SparseInstance(-5, "class");
        assertNotNull(instance);
        assertEquals(-5, instance.noAttributes());
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testSparseInstanceNoAttributesValueClassZero(){
        SparseInstance instance = new SparseInstance(0, "class");
        assertNotNull(instance);
        assertEquals(0, instance.noAttributes());
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testInitiateEmptyDatavector(){
        SparseInstance instance = new SparseInstance();
        instance.initiate(new double[]{});
        assertTrue(instance.data.isEmpty());
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testInitiateDatavector(){
        SparseInstance instance = new SparseInstance();
        instance.initiate(new double[]{1.0, 2.0});
        assertEquals(2, instance.data.size());
        assertEquals(0.0, instance.data.get(0), 0.001);
        assertEquals(1.0, instance.data.get(1), 0.001);
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testValue(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.value(0), 0.001);
    }

    @Test
    public void testClear(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.clear();
        assertTrue(instance.data.isEmpty());
    }

    @Test
    public void testContainsKey(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testContainsValue(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertNotNull(instance.entrySet());
        assertEquals(1, instance.entrySet().size());
    }

    @Test
    public void testGet(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0.0, instance.get(1), 0.001);
    }

    @Test
    public void testIsEmpty(){
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.keySet().contains(0));
        assertFalse(instance.keySet().contains(1));
    }

    @Test
    public void testPut(){
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(0, 1.0));
        assertEquals(1.0, instance.data.get(0), 0.001);
    }

    @Test
    public void testPutAll(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.data.get(0), 0.001);
        assertEquals(2.0, instance.data.get(1), 0.001);
    }

    @Test
    public void testRemove(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.remove(0), 0.001);
        assertNull(instance.remove(0));
    }

    @Test
    public void testSize(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
        instance.put(1, 2.0);
        assertEquals(2, instance.size());
    }

    @Test
    public void testValues(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertNotNull(instance.values());
        assertEquals(1, instance.values().size());
    }

    @Test
    public void testNoAttributesNoAttributesSet(){
        SparseInstance instance = new SparseInstance();
        instance.noAttributes = 10;
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testNoAttributesSizeZero(){
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testNoAttributesSize(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertNull(instance.data.get(0));
        assertNotNull(instance.data.get(1));
        assertEquals(1, instance.noAttributes());
        instance.removeAttribute(1);
        assertNull(instance.data.get(1));
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testToString(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.setClassValue("class");
        assertEquals("{0=1.0, 1=2.0;class}", instance.toString());
    }

    @Test
    public void testHashCode(){
        SparseInstance instance = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEqualsSameInstance(){
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.equals(instance));
    }

    @Test
    public void testEqualsDifferentClass(){
        SparseInstance instance = new SparseInstance();
        String string = "test";
        assertFalse(instance.equals(string));
    }

    @Test
    public void testEqualsDifferentData(){
        SparseInstance instance = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance.put(0, 1.0);
        assertFalse(instance.equals(instance2));
    }

    @Test
    public void testEqualsSameData(){
        SparseInstance instance = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance.put(0, 1.0);
        instance2.put(0, 1.0);
        assertTrue(instance.equals(instance2));
    }

    @Test
    public void testEqualsDifferentDefaultValue(){
        SparseInstance instance = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance.defaultValue = 1.0;
        assertFalse(instance.equals(instance2));
    }

    @Test
    public void testEqualsSameDefaultValue(){
        SparseInstance instance = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance.defaultValue = 1.0;
        instance2.defaultValue = 1.0;
        assertTrue(instance.equals(instance2));
    }

    @Test
    public void testCopy(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.setClassValue("class");
        SparseInstance copy = (SparseInstance)instance.copy();
        assertEquals(instance.noAttributes, copy.noAttributes);
        assertEquals(instance.defaultValue, copy.defaultValue, 0.001);
        assertEquals(instance.classValue(), copy.classValue());
        assertTrue(instance.data.equals(copy.data));
        assertTrue(instance.equals(copy));
    }

    @Test
    public void testRemoveAttributes(){
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 4.0);
        instance.put(4, 5.0);
        instance.put(5, 6.0);
        HashSet<Integer> set = new HashSet<>();
        set.add(1);
        set.add(3);
        set.add(5);
        instance.removeAttributes(set);
        assertNull(instance.data.get(1));
        assertNull(instance.data.get(3));
        assertNull(instance.data.get(5));
        assertEquals(0.0, instance.data.get(0), 0.001);
        assertEquals(0.0, instance.data.get(2), 0.001);
        assertEquals(0.0, instance.data.get(4), 0.001);

    }
}