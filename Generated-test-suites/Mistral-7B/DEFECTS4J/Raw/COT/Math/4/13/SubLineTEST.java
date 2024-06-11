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
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSimpleConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorFromEndpoints() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end   = new Vector2D(2.0, 2.0);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine = new SubLine(start, end);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorFromSegment() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end   = new Vector2D(2.0, 2.0);
        Line line = new Line(start, end);
        Segment segment = new Segment(start, end, line);
        Hyperplane<Euclidean2D> hyperplane = line;
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(start.getX(), end.getX()));
        SubLine subLine = new SubLine(segment);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1.5, 2.5), new Interval(3.5, 4.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5), hyperplane), segments.get(0));
        assertEquals(new Segment(new Vector2D(3.5, 3.5), new Vector2D(4.5, 4.5), hyperplane), segments.get(1));
    }

    @Test
    public void testIntersection() {
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.1, 0.2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.5, 2.0));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);
    }

    @Test
    public void testIntersectionWithEmptyRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet();
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.1, 0.2));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithNonOverlappingRemainingRegions() {
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.1, 0.2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(3.1, 3.2));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithNonOverlappingLines() {
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.1, 0.2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.5, 2.0));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithParallelLines() {
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.1, 0.2));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 1.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.5, 2.0));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.0), intersectionPoint);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assertEquals(new IntervalsSet(new Interval(start.getX(), end.getX())), intervalSet);
    }

}