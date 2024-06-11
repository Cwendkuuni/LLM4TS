package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStart());
        assertEquals(end, line.getEnd());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStart());
        assertEquals(end, line.getEnd());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment testSegment = segments.get(0);
        assertEquals(start, testSegment.getStart());
        assertEquals(end, testSegment.getEnd());
    }

    @Test
    public void testGetSegments() {
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 2.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment firstSegment = segments.get(0);
        Segment secondSegment = segments.get(1);
        Vector2D firstStart = firstSegment.getStart();
        Vector2D firstEnd = firstSegment.getEnd();
        Vector2D secondStart = secondSegment.getStart();
        Vector2D secondEnd = secondSegment.getEnd();
        assertEquals(0.0, firstStart.getX(), 1e-10);
        assertEquals(1.0, firstEnd.getX(), 1e-10);
        assertEquals(1.0, secondStart.getX(), 1e-10);
        assertEquals(2.0, secondEnd.getX(), 1e-10);
    }

    @Test
    public void testIntersectionWithSelf() {
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine1 = new SubLine(hyperplane, remainingRegion);
        SubLine subLine2 = new SubLine(subLine1);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
    }

    @Test
    public void testIntersectionWithDifferentLines() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), Math.PI / 4.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(1.0, 1.0), -Math.PI / 4.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithParallelLinesDifferentOffsets() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), 1.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(1.0, 0.0), 1.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithParallelLinesSameOffset() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), 1.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(1.0, 0.0), 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1.0, 0.0), intersection);
    }

    @Test
    public void testIntersectionWithPerpendicularLines() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), Math.PI / 2.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(1.0, 0.0), -Math.PI / 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithLinesIntersectingAtPoint() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), Math.PI / 4.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(1.0, 1.0), -Math.PI / 4.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersectionWithLinesIntersectingAtEndpoints() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), Math.PI / 4.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(2.0, 2.0), -Math.PI / 4.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithLinesNotIntersecting() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), Math.PI / 4.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(2.0, 2.0), Math.PI / 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithEmptyLines() {
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), Math.PI));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), -Math.PI));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet();
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet();
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

}