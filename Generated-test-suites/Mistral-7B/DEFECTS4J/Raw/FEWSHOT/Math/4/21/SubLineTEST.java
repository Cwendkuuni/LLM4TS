package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Interval;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
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
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet remainingRegion = new IntervalsSet(new Interval(0.5, 1.5));
        SubLine subLine = new SubLine(line, remainingRegion);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor with start and end points.
     */
    @Test
    public void testConstructorWithStartAndEndPoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());
    }

    /**
     * Test constructor with segment.
     */
    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());
    }

    /**
     * Test getSegments().
     */
    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet remainingRegion = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(startVector(0.5, 0.5), segment1.getStart());
        assertEquals(endVector(1.0, 1.0), segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(startVector(2.5, 2.5), segment2.getStart());
        assertEquals(endVector(3.0, 3.0), segment2.getEnd());
    }

    /**
     * Test intersection with another sub-line.
     */
    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());

        // intersection point does not belong to both sub-lines
        Vector2D point = new Vector2D(0.5, 0.5);
        assertNull(subLine1.intersection(subLine2, false));
        assertNull(subLine2.intersection(subLine1, false));

        // intersection point belongs to both sub-lines
        point = new Vector2D(1.5, 0.5);
        assertEquals(point, subLine1.intersection(subLine2, true));
        assertEquals(point, subLine2.intersection(subLine1, true));
    }

    /**
     * Test buildIntervalSet().
     */
    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assertEquals(2, intervalSet.getNumberOfIntervals());
        Interval interval1 = intervalSet.getInterval(0);
        Interval interval2 = intervalSet.getInterval(1);
        assertEquals(start.getX(), interval1.getInf(), 1e-12);
        assertEquals(end.getX(), interval2.getSup(), 1e-12);
    }

    /**
     * Test side().
     */
    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());

        Side side = subLine1.side(line2);
        assertEquals(Side.MINUS, side);

        side = subLine2.side(line1);
        assertEquals(Side.PLUS, side);
    }

    /**
     * Test split().
     */
    @Test
    public void testSplit() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet remainingRegion = new IntervalsSet(new Interval(0.5, 1.5));
        SubLine subLine = new SubLine(line, remainingRegion);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0), new Vector2D(0.5, 1));
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(0.5), true).wholeHyperplane();
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        assertEquals(new SubLine(line, new IntervalsSet(subHyperplane.getSubHyperplane())), splitSubHyperplane.getSubHyperplaneA());
        assertEquals(new SubLine(line, new IntervalsSet(subHyperplane.getSubHyperplane().getComplement())),
                     splitSubHyperplane.getSubHyperplaneB());
    }

    private Vector2D endVector(double x, double y) {
        return new Vector2D(x, y);
    }

    private Vector2D startVector(double x, double y) {
        return new Vector2D(x, y);
    }

}