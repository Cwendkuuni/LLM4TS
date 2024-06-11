package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link SubLine}.
 */
public class SubLineTest {

    /**
     * Test constructor with valid hyperplane and remaining region.
     */
    @Test
    public void testConstructorValid() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        final Interval interval = new Interval(0.5, 1.5);
        final IntervalsSet remainingRegion = new IntervalsSet(interval);
        final SubLine subLine = new SubLine(line, remainingRegion);
        Assert.assertNotNull(subLine);
        Assert.assertEquals(line, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor with two endpoints.
     */
    @Test
    public void testConstructorEndpoints() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Vector2D start = new Vector2D(0.0, 0.0);
        final Vector2D end   = new Vector2D(1.0, 1.0);
        final SubLine subLine = new SubLine(start, end);
        Assert.assertNotNull(subLine);
        Assert.assertEquals(new Line(start, end), subLine.getHyperplane());
        Assert.assertTrue(subLine.getRemainingRegion().isEmpty());
    }

    /**
     * Test constructor with a segment.
     */
    @Test
    public void testConstructorSegment() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Vector2D start = new Vector2D(0.0, 0.0);
        final Vector2D end   = new Vector2D(1.0, 1.0);
        final Segment segment = new Segment(start, end);
        final SubLine subLine = new SubLine(segment);
        Assert.assertNotNull(subLine);
        Assert.assertEquals(segment.getLine(), subLine.getHyperplane());
        Assert.assertTrue(subLine.getRemainingRegion().isEmpty());
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegments() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 0.0));
        final Interval interval1 = new Interval(0.5, 1.5);
        final Interval interval2 = new Interval(2.5, 3.5);
        final IntervalsSet remainingRegion = new IntervalsSet(interval1, interval2);
        final SubLine subLine = new SubLine(line, remainingRegion);

        final List<Segment> segments = subLine.getSegments();
        Assert.assertNotNull(segments);
        Assert.assertEquals(2, segments.size());
        final Segment seg1 = segments.get(0);
        Assert.assertEquals(new Vector2D(1.5, 0.0), seg1.getStart());
        Assert.assertEquals(new Vector2D(2.5, 0.0), seg1.getEnd());
        final Segment seg2 = segments.get(1);
        Assert.assertEquals(new Vector2D(2.5, 0.0), seg2.getStart());
        Assert.assertEquals(new Vector2D(3.0, 0.0), seg2.getEnd());
    }

    /**
     * Test intersection method with null sub-line.
     */
    @Test
    public void testIntersectionNullSubLine() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        final SubLine subLine = null;
        final Vector2D intersection = line1.intersection(subLine, true);
        Assert.assertNull(intersection);
    }

    /**
     * Test intersection method with intersecting sub-lines.
     */
    @Test
    public void testIntersectionIntersecting() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        final Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.0, 2.5)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));
        final Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertNotNull(intersection);
        Assert.assertEquals(new Vector2D(1.5, 1.0), intersection);
    }

    /**
     * Test intersection method with non-intersecting sub-lines.
     */
    @Test
    public void testIntersectionNonIntersecting() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        final Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 0.0));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.0, 2.5)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.0, 0.5), new Interval(2.5, 3.0)));
        final Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertNull(intersection);
    }

    /**
     * Test intersection method with endpoints included.
     */
    @Test
    public void testIntersectionEndpointsIncluded() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        final Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.0, 2.5)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5), new Interval(1.5, 2.0)));
        final Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertNotNull(intersection);
        Assert.assertEquals(new Vector2D(1.0, 1.0), intersection);
    }

    /**
     * Test intersection method with endpoints not included.
     */
    @Test
    public void testIntersectionEndpointsNotIncluded() {
        final Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        final Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.0), new Interval(2.0, 2.5)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.0), new Interval(1.5, 2.0)));
        final Vector2D intersection = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersection);
    }

}