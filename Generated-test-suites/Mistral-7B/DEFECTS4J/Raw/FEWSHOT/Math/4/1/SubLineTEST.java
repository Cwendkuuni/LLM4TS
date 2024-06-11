package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplaneType;
import org.apache.commons.math3.util.FastMath;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        // test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 2.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // test constructor with two endpoints
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        Hyperplane<Euclidean2D> line = new Line(start, end);
        IntervalsSet intervalSet = new IntervalsSet(new Interval(start.getX(), end.getX()));
        SubLine subLine2 = new SubLine(start, end);
        assertEquals(line, subLine2.getHyperplane());
        assertEquals(intervalSet, subLine2.getRemainingRegion());

        // test constructor with a segment
        Segment segment = new Segment(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), line);
        SubLine subLine3 = new SubLine(segment);
        assertEquals(line, subLine3.getHyperplane());
        assertEquals(intervalSet, subLine3.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        // test simple case
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervals = new IntervalsSet(new Interval(0.5, 0.6), new Interval(0.8, 0.9));
        SubLine subLine = new SubLine(line, intervals);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(0.5, 0.5), new Vector2D(0.6, 0.6), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(0.8, 0.8), new Vector2D(0.9, 0.9), line), segments.get(1));

        // test case where the sub-line is unbounded in negative infinity direction
        line = new Line(new Vector2D(0.0, 0.0), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        intervals = new IntervalsSet(new Interval(-1.0, 0.0));
        SubLine subLine2 = new SubLine(line, intervals);
        segments = subLine2.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Segment(new Vector2D(-1.0, -1.0), new Vector2D(0.0, 0.0), line), segments.get(0));

        // test case where the sub-line is unbounded in positive infinity direction
        line = new Line(new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
                       new Vector2D(0.0, 0.0));
        intervals = new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY));
        SubLine subLine3 = new SubLine(line, intervals);
        segments = subLine3.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Segment(new Vector2D(0.0, 0.0), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), line), segments.get(0));

        // test case where the sub-line is empty
        line = new Line(new Vector2D(0.0, 0.0), new Vector2D(0.0, 0.0));
        intervals = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        SubLine subLine4 = new SubLine(line, intervals);
        segments = subLine4.getSegments();
        assertEquals(0, segments.size());
    }

    @Test
    public void testIntersection() {
        // test simple case where the sub-lines intersect
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.8)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);

        // test case where the sub-lines do not intersect
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line4 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0.2, 0.8)));
        SubLine subLine4 = new SubLine(line4, new IntervalsSet(new Interval(0.5, 1.5)));
        Vector2D nullIntersection = subLine3.intersection(subLine4, true);
        assertNull(nullIntersection);

        // test case where the sub-lines do not intersect due to endpoints not belonging to the sub-lines
        Line line5 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line6 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine5 = new SubLine(line5, new IntervalsSet(new Interval(0.2, 0.8), new Interval(1.2, 1.8)));
        SubLine subLine6 = new SubLine(line6, new IntervalsSet(new Interval(0.5, 1.5)));
        Vector2D nullIntersection2 = subLine5.intersection(subLine6, false);
        assertNull(nullIntersection2);

        // test case where the sub-lines intersect at an endpoint
        Line line7 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line8 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 2.0));
        SubLine subLine7 = new SubLine(line7, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine8 = new SubLine(line8, new IntervalsSet(new Interval(0.5, 1.5)));
        Vector2D intersection2 = subLine7.intersection(subLine8, true);
        assertNull(intersection2);

        // test case where the sub-lines intersect at a point between endpoints
        Line line9 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line10 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine9 = new SubLine(line9, new IntervalsSet(new Interval(0.2, 0.8)));
        SubLine subLine10 = new SubLine(line10, new IntervalsSet(new Interval(0.5, 1.5)));
        Vector2D intersection3 = subLine9.intersection(subLine10, true);
        assertEquals(new Vector2D(0.6, 0.6), intersection3);
    }
}