package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Interval;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Treeable;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testCreateSubLine() {
        // Test creating SubLine from Hyperplane and IntervalsSet
        Hyperplane<Euclidean2D> hyperplane = new Line(new Point2D(0, 0), new Point2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test creating SubLine from two points
        Point2D start = new Point2D(0, 0);
        Point2D end = new Point2D(1, 1);
        SubLine subLineFromPoints = new SubLine(start, end);
        assertEquals(new Line(start, end), subLineFromPoints.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLineFromPoints.getRemainingRegion());

        // Test creating SubLine from a Segment
        Segment segment = new Segment(new Point2D(0, 0), new Point2D(1, 1));
        SubLine subLineFromSegment = new SubLine(segment);
        assertEquals(segment.getLine(), subLineFromSegment.getHyperplane());
        assertEquals(subLineFromSegment.getRemainingRegion(), segment.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Point2D(0, 0), new Point2D(1, 1));
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0, 0.5), new Interval(0.6, 1), new Interval(-1, -0.5));
        SubLine subLine = new SubLine(line, intervalsSet);

        List<Segment> segments = subLine.getSegments();
        assertEquals(3, segments.size());

        Segment segment1 = segments.get(0);
        assertEquals(new Point2D(0, 0), segment1.getStart());
        assertEquals(new Point2D(0.5, 0.5 * Math.sqrt(2)), segment1.getEnd());

        Segment segment2 = segments.get(1);
        assertEquals(new Point2D(0.6, 0.6 * Math.sqrt(2)), segment2.getStart());
        assertEquals(new Point2D(1, 1), segment2.getEnd());

        Segment segment3 = segments.get(2);
        assertEquals(new Point2D(-1, -1), segment3.getStart());
        assertEquals(new Point2D(-0.5, -0.5 * Math.sqrt(2)), segment3.getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        Line line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 1.5)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Point2D(0.5, 0.5), intersectionPoint);
    }

    @Test
    public void testIntersectionNull() {
        Line line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        Line line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.1, 1.5)));

        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionFalse() {
        Line line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        Line line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.6, 1)));

        assertNull(subLine1.intersection(subLine2, false));
    }

    private IntervalsSet buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
    }
}