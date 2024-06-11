package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
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
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertSame(hyperplane, subLine.getHyperplane());
        assertSame(remainingRegion, subLine.getRemainingRegion());

        // Test constructor from two endpoints
        Hyperplane<Euclidean2D> lineHyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> lineRemainingRegion = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine lineSubLine = new SubLine(new Vector2D(0.2, 0.2), new Vector2D(0.8, 0.8), lineHyperplane);
        assertSame(lineHyperplane, lineSubLine.getHyperplane());
        assertSame(lineRemainingRegion, lineSubLine.getRemainingRegion());

        // Test constructor from a segment
        Segment segment = new Segment(new Vector2D(0.2, 0.2), new Vector2D(0.8, 0.8), new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0)));
        SubLine segmentSubLine = new SubLine(segment);
        assertSame(segment.getLine(), segmentSubLine.getHyperplane());
        assertSame(segment.getRemainingRegion(), segmentSubLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.1, 0.3), new Interval(0.6, 0.8));
        SubLine subLine = new SubLine(line, intervalsSet);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);

        assertEquals(new Vector2D(0.1, 0.1), segment1.getStart());
        assertEquals(new Vector2D(0.15, 0.15), segment1.getEnd());
        assertEquals(new Vector2D(0.3, 0.3), segment2.getStart());
        assertEquals(new Vector2D(0.6, 0.6), segment2.getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.3), new Interval(0.6, 0.8)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5), new Interval(1.5, 2.0)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.3), new Interval(0.6, 0.8)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5), new Interval(1.5, 2.0)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.6), intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.3), new Interval(0.6, 0.8)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5), new Interval(1.5, 2.0)));

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.3), new Interval(0.6, 0.8)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5), new Interval(1.5, 2.0)));

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(0.5), intersectionPoint);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.3), new Interval(0.6, 0.8)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5), new Interval(1.5, 2.0)));

        Side side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.MINUS, side);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.3), new Interval(0.6, 0.8)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5), new Interval(1.5, 2.0)));

        side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.1, 0.3), new Interval(0.6, 0.8)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5), new Interval(1.5, 2.0)));

        Hyperplane<Euclidean2D> splitHyperplane1 = new Line(new Vector2D(0.5, 0.5), new Vector2D(0.6, 0.6));
        Hyperplane<Euclidean2D> splitHyperplane2 = new Line(new Vector2D(0.7, 0.7), new Vector2D(0.8, 0.8));
        SubHyperplane<Euclidean1D> splitSubHyperplanePlus = new OrientedPoint(new Vector1D(0.4), true).wholeHyperplane();
        SubHyperplane<Euclidean1D> splitSubHyperplaneMinus = new OrientedPoint(new Vector1D(0.9), false).wholeHyperplane();

        BSPTree<Euclidean1D> plusTree = new BSPTree<>(Boolean.FALSE);
        BSPTree<Euclidean1D> minusTree = new BSPTree<>(Boolean.FALSE);

        plusTree = plusTree.split(splitSubHyperplaneMinus);
        minusTree = minusTree.split(splitSubHyperplanePlus);

        SubHyperplane<Euclidean2D> splitResult = subLine1.split(subLine2.getHyperplane());
        assertNotNull(splitResult);

        SubLine splitLinePlus = ((SubLine) splitResult.getSubHyperplane1());
        SubLine splitLineMinus = ((SubLine) splitResult.getSubHyperplane2());

        assertEquals(splitHyperplane1, splitLinePlus.getHyperplane());
        assertEquals(splitSubHyperplanePlus, splitLinePlus.getRemainingRegion().getTree(false));

        assertEquals(splitHyperplane2, splitLineMinus.getHyperplane());
        assertEquals(splitSubHyperplaneMinus, splitLineMinus.getRemainingRegion().getTree(false));
    }
}