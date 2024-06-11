package org.apache.commons.math3.geometry.euclidean.twod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
        SubLine subLine = new SubLine(line, new IntervalsSet(1, 3));
        
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        
        Segment segment = segments.get(0);
        assertEquals(new Vector2D(1, 2), segment.getStart());
        assertEquals(new Vector2D(3, 4), segment.getEnd());
        assertEquals(line, segment.getLine());
    }
    
    @Test
    public void testIntersection_includeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(0, 1));
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }
    
    @Test
    public void testIntersection_notIncludeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(0, 1));
        
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }
    
    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        
        List<Interval> intervals = intervalSet.asList();
        assertEquals(1, intervals.size());
        
        Interval interval = intervals.get(0);
        assertEquals(1.0, interval.getInf(), 1e-10);
        assertEquals(3.0, interval.getSup(), 1e-10);
    }
    
    @Test
    public void testBuildNew() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervalSet = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, intervalSet);
        
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> newSubLine = subLine.buildNew(line, intervalSet);
        assertEquals(subLine, newSubLine);
    }
    
    @Test
    public void testSide_parallelLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine = new SubLine(line1, new IntervalsSet(0, 1));
        
        Side side = subLine.side(line2);
        assertEquals(Side.HYPER, side);
    }
    
    @Test
    public void testSide_intersectingLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        SubLine subLine = new SubLine(line1, new IntervalsSet(0, 1));
        
        Side side = subLine.side(line2);
        assertEquals(Side.BOTH, side);
    }
    
    @Test
    public void testSplit_parallelLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine = new SubLine(line1, new IntervalsSet(0, 1));
        
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine.split(line2);
        assertNull(splitSubLine.getPlus());
        assertEquals(subLine, splitSubLine.getMinus());
    }
    
    @Test
    public void testSplit_intersectingLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        SubLine subLine = new SubLine(line1, new IntervalsSet(0, 1));
        
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine.split(line2);
        SubLine plus = (SubLine) splitSubLine.getPlus();
        SubLine minus = (SubLine) splitSubLine.getMinus();
        
        assertEquals(Side.PLUS, plus.side(line2));
        assertEquals(Side.MINUS, minus.side(line2));
    }
    
}