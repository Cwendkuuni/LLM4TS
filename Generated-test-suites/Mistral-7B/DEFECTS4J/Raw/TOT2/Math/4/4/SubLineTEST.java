package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segement;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link SubLine} class.
 */
public class SubLineTest {

    /**
     * Test constructor with hyperplane and remaining region.
     */
    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.5, 0.6));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNotNull(subLine);
    }

    /**
     * Test constructor with two endpoints.
     */
    @Test
    public void testConstructorWithTwoEndpoints() {
        Vector2D start = new Point2D(0.0, 0.0);
        Vector2D end = new Point2D(1.0, 1.0);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    /**
     * Test constructor with a segment.
     */
    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Point2D(0.0, 0.0);
        Vector2D end = new Point2D(1.0, 1.0);
        Segement segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        assertNotNull(subLine);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegments() {
        Line line = new Line(new Point2D(0.0, 0.0), new Point2D(2.0, 0.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1.0, 1.5));
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(new Point2D(1.0, 0.0), segment.getStart());
        assertEquals(new Point2D(1.5, 0.0), segment.getEnd());
    }

    /**
     * Test intersection method with two parallel lines.
     */
    @Test
    public void testIntersectionWithTwoParallelLines() {
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0));
        Line line2 = new Line(new Point2D(1.0, 0.0), new Point2D(2.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.0)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Point2D(1.0, 0.0), intersection);
    }

    /**
     * Test intersection method with two intersecting lines.
     */
    @Test
    public void testIntersectionWithTwoIntersectingLines() {
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(2.0, 2.0));
        Line line2 = new Line(new Point2D(1.0, 1.0), new Point2D(3.0, 3.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Point2D(1.5, 1.5), intersection);
    }

    /**
     * Test intersection method with two non-intersecting lines.
     */
    @Test
    public void testIntersectionWithTwoNonIntersectingLines() {
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0));
        Line line2 = new Line(new Point2D(1.0, 1.0), new Point2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.0)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    /**
     * Test intersection method with two lines where one is a subline of another.
     */
    @Test
    public void testIntersectionWithOneSubLineOfAnother() {
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0));
        Line line2 = new Line(new Point2D(0.5, 0.0), new Point2D(1.5, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.0)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Point2D(0.5, 0.0), intersection);
    }

    /**
     * Test intersection method with two lines where one is a subline of another, but endpoints are not included.
     */
    @Test
    public void testIntersectionWithOneSubLineOfAnotherExcludeEndpoints() {
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0));
        Line line2 = new Line(new Point2D(0.5, 0.0), new Point2D(1.5, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.0)));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    /**
     * Test split method with one subline intersecting the hyperplane.
     */
    @Test
    public void testSplitWithOneSubLineIntersectingHyperplane() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0)).toHyperplane();
        Line line = new Line(new Point2D(0.5, 0.5), new Point2D(1.5, 1.5));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0.5, 1.0)));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> splitSubHyperplane = subLine.split(hyperplane);
        assertEquals(subLine.getStart(), splitSubHyperplane.getSubHyperplane1().getStart());
        assertEquals(new Point2D(0.5, 0.5), splitSubHyperplane.getSubHyperplane1().getEnd());
        assertEquals(new Point2D(1.5, 1.5), splitSubHyperplane.getSubHyperplane2().getStart());
        assertEquals(subLine.getEnd(), splitSubHyperplane.getSubHyperplane2().getEnd());
    }

    /**
     * Test split method with one subline not intersecting the hyperplane.
     */
    @Test
    public void testSplitWithOneSubLineNotIntersectingHyperplane() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0)).toHyperplane();
        Line line = new Line(new Point2D(0.5, 0.5), new Point2D(1.5, 0.5));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0.5, 1.0)));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> splitSubHyperplane = subLine.split(hyperplane);
        assertNull(splitSubHyperplane);
    }

    /**
     * Test split method with two sublines intersecting the hyperplane in different sides.
     */
    @Test
    public void testSplitWithTwoSubLinesIntersectingHyperplaneOnDifferentSides() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0)).toHyperplane();
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0));
        Line line2 = new Line(new Point2D(0.5, 0.5), new Point2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> splitSubHyperplane = subLine1.split(hyperplane);
        assertEquals(subLine1.getStart(), splitSubHyperplane.getSubHyperplane1().getStart());
        assertEquals(new Point2D(0.5, 0.5), splitSubHyperplane.getSubHyperplane1().getEnd());
        assertEquals(new Point2D(1.5, 1.5), splitSubHyperplane.getSubHyperplane2().getStart());
        assertEquals(subLine2.getEnd(), splitSubHyperplane.getSubHyperplane2().getEnd());
    }

    /**
     * Test split method with two sublines intersecting the hyperplane on the same side.
     */
    @Test
    public void testSplitWithTwoSubLinesIntersectingHyperplaneOnSameSide() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0)).toHyperplane();
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0));
        Line line2 = new Line(new Point2D(0.5, 0.5), new Point2D(1.5, 0.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.0)));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> splitSubHyperplane = subLine1.split(hyperplane);
        assertNull(splitSubHyperplane);
    }

}