package org.apache.commons.math3.geometry.euclidean.twod.tests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.junit.Test;

/**
 * Test cases for {@link SubLine}.
 */
public class SubLineTest {

    /**
     * Test constructor with hyperplane and remaining region.
     */
    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).getHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertSame(hyperplane, subLine.getHyperplane());
        assertSame(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor with two endpoints.
     */
    @Test
    public void testConstructorWithTwoEndpoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        Line line = (Line) subLine.getHyperplane();
        assertSame(line.getStart(), start);
        assertSame(line.getEnd(), end);

        IntervalsSet intervalSet = (IntervalsSet) subLine.getRemainingRegion();
        List<Interval> intervals = intervalSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(start.getX(), interval.getInf(), 1e-9);
        assertEquals(end.getX(), interval.getSup(), 1e-9);
    }

    /**
     * Test constructor with a segment.
     */
    @Test
    public void testConstructorWithSegment() {
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine = new SubLine(segment);

        Line line = (Line) subLine.getHyperplane();
        assertSame(line, segment.getLine());

        IntervalsSet intervalSet = (IntervalsSet) subLine.getRemainingRegion();
        List<Interval> intervals = intervalSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(segment.getStart().getX(), interval.getInf(), 1e-9);
        assertEquals(segment.getEnd().getX(), interval.getSup(), 1e-9);
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2));
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0, 0), segment1.getStart());
        assertEquals(new Vector2D(1, 0), segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(1.5, 0), segment2.getStart());
        assertEquals(new Vector2D(2, 0), segment2.getEnd());
    }

    /**
     * Test intersection method with another sub-line on the same line.
     */
    @Test
    public void testIntersectionOnTheSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0, 1));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertSame(new Vector2D(0.5, 0), intersectionPoint);
    }

    /**
     * Test intersection method with another sub-line not on the same line.
     */
    @Test
    public void testIntersectionNotOnTheSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 2));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        assertNull(subLine1.intersection(subLine2, true));
    }

    /**
     * Test intersection method with another sub-line with endpoints on the instance.
     */
    @Test
    public void testIntersectionWithEndpointsOnTheInstance() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 2));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        assertNull(subLine1.intersection(subLine2, false));
    }

    /**
     * Test intersection method with another sub-line with endpoints inside the instance.
     */
    @Test
    public void testIntersectionWithEndpointsInsideTheInstance() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(0.5, 0), new Vector2D(1.5, 0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.5, 1.5));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    /**
     * Test intersection method with another sub-line with endpoints outside the instance.
     */
    @Test
    public void testIntersectionWithEndpointsOutsideTheInstance() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(1.5, 0), new Vector2D(2, 0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.5, 2));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        assertNull(subLine1.intersection(subLine2, true));
    }

    /**
     * Test side method with a hyperplane on the same line.
     */
    @Test
    public void testSideOnTheSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5)));

        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Side side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.MINUS, side);
    }

    /**
     * Test side method with a hyperplane not on the same line.
     */
    @Test
    public void testSideNotOnTheSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5)));

        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Side side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.HYPER, side);
    }

    /**
     * Test split method with a hyperplane on the same line.
     */
    @Test
    public void testSplitOnTheSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5)));

        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Hyperplane<Euclidean2D> hyperplane = subLine2.getHyperplane();
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);

        SubLine subLinePlus = (SubLine) split.getPlus();
        assertSame(line1, subLinePlus.getHyperplane());
        IntervalsSet intervalsPlus = (IntervalsSet) subLinePlus.getRemainingRegion();
        List<Interval> intervals = intervalsPlus.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(new Vector2D(0, 0.5).getX(), interval.getInf(), 1e-9);
        assertEquals(new Vector2D(0.5, 0).getX(), interval.getSup(), 1e-9);

        SubLine subLineMinus = (SubLine) split.getMinus();
        assertSame(line1, subLineMinus.getHyperplane());
        intervals = ((IntervalsSet) subLineMinus.getRemainingRegion()).asList();
        assertEquals(1, intervals.size());
        interval = intervals.get(0);
        assertEquals(new Vector2D(0.5, 0).getX(), interval.getInf(), 1e-9);
        assertEquals(new Vector2D(1, 0).getX(), interval.getSup(), 1e-9);
    }

    /**
     * Test split method with a hyperplane not on the same line.
     */
    @Test
    public void testSplitNotOnTheSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5)));

        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Hyperplane<Euclidean2D> hyperplane = subLine2.getHyperplane();
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);

        assertNull(split.getPlus());
        assertNull(split.getMinus());
    }

}