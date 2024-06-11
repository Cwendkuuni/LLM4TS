package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Orientation;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line, new IntervalsSet(new Interval(0.0, 1.0)));

        List<Segment> segments = subLine1.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), line), segment);

        SubLine subLine2 = new SubLine(line, new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0)));
        segments = subLine2.getSegments();
        assertEquals(2, segments.size());
        segment = segments.get(0);
        assertEquals(new Segment(new Vector2D(0.0, 0.0), new Vector2D(0.5, 0.5), line), segment);
        segment = segments.get(1);
        assertEquals(new Segment(new Vector2D(1.5, 1.5), new Vector2D(2.0, 2.0), line), segment);
    }

    @Test
    public void testSubLineIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1.0, 1.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(Double.POSITIVE_INFINITY, 0.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY)));

        line2 = new Line(new Vector2D(0.0, Double.POSITIVE_INFINITY), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), intersectionPoint);
    }

    @Test
    public void testSubLineSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));

        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5)));

        Side side = subLine.side(line2);
        assertEquals(Side.MINUS, side);

        subLine = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0)));

        side = subLine.side(line2);
        assertEquals(Side.PLUS, side);

        line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));

        side = subLine.side(line2);
        assertEquals(Side.HYPER, side);
    }
}