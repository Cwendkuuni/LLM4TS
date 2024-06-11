import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testInitializedConstructor() {
        double[] dataVector = {0.1, 0.2, 0.3};
        Object classValue = "class";
        SparseInstance instance = new SparseInstance(dataVector, 0.0, classValue);
        assertEquals(dataVector.length, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        assertEquals(0.1, instance.value(0), 0.00001);
        assertEquals(0.2, instance.value(1), 0.00001);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.1);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.1);
        assertTrue(instance.containsValue(0.1));
        assertFalse(instance.containsValue(0.2));
    }

    // More test methods for the remaining methods in SparseInstance class

}

