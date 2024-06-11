package org.apache.commons.math3.geometry.euclidean.twod.test;

import junit.framework.Assert;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test for {@link SubLine} class.
 */
public class SubLineTest {

    /**
     * Test method for {@link SubLine#SubLine(Hyperplane, Region)} constructor.
     */
    @Test
    public void testSubLineConstructorHyperplaneAndRegion() {
        final Euclidean2D space = new Euclidean2D();
        final Vector2D point1 = new Vector2D(1.0, 2.0);
        final Vector2D point2 = new Vector2D(3.0, 4.0);
        final Line line = new Line(point1, point2);
        final Interval interval1 = new Interval(1.5, 2.5);
        final Interval interval2 = new Interval(3.5, 4.5);
        final IntervalsSet set = new IntervalsSet(interval1, interval2);
        final SubLine subLine = new SubLine(line, set);

        Assert.assertEquals(line, subLine.getHyperplane());
        Assert.assertEquals(set, subLine.getRemainingRegion());
        Assert.assertEquals(2, subLine.getSegments().size());
        Assert.assertNotNull(subLine.getSegments().get(0).getStart());
        Assert.assertNotNull(subLine.getSegments().get(0).getEnd());
        Assert.assertNotNull(subLine.getSegments().get(1).getStart());
        Assert.assertNotNull(subLine.getSegments().get(1).getEnd());
    }

    /**
     * Test method for {@link SubLine#SubLine(Vector2D, Vector2D)} constructor.
     */
    @Test
    public void testSubLineConstructorVector2DVector2D() {
        final Euclidean2D space = new Euclidean2D();
        final Vector2D point1 = new Vector2D(1.0, 2.0);
        final Vector2D point2 = new Vector2D(3.0, 4.0);
        final SubLine subLine = new SubLine(point1, point2);

        Assert.assertEquals(new Line(point1, point2), subLine.getHyperplane());
        Assert.assertNotNull(subLine.getRemainingRegion());
        Assert.assertTrue(subLine.getRemainingRegion() instanceof IntervalsSet);
        Assert.assertEquals(1, subLine.getSegments().size());
        Assert.assertNotNull(subLine.getSegments().get(0).getStart());
        Assert.assertNotNull(subLine.getSegments().get(0).getEnd());
    }

    /**
     * Test method for {@link SubLine#SubLine(Segment)} constructor.
     */
    @Test
    public void testSubLineConstructorSegment() {
        final Euclidean2D space = new Euclidean2D();
        final Vector2D point1 = new Vector2D(1.0, 2.0);
        final Vector2D point2 = new Vector2D(3.0, 4.0);
        final Segment segment = new Segment(point1, point2);
        final SubLine subLine = new SubLine(segment);

        Assert.assertEquals(new Line(point1, point2), subLine.getHyperplane());
        Assert.assertNotNull(subLine.getRemainingRegion());
        Assert.assertTrue(subLine.getRemainingRegion() instanceof IntervalsSet);
        Assert.assertEquals(1, subLine.getSegments().size());
        Assert.assertNotNull(subLine.getSegments().get(0).getStart());
        Assert.assertNotNull(subLine.getSegments().get(0).getEnd());
    }

