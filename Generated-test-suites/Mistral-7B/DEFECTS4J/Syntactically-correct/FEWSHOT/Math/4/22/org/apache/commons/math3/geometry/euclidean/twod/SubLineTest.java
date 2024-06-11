package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitType;
import org.apache.commons.math3.geometry.partitioning.VectorLine;
import org.apache.commons.math3.geometry.util.Precision;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Line line = new Line(new Vector2D(1.0, 0.0), new Vector2D(0.0, 1.0));
        IntervalsSet region = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 4.0));
        SubLine subLine1 = new SubLine(line, region);
        SubLine subLine2 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine3 = new SubLine(new Segment(new Vector2D(1.0, 1.0), new Vector2D(3.0, 1.0)));

        assertEquals(line, subLine1.getHyperplane());
        assertEquals(region, subLine1.getRemainingRegion());
        assertEquals(2, subLine1.getSegments().size());

        // Segment [(0.0, 1.0), (2.0, 2.0)]
        assertTrue(subLine1.getSegments().get(0).getStart().equals(new Vector2D(0.0, 1.0), 0.0));
        assertTrue(subLine1.getSegments().get(0).getEnd().equals(new Vector2D(2.0, 2.0), 0.0));

        // Segment [(2.0, 2.0), (Inf, Inf)]
        assertTrue(subLine1.getSegments().get(1).getStart().equals(new Vector2D(2.0, 2.0), 0.0));
        assertTrue(subLine1.getSegments().get(1).getEnd().isInfinite());

        // Line intersects with itself
        Line lineSelfIntersect = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLineSelfIntersect = new SubLine(lineSelfIntersect, new IntervalsSet(new Interval(0.0, 2.0)));
        assertEquals(2, subLineSelfIntersect.getSegments().size());
        assertEquals(subLineSelfIntersect.getSegments().get(0).getStart(), subLineSelfIntersect.getSegments().get(0).getEnd());

        // Empty subline
        Hyperplane<Euclidean2D> emptyHyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region emptyRegion = new IntervalsSet();
        SubLine emptySubLine = new SubLine(emptyHyperplane, emptyRegion);
        assertTrue(emptySubLine.getSegments().isEmpty());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 3.0)));

        // Lines don't intersect
        assertNull(subLine1.intersection(subLine2, false));
        assertNull(subLine2.intersection(subLine1, false));

        // Lines intersect at a point
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 4.0)));
        Vector2D intersection = subLine1.intersection(subLine3, false);
        assertNotNull(intersection);
        assertEquals(intersection.getX(), 1.0, Precision.EPSILON);
        assertEquals(intersection.getY(), 0.0, Precision.EPSILON);

        // Lines intersect at a point and include endpoints
        Line line4 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine4 = new SubLine(line4, new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 4.0)));
        intersection = subLine1.intersection(subLine4, true);
        assertNotNull(intersection);
        assertEquals(intersection.getX(), 1.0, Precision.EPSILON);
        assertEquals(intersection.getY(), 0.0, Precision.EPSILON);

        // Lines intersect at two points
        Line line5 = new Line(new Vector2D(0.0, 0.0), new Vector2D(4.0, 4.0));
        SubLine subLine5 = new SubLine(line5, new IntervalsSet(new Interval(0.0, 2.0), new Interval(2.0, 4.0)));
        intersection = subLine1.intersection(subLine5, false);
        assertNull(intersection);

        // Lines intersect at two points and include endpoints
        SubLine subLine5IncludeEndPoints = new SubLine(line5, new IntervalsSet(new Interval(0.0, 2.0), new Interval(2.0, 4.0)));
        intersection = subLine1.intersection(subLine5IncludeEndPoints, true);
        assertNotNull(intersection);
        assertEquals(intersection.getX(), 1.0, Precision.EPSILON);
        assertEquals(intersection.getY(), 0.0, Precision.EPSILON);
        intersection = subLine1.intersection(subLine5IncludeEndPoints, true);
        assertNotNull(intersection);
        assertEquals(intersection.getX(), 3.0, Precision.EPSILON);
        assertEquals(intersection.getY(), 2.0, Precision.EPSILON);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 3.0)));

        // Lines don't intersect
        assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
        assertEquals(Side.HYPER, subLine2.side(subLine1.getHyperplane()));

        // Lines intersect at a point
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 4.0)));
        assertEquals(Side.INSIDE, subLine1.side(subLine3.getHyperplane())); // Inside the subline
        assertEquals(Side.OUTSIDE, subLine3.side(subLine1.getHyperplane())); // Outside the subline

        // Lines intersect at two points
        Line line4 = new Line(new Vector2D(0.0, 0.0), new Vector2D(4.0, 4.0));
        SubLine subLine4 = new SubLine(line4, new IntervalsSet(new Interval(0.0, 2.0), new Interval(2.0, 4.0)));
        assertEquals(Side.INSIDE, subLine1.side(subLine4.getHyperplane())); // Inside the subline
        assertEquals(Side.OUTSIDE, subLine4.side(subLine1.getHyperplane())); // Outside the subline

        // Line is parallel to the subline and intersects it at infinity
        Line line5 = new Line(new Vector2D(0.0, 0.0), new Vector2D(4.0, 0.0));
        SubLine subLine5 = new SubLine(line5, new IntervalsSet(new Interval(3.0, 4.0)));
        assertEquals(Side.MINUS, subLine1.side(subLine5.getHyperplane())); // Negative side (left)
        assertEquals(Side.PLUS, subLine5.side(subLine1.getHyperplane())); // Positive side (right)
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 3.0)));

        // Lines don't intersect
        assertNull(subLine1.split(subLine2.getHyperplane()));
        assertNull(subLine2.split(subLine1.getHyperplane()));

        // Lines intersect at a point
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 4.0)));
        SplitSubHyperplane split = subLine1.split(subLine3.getHyperplane());
        assertNotNull(split);
        assertFalse(split.getLeftSubHyperplane() instanceof SubLine);
        assertFalse(split.getRightSubHyperplane() instanceof SubLine);

        // Lines intersect at two points
        Line line4 = new Line(new Vector2D(0.0, 0.0), new Vector2D(4.0, 4.0));
        SubLine subLine4 = new SubLine(line4, new IntervalsSet(new Interval(0.0, 2.0), new Interval(2.0, 4.0)));
        split = subLine1.split(subLine4.getHyperplane());
        assertNotNull(split);
        assertTrue(split.getLeftSubHyperplane() instanceof SubLine);
        assertTrue(split.getRightSubHyperplane() instanceof SubLine);

        // Line is parallel to the subline and intersects it at infinity
        Line line5 = new Line(new Vector2D(0.0, 0.0), new Vector2D(4.0, 0.0));
        SubLine subLine5 = new SubLine(line5, new IntervalsSet(new Interval(3.0, 4.0)));
        split = subLine1.split(subLine5.getHyperplane());
        assertNotNull(split);
        assertFalse(split.getLeftSubHyperplane() instanceof SubLine);
        assertTrue(split.getRightSubHyperplane() instanceof SubLine);

        // Line intersects the subline along a segment
        Line line6 = new Line(new Vector2D(0.0, 0.0), new Vector2D(4.0, 4.0));
        SubLine subLine6 = new SubLine(line6, new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 4.0)));
        Vector2D point = new Vector2D(1.5, 1.5);
        SubHyperplane<Euclidean2D> expectedSplit = new SubLine(line6, new IntervalsSet(new Interval(1.5, 3.0)));
        split = subLine1.split(new Line(point, new Vector2D(3.0, 3.0)), false);
        SubHyperplane<Euclidean2D> actualSplit = split.getLeftSubHyperplane();
        assertEquals(expectedSplit, actualSplit);
    }

    @Test
    public void testBuildNew() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        Region region1 = new IntervalsSet(new Interval(0.0, 2.0));
        SubLine subLine1 = new SubLine(line1, region1);
        AbstractSubHyperplane newSubLine = SubLine.buildNew(subLine1.getHyperplane(), subLine1.getRemainingRegion());
        assertEquals(subLine1, newSubLine);

        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region region2 = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 4.0));
        SubLine subLine2 = new SubLine(line2, region2);
        AbstractSubHyperplane newSubLine2 = SubLine.buildNew(subLine2.getHyperplane(), subLine2.getRemainingRegion());
        assertEquals(subLine2, newSubLine2);
    }

    private void assertEquals(SubLine expected, SubLine actual) {
        assertEquals(expected.getHyperplane(), actual.getHyperplane());
        assertEquals(expected.getRemainingRegion(), actual.getRemainingRegion());
        assertEquals(expected.getSegments(), actual.getSegments());
    }

    private void assertEquals(Line expected, Line actual) {
        assertEquals(expected.getOffset(actual), 0.0, Precision.EPSILON);
        assertEquals(expected.getSlope(), actual.getSlope(), Precision.EPSILON);
        assertEquals(expected.getIntercept(), actual.getIntercept(), Precision.EPSILON);
    }

    private void assertEquals(List<Segment> expected, List<Segment> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getStart(), actual.get(i).getStart());
            assertEquals(expected.get(i).getEnd(), actual.get(i).getEnd());
        }
    }
}