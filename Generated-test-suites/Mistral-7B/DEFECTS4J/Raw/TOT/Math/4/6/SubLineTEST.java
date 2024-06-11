package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link SubLine} class.
 */
public class SubLineTest {

    /**
     * Test method for {@link SubLine#SubLine(Vector2D, Vector2D)} constructor.
     */
    @Test
    public void testSubLineConstructor() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end   = new Vector2D(3.0, 3.0);
        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());

        Region<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        assertEquals(remainingRegion.getMinX(), 0.0, 1e-9);
        assertEquals(remainingRegion.getMaxX(), 2.0, 1e-9);
        assertEquals(hyperplane.getNormal().getX(), 0.0, 1e-9);
        assertEquals(hyperplane.getDistance(), 1.5, 1e-9);
    }

    /**
     * Test method for {@link SubLine#SubLine(Segment)} constructor.
     */
    @Test
    public void testSubLineConstructorSegment() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end   = new Vector2D(3.0, 3.0);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);

        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment2 = segments.get(0);
        assertEquals(start, segment2.getStart());
        assertEquals(end, segment2.getEnd());

        Region<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        assertEquals(remainingRegion.getMinX(), 0.0, 1e-9);
        assertEquals(remainingRegion.getMaxX(), 2.0, 1e-9);
        assertEquals(hyperplane.getNormal().getX(), 0.0, 1e-9);
        assertEquals(hyperplane.getDistance(), 1.5, 1e-9);
    }

    /**
     * Test method for {@link SubLine#getSegments()} method.
     */
    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end   = new Vector2D(3.0, 3.0);
        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());

        Vector2D start2 = new Vector2D(1.0, 0.0);
        Vector2D end2   = new Vector2D(3.0, 6.0);
        SubLine subLine2 = new SubLine(start2, end2);

        List<Segment> segments2 = subLine2.getSegments();
        assertEquals(3, segments2.size());

        Vector2D start3 = new Vector2D(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Vector2D end3   = new Vector2D(Integer.MIN_VALUE, Integer.MIN_VALUE);
        SubLine subLine3 = new SubLine(start3, end3);

        List<Segment> segments3 = subLine3.getSegments();
        assertEquals(0, segments3.size());
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method.
     */
    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(1.0, 1.0);
        Vector2D end1   = new Vector2D(3.0, 3.0);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0.0, 0.0);
        Vector2D end2   = new Vector2D(4.0, 4.0);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        start2 = new Vector2D(1.5, 1.5);
        end2   = new Vector2D(4.5, 4.5);
        subLine2 = new SubLine(start2, end2);

        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        start2 = new Vector2D(1.5, 1.5);
        end2   = new Vector2D(4.5, 4.5);
        subLine2 = new SubLine(start2, end2);

        Line line1 = (Line) subLine1.getHyperplane();
        Line line2 = (Line) subLine2.getHyperplane();
        Vector2D intersection2 = line1.intersection(line2);

        intersection = subLine1.intersection(subLine2, true);
        assertEquals(intersection2, intersection, 1e-9);

        start2 = new Vector2D(1.0, 2.0);
        end2   = new Vector2D(3.0, 2.0);
        subLine2 = new SubLine(start2, end2);

        intersection = subLine1.intersection(subLine2, true);
        assertEquals(intersection2, intersection, 1e-9);

        start2 = new Vector2D(1.0, 4.0);
        end2   = new Vector2D(3.0, 4.0);
        subLine2 = new SubLine(start2, end2);

        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        start2 = new Vector2D(1.0, 4.0);
        end2   = new Vector2D(3.0, 4.0);
        subLine2 = new SubLine(start2, end2);

        Line line1_2 = (Line) subLine1.getHyperplane();
        Line line2_2 = (Line) subLine2.getHyperplane();
        Vector2D intersection3 = line1_2.intersection(line2_2);

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine2, true);
        assertEquals(intersection3, intersection, 1e-9);
    }

}