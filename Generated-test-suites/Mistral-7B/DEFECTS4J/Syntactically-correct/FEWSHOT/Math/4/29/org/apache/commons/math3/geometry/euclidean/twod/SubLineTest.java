package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test with Hyperplane and RemainingRegion
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1, 2), new Interval(3, 4));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test with Line and IntervalSet
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervalSet = new IntervalsSet(new Interval(1, 2), new Interval(3, 4));
        SubLine subLine1 = new SubLine(line, intervalSet);

        assertEquals(line, subLine1.getHyperplane());
        assertEquals(intervalSet, subLine1.getRemainingRegion());
    }

    @Test
    public void testSubLineGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        IntervalsSet intervalSet = new IntervalsSet(new Interval(0, 1), new Interval(2, 3));
        SubLine subLine = new SubLine(line, intervalSet);

        List<Segment> segments = subLine.getSegments();

        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(1, 1), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(2, 2), new Vector2D(3, 3), line), segments.get(1));
    }

    @Test
    public void testSubLineIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        line1.setHyperplane(new Line(new Vector2D(0, 0), new Vector2D(3, 3)));
        line2.setHyperplane(new Line(new Vector2D(1, 1), new Vector2D(3, 3)));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(2, 2), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(2, 2), intersectionPoint);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);

        List<Interval> intervals = intervalSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(new Interval(0, 1), intervals.get(0));
        assertEquals(new Interval(2, 3), intervals.get(1));
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        Side side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.MINUS, side);

        side = subLine2.side(subLine1.getHyperplane());
        assertEquals(Side.PLUS, side);

        line1.setHyperplane(new Line(new Vector2D(0, 0), new Vector2D(3, 3)));
        line2.setHyperplane(new Line(new Vector2D(1, 1), new Vector2D(3, 3)));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.PLUS, side);

        side = subLine2.side(subLine1.getHyperplane());
        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        assertNull(split);

        line1.setHyperplane(new Line(new Vector2D(0, 0), new Vector2D(3, 3)));
        line2.setHyperplane(new Line(new Vector2D(1, 1), new Vector2D(3, 3)));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        split = subLine1.split(subLine2.getHyperplane());
        SubLine subLinePlus = split.getSubHyperplane1();
        SubLine subLineMinus = split.getSubHyperplane2();

        assertEquals(subLine1, subLinePlus);
        assertEquals(subLine2, subLineMinus);

        List<Segment> segmentsPlus = subLinePlus.getSegments();
        assertEquals(1, segmentsPlus.size());
        assertEquals(new Segment(new Vector2D(1, 1), new Vector2D(2, 2), line1), segmentsPlus.get(0));

        List<Segment> segmentsMinus = subLineMinus.getSegments();
        assertEquals(1, segmentsMinus.size());
        assertEquals(new Segment(new Vector2D(3, 3), new Vector2D(3, 3), line1), segmentsMinus.get(0));
    }

    @Test
    public void testBuildNew() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1, 2), new Interval(3, 4));
        SubLine subLine = new SubLine(line, remainingRegion);

        SubLine newSubLine = SubLine.buildNew(subLine.getHyperplane(), subLine.getRemainingRegion());

        assertEquals(subLine, newSubLine);
    }
}