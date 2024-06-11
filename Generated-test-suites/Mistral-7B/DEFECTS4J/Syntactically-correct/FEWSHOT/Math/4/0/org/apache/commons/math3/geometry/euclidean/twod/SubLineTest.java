package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.WholeSpace;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(1, 2));
        Point2D start = new Vector2D(0, 0);
        Point2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(new IntervalsSet(start, end), subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorHyperplaneAndRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(1, 2));
        IntervalsSet region = new IntervalsSet(new Vector1D(1), new Vector1D(3));
        SubLine subLine = new SubLine(hyperplane, region);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(region, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorSegment() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(1, 2));
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(2, 2), hyperplane);
        SubLine subLine = new SubLine(segment);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(segment.getRemainingRegion(), subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(0), new Vector1D(1), new Vector1D(3), new Vector1D(4));
        SubLine subLine = new SubLine(line, intervalsSet);

        List<Segment> segments = subLine.getSegments();

        assertEquals(3, segments.size());

        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0, 0), segment1.getStart());
        assertEquals(new Vector2D(1, 1), segment1.getEnd());

        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(1, 1), segment2.getStart());
        assertEquals(new Vector2D(2, 2), segment2.getEnd());

        Segment segment3 = segments.get(2);
        assertEquals(new Vector2D(3, 3), segment3.getStart());
        assertEquals(new Vector2D(4, 4), segment3.getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector1D(0), new Vector1D(1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Vector1D(1), new Vector1D(2)));

        Vector2D result = subLine1.intersection(subLine2, true);

        assertEquals(new Vector2D(1, 1), result);

        result = subLine1.intersection(subLine2, false);

        assertNull(result);
    }

    @Test
    public void testIntersectionNullSubLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector1D(0), new Vector1D(1)));

        assertNull(subLine1.intersection(null, true));
        assertNull(subLine1.intersection(null, false));
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector1D(0), new Vector1D(1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Vector1D(1), new Vector1D(2)));

        Side result = subLine1.side(line2);

        assertEquals(Side.MINUS, result);
    }

    @Test
    public void testBuildNew() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(new Vector1D(0), new Vector1D(1));
        SubLine subLine = new SubLine(line, region);

        AbstractSubHyperplane<Euclidean2D, Euclidean1D> newSubLine = subLine.buildNew(new Line(new Vector2D(1, 1), new Vector2D(2, 2)), new Region<Euclidean1D>(new IntervalsSet(new Vector1D(1), new Vector1D(2))));

        assertEquals(subLine.getClass(), newSubLine.getClass());
    }

    @Test
    public void testSplit() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Vector1D(0), new Vector1D(1)));

        SplitSubHyperplane<Euclidean2D> result = subLine.split(hyperplane);

        assertEquals(SubLine.class, result.getSubHyperplane1().getClass());
        assertEquals(SubLine.class, result.getSubHyperplane2().getClass());
    }

    @Test
    public void testSplitNullHyperplane() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Vector1D(0), new Vector1D(1)));

        assertNull(subLine.split(null));
    }
}