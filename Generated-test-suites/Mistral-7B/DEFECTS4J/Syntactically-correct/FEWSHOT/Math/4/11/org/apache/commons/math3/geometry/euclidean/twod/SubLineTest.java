package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubLine;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        // Simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertSame(hyperplane, subLine.getHyperplane());
        assertSame(remainingRegion, subLine.getRemainingRegion());

        // Constructor from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start, end);
        SubLine subLine2 = new SubLine(start, end);
        assertSame(hyperplane2, subLine2.getHyperplane());

        // Constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(start, end));
        SubLine subLine3 = new SubLine(segment);
        assertSame(hyperplane2, subLine3.getHyperplane());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervals = new IntervalsSet(new Interval(0, 0.5), new Interval(0.5, 1));
        SubLine subLine = new SubLine(line, intervals);
        List<Segment> segments = subLine.getSegments();

        assertEquals(2, segments.size());
        assertEquals(0, segments.get(0).getStart().getX(), 1e-12);
        assertEquals(0.5, segments.get(0).getEnd().getX(), 1e-12);
        assertEquals(0.5, segments.get(1).getStart().getX(), 1e-12);
        assertEquals(1, segments.get(1).getEnd().getX(), 1e-12);
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 2));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Vector2D intersectionPoint2 = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1, 1), intersectionPoint2);

        intersectionPoint2 = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1, 1), intersectionPoint2);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(1, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1, 1), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1, 1), intersectionPoint);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Side side = subLine1.side(line2);
        assertEquals(Side.MINUS, side);

        side = subLine2.side(line1);
        assertEquals(Side.PLUS, side);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 2));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        side = subLine1.side(line2);
        assertEquals(Side.HYPER, side);

        side = subLine2.side(line1);
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(line2);
        assertNull(split);

        split = subLine2.split(line1);
        assertNull(split);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 2));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        SplitSubHyperplane<Euclidean2D> splitLine1 = subLine1.split(line2);
        assertNull(splitLine1);

        SplitSubHyperplane<Euclidean2D> splitLine2 = subLine2.split(line1);
        assertNull(splitLine2);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(1, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(line2);
        assertSame(subLine1, split1.getSubHyperplane1());
        assertNull(split1.getSubHyperplane2());

        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(line1);
        assertSame(subLine2, split2.getSubHyperplane2());
        assertNull(split2.getSubHyperplane1());
    }

    @Test
    public void testBuildNew() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervals = new IntervalsSet(new Interval(0, 1));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> newSubLine = SubLine.buildNew(line1, intervals);
        SubLine subLine = (SubLine) newSubLine;
        assertSame(line1, subLine.getHyperplane());
        assertSame(intervals, subLine.getRemainingRegion());
    }
}