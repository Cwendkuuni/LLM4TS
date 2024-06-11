package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntervalsSetTest {
    
    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.asList().isEmpty());
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Double.NaN, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testConstructor2() {
        double lower = -10.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        assertEquals(1, intervalsSet.asList().size());
        
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(lower, interval.getInf(), 0.0);
        assertEquals(upper, interval.getSup(), 0.0);
        assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        assertEquals((upper + lower) / 2.0, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testConstructor3() {
        double lower = -10.0;
        double upper = 10.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        assertEquals(1, intervalsSet.asList().size());
        
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(lower, interval.getInf(), 0.0);
        assertEquals(upper, interval.getSup(), 0.0);
        assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        assertEquals((upper + lower) / 2.0, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testConstructor4() {
        double lower = -10.0;
        double upper = 10.0;
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(lower), false).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        
        assertEquals(1, intervalsSet.asList().size());
        
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(lower, interval.getInf(), 0.0);
        assertEquals(upper, interval.getSup(), 0.0);
        assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        assertEquals((upper + lower) / 2.0, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testBuildNew() {
        double lower = -10.0;
        double upper = 10.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        
        assertEquals(intervalsSet.asList().size(), newIntervalsSet.asList().size());
        for (int i = 0; i < intervalsSet.asList().size(); i++) {
            assertEquals(intervalsSet.asList().get(i).getInf(), newIntervalsSet.asList().get(i).getInf(), 0.0);
            assertEquals(intervalsSet.asList().get(i).getSup(), newIntervalsSet.asList().get(i).getSup(), 0.0);
        }
        assertEquals(intervalsSet.getSize(), newIntervalsSet.getSize(), 0.0);
        assertEquals(intervalsSet.getBarycenter().getX(), newIntervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        double lower = -10.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
        assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        assertEquals((upper + lower) / 2.0, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testGetInf1() {
        double lower = -10.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        assertEquals(lower, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetInf2() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup1() {
        double lower = -10.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        assertEquals(upper, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testGetSup2() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsList1() {
        double lower = -10.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        List<Interval> intervalList = intervalsSet.asList();
        
        assertEquals(1, intervalList.size());
        
        Interval interval = intervalList.get(0);
        assertEquals(lower, interval.getInf(), 0.0);
        assertEquals(upper, interval.getSup(), 0.0);
    }
    
    @Test
    public void testAsList2() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        List<Interval> intervalList = intervalsSet.asList();
        
        assertTrue(intervalList.isEmpty());
    }
    
    @Test
    public void testRecurseList() {
        double lower = -10.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        List<Interval> intervalList = new ArrayList<>();
        intervalsSet.recurseList(intervalsSet.getTree(false), intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        assertEquals(1, intervalList.size());
        
        Interval interval = intervalList.get(0);
        assertEquals(lower, interval.getInf(), 0.0);
        assertEquals(upper, interval.getSup(), 0.0);
    }
}