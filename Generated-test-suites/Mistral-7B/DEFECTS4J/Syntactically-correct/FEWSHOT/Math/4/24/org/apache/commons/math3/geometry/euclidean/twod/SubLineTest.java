package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.apache.commons.math3.geometry.euclidean.oned.Interval.interval;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.ZERO;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.ONE;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.POINT_INFINITY;
import static org.apache.commons.math3.geometry.partitioning.Side.HYPER;
import static org.apache.commons.math3.geometry.partitioning.Side.MINUS;
import static org.apache.commons.math3.geometry.partitioning.Side.PLUS;

import org.apache.commons.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.commons.math3.geometry.euclidean.oned.IntervalSet;
import org.apache.commons.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.commons.math3.geometry.partitioning.Region;
import org.apache.commons.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.commons.math3.util.FastMath;
import org.apache.commons.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, new IntervalsSet(interval(0.0, 0.5), interval(0.75, 1.0)));
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(new IntervalsSet(interval(0.0, 0.5), interval(0.75, 1.0)), subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineCreationFromSegment() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        assertEquals(segment.getLine(), subLine.getHyperplane());
        assertEquals(new IntervalsSet(interval(segment.getStart().getX(), segment.getEnd().getX())),
                subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervals = new IntervalsSet(interval(0.1, 0.5), interval(0.7, 0.9));
        SubLine subLine = new SubLine(line, intervals);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(0.1, 0.1), new Vector2D(0.5, 0.5), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(0.7, 0.7), new Vector2D(0.9, 0.9), line), segments.get(1));
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(interval(0.1, 0.5), interval(0.7, 0.9)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(interval(0.5, 1.0), interval(1.2, 1.5)));

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        line1.setHyperplane(new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0)));
        line2.setHyperplane(new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0)));
        subLine1 = new SubLine(line1, new IntervalsSet(interval(0.1, 0.5), interval(0.7, 0.9)));
        subLine2 = new SubLine(line2, new IntervalsSet(interval(1.0, 1.5), interval(2.5, 3.0)));

        intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1.0, 1.0), intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1.0, 1.0), intersection);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(interval(0.1, 0.5), interval(0.7, 0.9)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(interval(0.5, 1.0), interval(1.2, 1.5)));

        Side side = subLine1.side(line2.getHyperplane());
        assertEquals(Side.HYPER, side);

        line1.setHyperplane(new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0)));
        line2.setHyperplane(new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0)));
        subLine1 = new SubLine(line1, new IntervalsSet(interval(0.1, 0.5), interval(0.7, 0.9)));
        subLine2 = new SubLine(line2, new IntervalsSet(interval(1.0, 1.5), interval(2.5, 3.0)));

        side = subLine1.side(line2.getHyperplane());
        assertEquals(Side.MINUS, side);

        side = subLine2.side(line1.getHyperplane());
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(interval(0.1, 0.5), interval(0.7, 0.9)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(interval(0.5, 1.0), interval(1.2, 1.5)));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(line2.getHyperplane());
        assertNull(split.getSubHyperplane1());
        assertNull(split.getSubHyperplane2());

        line1.setHyperplane(new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0)));
        line2.setHyperplane(new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0)));
        subLine1 = new SubLine(line1, new IntervalsSet(interval(0.1, 0.5), interval(0.7, 0.9)));
        subLine2 = new SubLine(line2, new IntervalsSet(interval(1.0, 1.5), interval(2.5, 3.0)));

        split = subLine1.split(line2.getHyperplane());
        SubLine subLinePlus = new SubLine(line1.copySelf(), new IntervalsSet(interval(0.0, 0.1), interval(0.7, 0.9)));
        SubLine subLineMinus = new SubLine(line1.copySelf(), new IntervalsSet(interval(0.1, 0.7), interval(0.9, 2.0)));
        assertEquals(subLinePlus, split.getSubHyperplane1());
        assertEquals(subLineMinus, split.getSubHyperplane2());

        split = subLine2.split(line1.getHyperplane());
        SubLine subLinePlus2 = new SubLine(line2.copySelf(), new IntervalsSet(interval(0.5, 1.0), interval(1.2, 1.5)));
        SubLine subLineMinus2 = new SubLine(line2.copySelf(), new IntervalsSet(interval(0.0, 0.5), interval(1.5, 2.5)));
        assertEquals(subLinePlus2, split.getSubHyperplane1());
        assertEquals(subLineMinus2, split.getSubHyperplane2());
    }

}