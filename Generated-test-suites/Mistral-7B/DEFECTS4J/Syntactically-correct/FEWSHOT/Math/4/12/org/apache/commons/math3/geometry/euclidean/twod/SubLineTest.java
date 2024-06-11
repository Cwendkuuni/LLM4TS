package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector2D;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitType;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testCreateSubLine() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        IntervalsSet intervals = (IntervalsSet) subLine.getRemainingRegion();

        assertEquals(line.getSlope(), subLine.getLine().getSlope(), 0.0);
        assertEquals(line.getIntercept(), subLine.getLine().getIntercept(), 0.0);
        assertEquals(start, subLine.getSegments().get(0).getStart());
        assertEquals(end, subLine.getSegments().get(0).getEnd());

        for (Segment segment : subLine.getSegments()) {
            assertEquals(line, segment.getLine());
        }
    }

    @Test
    public void testIntersection() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(2.0, 2.0);
        Vector2D p3 = new Vector2D(3.0, 3.0);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p2, p3);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        // intersection should be null
        assertNull(subLine1.intersection(subLine2, false));

        Line line3 = new Line(p1, p3);
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY),
                                                               new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));

        Vector2D intersectionPoint = subLine1.intersection(subLine3, false);
        assertEquals(p2, intersectionPoint);

        // intersection should be null
        assertNull(subLine1.intersection(subLine3, true));

        // intersection should not be null
        intersectionPoint = subLine1.intersection(subLine3, true);
        assertEquals(p2, intersectionPoint);

        // test intersection with infinite lines
        Line infLine1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(Double.POSITIVE_INFINITY, 0.0));
        SubLine infSubLine1 = new SubLine(infLine1, new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY)));

        intersectionPoint = subLine1.intersection(infSubLine1, false);
        assertEquals(p2, intersectionPoint);

        // intersection should be null
        assertNull(subLine1.intersection(infSubLine1, true));

        // intersection should not be null
        intersectionPoint = subLine1.intersection(infSubLine1, true);
        assertEquals(p2, intersectionPoint);

        // test intersection with lines that are not parallel
        Line line4 = new Line(new Vector2D(0.0, 1.0), new Vector2D(2.0, 3.0));
        SubLine subLine4 = new SubLine(line4, new IntervalsSet(new Interval(0.0, 2.0)));

        intersectionPoint = subLine1.intersection(subLine4, false);
        assertNull(intersectionPoint);

        // intersection should not be null
        intersectionPoint = subLine1.intersection(subLine4, true);
        assertFalse(intersectionPoint.getX() == 0.0 || intersectionPoint.getY() == 0.0);

        // intersection should be null
        assertNull(subLine1.intersection(subLine4, false));
    }

    @Test
    public void testSplit() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(2.0, 2.0);
        Vector2D p3 = new Vector2D(3.0, 3.0);
        Vector2D p4 = new Vector2D(4.0, 4.0);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p2, p3);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        Hyperplane<Euclidean2D> splitHyperplane = new Line(p2, p4);
        SplitSubHyperplane<Euclidean2D> splitResult = subLine1.split(splitHyperplane);

        SubLine subLinePlus = (SubLine) splitResult.getSubHyperplane1();
        SubLine subLineMinus = (SubLine) splitResult.getSubHyperplane2();

        assertEquals(p1, subLinePlus.getSegments().get(0).getStart());
        assertEquals(p2, subLinePlus.getSegments().get(0).getEnd());
        assertEquals(p2, subLineMinus.getSegments().get(0).getStart());
        assertEquals(p3, subLineMinus.getSegments().get(0).getEnd());

        // check if the lines are split correctly
        List<Segment> segmentsPlus = subLinePlus.getSegments();
        List<Segment> segmentsMinus = subLineMinus.getSegments();

        for (Segment segmentPlus : segmentsPlus) {
            for (Segment segmentMinus : segmentsMinus) {
                if (line1.intersection(segmentPlus.getLine()) != null) {
                    assertEquals(line2, segmentMinus.getLine());
                    assertEquals(p4, segmentMinus.getStart());
                    assertEquals(p3, segmentMinus.getEnd());

                    assertEquals(segmentPlus.getLine(), segmentMinus.getLine());
                    assertEquals(segmentPlus.getStart(), segmentMinus.getEnd());
                    assertEquals(segmentPlus.getEnd(), segmentMinus.getStart());

                    // intersection should be null
                    assertNull(subLinePlus.intersection(subLineMinus, false));

                    // intersection should not be null
                    Vector2D intersectionPoint = subLinePlus.intersection(subLineMinus, true);
                    assertEquals(p2, intersectionPoint);

                    // intersection should be null
                    assertNull(subLineMinus.intersection(subLinePlus, false));

                    // intersection should not be null
                    intersectionPoint = subLineMinus.intersection(subLinePlus, true);
                    assertEquals(p2, intersectionPoint);
                }
            }
        }

        // check if the lines are not split
        Hyperplane<Euclidean2D> notSplitHyperplane = new Line(p1, p3);
        splitResult = subLine1.split(notSplitHyperplane);

        assertNull(splitResult);
    }

}