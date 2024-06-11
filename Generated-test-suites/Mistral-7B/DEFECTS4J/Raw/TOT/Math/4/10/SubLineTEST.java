package org.apache.commons.math3.geometry.euclidean.twod.tests;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    /**
     * Test construction from a line and an interval set.
     */
    @Test
    public void testConstructFromLineAndIntervalSet() {
        EuclideanSpace space = new EuclideanSpace2D();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet iset = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0));
        SubLine subLine = new SubLine(hyperplane, iset);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(iset, subLine.getRemainingRegion());
    }

    /**
     * Test construction from two endpoints.
     */
    @Test
    public void testConstructFromEndpoints() {
        EuclideanSpace space = new EuclideanSpace2D();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        IntervalsSet iset = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
        SubLine subLine = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(iset, subLine.getRemainingRegion());
    }

    /**
     * Test construction from a segment.
     */
    @Test
    public void testConstructFromSegment() {
        EuclideanSpace space = new EuclideanSpace2D();
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end   = new Vector2D(2.0, 2.0);
        Segment segment = new Segment(start, end);
        Hyperplane<Euclidean2D> hyperplane = segment.getLine();
        IntervalsSet iset = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
        SubLine subLine = new SubLine(segment);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(iset, subLine.getRemainingRegion());
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegments() {
        EuclideanSpace space = new EuclideanSpace2D();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        IntervalsSet iset = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(hyperplane, iset);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(0.0, 1.0), segment1.getStart());
        assertEquals(new Vector2D(1.0, 1.0), segment1.getEnd());
        assertEquals(new Vector2D(2.0, 2.0), segment2.getStart());
        assertEquals(new Vector2D(2.0, 3.0), segment2.getEnd());
    }

    /**
     * Test intersection method with two lines that do not intersect.
     */
    @Test
    public void testIntersectionWithLinesThatDoNotIntersect() {
        EuclideanSpace space = new EuclideanSpace2D();
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(hyperplane1);
        SubLine subLine2 = new SubLine(hyperplane2);
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));
    }

    /**
     * Test intersection method with two lines that intersect at a point.
     */
    @Test
    public void testIntersectionWithLinesThatIntersectAtAPoint() {
        EuclideanSpace space = new EuclideanSpace2D();
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        SubLine subLine1 = new SubLine(hyperplane1);
        SubLine subLine2 = new SubLine(hyperplane2);
        Vector2D expected = new Vector2D(1.5, 1.5);
        assertEquals(expected, subLine1.intersection(subLine2, true));
        assertEquals(expected, subLine1.intersection(subLine2, false));
    }

    /**
     * Test intersection method with two lines that are parallel but do not intersect.
     */
    @Test
    public void testIntersectionWithParallelLinesThatDoNotIntersect() {
        EuclideanSpace space = new EuclideanSpace2D();
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(hyperplane1);
        SubLine subLine2 = new SubLine(hyperplane2);
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));
    }

    /**
     * Test intersection method with a line and a line segment.
     */
    @Test
    public void testIntersectionWithALineAndALineSegment() {
        EuclideanSpace space = new EuclideanSpace2D();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Vector2D start = new Vector2D(0.5, 0.5);
        Vector2D end   = new Vector2D(1.5, 1.5);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(hyperplane);
        Vector2D expected = new Vector2D(1.0, 1.0);
        assertEquals(expected, subLine.intersection(new SubLine(segment), true));
        assertEquals(expected, subLine.intersection(new SubLine(segment), false));
    }

    /**
     * Test intersection method with a line and a line segment that does not intersect.
     */
    @Test
    public void testIntersectionWithALineAndALineSegmentThatDoesNotIntersect() {
        EuclideanSpace space = new EuclideanSpace2D();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Vector2D start = new Vector2D(0.5, 0.5);
        Vector2D end   = new Vector2D(1.5, 1.5);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(hyperplane);
        assertNull(subLine.intersection(new SubLine(segment), true));
        assertNull(subLine.intersection(new SubLine(segment), false));
    }

}