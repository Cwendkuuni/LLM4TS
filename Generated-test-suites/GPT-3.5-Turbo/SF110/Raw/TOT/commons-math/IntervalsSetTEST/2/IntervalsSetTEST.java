package org.apache.commons.math3.geometry.euclidean.oned;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testConstructor1() {
        // Typical use case: create an empty intervals set
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.asList().isEmpty());
    }
    
    @Test
    public void testConstructor2() {
        // Typical use case: create an intervals set with a single interval
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(0.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(1.0, intervalsSet.asList().get(0).getSup(), 0.0);
    }
    
    @Test
    public void testConstructor3() {
        // Typical use case: create an intervals set from pre-built tree
        BSPTree<Euclidean1D> tree = new BSPTree<>(true);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree(false));
    }
    
    @Test
    public void testConstructor4() {
        // Typical use case: create an intervals set from a collection of sub-hyperplanes
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(2, intervalsSet.asList().size());
        assertEquals(0.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(0.0, intervalsSet.asList().get(0).getSup(), 0.0);
        assertEquals(1.0, intervalsSet.asList().get(1).getInf(), 0.0);
        assertEquals(1.0, intervalsSet.asList().get(1).getSup(), 0.0);
    }
    
    @Test
    public void testBuildNew() {
        // Typical use case: build new intervals set from a pre-built tree
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        BSPTree<Euclidean1D> tree = new BSPTree<>(true);
        
        IntervalsSet newSet = intervalsSet.buildNew(tree);
        assertEquals(tree, newSet.getTree(false));
    }
    
    @Test
    public void testGetInf() {
        // Typical use case: get the infimum of an intervals set
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup() {
        // Typical use case: get the supremum of an intervals set
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsList() {
        // Typical use case: convert intervals set to list of intervals
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(0.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(1.0, intervalsSet.asList().get(0).getSup(), 0.0);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        // Typical use case: compute geometrical properties of intervals set
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        intervalsSet.computeGeometricalProperties();
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(0.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(1.0, intervalsSet.asList().get(0).getSup(), 0.0);
    }
    
    @Test
    public void testRecurseList() {
        // Typical use case: recurse through the intervals set tree to create list of intervals
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        assertEquals(1, list.size());
        assertEquals(0.0, list.get(0).getInf(), 0.0);
        assertEquals(1.0, list.get(0).getSup(), 0.0);
    }

}