package net.sf.javaml.core;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    private SparseInstance sparseInstance;

    @Before
    public void setUp() {
        sparseInstance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        int expected = 5;
        sparseInstance.setNoAttributes(expected);
        assertEquals(expected, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstanceDefaultConstructor() {
        assertNotNull(sparseInstance);
        assertEquals(-1, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructorWithClassValue() {
        Object expectedClassValue = "class value";
        SparseInstance instance = new SparseInstance(5, expectedClassValue);
        assertNotNull(instance);
        assertEquals(expectedClassValue, instance.classValue());
    }

    @Test
    public void testValue() {
        int pos = 3;
        double expectedValue = 10.0;
        sparseInstance.put(pos, expectedValue);
        assertEquals(expectedValue, sparseInstance.value(pos), 0.0001);
    }

    // More test cases...

    @After
    public void tearDown() {
        sparseInstance = null;
    }
}