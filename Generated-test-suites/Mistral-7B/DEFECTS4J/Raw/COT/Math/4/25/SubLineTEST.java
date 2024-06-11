package org.apache.commons.math3.geometry.euclidean.twod.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Tests for {@link SubLine} class
 */
public class SubLineTest {

    /**
     * Test constructor with hyperplane and remaining region
     */
    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertArrayEquals(new double[] { 0.0, 1.0 }, subLine.getHyperplane().getParameters().getCoordinates(), 0.0);
        assertArrayEquals(new double[] { 0.0, 1.0 }, subLine.getRemainingRegion().asList().get(0).getExtremes(), 0.0);
    }

    /**
     * Test constructor with two endpoints
     */
    @Test
    public void testConstructorWithTwoEndpoints() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        assertArrayEquals(new double[] { start.getX(), start.getY(), end.getX(), end.getY() },
                         line.getParameters().getCoordinates(), 0.0);
        List<Interval> intervals = ((IntervalsSet) subLine.getRemainingRegion()).asList();
        assertArrayEquals(new double[] { 0.0, 1.0 }, intervals.get(0).getExtremes(), 0.0);
    }

    /**
     * Test constructor with a segment
     */
    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        Line line = (Line) subLine.getHyperplane();
        assertArrayEquals(new double[] { start.getX(), start.getY(), end.getX(), end.getY() },
                         line.getParameters().getCoordinates(), 0.0);
        List<Interval> intervals = ((IntervalsSet) subLine.getRemainingRegion()).asList();
        assertArrayEquals(new double[] { 0.0, 1.0 }, intervals.get(0).getExtremes(), 0.0);
    }

    /**
     * Test getSegments method
     */
    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 1.0);
        Segment segment1 = new Segment(start1, end1, new Line(start1, end1));
        Segment segment2 = new Segment(start2, end2, new Line(start2, end2));
        SubLine subLine = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 1.0)),
                new IntervalsSet(new Interval(0.0, 1.0), new Interval(1.0, 2.0)));
        List<Segment> segments = subLine.getSegments();
        assertArrayEquals(new Object[] { segment1, segment2 }, segments.toArray());
    }

    /**
     * Test intersection method with two sub-lines not intersecting
     */
    @Test
    public void testIntersectionWithNonIntersectingSubLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 2.0);
        Segment segment1 = new Segment(start1, end1, new Line(start1, end1));
        Segment segment2 = new Segment(start2, end2, new Line(start2, end2));
        SubLine subLine1 = new SubLine(segment1);
        SubLine subLine2 = new SubLine(segment2);
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));
    }

    /**
     * Test intersection method with two sub-lines intersecting at a point
     */
    @Test
    public void testIntersectionWithIntersectingSubLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(0.5, 0.5);
        Vector2D end2 = new Vector2D(1.5, 0.5);
        Segment segment1 = new Segment(start1, end1, new Line(start1, end1));
        Segment segment2 = new Segment(start2, end2, new Line(start2, end2));
        SubLine subLine1 = new SubLine(segment1);
        SubLine subLine2 = new SubLine(segment2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertArrayEquals(new double[] { 0.5, 0.5 }, intersection.toCoordinateArray(), 0.0);
        assertNull(subLine1.intersection(subLine2, false));
    }

    /**
     * Test intersection method with two sub-lines intersecting at endpoints
     */
    @Test
    public void testIntersectionWithIntersectingSubLinesEndpoints() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0 / 3.0, 2.0 / 3.0);
        Vector2D start2 = new Vector2D(2.0 / 3.0, 1.0 / 3.0);
        Vector2D end2 = new Vector2D(1.0, 0.0);
        Segment segment1 = new Segment(start1, end1, new Line(start1, end1));
        Segment segment2 = new Segment(start2, end2, new Line(start2, end2));
        SubLine subLine1 = new SubLine(segment1);
        SubLine subLine2 = new SubLine(segment2);
        assertNull(subLine1.intersection(subLine2, true));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertArrayEquals(new double[] { 1.0 / 3.0, 1.0 / 3.0 }, intersection.toCoordinateArray(), 0.0);
    }

    /**
     * Test intersection method with infinite lines
     */
    @Test
    public void testIntersectionWithInfiniteLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(Double.POSITIVE_INFINITY, 0.0);
        Vector2D start2 = new Vector2D(0.0, Double.POSITIVE_INFINITY);
        Vector2D end2 = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Segment segment1 = new Segment(start1, end1, new Line(start1, end1));
        Segment segment2 = new Segment(start2, end2, new Line(start2, end2));
        SubLine subLine1 = new SubLine(segment1);
        SubLine subLine2 = new SubLine(segment2);
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));
    }

    /**
     * Test side method with parallel lines
     */
    @Test
    public void testSideWithParallelLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.0, 0.5);
        Vector2D end2 = new Vector2D(2.0, 1.0);
        SubLine subLine1 = new SubLine(new Line(start1, end1));
        SubLine subLine2 = new SubLine(new Line(start2, end2));
        Side side = subLine1.side(subLine2.getHyperplane());
        assertArrayEquals(new double[] { 1.0, 0.0 }, subLine2.getHyperplane().getParameters().getCoordinates(), 0.0);
        assertEquals(Side.HYPER, side);
    }

    /**
     * Test side method with intersecting lines
     */
    @Test
    public void testSideWithIntersectingLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(0.5, 1.0);
        Vector2D end2 = new Vector2D(1.5, 0.5);
        SubLine subLine1 = new SubLine(new Line(start1, end1));
        SubLine subLine2 = new SubLine(new Line(start2, end2));
        Side side = subLine1.side(subLine2.getHyperplane());
        assertArrayEquals(new double[] { 0.5, 1.0 }, subLine2.getHyperplane().getParameters().getCoordinates(), 0.0);
        assertEquals(Side.MINUS, side);
    }

    /**
     * Test split method with parallel lines
     */
    @Test
    public void testSplitWithParallelLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.0, 0.5);
        Vector2D end2 = new Vector2D(2.0, 1.0);
        SubLine subLine1 = new SubLine(new Line(start1, end1));
        SubLine subLine2 = new SubLine(new Line(start2, end2));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector1D(1.0)).toHyperplane();
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        assertEquals(subLine1, split.getSubHyperplane1());
        assertEquals(subLine2, split.getSubHyperplane2());
    }

    /**
     * Test split method with intersecting lines
     */
    @Test
    public void testSplitWithIntersectingLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(0.5, 1.0);
        Vector2D end2 = new Vector2D(1.5, 0.5);
        SubLine subLine1 = new SubLine(new Line(start1, end1));
        SubLine subLine2 = new SubLine(new Line(start2, end2));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector1D(1.0)).toHyperplane();
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        assertEquals(new SubLine(new Line(start1, new Vector2D(0.5, 0.5)), new IntervalsSet(new Interval(0.0, 0.5))),
                split.getSubHyperplane1());
        assertEquals(new SubLine(new Line(start2, new Vector2D(1.5, 0.5)), new IntervalsSet(new Interval(0.5, 1.0))),
                split.getSubHyperplane2());
    }

}