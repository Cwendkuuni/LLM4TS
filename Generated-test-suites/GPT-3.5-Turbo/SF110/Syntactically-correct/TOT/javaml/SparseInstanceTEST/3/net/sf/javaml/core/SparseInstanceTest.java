import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertNotNull(instance);
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(10);
        assertNotNull(instance);
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(10, 0.5);
        assertNotNull(instance);
        assertEquals(10, instance.noAttributes());
        assertEquals(0.5, instance.get(0), 0.001);
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        SparseInstance instance = new SparseInstance(10, "class");
        assertNotNull(instance);
        assertEquals(10, instance.noAttributes());
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testSparseInstanceWithAllParameters() {
        SparseInstance instance = new SparseInstance(10, 0.5, "class");
        assertNotNull(instance);
        assertEquals(10, instance.noAttributes());
        assertEquals(0.5, instance.get(0), 0.001);
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance();
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    // ... Add more test cases for remaining methods ...

}