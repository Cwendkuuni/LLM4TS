package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SubLine.
 */
public class SubLineTest {

    /**
     * Test constructor SubLine(Hyperplane hyperplane, Region remainingRegion).
     */
    @Test
    public void testConstructorHyperplaneAndRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1)).getHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 2), new Interval(3, 5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor SubLine(Vector2D start, Vector2D end).
     */
    @Test
    public void testConstructorStartAndEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    /**
     * Test constructor SubLine(Segment segment).
     */
    @Test
    public void testConstructorSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        assertEquals(segment.getLine(), subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    /**
     * Test method getSegments().
     */
    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(new Interval(0, 2), new Interval(3, 5));
        SubLine subLine = new SubLine(line, region);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(2, 0), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(3, 0), new Vector2D(5, 1), line), segments.get(1));
    }

    /**
     * Test method intersection(SubLine subLine, boolean includeEndPoints).
     */
    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(2, 3)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        Vector2D expectedIntersection = new Vector2D(1, 1);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(expectedIntersection, intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertNotNull(intersection);
        assertEquals(expectedIntersection, intersection);
        intersection = subLine1.intersection(subLine2, true);
        subLine1.getRemainingRegion().remove(subLine1.getSegments().get(0).getInterval());
        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    /**
     * Helper method to build an interval set from two points.
     * @param start start point
     * @param end end point
     * @return an interval set
     */
    private IntervalsSet buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(),
                               line.toSubSpace(end).getX());
    }

}