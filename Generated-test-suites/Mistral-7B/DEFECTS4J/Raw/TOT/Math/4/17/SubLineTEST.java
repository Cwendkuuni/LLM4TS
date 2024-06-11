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
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SubLine} class.
 */
public class SubLineTest {

    /**
     * Test sub-line creation from hyperplane and interval set.
     */
    @Test
    public void testSubLineFromHyperplaneAndIntervalSet() {
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        final Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.5, 0.7), new Interval(1.2, 1.5));
        final SubLine subLine = new SubLine(hyperplane, remainingRegion);
        final List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(2, segments.size());
        final Segment segment1 = segments.get(0);
        Assert.assertEquals(0.5, segment1.getStart().getX(), 1e-11);
        Assert.assertEquals(0.7, segment1.getEnd().getX(), 1e-11);
        final Segment segment2 = segments.get(1);
        Assert.assertEquals(1.2, segment2.getStart().getX(), 1e-11);
        Assert.assertEquals(1.5, segment2.getEnd().getX(), 1e-11);
    }

    /**
     * Test sub-line creation from endpoints.
     */
    @Test
    public void testSubLineFromEndpoints() {
        final Vector2D start = new Vector2D(0, 0);
        final Vector2D end   = new Vector2D(1, 1);
        final SubLine subLine = new SubLine(start, end);
        final List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        final Segment segment = segments.get(0);
        Assert.assertEquals(start, segment.getStart(), 1e-11);
        Assert.assertEquals(end, segment.getEnd(), 1e-11);
    }

    /**
     * Test sub-line intersection.
     */
    @Test
    public void testSubLineIntersection() {
        final Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        final Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.5, 0.7), new Interval(1.2, 1.5));
        final SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        final Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        final Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.8, 1.2));
        final SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        final Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertNull(intersectionPoint);

        // unbounded sub-lines
        final Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(0, 0), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        final Region<Euclidean1D> remainingRegion3 = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        final SubLine subLine3 = new SubLine(hyperplane3, remainingRegion3);

        final SubLine subLine4 = new SubLine(hyperplane1, remainingRegion1);
        final Vector2D intersectionPoint1 = subLine3.intersection(subLine4, true);
        Assert.assertEquals(new Vector2D(0, 0), intersectionPoint1, 1e-11);

        // bounded sub-lines
        final Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(0, 0), new Vector2D(2, 0));
        final Region<Euclidean1D> remainingRegion4 = new IntervalsSet(new Interval(0, 1), new Interval(1, 2));
        final SubLine subLine5 = new SubLine(hyperplane4, remainingRegion4);

        final Vector2D intersectionPoint2 = subLine1.intersection(subLine5, true);
        Assert.assertEquals(new Vector2D(0.5, 0), intersectionPoint2, 1e-11);

        final Vector2D intersectionPoint3 = subLine5.intersection(subLine1, true);
        Assert.assertEquals(new Vector2D(0.5, 0), intersectionPoint3, 1e-11);

        final Vector2D intersectionPoint4 = subLine2.intersection(subLine5, true);
        Assert.assertNull(intersectionPoint4);

        // intersection point inside sub-line ranges
        final Vector2D intersectionPoint5 = subLine1.intersection(subLine2, false);
        Assert.assertEquals(new Vector2D(0.6, 0.6), intersectionPoint5, 1e-11);

        // intersection point outside sub-line ranges
        final Vector2D intersectionPoint6 = subLine1.intersection(subLine4, false);
        Assert.assertNull(intersectionPoint6);
    }

    /**
     * Test sub-line splitting.
     */
    @Test
    public void testSubLineSplitting() {
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        final Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1), new Interval(1, 2));
        final SubLine subLine = new SubLine(hyperplane, remainingRegion);

        final Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        final Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0, 0.5));
        final SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        final SplitSubHyperplane<Euclidean2D> split = subLine.split(subLine2);

        Assert.assertNotNull(split);
        Assert.assertNotNull(split.getThis());
        Assert.assertNotNull(split.getOther());

        final List<Segment> segments1 = split.getThis().getSegments();
        Assert.assertEquals(1, segments1.size());
        final Segment segment1 = segments1.get(0);
        Assert.assertEquals(new Vector2D(0, 0), segment1.getStart(), 1e-11);
        Assert.assertEquals(new Vector2D(0.5, 0.5), segment1.getEnd(), 1e-11);

        final List<Segment> segments2 = split.getOther().getSegments();
        Assert.assertEquals(1, segments2.size());
        final Segment segment2 = segments2.get(0);
        Assert.assertEquals(new Vector2D(0.5, 0.5), segment2.getStart(), 1e-11);
        Assert.assertEquals(new Vector2D(1, 1), segment2.getEnd(), 1e-11);

        final Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        final Region<Euclidean1D> remainingRegion3 = new IntervalsSet(new Interval(0.5, 1));
        final SubLine subLine3 = new SubLine(hyperplane3, remainingRegion3);

        final SplitSubHyperplane<Euclidean2D> split2 = subLine.split(subLine3);

        Assert.assertNotNull(split2);
        Assert.assertNotNull(split2.getThis());
        Assert.assertNotNull(split2.getOther());

        final List<Segment> segments3 = split2.getThis().getSegments();
        Assert.assertEquals(1, segments3.size());
        final Segment segment3 = segments3.get(0);
        Assert.assertEquals(new Vector2D(0, 0), segment3.getStart(), 1e-11);
        Assert.assertEquals(new Vector2D(0.5, 0), segment3.getEnd(), 1e-11);

        final List<Segment> segments4 = split2.getOther().getSegments();
        Assert.assertEquals(1, segments4.size());
        final Segment segment4 = segments4.get(0);
        Assert.assertEquals(new Vector2D(0.5, 0), segment4.getStart(), 1e-11);
        Assert.assertEquals(new Vector2D(1, 1), segment4.getEnd(), 1e-11);

        final Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        final Region<Euclidean1D> remainingRegion4 = new IntervalsSet();
        final SubLine subLine4 = new SubLine(hyperplane4, remainingRegion4);

        final SplitSubHyperplane<Euclidean2D> split3 = subLine.split(subLine4);

        Assert.assertNull(split3);

        final Hyperplane<Euclidean2D> hyperplane5 = new Line(new Vector2D(0, 0), new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        final Region<Euclidean1D> remainingRegion5 = new IntervalsSet();
        final SubLine subLine5 = new SubLine(hyperplane5, remainingRegion5);

        final SplitSubHyperplane<Euclidean2D> split4 = subLine.split(subLine5);

        Assert.assertNull(split4);

        final Hyperplane<Euclidean2D> hyperplane6 = new Line(new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), new Vector2D(2, 0));
        final Region<Euclidean1D> remainingRegion6 = new IntervalsSet();
        final SubLine subLine6 = new SubLine(hyperplane6, remainingRegion6);

        final SplitSubHyperplane<Euclidean2D> split5 = subLine.split(subLine6);

        Assert.assertNull(split5);

        final Hyperplane<Euclidean2D> hyperplane7 = new Line(new Vector2D(-1, -1), new Vector2D(1, 1));
        final Region<Euclidean1D> remainingRegion7 = new IntervalsSet();
        final SubLine subLine7 = new SubLine(hyperplane7, remainingRegion7);

        final SplitSubHyperplane<Euclidean2D> split6 = subLine.split(subLine7);

        Assert.assertNull(split6);
    }

}