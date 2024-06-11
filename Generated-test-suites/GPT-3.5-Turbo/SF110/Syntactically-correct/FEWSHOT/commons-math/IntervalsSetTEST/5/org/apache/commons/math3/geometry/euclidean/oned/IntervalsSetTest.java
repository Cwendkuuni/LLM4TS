package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IntervalsSetTest {
    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getTree());
    }
    
    @Test
    public void testConstructor2() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        // assert statements
    }
    
    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        // assert statements
    }
    
    @Test
    public void testConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        // assert statements
    }
    
    @Test
    public void testBuildTree1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> result = intervalsSet.buildTree(Double.POSITIVE_INFINITY, 0.0);
        // assert statements
    }
    
    @Test
    public void testBuildTree2() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> result = intervalsSet.buildTree(1.0, Double.POSITIVE_INFINITY);
        // assert statements
    }
    
    @Test
    public void testBuildTree3() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> result = intervalsSet.buildTree(1.0, 2.0);
        // assert statements
    }
    
    @Test
    public void testBuildTree4() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> result = intervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        // assert statements
    }
    
    @Test
    public void testBuildNew() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet result = intervalsSet.buildNew(tree);
        // assert statements
    }
    
    @Test
    public void testComputeGeometricalProperties1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // assert statements
    }
    
    @Test
    public void testComputeGeometricalProperties2() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // assert statements
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double result = intervalsSet.getInf();
        // assert statements
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double result = intervalsSet.getSup();
        // assert statements
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> result = intervalsSet.asList();
        // assert statements
    }
    
    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> node = new BSPTree<>();
        List<Interval> list = new ArrayList<>();
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        intervalsSet.recurseList(node, list, lower, upper);
        // assert statements
    }
}