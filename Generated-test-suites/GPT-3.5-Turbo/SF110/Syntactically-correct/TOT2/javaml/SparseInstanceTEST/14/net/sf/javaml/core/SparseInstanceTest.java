package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class SparseInstanceTest {
    
    private SparseInstance sparseInstance;
    
    @Before
    public void setUp() {
        sparseInstance = new SparseInstance();
    }
    
    @Test
    public void testSetNoAttributes() {
        int noAttributes = 5;
        sparseInstance.setNoAttributes(noAttributes);
        assertEquals(noAttributes, sparseInstance.noAttributes());
    }
    
    @Test
    public void testClear() {
        sparseInstance.put(1, 1.0);
        sparseInstance.clear();
        assertTrue(sparseInstance.isEmpty());
    }
    
    @Test
    public void testGetValue() {
        sparseInstance.put(1, 2.0);
        assertEquals(2.0, sparseInstance.value(1), 0.001);
    }
    
    @Test
    public void testContainsKey() {
        sparseInstance.put(1, 2.0);
        assertTrue(sparseInstance.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        sparseInstance.put(1, 2.0);
        assertTrue(sparseInstance.containsValue(2.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
        assertEquals(entrySet.size(), sparseInstance.size());
    }

    @Test
    public void testRemoveAttribute() {
        sparseInstance.put(1, 2.0);
        sparseInstance.removeAttribute(1);
        assertFalse(sparseInstance.containsKey(1));
        assertEquals(0, sparseInstance.size());
    }

    @Test
    public void testEquals() {
        SparseInstance otherSparseInstance = new SparseInstance(10, 0.1);
        otherSparseInstance.put(1, 2.1);
        sparseInstance.put(1, 2.1);
        assertTrue(sparseInstance.equals(otherSparseInstance));
    }
    
    @After
    public void tearDown() {
        sparseInstance = null;
    }
}