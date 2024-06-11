package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;

import java.util.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test constructor from two endpoints
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(2, 2);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start, end).toHyperplane();
        Region<Euclidean1D> remainingRegion2 = buildIntervalSet(start, end);
        SubLine subLine2 = new SubLine(start, end);
        assertEquals(hyperplane2, subLine2.getHyperplane());
        assertEquals(remainingRegion2, subLine2.getRemainingRegion());

        // Test constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        Hyperplane<Euclidean2D> hyperplane3 = segment.getLine().toHyperplane();
        Region<Euclidean1D> remainingRegion3 = buildIntervalSet(segment.getStart(), segment.getEnd());
        SubLine subLine3 = new SubLine(segment);
        assertEquals(hyperplane3, subLine3.getHyperplane());
        assertEquals(remainingRegion3, subLine3.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervals = new IntervalsSet(new Interval(0.1, 0.2), new Interval(0.5, 0.6), new Interval(0.8, 0.9));
        SubLine subLine = new SubLine(line, intervals);
        List<Segment> segments = subLine.getSegments();

        // List should contain 3 segments
        assertEquals(3, segments.size());

        // Check first segment
        Segment segment1 = segments.get(0);
        Vector2D start1 = segment1.getStart();
        Vector2D end1 = segment1.getEnd();
        Vector2D expectedStart1 = new Vector2D(0.1, Precision.round(line.getSlope() * 0.1 + line.getYIntercept(), 4));
        Vector2D expectedEnd1 = new Vector2D(0.2, Precision.round(line.getSlope() * 0.2 + line.getYIntercept(), 4));
        assertEquals(expectedStart1, start1);
        assertEquals(expectedEnd1, end1);

        // Check second segment
        Segment segment2 = segments.get(1);
        Vector2D start2 = segment2.getStart();
        Vector2D end2 = segment2.getEnd();
        Vector2D expectedStart2 = new Vector2D(0.5, Precision.round(line.getSlope() * 0.5 + line.getYIntercept(), 4));
        Vector2D expectedEnd2 = new Vector2D(0.6, Precision.round(line.getSlope() * 0.6 + line.getYIntercept(), 4));
        assertEquals(expectedStart2, start2);
        assertEquals(expectedEnd2, end2);

        // Check third segment
        Segment segment3 = segments.get(2);
        Vector2D start3 = segment3.getStart();
        Vector2D end3 = segment3.getEnd();
        Vector2D expectedStart3 = new Vector2D(0.8, Precision.round(line.getSlope() * 0.8 + line.getYIntercept(), 4));
        Vector2D expectedEnd3 = new Vector2D(0.9, Precision.round(line.getSlope() * 0.9 + line.getYIntercept(), 4));
        assertEquals(expectedStart3, start3);
        assertEquals(expectedEnd3, end3);
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.2), new Interval(0.5, 0.6), new Interval(0.8, 0.9)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.1, 1.2), new Interval(1.5, 1.6), new Interval(1.8, 1.9)));

        // Test intersection with no intersection points
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        // Test intersection with intersection points on both sub-lines
        Line newLine1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line newLine2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine1New = new SubLine(newLine1, new IntervalsSet(new Interval(0.1, 0.2), new Interval(0.5, 0.6), new Interval(0.8, 0.9)));
        SubLine subLine2New = new SubLine(newLine2, new IntervalsSet(new Interval(1.1, 1.2), new Interval(1.5, 1.6), new Interval(1.8, 1.9)));
        Vector2D intersectionPointNew = subLine1New.intersection(subLine2New, true);
        Vector2D expectedIntersectionPointNew = new Vector2D(1, 1);
        assertEquals(expectedIntersectionPointNew, intersectionPointNew);

        // Test intersection with intersection points only on one sub-line
        SubLine subLine1WithEndPoint = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.2), new Interval(0.5, Double.POSITIVE_INFINITY), new Interval(0.8, Double.POSITIVE_INFINITY)));
        Vector2D intersectionPointWithEndPoint = subLine1WithEndPoint.intersection(subLine2, false);
        assertNull(intersectionPointWithEndPoint);

        SubLine subLine2WithEndPoint = new SubLine(line2, new IntervalsSet(new Interval(1.1, 1.2), new Interval(Double.NEGATIVE_INFINITY, 1.5), new Interval(1.8, 1.9)));
        Vector2D intersectionPointWithStartEndPoint = subLine1.intersection(subLine2WithEndPoint, true);
        assertNull(intersectionPointWithStartEndPoint);
    }

    private IntervalsSet buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
    }
}