package net.sf.javaml.core;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

    private SparseInstance instance;

    @Before
    public void setUp() {
        instance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstance1() {
        SparseInstance sparseInstance = new SparseInstance();
        assertNotNull(sparseInstance);
    }

    @Test
    public void testSparseInstance2() {
        SparseInstance sparseInstance = new SparseInstance(3);
        assertNotNull(sparseInstance);
        assertEquals(3, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstance3() {
        SparseInstance sparseInstance = new SparseInstance(3, 0.5);
        assertNotNull(sparseInstance);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(0.5, sparseInstance.value(0), 0);
    }

    @Test
    public void testSparseInstance4() {
        SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 0.0, 2.5});
        assertNotNull(sparseInstance);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(1.0, sparseInstance.value(0), 0);
        assertEquals(2.5, sparseInstance.value(2), 0);
    }

    // Add more test cases for other methods

}