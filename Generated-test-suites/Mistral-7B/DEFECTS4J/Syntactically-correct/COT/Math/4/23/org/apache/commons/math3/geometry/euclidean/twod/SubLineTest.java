package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for SubLine.
 */
public class SubLineTest {

    /**
     * Basic test for constructor with Hyperplane and RemainingRegion.
     */
    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 2), new Interval(3, 4));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        Assert.assertEquals(hyperplane, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Basic test for constructor with two endpoints.
     */
    @Test
    public void testConstructorWithTwoEndpoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        Assert.assertEquals(line.getStart(), start);
        Assert.assertEquals(line.getEnd(), end);
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(segments.size(), 1);
        Segment segment = segments.get(0);
        Assert.assertEquals(segment.getStart(), start);
        Assert.assertEquals(segment.getEnd(), end);
    }

    /**
     * Basic test for constructor with a segment.
     */
    @Test
    public void testConstructorWithSegment() {
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine = new SubLine(segment);
        Line line = (Line) subLine.getHyperplane();
        Assert.assertEquals(line.getStart(), segment.getStart());
        Assert.assertEquals(line.getEnd(), segment.getEnd());
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(segments.size(), 1);
        Segment segmentTest = segments.get(0);
        Assert.assertEquals(segmentTest.getStart(), segment.getStart());
        Assert.assertEquals(segmentTest.getEnd(), segment.getEnd());
    }

    /**
     * Test for getting the endpoints of the sub-line.
     */
    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 2), new Interval(3, 4));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(segments.size(), 2);
        Segment firstSegment = segments.get(0);
        Assert.assertEquals(firstSegment.getStart(), new Vector2D(0, FastMath.POSITIVE_INFINITY));
        Assert.assertEquals(firstSegment.getEnd(), new Vector2D(2, FastMath.POSITIVE_INFINITY));
        Segment secondSegment = segments.get(1);
        Assert.assertEquals(secondSegment.getStart(), new Vector2D(3, FastMath.NEGATIVE_INFINITY));
        Assert.assertEquals(secondSegment.getEnd(), new Vector2D(4, FastMath.NEGATIVE_INFINITY));
    }

    /**
     * Test for intersection method with includeEndPoints set to true.
     */
    @Test
    public void testIntersectionWithIncludeEndPointsTrue() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(2, 3));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 3), new Interval(1, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertNotNull(intersectionPoint);
        Assert.assertEquals(intersectionPoint.getX(), 1);
        Assert.assertEquals(intersectionPoint.getY(), 1);
    }

    /**
     * Test for intersection method with includeEndPoints set to false.
     */
    @Test
    public void testIntersectionWithIncludeEndPointsFalse() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(2, 3));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 3), new Interval(1, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersectionPoint);
    }

    /**
     * Test for intersection method with parallel lines.
     */
    @Test
    public void testIntersectionWithParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(2, 3));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 2), new Interval(3, 4));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertNull(intersectionPoint);
    }

    /**
     * Test for side method with another sub-line on the same side.
     */
    @Test
    public void testSideWithSameSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(2, 3));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 3), new Interval(1, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Side side = subLine1.side(subLine2.getHyperplane());
        Assert.assertEquals(side, Side.MINUS);
    }

    /**
     * Test for side method with another sub-line on the opposite side.
     */
    @Test
    public void testSideWithOppositeSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, -1);
        Vector2D end2 = new Vector2D(2, -2);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(2, 3));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 3), new Interval(1, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Side side = subLine1.side(subLine2.getHyperplane());
        Assert.assertEquals(side, Side.PLUS);
    }

    /**
     * Test for split method with another hyperplane on the same side.
     */
    @Test
    public void testSplitWithSameSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(2, 3));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 3), new Interval(1, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Hyperplane<Euclidean1D> expectedHyperplane1 = new Line(new Vector1D(0), new Vector1D(1));
        Hyperplane<Euclidean1D> expectedHyperplane2 = new Line(new Vector1D(1), new Vector1D(2));
        SubHyperplane<Euclidean2D> actualHyperplane1 = subLine1.split(hyperplane2);
        SubHyperplane<Euclidean2D> actualHyperplane2 = subLine2.split(hyperplane1);
        Assert.assertEquals(actualHyperplane1.getHyperplane(), expectedHyperplane1);
        Assert.assertEquals(actualHyperplane2.getHyperplane(), expectedHyperplane2);
    }

    /**
     * Test for split method with another hyperplane on different sides.
     */
    @Test
    public void testSplitWithDifferentSides() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, -1);
        Vector2D end2 = new Vector2D(2, -2);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(2, 3));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 3), new Interval(1, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Hyperplane<Euclidean1D> expectedHyperplane1 = new Line(new Vector1D(0), new Vector1D(1));
        Hyperplane<Euclidean1D> expectedHyperplane2 = new Line(new Vector1D(1), new Vector1D(2));
        SubHyperplane<Euclidean2D> actualHyperplane1 = subLine1.split(hyperplane2);
        SubHyperplane<Euclidean2D> actualHyperplane2 = subLine2.split(hyperplane1);
        Assert.assertEquals(actualHyperplane1.getLeftSubHyperplane(), null);
        Assert.assertEquals(actualHyperplane1.getRightSubHyperplane(), subLine2);
        Assert.assertEquals(actualHyperplane2.getLeftSubHyperplane(), subLine1);
        Assert.assertEquals(actualHyperplane2.getRightSubHyperplane(), null);
    }

}