package org.apache.commons.math3.geometry.euclidean.twod.test;

import junit.framework.TestCase;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;

/**
 * Test for {@link SubLine} class.
 */
public class SubLineTest extends TestCase {

    /**
     * Test method for {@link SubLine#SubLine(Hyperplane, Region)} constructor.
     */
    public void testSubLineConstructionWithHyperplaneAndRegion() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).getHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNotNull(subLine);
    }

    /**
     * Test method for {@link SubLine#SubLine(Vector2D, Vector2D)} constructor.
     */
    public void testSubLineConstructionWithVector2DAndVector2D() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    /**
     * Test method for {@link SubLine#SubLine(Segment)} constructor.
     */
    public void testSubLineConstructionWithSegment() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);
        assertNotNull(subLine);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    /**
     * Test method for {@link SubLine#getSegments()} method.
     */
    public void testGetSegments() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Vector2D start2 = new Vector2D(5, 6);
        Vector2D end2 = new Vector2D(7, 6);
        Hyperplane<Euclidean2D> hyperplane = new Line(start1, end1);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(2, 4), new Interval(5, 6));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertNotNull(segments);
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(start1, segment1.getStart());
        assertEquals(end1, segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(start2, segment2.getStart());
        assertEquals(end2, segment2.getEnd());
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method.
     */
    public void testIntersectionWithDifferentSubLines() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Vector2D start2 = new Vector2D(3, 1);
        Vector2D end2 = new Vector2D(5, 1);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1).getHyperplane();
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(2, 4));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2).getHyperplane();
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(3, 4));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method
     * with intersection point on both lines.
     */
    public void testIntersectionWithSameSubLines() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Vector2D start2 = new Vector2D(3, 1);
        Vector2D end2 = new Vector2D(3, 3);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1).getHyperplane();
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(2, 4));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2).getHyperplane();
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(2, 4));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        Vector2D expectedIntersection = new Vector2D(2, 2);
        assertEquals(expectedIntersection, intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method
     * with intersection point on only one line.
     */
    public void testIntersectionWithOverlappingSubLines() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Vector2D start2 = new Vector2D(3, 1);
        Vector2D end2 = new Vector2D(5, 1);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1).getHyperplane();
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(2, 4));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2).getHyperplane();
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(3, 4));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        Vector2D expectedIntersection = new Vector2D(3, 2);
        assertEquals(expectedIntersection, intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method
     * with non-intersecting lines.
     */
    public void testIntersectionWithNonIntersectingSubLines() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Vector2D start2 = new Vector2D(4, 5);
        Vector2D end2 = new Vector2D(6, 5);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1).getHyperplane();
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(2, 4));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2).getHyperplane();
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(5, 6));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method
     * with intersection point on one line endpoint.
     */
    public void testIntersectionWithEndpointsOnSameLine() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Vector2D start2 = new Vector2D(3, 2);
        Vector2D end2 = new Vector2D(3, 3);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1).getHyperplane();
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(2, 4));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2).getHyperplane();
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(2, 4));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method
     * with intersection point on one line endpoint and other line.
     */
    public void testIntersectionWithEndpointsOnDifferentLines() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Vector2D start2 = new Vector2D(3, 1);
        Vector2D end2 = new Vector2D(3, 3);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1).getHyperplane();
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(2, 4));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2).getHyperplane();
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(3, 4));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        Vector2D expectedIntersection = new Vector2D(3, 2);
        assertEquals(expectedIntersection, intersection);
    }

    /**
     * Test method for {@link SubLine#side(Hyperplane)} method.
     */
    public void testSideMethod() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Hyperplane<Euclidean2D> hyperplane = new Line(start1, end1).getHyperplane();
        SubLine subLine = new SubLine(hyperplane, new IntervalsSet(new Interval(2, 4)));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(4, 3), new Vector2D(5, 3)).getHyperplane();
        Side side = subLine.side(hyperplane2);
        assertEquals(Side.HYPER, side);
    }

    /**
     * Test method for {@link SubLine#split(Hyperplane)} method.
     */
    public void testSplitMethod() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 2);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1).getHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(2, 4));
        SubLine subLine = new SubLine(hyperplane1, remainingRegion);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(4, 3), new Vector2D(5, 3)).getHyperplane();
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane2);
        assertNotNull(splitSubHyperplane);
        SubLine leftSubLine = splitSubHyperplane.getLeftSubHyperplane();
        SubLine rightSubLine = splitSubHyperplane.getRightSubHyperplane();
       assertEquals(subLine.getHyperplane(), leftSubLine.getHyperplane());
        assertEquals(subLine.getRemainingRegion().getSplit(hyperplane2).getMinus(), leftSubLine.getRemainingRegion());
        assertEquals(subLine.getHyperplane(), rightSubLine.getHyperplane());
        assertEquals(subLine.getRemainingRegion().getSplit(hyperplane2).getPlus(), rightSubLine.getRemainingRegion());
    }
}