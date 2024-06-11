package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Interval;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubSpace;
import org.apache.commons.math3.geometry.partitioning.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Vector2D start = new Vector2D(0.5, 0.5);
        Vector2D end = new Vector2D(1.5, 1.5);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.5, 1.5));
        SubLine subLine1 = new SubLine(start, end);
        assertEquals(hyperplane1, subLine1.getHyperplane());
        assertEquals(remainingRegion1, subLine1.getRemainingRegion());

        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0, 1));
        SubLine subLine2 = new SubLine(segment);
        assertEquals(segment.getLine(), subLine2.getHyperplane());
        assertEquals(remainingRegion2, subLine2.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet remainingRegion = new IntervalsSet(new Interval(0, 0.5), new Interval(0.5, 1));
        SubLine subLine = new SubLine(line, remainingRegion);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0.5, 0.5), segment1.getStart());
        assertEquals(new Vector2D(0.5, 0.5), segment1.getEnd());

        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(0.5, 0.5), segment2.getStart().getCoordinates()[0]);
        assertEquals(new Vector2D(0.5, 0.5), segment2.getStart().getCoordinates()[1]);
        assertEquals(new Vector2D(1, 1), segment2.getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Vector2D result = subLine1.intersection(subLine2, false);
        assertNull(result);

        result = subLine1.intersection(subLine2, true);
        assertNull(result);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1_1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2_1 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Vector2D point = new Vector2D(1, 1);
        result = subLine1_1.intersection(subLine2_1, false);
        assertEquals(point, result);

        result = subLine1_1.intersection(subLine2_1, true);
        assertEquals(point, result);
    }
}