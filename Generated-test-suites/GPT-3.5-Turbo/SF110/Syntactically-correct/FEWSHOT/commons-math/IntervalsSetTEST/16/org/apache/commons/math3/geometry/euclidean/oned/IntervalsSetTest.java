package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IntervalsSetTest {
    
    @Test
    public void testBuildTree() {
        BSPTree<Euclidean1D> expectedTree1 = new BSPTree<Euclidean1D>(Boolean.TRUE);
        BSPTree<Euclidean1D> expectedTree2 = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane(),
                new BSPTree<Euclidean1D>(Boolean.FALSE),
                new BSPTree<Euclidean1D>(Boolean.TRUE),
                null);
        BSPTree<Euclidean1D> expectedTree3 = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(-1.0), false).wholeHyperplane(),
                new BSPTree<Euclidean1D>(Boolean.FALSE),
                new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
                        new BSPTree<Euclidean1D>(Boolean.FALSE),
                        new BSPTree<Euclidean1D>(Boolean.TRUE),
                        null),
                null);
                
        BSPTree<Euclidean1D> actualTree1 = IntervalsSet.buildTree(Double.POSITIVE_INFINITY, 0.0);
        BSPTree<Euclidean1D> actualTree2 = IntervalsSet.buildTree(0.0, 0.0);
        BSPTree<Euclidean1D> actualTree3 = IntervalsSet.buildTree(-1.0, 1.0);
        
        assertEquals(expectedTree1, actualTree1);
        assertEquals(expectedTree2, actualTree2);
        assertEquals(expectedTree3, actualTree3);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet expectedSet = new IntervalsSet(tree);
        
        IntervalsSet actualSet = new IntervalsSet().buildNew(tree);
        
        assertEquals(expectedSet, actualSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
        
        int SIZE = 4;
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(-1.0, 0.0));
        intervals.add(new Interval(0.0, 1.0));
        intervals.add(new Interval(2.0, 3.0));
        intervals.add(new Interval(3.0, 4.0));

        intervalsSet = new IntervalsSet(intervals);
        intervalsSet.computeGeometricalProperties();
        double expectedSize = intervals.get(0).getSize() + intervals.get(1).getSize() + intervals.get(2).getSize() + intervals.get(3).getSize();
        double expectedBarycenter = (intervals.get(0).getSize() * intervals.get(0).getBarycenter() +
                intervals.get(1).getSize() * intervals.get(1).getBarycenter() +
                intervals.get(2).getSize() * intervals.get(2).getBarycenter() +
                intervals.get(3).getSize() * intervals.get(3).getBarycenter()) / expectedSize;
        
        assertEquals(expectedBarycenter, intervalsSet.getBarycenter().getX(), 0.0);
        assertEquals(expectedSize, intervalsSet.getSize(), 0.0);
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet emptySet = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, emptySet.getInf(), 0.0);
        
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0));
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet emptySet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, emptySet.getSup(), 0.0);
        
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0));
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet emptySet = new IntervalsSet();
        List<Interval> expectedEmptyList = new ArrayList<>();
        List<Interval> actualEmptyList = emptySet.asList();
        
        assertEquals(expectedEmptyList, actualEmptyList);
        
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(0.0, 1.0));
        intervals.add(new Interval(1.0, 2.0));
        IntervalsSet intervalsSet = new IntervalsSet(intervals);
        List<Interval> expectedList = new ArrayList<>(intervals);
        List<Interval> actualList = intervalsSet.asList();
        
        assertEquals(expectedList, actualList);
    }

}