package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
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
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;

/**
 * Test cases for {@link SubLine} class.
 */
public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Test empty line
        SubLine emptyLine = new SubLine(new Line(new Point2D(), new Point2D()), Collections.emptyList());
        List<Segment> segments = emptyLine.getSegments();
        assertTrue(segments.isEmpty());

        // Test line with one segment
        Hyperplane<Euclidean2D> hyperplane = new Line(new Point2D(1.0, 0.0), new Point2D(2.0, 0.0));
        SubLine line = new SubLine(hyperplane, new IntervalSet(new Interval(0.0, 1.0)));
        segments = line.getSegments();
        List<Segment> expected = new ArrayList<>();
        expected.add(new Segment(new Point2D(1.0, 0.0), new Point2D(2.0, 0.0), line.getHyperplane()));
        assertArrayEquals(expected.toArray(), segments.toArray());

        // Test line with multiple segments
        hyperplane = new Line(new Point2D(0.0, 0.0), new Point2D(3.0, 3.0));
        IntervalSet remainingRegion = new IntervalSet(new Interval(0.5, 1.5), new Interval(2.0, 2.5));
        SubLine multipleSegmentsLine = new SubLine(hyperplane, remainingRegion);
        segments = multipleSegmentsLine.getSegments();
        expected.clear();
        expected.add(new Segment(new Point2D(0.5, 0.0), new Point2D(1.5, 0.0), multipleSegmentsLine.getHyperplane()));
        expected.add(new Segment(new Point2D(2.0, 2.0), new Point2D(2.5, 2.0), multipleSegmentsLine.getHyperplane()));
        assertArrayEquals(expected.toArray(), segments.toArray());
    }

    @Test
    public void testIntersection() {
        // Test intersection with null subLine
        SubLine subLine = new SubLine(new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0)), new IntervalSet());
        assertNull(subLine.intersection(null, true));
        assertNull(subLine.intersection(null, false));

        // Test intersection with self
        SubLine identicalSubLine = new SubLine(new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0)), new IntervalSet());
        Vector2D intersectionPoint = identicalSubLine.intersection(identicalSubLine, true);
        assertNull(intersectionPoint);
        intersectionPoint = identicalSubLine.intersection(identicalSubLine, false);
        assertNull(intersectionPoint);

        // Test intersection with parallel lines
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalSet());
        Line line2 = new Line(new Point2D(1.0, 0.0), new Point2D(2.0, 1.0));
        SubLine subLine2 = new SubLine(line2, new IntervalSet());
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));

        // Test intersection with intersecting lines
        Line line3 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        SubLine subLine3 = new SubLine(line3, new IntervalSet());
        Line line4 = new Line(new Point2D(1.0, 0.0), new Point2D(2.0, 1.0));
        SubLine subLine4 = new SubLine(line4, new IntervalSet());
        Vector2D intersectionPoint1 = subLine3.intersection(subLine4, true);
        Point2D expectedIntersectionPoint1 = new Point2D(0.5, 0.5);
        assertEquals(expectedIntersectionPoint1.getX(), intersectionPoint1.getX(), 1e-6);
        assertEquals(expectedIntersectionPoint1.getY(), intersectionPoint1.getY(), 1e-6);
        intersectionPoint1 = subLine3.intersection(subLine4, false);
        assertArrayEquals(expectedIntersectionPoint1.toArray(), intersectionPoint1.toArray(), 1e-6);

        // Test intersection with intersecting lines, endpoints included
        intersectionPoint1 = subLine3.intersection(subLine4, true);
        assertTrue(intersectionPoint1 != null);
        Vector2D intersectionPoint2 = subLine3.intersection(subLine4, false);
        assertTrue(intersectionPoint2 != null);
        assertEquals(intersectionPoint1.getX(), intersectionPoint2.getX(), 1e-6);
        assertEquals(intersectionPoint1.getY(), intersectionPoint2.getY(), 1e-6);

        // Test intersection with non-intersecting lines
        Line line5 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0));
        SubLine subLine5 = new SubLine(line5, new IntervalSet());
        Line line6 = new Line(new Point2D(1.0, 1.0), new Point2D(2.0, 2.0));
        SubLine subLine6 = new SubLine(line6, new IntervalSet());
        assertNull(subLine5.intersection(subLine6, true));
        assertNull(subLine5.intersection(subLine6, false));
    }

    @Test
    public void testSide() {
        // Test side with null hyperplane
        SubLine subLine = new SubLine(new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0)), new IntervalSet());
        assertNull(subLine.side(null));

        // Test side with self
        SubLine identicalSubLine = new SubLine(new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0)), new IntervalSet());
        Side side = identicalSubLine.side(identicalSubLine.getHyperplane());
        assertEquals(Side.INSIDE, side);

        // Test side with parallel lines
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalSet());
        Line line2 = new Line(new Point2D(1.0, 0.0), new Point2D(2.0, 1.0));
        SubLine subLine2 = new SubLine(line2, new IntervalSet());
        Side side1 = subLine1.side(subLine2.getHyperplane());
        Side side2 = subLine2.side(subLine1.getHyperplane());
        assertEquals(Side.HYPER, side1);
        assertEquals(Side.HYPER, side2);

        // Test side with intersecting lines
        Line line3 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        SubLine subLine3 = new SubLine(line3, new IntervalSet());
        Line line4 = new Line(new Point2D(1.0, 0.0), new Point2D(2.0, 1.0));
        SubLine subLine4 = new SubLine(line4, new IntervalSet());
        Side side3 = subLine3.side(subLine4.getHyperplane());
        Side side4 = subLine4.side(subLine3.getHyperplane());
        assertEquals(Side.MINUS, side3);
        assertEquals(Side.PLUS, side4);
    }

    @Test
    public void testSplit() {
        // Test split with null hyperplane
        SubLine subLine = new SubLine(new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0)), new IntervalSet());
        SplitSubHyperplane<Euclidean2D> splitLine = subLine.split(null);
        assertNull(splitLine);

        // Test split with self
        SubLine identicalSubLine = new SubLine(new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0)), new IntervalSet());
        SplitSubHyperplane<Euclidean2D> splitIdenticalSubLine = identicalSubLine.split(identicalSubLine.getHyperplane());
        assertNull(splitIdenticalSubLine);

        // Test split with parallel lines
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalSet());
        Line line2 = new Line(new Point2D(1.0, 0.0), new Point2D(2.0, 1.0));
        SubLine subLine2 = new SubLine(line2, new IntervalSet());
        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(subLine2.getHyperplane());
        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(subLine1.getHyperplane());
        assertNull(split1);
        assertNull(split2);

        // Test split with intersecting lines
        Line line3 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        SubLine subLine3 = new SubLine(line3, new IntervalSet());
        Line line4 = new Line(new Point2D(1.0, 0.0), new Point2D(2.0, 1.0));
        SubLine subLine4 = new SubLine(line4, new IntervalSet());
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Point2D(0.5, 0.0), new Point2D(0.5, 1.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Point2D(1.5, 0.0), new Point2D(1.5, 1.0));
        SplitSubHyperplane<Euclidean2D> split3 = subLine3.split(hyperplane1);
        SplitSubHyperplane<Euclidean2D> split4 = subLine4.split(hyperplane1);
        SubLine subLine5 = new SubLine(line3.copySelf(), new IntervalSet(split3.getPlus()));
        SubLine subLine6 = new SubLine(line3.copySelf(), new IntervalSet(split3.getMinus()));
        SubLine subLine7 = new SubLine(line4.copySelf(), new IntervalSet(split4.getPlus()));
        SubLine subLine8 = new SubLine(line4.copySelf(), new IntervalSet(split4.getMinus()));
        SplitSubHyperplane<Euclidean2D> expectedSplit3 = new SplitSubHyperplane<Euclidean2D>(subLine5, subLine6);
        SplitSubHyperplane<Euclidean2D> expectedSplit4 = new SplitSubHyperplane<Euclidean2D>(subLine7, subLine8);
        assertEquals(expectedSplit3, split3);
        assertEquals(expectedSplit4, split4);

        // Test split with non-intersecting lines
        Line line5 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 0.0));
        SubLine subLine5 = new SubLine(line5, new IntervalSet());
        Line line6 = new Line(new Point2D(1.0, 1.0), new Point2D(2.0, 2.0));
        SubLine subLine6 = new SubLine(line6, new IntervalSet());
        assertNull(subLine5.split(subLine6.getHyperplane()));
        assertNull(subLine6.split(subLine5.getHyperplane()));
    }
}