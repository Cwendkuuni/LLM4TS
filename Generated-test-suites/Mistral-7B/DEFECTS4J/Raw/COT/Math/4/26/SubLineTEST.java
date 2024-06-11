package org.apache.commons.math3.geometry.euclidean.twod.test;

import junit.framework.TestCase;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;

/**
 * Test cases for {@link SubLine}.
 */
public class SubLineTest extends TestCase {

    /**
     * Test constructor with hyperplane and remaining region.
     */
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0, 0), new Vector2D(1, 0)));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 2));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNotNull(subLine);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
        assertEquals(getExpectedSegments(hyperplane), subLine.getSegments());
    }

    /**
     * Test constructor with two endpoints.
     */
    public void testConstructorWithTwoEndpoints() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end   = new Vector2D(3, 3);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
        assertEquals(getExpectedHyperplane(start, end), subLine.getHyperplane());
        assertEquals(getExpectedIntervalSet(start, end), subLine.getRemainingRegion());
        assertEquals(getExpectedSegments(start, end), subLine.getSegments());
    }

    /**
     * Test constructor with a segment.
     */
    public void testConstructorWithSegment() {
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(2, 0)));
        SubLine subLine = new SubLine(segment);
        assertNotNull(subLine);
        assertEquals(segment.getLine(), subLine.getHyperplane());
        assertEquals(segment.getIntervalSet(), subLine.getRemainingRegion());
        assertEquals(getExpectedSegments(segment), subLine.getSegments());
    }

    /**
     * Test getSegments method.
     */
    public void testGetSegments() {
        Segment segment1 = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(2, 0)));
        SubLine subLine1 = new SubLine(segment1);
        List<Segment> segments1 = subLine1.getSegments();
        assertEquals(1, segments1.size());
        assertEquals(segment1, segments1.get(0));

        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 2), new Interval(3, 4));
        SubLine subLine2 = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments2 = subLine2.getSegments();
        assertEquals(2, segments2.size());
        assertEquals(getExpectedSegment(0, new Vector2D(0, 0), new Vector2D(1, 1)), segments2.get(0));
        assertEquals(getExpectedSegment(1, new Vector2D(3, 3), new Vector2D(4, 4)), segments2.get(1));
    }

    /**
     * Test intersection method with another sub-line and include endpoints.
     */
    public void testIntersectionWithAnotherSubLineAndIncludeEndpoints() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0, 0), new Vector2D(1, 0)));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(1, 0), new Vector2D(2, 1)));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 3));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(1, 1), intersection);
    }

    /**
     * Test intersection method with another sub-line and don't include endpoints.
     */
    public void testIntersectionWithAnotherSubLineAndDontIncludeEndpoints() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0, 0), new Vector2D(1, 0)));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(1, 0), new Vector2D(2, 1)));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1, 3));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    /**
     * Test intersection method with another line.
     */
    public void testIntersectionWithAnotherLine() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Vector2D intersection = subLine1.intersection(line2, true);
        assertNull(intersection);

        line2 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        intersection = subLine1.intersection(line2, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    /**
     * Test getExpectedSegments method.
     * @param hyperplane the hyperplane
     * @return the expected segments
     */
    private List<Segment> getExpectedSegments(Hyperplane<Euclidean2D> hyperplane) {
        Line line = (Line) hyperplane;
        Interval interval = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        IntervalsSet intervalSet = new IntervalsSet(interval);
        return getExpectedSegments(line, intervalSet);
    }

    /**
     * Test getExpectedSegments method.
     * @param start the start point
     * @param end the end point
     * @return the expected segments
     */
    private List<Segment> getExpectedSegments(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        Interval interval = new Interval(line.toSpace(start).getX(), line.toSpace(end).getX());
        IntervalsSet intervalSet = new IntervalsSet(interval);
        return getExpectedSegments(line, intervalSet);
    }

    /**
     * Test getExpectedSegments method.
     * @param segment the segment
     * @return the expected segments
     */
    private List<Segment> getExpectedSegments(Segment segment) {
        Line line = segment.getLine();
        Interval interval = new Interval(line.toSpace(segment.getStart()).getX(), line.toSpace(segment.getEnd()).getX());
        IntervalsSet intervalSet = new IntervalsSet(interval);
        return getExpectedSegments(line, intervalSet);
    }

    /**
     * Test getExpectedSegments method.
     * @param line the line
     * @param intervalSet the interval set
     * @return the expected segments
     */
    private List<Segment> getExpectedSegments(Line line, IntervalsSet intervalSet) {
        List<Segment> segments = new ArrayList<>();
        for (Interval interval : intervalSet.asList()) {
            Vector2D start = line.toSpace(new Vector1D(interval.getInf()));
            Vector2D end   = line.toSpace(new Vector1D(interval.getSup()));
            segments.add(new Segment(start, end, line));
        }
        return segments;
    }

    /**
     * Test getExpectedSegment method.
     * @param index the index of the segment
     * @param start the start point
     * @param end the end point
     * @return the expected segment
     */
    private Segment getExpectedSegment(int index, Vector2D start, Vector2D end) {
        return new Segment(start, end, new Line(start, end));
    }

    /**
     * Test getExpectedHyperplane method.
     * @param start the start point
     * @param end the end point
     * @return the expected hyperplane
     */
    private Hyperplane<Euclidean2D> getExpectedHyperplane(Vector2D start, Vector2D end) {
        return new Line(start, end);
    }

    /**
     * Test getExpectedIntervalSet method.
     * @param start the start point
     * @param end the end point
     * @return the expected interval set
     */
    private Region<Euclidean1D> getExpectedIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
    }

}