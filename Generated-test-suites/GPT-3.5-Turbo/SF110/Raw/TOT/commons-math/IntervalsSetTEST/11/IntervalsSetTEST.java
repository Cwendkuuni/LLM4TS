
package org.apache.commons.math3.geometry.euclidean.oned;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testBuildTree() {
        // Test case: test with lower = Double.NEGATIVE_INFINITY and upper = Double.POSITIVE_INFINITY
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(1, intervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).count());
        
        // Test case: test with lower = Double.NEGATIVE_INFINITY and upper = 0.0
        intervalsSet = new IntervalsSet();
        assertEquals(2, intervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0).count());
        
        // Test case: test with lower = 0.0 and upper = Double.POSITIVE_INFINITY
        intervalsSet = new IntervalsSet();
        assertEquals(2, intervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY).count());
        
        // Test case: test with lower = 0.0 and upper = 0.0
        intervalsSet = new IntervalsSet();
        assertEquals(1, intervalsSet.buildTree(0.0, 0.0).count());
        
        // Test case: test with lower = 1.0 and upper = 2.0
        intervalsSet = new IntervalsSet();
        assertEquals(3, intervalsSet.buildTree(1.0, 2.0).count());
    }
    
    @Test
    public void testBuildNew() {
        // Test case: test with tree containing one interval
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(tree, newIntervalsSet.getTree());
        
        // Test case: test with tree containing multiple intervals
        tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), tree, tree, null);
        intervalsSet = new IntervalsSet();
        newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(tree, newIntervalsSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        // Test case: test with tree containing no cut
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        
        // Test case: test with tree containing cuts
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), tree, tree, null);
        tree.insertCut(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), tree, tree, null);
        intervalsSet = new IntervalsSet(tree);
        intervalsSet.computeGeometricalProperties();
        assertEquals(2.0, intervalsSet.getSize(), 0.0);
    }
    
    @Test
    public void testGetInf() {
        // Test case: test with tree containing no cut
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        
        // Test case: test with tree containing cuts
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), tree, tree, null);
        tree.insertCut(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), tree, tree, null);
        intervalsSet = new IntervalsSet(tree);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup() {
        // Test case: test with tree containing no cut
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        
        // Test case: test with tree containing cuts
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), tree, tree, null);
        tree.insertCut(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), tree, tree, null);
        intervalsSet = new IntervalsSet(tree);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsList() {
        // Test case: test with tree containing no cut
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(expectedIntervals, intervalsSet.asList());
        
        // Test case: test with tree containing cuts
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), tree, tree, null);
        tree.insertCut(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), tree, tree, null);
        intervalsSet = new IntervalsSet(tree);
        expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(Double.NEGATIVE_INFINITY, 1.0));
        expectedIntervals.add(new Interval(1.0, 2.0));
        expectedIntervals.add(new Interval(2.0, Double.POSITIVE_INFINITY));
        assertEquals(expectedIntervals, intervalsSet.asList());
    }
    
}
