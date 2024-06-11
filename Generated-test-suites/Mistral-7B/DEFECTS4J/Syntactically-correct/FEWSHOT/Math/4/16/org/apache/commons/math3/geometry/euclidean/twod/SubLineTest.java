package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.IntervalSet;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test empty sub-line
        SubLine subLine1 = new SubLine(null, new IntervalsSet(new Interval[0]));
        assertTrue(subLine1.isEmpty());
        assertNull(subLine1.getHyperplane());
        assertArrayEquals(new Vector2D[0], subLine1.getSegments().toArray(new Segment[0]));

        // Test simple sub-line
        Vector2D pt1 = new Vector2D(1.0, 2.0);
        Vector2D pt2 = new Vector2D(3.0, 4.0);
        SubLine subLine2 = new SubLine(pt1, pt2);
        Line line = (Line) subLine2.getHyperplane();
        assertEquals(line.getSlope(), (pt2.getY() - pt1.getY()) / (pt2.getX() - pt1.getX()), 0.001);
        List<Segment> segments = subLine2.getSegments();
        assertEquals(segments.size(), 1);
        Segment segment = segments.get(0);
        assertEquals(segment.getStart(), pt1);
        assertEquals(segment.getEnd(), pt2);

        // Test sub-line from another sub-line
        SubLine subLine3 = new SubLine(subLine2);
        assertEquals(subLine3.getHyperplane(), subLine2.getHyperplane());
        assertEquals(subLine3.getRemainingRegion(), subLine2.getRemainingRegion());
        assertEquals(subLine3.getSegments(), subLine2.getSegments());
    }

    @Test
    public void testSubLineGetIntersection() {
        // Test intersection with empty sub-line
        SubLine emptySubLine = new SubLine(null, new IntervalsSet(new Interval[0]));
        SubLine subLine1 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        assertNull(subLine1.intersection(emptySubLine, false));

        // Test intersection with parallel lines
        SubLine subLine2 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        assertNull(subLine1.intersection(subLine2, false));

        // Test intersection with intersecting lines
        SubLine subLine3 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine4 = new SubLine(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Vector2D intersectionPoint = subLine3.intersection(subLine4, false);
        assertTrue(intersectionPoint != null);
        assertEquals(intersectionPoint.getX(), 1.0, 0.001);
        assertEquals(intersectionPoint.getY(), 1.0, 0.001);

        // Test intersection with lines with same start point
        SubLine subLine5 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine6 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 2.0));
        assertNull(subLine5.intersection(subLine6, false));

        // Test intersection with lines having end points on each other
        SubLine subLine7 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine8 = new SubLine(new Vector2D(1.0, 1.0), new Vector2D(0.0, 0.0));
        assertNull(subLine7.intersection(subLine8, false));

        // Test intersection with lines having different endpoints
        SubLine subLine9 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine10 = new SubLine(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Vector2D intersectionPoint2 = subLine9.intersection(subLine10, false);
        assertNull(intersectionPoint2);

        // Test intersection with lines having equal angle but different offsets
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(-1.0, -1.0), new Vector2D(1.0, 0.0));
        SubLine subLine11 = new SubLine(line1);
        SubLine subLine12 = new SubLine(line2);
        assertNull(subLine11.intersection(subLine12, false));

        // Test intersection with lines having different angles
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line4 = new Line(new Vector2D(0.0, 0.0), new Vector2D(0.0, 1.0));
        SubLine subLine13 = new SubLine(line3);
        SubLine subLine14 = new SubLine(line4);
        assertNull(subLine13.intersection(subLine14, false));
    }

    @Test
    public void testSubLineSplit() {
        // Test splitting empty sub-line
        SubLine emptySubLine = new SubLine(null, new IntervalsSet(new Interval[0]));
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane1 = emptySubLine.split(hyperplane1);
        assertNull(splitSubHyperplane1.getSubHyperplaneA());
        assertNull(splitSubHyperplane1.getSubHyperplaneB());

        // Test splitting sub-line with only one segment
        SubLine subLine1 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane2 = subLine1.split(hyperplane2);
        assertNull(splitSubHyperplane2.getSubHyperplaneA());
        assertNull(splitSubHyperplane2.getSubHyperplaneB());

        // Test splitting sub-line with multiple segments
        SubLine subLine2 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine3 = new SubLine(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane3 = subLine2.split(hyperplane3);
        SubLine subLine4 = (SubLine) splitSubHyperplane3.getSubHyperplaneA();
        SubLine subLine5 = (SubLine) splitSubHyperplane3.getSubHyperplaneB();
        List<Segment> segmentsA = subLine4.getSegments();
        List<Segment> segmentsB = subLine5.getSegments();
        assertEquals(segmentsA.size(), 1);
        assertEquals(segmentsB.size(), 1);
        Segment segmentA = segmentsA.get(0);
        Segment segmentB = segmentsB.get(0);
        assertEquals(segmentA.getStart().getX(), 0.0, 0.001);
        assertEquals(segmentA.getStart().getY(), 0.0, 0.001);
        assertEquals(segmentA.getEnd().getX(), 1.0, 0.001);
        assertEquals(segmentA.getEnd().getY(), 1.0, 0.001);
        assertEquals(segmentB.getStart().getX(), 1.5, 0.001);
        assertEquals(segmentB.getStart().getY(), 1.5, 0.001);
        assertEquals(segmentB.getEnd().getX(), 2.5, 0.001);
        assertEquals(segmentB.getEnd().getY(), 2.5, 0.001);

        // Test splitting sub-line with intersection
        SubLine subLine6 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane4 = subLine6.split(hyperplane4);
        assertNull(splitSubHyperplane4.getSubHyperplaneA());
        assertNull(splitSubHyperplane4.getSubHyperplaneB());

        // Test splitting sub-line with parallel hyperplanes
        Hyperplane<Euclidean2D> hyperplane5 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane5 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0))).split(hyperplane5);
        SubLine subLine7 = (SubLine) splitSubHyperplane5.getSubHyperplaneA();
        SubLine subLine8 = (SubLine) splitSubHyperplane5.getSubHyperplaneB();
        List<Segment> segmentsC = subLine7.getSegments();
        List<Segment> segmentsD = subLine8.getSegments();
        assertEquals(segmentsC.size(), 0);
        assertEquals(segmentsD.size(), 1);
        Segment segmentC = segmentsC.get(0);
        Segment segmentD = segmentsD.get(0);
        assertFalse(segmentC.isEmpty());
        assertTrue(segmentD.isEmpty());
        assertEquals(segmentD.getStart(), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(segmentD.getEnd(), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    private void assertEquals(double expected, double actual, double tolerance) {
        assertTrue(Math.abs(expected - actual) <= tolerance);
    }

    private void assertEquals(Line expected, Line actual) {
        assertEquals(expected.getSlope(), actual.getSlope(), 0.001);
        assertEquals(expected.getYIntercept(), actual.getYIntercept(), 0.001);
    }

    private void assertEquals(Segment expected, Segment actual) {
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
        assertEquals(expected.getLine(), actual.getLine());
    }

    private void assertEquals(SubLine expected, SubLine actual) {
        assertEquals(expected.getHyperplane(), actual.getHyperplane());
        assertEquals(expected.getRemainingRegion(), actual.getRemainingRegion());
        assertEquals(expected.getSegments(), actual.getSegments());
    }
}