    /**
     * Test method for {@link SubLine#getSegments()} method.
     */
    @Test
    public void testGetSegments() {
        final Euclidean2D space = new Euclidean2D();
        final Vector2D point1 = new Vector2D(1.0, 2.0);
        final Vector2D point2 = new Vector2D(3.0, 4.0);
        final Line line = new Line(point1, point2);
        final Interval interval1 = new Interval(1.5, 2.5);
        final Interval interval2 = new Interval(3.5, 4.5);
        final IntervalsSet set = new IntervalsSet(interval1, interval2);
        final SubLine subLine = new SubLine(line, set);

        final List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(2, segments.size());
        Assert.assertNotNull(segments.get(0).getStart());
        Assert.assertNotNull(segments.get(0).getEnd());
        Assert.assertNotNull(segments.get(1).getStart());
        Assert.assertNotNull(segments.get(1).getEnd());
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method.
     */
    @Test
    public void testIntersection() {
        final Euclidean2D space = new Euclidean2D();
        final Vector2D point1 = new Vector2D(0.0, 0.0);
        final Vector2D point2 = new Vector2D(2.0, 0.0);
        final Vector2D point3 = new Vector2D(2.0, 2.0);
        final Vector2D point4 = new Vector2D(0.0, 2.0);

        // Test intersection of lines in finite domain
        {
            final Line line1 = new Line(point1, point2);
            final Line line2 = new Line(point3, point4);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection = subLine1.intersection(subLine2, true);
            Assert.assertNotNull(intersection);
            Assert.assertEquals(point1, intersection, 1.0e-10);
        }

        // Test intersection of parallel lines in infinite domain
        {
            final Line line1 = new Line(point1, point2);
            final Line line2 = new Line(point2, point3);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection = subLine1.intersection(subLine2, true);
            Assert.assertNull(intersection);
        }

        // Test intersection of lines with different directions in infinite domain
        {
            final Line line1 = new Line(point1, point3);
            final Line line2 = new Line(point2, point4);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection = subLine1.intersection(subLine2, true);
            Assert.assertNull(intersection);
        }

        // Test intersection of lines with the same direction in infinite domain
        {
            final Line line1 = new Line(point1, point2);
            final Line line2 = new Line(point2, point1);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection = subLine1.intersection(subLine2, true);
            Assert.assertNull(intersection);
        }

        // Test intersection of lines with no intersection in finite domain
        {
            final Line line1 = new Line(point1, point2);
            final Line line2 = new Line(point3, point4);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection = subLine1.intersection(subLine2, true);
            Assert.assertNull(intersection);
        }

        // Test intersection of lines with no intersection in infinite domain
        {
            final Line line1 = new Line(point1, point2);
            final Line line2 = new Line(point3, point3);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection = subLine1.intersection(subLine2, true);
            Assert.assertNull(intersection);
        }

        // Test intersection of lines with one point of intersection in finite domain
        {
            final Line line1 = new Line(point1, point3);
            final Line line2 = new Line(point2, point4);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection = subLine1.intersection(subLine2, true);
            Assert.assertNotNull(intersection);
            Assert.assertTrue(Precision.abs(point2.getX() - intersection.getX()) < 1.0e-10);
            Assert.assertTrue(Precision.abs(point2.getY() - intersection.getY()) < 1.0e-10);
        }

        // Test intersection of lines with one point of intersection in infinite domain
        {
            final Line line1 = new Line(point1, point3);
            final Line line2 = new Line(point3, point3);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection = subLine1.intersection(subLine2, true);
            Assert.assertNotNull(intersection);
            Assert.assertTrue(Precision.abs(point3.getX() - intersection.getX()) < 1.0e-10);
            Assert.assertTrue(Precision.abs(point3.getY() - intersection.getY()) < 1.0e-10);
        }

        // Test intersection of lines with two points of intersection in finite domain
        {
            final Line line1 = new Line(point1, point3);
            final Line line2 = new Line(point4, point2);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection1 = subLine1.intersection(subLine2, true);
            final Vector2D intersection2 = subLine1.intersection(subLine2, true);

            Assert.assertNotNull(intersection1);
            Assert.assertTrue(Precision.abs(point2.getX() - intersection1.getX()) < 1.0e-10);
            Assert.assertTrue(Precision.abs(point2.getY() - intersection1.getY()) < 1.0e-10);
            Assert.assertNotNull(intersection2);
            Assert.assertTrue(Precision.abs(point1.getX() - intersection2.getX()) < 1.0e-10);
            Assert.assertTrue(Precision.abs(point1.getY() - intersection2.getY()) < 1.0e-10);
        }

        // Test intersection of lines with two points of intersection in infinite domain
        {
            final Line line1 = new Line(point1, point3);
            final Line line2 = new Line(point3, point3);
            final SubLine subLine1 = new SubLine(line1, null);
            final SubLine subLine2 = new SubLine(line2, null);

            final Vector2D intersection1 = subLine1.intersection(subLine2, true);
            final Vector2D intersection2 = subLine1.intersection(subLine2, true);

            Assert.assertNotNull(intersection1);
            Assert.assertTrue(Precision.abs(point3.getX() - intersection1.getX()) < 1.0e-10);
            Assert.assertTrue(Precision.abs(point3.getY() - intersection1.getY()) < 1.0e-10);
            Assert.assertNotNull(intersection2);
            Assert.assertTrue(Precision.abs(point3.getX() - intersection2.getX()) < 1.0e-10);
            Assert.assertTrue(Precision.abs(point3.getY() - intersection2.getY()) < 1.0e-10);
        }
    }

}