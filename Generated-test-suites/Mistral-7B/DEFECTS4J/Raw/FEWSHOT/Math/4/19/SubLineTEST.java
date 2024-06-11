package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Interval;
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
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Segment segment = new Segment(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5), line);
        SubLine subLine = new SubLine(segment);

        assertNotNull(subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());

        Line expectedLine = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        assertEquals(expectedLine, subLine.getHyperplane());

        IntervalsSet expectedIntervalSet = new IntervalsSet(
                new Interval(new Vector1D(0.5), new Vector1D(1.5)));
        assertEquals(expectedIntervalSet, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Segment segment1 = new Segment(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.75), line);
        Segment segment2 = new Segment(new Vector2D(0.75, 0.75), new Vector2D(1.25, 1.25), line);
        SubLine subLine = new SubLine(segment1, segment2);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        Segment segment1Expected = new Segment(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.75), line);
        Segment segment2Expected = new Segment(new Vector2D(0.75, 0.75), new Vector2D(1.25, 1.25), line);

        for (int i = 0; i < segments.size(); i++) {
            assertEquals(segments.get(i), (i == 0) ? segment1Expected : segment2Expected);
        }
    }

    @Test
    public void testSubLineIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(
                new Interval(new Vector1D(0.25), new Vector1D(0.75)),
                new Interval(new Vector1D(1.25), new Vector1D(1.75))));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(
                new Interval(new Vector1D(0.5), new Vector1D(0.8)),
                new Interval(new Vector1D(1.8), new Vector1D(2.3))));

        Vector2D expectedIntersection = new Vector2D(1.0, 1.0);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(expectedIntersection, intersection);

        assertNull(subLine1.intersection(subLine2, false));
        assertNull(subLine2.intersection(subLine1, false));
    }

    @Test
    public void testSubLineSplit() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(line, Side.PLUS);
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(line, Side.MINUS);

        Region<Euclidean1D> region = new IntervalsSet(
                new Interval(new Vector1D(0.25), new Vector1D(0.75)),
                new Interval(new Vector1D(0.9), new Vector1D(1.1)),
                new Interval(new Vector1D(1.3), new Vector1D(1.6)));

        SubLine subLine = new SubLine(line, region);

        Hyperplane<Euclidean2D> splitHyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5)), Side.PLUS);

        SplitSubHyperplane<Euclidean2D> split = subLine.split(splitHyperplane);

        assertNotNull(split);

        SubLine subLinePlus = split.getSubHyperplane1();
        SubLine subLineMinus = split.getSubHyperplane2();

        IntervalsSet expectedRegionPlus = new IntervalsSet(
                new Interval(new Vector1D(0.25), new Vector1D(0.45)),
                new Interval(new Vector1D(0.55), new Vector1D(0.9)),
                new Interval(new Vector1D(1.1), new Vector1D(1.3)),
                new Interval(new Vector1D(1.55), new Vector1D(1.6)));

        IntervalsSet expectedRegionMinus = new IntervalsSet(
                new Interval(new Vector1D(0.45), new Vector1D(0.55)),
                new Interval(new Vector1D(0.9), new Vector1D(1.1)),
                new Interval(new Vector1D(1.3), new Vector1D(1.55)));

        assertEquals(expectedRegionPlus, subLinePlus.getRemainingRegion());
        assertEquals(expectedRegionMinus, subLineMinus.getRemainingRegion());
    }

    @Test
    public void testSubLineSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(
                new Interval(new Vector1D(0.25), new Vector1D(0.75)),
                new Interval(new Vector1D(1.25), new Vector1D(1.75))));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(
                new Interval(new Vector1D(0.5), new Vector1D(0.8)),
                new Interval(new Vector1D(1.8), new Vector1D(2.3))));

        Side expectedSide = Side.MINUS;

        Side side = subLine1.side(subLine2.getHyperplane());

        assertEquals(expectedSide, side);

        expectedSide = Side.PLUS;

        side = subLine2.side(subLine1.getHyperplane());

        assertEquals(expectedSide, side);
    }
}