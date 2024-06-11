import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;

import java.util.ArrayList;
import java.util.List;

public class IntervalsSetTest {
    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        Assert.assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        Assert.assertEquals(0.0, intervalsSet.getSize(), 0.0);
        Assert.assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        Assert.assertEquals(new ArrayList<Interval>(), intervalsSet.asList());
    }
    
    @Test
    public void testConstructor2() {
        double lower = 1.0;
        double upper = 5.0;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        Assert.assertEquals(lower, intervalsSet.getInf(), 0.0);
        Assert.assertEquals(upper, intervalsSet.getSup(), 0.0);
        Assert.assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        Assert.assertEquals(new Vector1D((lower + upper) / 2), intervalsSet.getBarycenter());
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(lower, upper));
        Assert.assertEquals(intervals, intervalsSet.asList());
    }
    
    @Test
    public void testConstructor3() {
        double lower = -10.0;
        double upper = -5.0;
        
        BSPTree<Euclidean1D> tree = new BSPTree<>(true);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        Assert.assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        Assert.assertEquals(0.0, intervalsSet.getSize(), 0.0);
        Assert.assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        Assert.assertEquals(new ArrayList<Interval>(), intervalsSet.asList());
    }
    
    @Test
    public void testConstructor4() {
        double lower1 = 0.0;
        double upper1 = 2.0;
        double lower2 = 4.0;
        double upper2 = 6.0;
        
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(lower1), false).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(upper1), true).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(lower2), false).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(upper2), true).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        
        Assert.assertEquals(lower1, intervalsSet.getInf(), 0.0);
        Assert.assertEquals(upper2, intervalsSet.getSup(), 0.0);
        Assert.assertEquals(upper2 - lower1, intervalsSet.getSize(), 0.0);
        Assert.assertEquals(new Vector1D((lower1 + upper2) / 2), intervalsSet.getBarycenter());
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(lower1, upper1));
        intervals.add(new Interval(lower2, upper2));
        Assert.assertEquals(intervals, intervalsSet.asList());
    }
    
    @Test
    public void testBuildTree() {
        double lower = -5.0;
        double upper = 5.0;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        
        Assert.assertEquals(IntervalsSet.class, tree.getClass());
        Assert.assertEquals(lower, tree.getInf(), 0.0);
        Assert.assertEquals(upper, tree.getSup(), 0.0);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(true);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        
        Assert.assertEquals(tree, intervalsSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalProperties1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        
        Assert.assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        Assert.assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }
    
    @Test
    public void testComputeGeometricalProperties2() {
        double lower = 2.0;
        double upper = 4.0;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        
        Assert.assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        Assert.assertEquals(new Vector1D((lower + upper) / 2), intervalsSet.getBarycenter());
    }

    @Test
    public void testGetInf() {
        double lower = -3.0;
        double upper = 3.0;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        Assert.assertEquals(lower, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup() {
        double lower = -3.0;
        double upper = 3.0;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        Assert.assertEquals(upper, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsList() {
        double lower1 = -3.0;
        double upper1 = -1.0;
        double lower2 = 2.0;
        double upper2 = 4.0;
        
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = intervalsSet.asList();
        List<Interval> expectedIntervals = new ArrayList<>();
        Assert.assertEquals(expectedIntervals, intervals);
        
        intervalsSet = new IntervalsSet(lower1, upper1);
        intervals = intervalsSet.asList();
        expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower1, upper1));
        Assert.assertEquals(expectedIntervals, intervals);
        
        intervalsSet = new IntervalsSet(lower1, upper1);
        intervals = intervalsSet.asList();
        expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower1, upper1));
        Assert.assertEquals(expectedIntervals, intervals);
        
        intervalsSet = new IntervalsSet(lower1, upper1);
        intervals = intervalsSet.asList();
        expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower1, upper1));
        Assert.assertEquals(expectedIntervals, intervals);
        
        intervalsSet = new IntervalsSet(lower1, upper1);
        intervals = intervalsSet.asList();
        expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower1, upper1));
        Assert.assertEquals(expectedIntervals, intervals);
        
        intervalsSet = new IntervalsSet(lower1, upper1);
        intervals = intervalsSet.asList();
        expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower1, upper1));
        Assert.assertEquals(expectedIntervals, intervals);
        
        intervalsSet = new IntervalsSet(lower1, upper1);
        intervals = intervalsSet.asList();
        expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower1, upper1));
        Assert.assertEquals(expectedIntervals, intervals);
    }
}