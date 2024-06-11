package org.apache.commons.math3.geometry.euclidean.oned;
import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {
    
    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructor2() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertNotNull(intervalsSet);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testConstructor3() {
        // Add test for constructor with BSPTree
    }
    
    @Test
    public void testConstructor4() {
        // Add test for constructor with Collection<SubHyperplane>
    }
    
    @Test
    public void testBuildNew() {
        // Add test for buildNew method
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        // Add test for computeGeometricalProperties method
    }
    
    @Test
    public void testGetInf() {
        // Add test for getInf method
    }
    
    @Test
    public void testGetSup() {
        // Add test for getSup method
    }
    
    @Test
    public void testAsList() {
        // Add test for asList method
    }
}