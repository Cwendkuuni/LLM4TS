import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SparseInstanceTest {
    private SparseInstance sparseInstance;

    @Before
    public void setUp() {
        sparseInstance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        // Typical use case
        sparseInstance.setNoAttributes(10);
        assertEquals(10, sparseInstance.noAttributes());

        // Edge case: zero attributes
        sparseInstance.setNoAttributes(0);
        assertEquals(0, sparseInstance.noAttributes());

        // Error scenario: negative number of attributes
        sparseInstance.setNoAttributes(-5);
        assertEquals(0, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstance() {
        // Typical use case: without class value
        SparseInstance newInstance = new SparseInstance(5);
        assertEquals(5, newInstance.noAttributes());
        assertNull(newInstance.classValue());

        // Typical use case: with class value
        Object classValue = "class1";
        SparseInstance newInstanceWithClass = new SparseInstance(5, classValue);
        assertEquals(5, newInstanceWithClass.noAttributes());
        assertEquals(classValue, newInstanceWithClass.classValue());
    }

    // Similar tests can be implemented for other constructors and methods.

    // Additional tests for the remaining methods

    @Test
    public void testValue() {
        // Typical use case
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.value(0), 0.001);

        // Edge case: attribute with defaultValue
        instance.setNoAttributes(5);
        assertEquals(0.0, instance.value(4), 0.001);

        // Error scenario: invalid attribute position
        instance.clear();
        assertEquals(0.0, instance.value(-1), 0.001);
    }

    @Test
    public void testClear() {
        // Typical use case: clearing instance with data
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.clear();
        assertEquals(0, instance.size());

        // Typical use case: clearing empty instance
        instance.clear();
        assertEquals(0, instance.size());
    }

    @Test
    public void testContainsKey() {
        // Typical use case: key present
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));

        // Typical use case: key not present
        assertFalse(instance.containsKey(1));
    }

    // Similar tests can be implemented for the remaining methods

}