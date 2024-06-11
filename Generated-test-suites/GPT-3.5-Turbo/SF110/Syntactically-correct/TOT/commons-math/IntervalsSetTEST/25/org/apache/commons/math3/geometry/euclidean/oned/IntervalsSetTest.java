package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.*;

public class IntervalsSetTest {

    @Test
    public void testConstructor() {
        // Test default constructor
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
        
        // Test constructor with lower and upper bounds
        intervalsSet = new IntervalsSet(0.0, 1.0);
        assertNotNull(intervalsSet);
        
        // Test constructor with BSPTree
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        
        // Test constructor with Collection of SubHyperplanes
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        // Test with null cut
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        
        // Test with intervals, positive size
        intervalsSet = new IntervalsSet(0.0, 1.0);
        intervalsSet.computeGeometricalProperties();
        assertEquals(1.0, intervalsSet.getSize(), 0.0);
        assertEquals(new Vector1D(0.5), intervalsSet.getBarycenter());
        
        // Test with intervals, size below SAFE_MIN
        intervalsSet = new IntervalsSet(0.0, 0.00001);
        intervalsSet.computeGeometricalProperties();
        assertEquals(0.00001, intervalsSet.getSize(), 0.0);
        assertEquals(new Vector1D(0.0), intervalsSet.getBarycenter());
    }
    
    @Test
    public void testGetInf() {
        // Test with intervals inside BSP tree
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        
        // Test with nested intervals inside BSP tree
        intervalsSet = new IntervalsSet(0.0, 0.5);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        
        // Test with empty intervals
        intervalsSet = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup() {
        // Test with intervals inside BSP tree
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
        
        // Test with nested intervals inside BSP tree
        intervalsSet = new IntervalsSet(0.0, 0.5);
        assertEquals(0.5, intervalsSet.getSup(), 0.0);
        
        // Test with empty intervals
        intervalsSet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsList() {
        // Test with intervals inside BSP tree
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        List<Interval> intervalList = intervalsSet.asList();
        assertEquals(1, intervalList.size());
        assertEquals(new Interval(0.0, 1.0), intervalList.get(0));
        
        // Test with nested intervals inside BSP tree
        intervalsSet = new IntervalsSet(0.0, 0.5);
        intervalList = intervalsSet.asList();
        assertEquals(1, intervalList.size());
        assertEquals(new Interval(0.0, 0.5), intervalList.get(0));
        
        // Test with empty intervals
        intervalsSet = new IntervalsSet();
        intervalList = intervalsSet.asList();
        assertTrue(intervalList.isEmpty());
    }
}