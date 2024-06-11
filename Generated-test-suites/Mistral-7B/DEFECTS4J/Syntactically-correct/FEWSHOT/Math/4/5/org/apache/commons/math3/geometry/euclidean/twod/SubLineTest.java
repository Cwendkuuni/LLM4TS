package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Interval;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        //Test empty constructor
        SubLine emptySubLine = new SubLine(null, null);
        assertNull(emptySubLine.getHyperplane());
        assertNull(emptySubLine.getRemainingRegion());

        //Test constructor with hyperplane and remaining region
        Line line = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        IntervalsSet intervals = new IntervalsSet(line.toSubSpace(new Vector1D(2.0)));
        SubLine subLine = new SubLine(line, intervals);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervals, subLine.getRemainingRegion());

        //Test constructor with start and end points
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(3.0, 3.0);
        SubLine subLine2 = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine2.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine2.getRemainingRegion());

        //Test constructor with segment
        Segment segment = new Segment(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0), new Line(start, end));
        SubLine subLine3 = new SubLine(segment);
        assertEquals(line, subLine3.getHyperplane());
        assertEquals(intervals, subLine3.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        IntervalsSet intervals = new IntervalsSet(line.toSubSpace(new Vector1D(1.0)), line.toSubSpace(new Vector1D(3.0)), line.toSubSpace(new Vector1D(4.0)), line.toSubSpace(new Vector1D(6.0)));
        SubLine subLine = new SubLine(line, intervals);
        List<Segment> segments = subLine.getSegments();
        assertEquals(4, segments.size());
        for (Segment segment : segments) {
            Line lineSegment = segment.getLine();
            Vector2D start = segment.getStart();
            Vector2D end = segment.getEnd();
            assertEquals(line, lineSegment);
            assertEquals(start, segment.getStart());
            assertEquals(end, segment.getEnd());
        }
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        Line line2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(8.0, 8.0));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(3.0, 3.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        line2 = new Line(new Vector2D(3.0, 3.0), new Vector2D(7.0, 7.0));
        subLine1 = new SubLine(line1, null);
        subLine2 = new SubLine(line2, null);

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        Line line2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(8.0, 8.0));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);

        Side side = subLine1.side(line2);
        assertEquals(Side.MINUS, side);

        Line line3 = new Line(new Vector2D(3.0, 3.0), new Vector2D(7.0, 7.0));
        SubLine subLine3 = new SubLine(line3, null);

        side = subLine1.side(line3);
        assertEquals(Side.HYPER, side);

        side = subLine3.side(line1);
        assertEquals(Side.PLUS, side);

        side = subLine3.side(line2);
        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        Line line2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(8.0, 8.0));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);

        SubHyperplane<Euclidean1D> subHyperplane = subLine2.split(subLine1);

        SubLine subLinePlus = (SubLine) subHyperplane.getPlus();
        SubLine subLineMinus = (SubLine) subHyperplane.getMinus();

        assertEquals(new Line(new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0)), subLinePlus.getHyperplane());
        assertEquals(new Line(new Vector2D(4.0, 4.0), new Vector2D(5.0, 5.0)), subLineMinus.getHyperplane());

        Line line3 = new Line(new Vector2D(3.0, 3.0), new Vector2D(7.0, 7.0));
        SubLine subLine3 = new SubLine(line3, null);

        subHyperplane = subLine1.split(subLine3);

        subLinePlus = (SubLine) subHyperplane.getPlus();
        subLineMinus = (SubLine) subHyperplane.getMinus();

        assertEquals(new Line(new Vector2D(3.0, 3.0), new Vector2D(4.0, 4.0)), subLinePlus.getHyperplane());
        assertEquals(new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0)), subLineMinus.getHyperplane());
    }

    private IntervalsSet buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start), line.toSubSpace(end));
    }
}