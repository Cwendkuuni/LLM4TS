import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes);
    }

    @Test
    public void testSparseInstance_noAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes);
    }

    @Test
    public void testSparseInstance_noAttributes_defaultValue() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstance_noAttributes_defaultValue_classValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 0.1, classValue);
        assertEquals(5, instance.noAttributes);
        assertEquals(0.1, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    // Add more test methods to cover all methods in the SparseInstance class

    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(2.0, instance.value(1), 0.001);
    }

    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsKey(0));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
    }

    // Repeat the above format for remaining methods

}