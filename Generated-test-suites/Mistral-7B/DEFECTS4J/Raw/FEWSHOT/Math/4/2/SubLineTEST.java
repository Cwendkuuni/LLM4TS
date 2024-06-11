package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        SubLine subLineFromPoints = new SubLine(start, end);
        assertEquals(new Line(start, end), subLineFromPoints.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLineFromPoints.getRemainingRegion());

        Segment segment = new Segment(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5), new Line(start, end));
        SubLine subLineFromSegment = new SubLine(segment);
        assertEquals(new Line(start, end), subLineFromSegment.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLineFromSegment.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.1, 0.2), new Interval(0.4, 0.5), new Interval(0.8, 0.9));
        SubLine subLine = new SubLine(line, intervalsSet);

        List<Segment> segments = subLine.getSegments();
        assertEquals(3, segments.size());
        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);
        Segment segment3 = segments.get(2);

        assertEquals(new Vector2D(0.1, 0.1), segment1.getStart());
        assertEquals(new Vector2D(0.1, 0.2), segment1.getEnd());
        assertEquals(line, segment1.getLine());

        assertEquals(new Vector2D(0.4, 0.4), segment2.getStart());
        assertEquals(new Vector2D(0.4, 0.5), segment2.getEnd());
        assertEquals(line, segment2.getLine());

        assertEquals(new Vector2D(0.8, 0.8), segment3.getStart());
        assertEquals(new Vector2D(0.8, 0.9), segment3.getEnd());
        assertEquals(line, segment3.getLine());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.3), new Interval(0.6, 0.7)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.4, 0.5), new Interval(0.8, 0.9)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(2.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.3), new Interval(0.6, 0.7)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.4, 0.5), new Interval(0.8, 0.9)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 1.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(0.5, 1.0), intersectionPoint);
    }

    private static IntervalsSet buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.3), new Interval(0.6, 0.7)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.4, 0.5), new Interval(0.8, 0.9)));

        Side side1 = subLine1.side(line2);
        assertEquals(Side.HYPER, side1);

        Side side2 = subLine2.side(line1);
        assertEquals(Side.MINUS, side2);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(2.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.3), new Interval(0.6, 0.7)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.4, 0.5), new Interval(0.8, 0.9)));

        Side side3 = subLine1.side(line2);
        assertEquals(Side.PLUS, side3);

        Side side4 = subLine2.side(line1);
        assertEquals(Side.MINUS, side4);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.3), new Interval(0.6, 0.7)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.4, 0.5), new Interval(0.8, 0.9)));

        BSPTree<Euclidean1D> bspTree1 = new BSPTree<>(Boolean.FALSE);
        BSPTree<Euclidean1D> bspTree2 = new BSPTree<>(Boolean.FALSE);
        Hyperplane<Euclidean1D> splitHyperplane1 = new Hyperplane<Euclidean1D>(new Line(new Vector1D(0.5), 0.0));
        Hyperplane<Euclidean1D> splitHyperplane2 = new Hyperplane<Euclidean1D>(new Line(new Vector1D(1.5), 0.0));

        bspTree1.insert(splitHyperplane1, SubHyperplane.UNDEFINED);
        bspTree2.insert(splitHyperplane2, SubHyperplane.UNDEFINED);

        SubLine leftSubLine1 = subLine1.split(splitHyperplane1);
        SubLine rightSubLine1 = subLine1.split(splitHyperplane2);
        SubLine leftSubLine2 = subLine2.split(splitHyperplane1);
        SubLine rightSubLine2 = subLine2.split(splitHyperplane2);

        assertEquals(new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.3))), leftSubLine1);
        assertEquals(new SubLine(line1, new IntervalsSet(new Interval(0.6, 0.7))), rightSubLine1);
        assertEquals(new SubLine(line2, new IntervalsSet(new Interval(0.4, 0.5))), leftSubLine2);
        assertEquals(new SubLine(line2, new IntervalsSet(new Interval(0.8, 0.9))), rightSubLine2);
    }

    @Test
    public void testSplitWithEmptySubRegion() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.3), new Interval(0.6, 0.7)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.4, 0.5), new Interval(0.8, 0.9)));

        BSPTree<Euclidean1D> bspTree1 = new BSPTree<>(Boolean.FALSE);
        BSPTree<Euclidean1D> bspTree2 = new BSPTree<>(Boolean.FALSE);
        Hyperplane<Euclidean1D> splitHyperplane1 = new Hyperplane<Euclidean1D>(new Line(new Vector1D(0.5), 0.0));

        bspTree1.insert(splitHyperplane1, SubHyperplane.UNDEFINED);

        SubLine leftSubLine1 = subLine1.split(splitHyperplane1);
        SubLine rightSubLine1 = subLine1; // the remaining region is empty

        assertEquals(new SubLine(line1, new IntervalsSet(new Interval(0.2, 0.3))), leftSubLine1);
        assertEquals(new SubLine(line1, new IntervalsSet()), rightSubLine1);

        bspTree2.insert(splitHyperplane1, SubHyperplane.UNDEFINED);

        SubLine leftSubLine2 = subLine2; // the remaining region is empty
        SubLine rightSubLine2 = subLine2.split(splitHyperplane1);

        assertEquals(new SubLine(line2, new IntervalsSet()), leftSubLine2);
        assertEquals(new SubLine(line2, new IntervalsSet(new Interval(0.8, 0.9))), rightSubLine2);
    }
}