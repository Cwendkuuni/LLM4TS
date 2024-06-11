package org.apache.commons.math3.geometry.euclidean.twod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Region<Euclidean1D> remainingRegion = new IntervalsSet();
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithPoints() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Segment segment = new Segment(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine = new SubLine(segment);
        assertEquals(segment.getLine(), subLine.getHyperplane());
        assertEquals(buildIntervalSet(segment.getStart(), segment.getEnd()), subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        SubLine subLine = new SubLine(new Vector2D(1, 1), new Vector2D(2, 2));
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Segment(new Vector2D(1, 1), new Vector2D(2, 2), subLine.getHyperplane()), segments.get(0));
    }

    @Test
    public void testIntersection() {
        SubLine subLine1 = new SubLine(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine2 = new SubLine(new Vector2D(3, 3), new Vector2D(4, 4));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    private IntervalsSet buildIntervalSet(final Vector2D start, final Vector2D end) {
        final Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(),
                                line.toSubSpace(end).getX());
    }

    private static boolean equals(double x, double y) {
        return FastMath.abs(x - y) <= 1.0e-10;
    }

}