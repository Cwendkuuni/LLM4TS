package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitType;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        // Test empty constructor
        assertNull(new SubLine<>(null, null));

        // Test constructor with hyperplane and region
        final Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        final IntervalSet intervalSet = new IntervalsSet(new Interval(0, 3.5));
        final SubLine subLine = new SubLine<>(line, intervalSet);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervalSet, subLine.getRemainingRegion());

        // Test constructor with two endpoints
        final Vector2D start = new Vector2D(0, 0);
        final Vector2D end = new Vector2D(1, 1);
        final SubLine subLine1 = new SubLine<>(start, end);
        assertEquals(new Line(start, end), subLine1.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(start.getX(), end.getX())), subLine1.getRemainingRegion());

        // Test constructor with a segment
        final Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), line);
        final SubLine subLine2 = new SubLine<>(segment);
        assertEquals(line, subLine2.getHyperplane());
        assertEquals(segment.getIntervalSet(), subLine2.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        final Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        final List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(0.5, 1.5));
        intervals.add(new Interval(2.5, 3.5));
        final IntervalsSet region = new IntervalsSet(intervals);
        final SubLine subLine = new SubLine<>(line, region);

        final List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        final Segment segment1 = segments.get(0);
        final Segment expectedSegment1 = new Segment(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5), line);
        assertEquals(expectedSegment1.getStart(), segment1.getStart());
        assertEquals(expectedSegment1.getEnd(), segment1.getEnd());
        assertEquals(expectedSegment1.getLine(), segment1.getLine());

        final Segment segment2 = segments.get(1);
        final Segment expectedSegment2 = new Segment(new Vector2D(2.5, 2.5), new Vector2D(3.5, 3.5), line);
        assertEquals(expectedSegment2.getStart(), segment2.getStart());
        assertEquals(expectedSegment2.getEnd(), segment2.getEnd());
        assertEquals(expectedSegment2.getLine(), segment2.getLine());
    }

    @Test
    public void testIntersection() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));

        final SubLine subLine1 = new SubLine<>(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        final SubLine subLine2 = new SubLine<>(line2, new IntervalsSet(new Interval(1.5, 2.5)));

        // Test intersection with no intersection
        assertNull(subLine1.intersection(subLine2, false));

        // Test intersection with intersection on both lines
        final Vector2D intersectionPoint = new Vector2D(1, 1);
        final SubLine subLine1Intersect = new SubLine<>(line1, new IntervalsSet(new Interval(0.5, 2.5)));
        final SubLine subLine2Intersect = new SubLine<>(line2, new IntervalsSet(new Interval(1, 2)));
        final Vector2D expectedIntersectionPoint = intersectionPoint;
        assertEquals(expectedIntersectionPoint, subLine1Intersect.intersection(subLine2Intersect, false));

        // Test intersection with intersection on one line
        final SubLine subLine1WithEndPoints = new SubLine<>(line1, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 2.5)));
        final SubLine subLine2IntersectWithEndPoints = new SubLine<>(line2, new IntervalsSet(new Interval(1, Double.POSITIVE_INFINITY)));
        assertNull(subLine1WithEndPoints.intersection(subLine2IntersectWithEndPoints, false));

        // Test intersection with intersection on one line and includeEndPoints set to true
        final SubLine subLine1WithIncludeEndPoints = new SubLine<>(line1, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 2.5)));
        final SubLine subLine2IntersectWithIncludeEndPoints = new SubLine<>(line2, new IntervalsSet(new Interval(1, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 1)));
        assertEquals(expectedIntersectionPoint, subLine1WithIncludeEndPoints.intersection(subLine2IntersectWithIncludeEndPoints, true));

        // Test intersection with intersection on one line and includeEndPoints set to false
        assertNull(subLine1WithIncludeEndPoints.intersection(subLine2IntersectWithIncludeEndPoints, false));
    }

    @Test
    public void testSide() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));

        final SubLine subLine1 = new SubLine<>(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        final SubLine subLine2 = new SubLine<>(line2, new IntervalsSet(new Interval(1.5, 2.5)));

        // Test side of line1 against line2
        final Side expectedSide = Side.MINUS;
        assertEquals(expectedSide, subLine1.side(line2));

        // Test side of line2 against line1
        final Side expectedSideReverse = Side.PLUS;
        assertEquals(expectedSideReverse, line2.side(subLine1));

        // Test side of two parallel lines
        final Line line3 = new Line(new Vector2D(0, 0), new Vector2D(2, 0));
        final SubLine subLine3 = new SubLine<>(line3, new IntervalsSet(new Interval(0.5, 1.5)));
        final Side expectedSideParallel = Side.HYPER;
        assertEquals(expectedSideParallel, subLine1.side(line3));
        assertEquals(expectedSideParallel, line3.side(subLine1));
    }

    @Test
    public void testSplit() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));

        final SubLine subLine1 = new SubLine<>(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        final SubLine subLine2 = new SubLine<>(line2, new IntervalsSet(new Interval(1.5, 2.5)));

        // Test split with no intersection
        final SubHyperplane<Euclidean2D> expectedNull = null;
        assertNull(subLine1.split(line2));
        assertNull(line2.split(subLine1));

        // Test split with intersection on both lines
        final Vector2D intersectionPoint = new Vector2D(1, 1);
        final SubLine subLine1Intersect = new SubLine<>(line1, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 2.5)));
        final SubLine subLine2Intersect = new SubLine<>(line2, new IntervalsSet(new Interval(1, Double.POSITIVE_INFINITY)));
        final SubHyperplane<Euclidean2D> subLine1Split = new SplitSubHyperplane<>(subLine1, subLine2Intersect);
        final SubHyperplane<Euclidean2D> expectedSubLine1Split = new SplitSubHyperplane<>(subLine1Intersect, null);
        assertEquals(expectedSubLine1Split, subLine1.split(line2));
        assertEquals(expectedSubLine1Split, line2.split(subLine1));

        // Test split with intersection on one line
        final SubLine subLine1WithEndPoints = new SubLine<>(line1, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 2.5)));
        final SubLine subLine2IntersectWithEndPoints = new SubLine<>(line2, new IntervalsSet(new Interval(1, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 1)));
        final SubHyperplane<Euclidean2D> expectedNullSplit = null;
        assertNull(subLine1WithEndPoints.split(subLine2IntersectWithEndPoints));
        assertNull(subLine2IntersectWithEndPoints.split(subLine1WithEndPoints));

        // Test split with intersection on one line and intersection point on the split line
        final SubLine subLine1WithSplitPoint = new SubLine<>(line1, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 2.5)));
        final SubLine subLine2IntersectWithSplitPoint = new SubLine<>(line2, new IntervalsSet(new Interval(1, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 1), new Interval(1, 1.01)));
        final SplitSubHyperplane<Euclidean2D> subLine1SplitWithSplitPoint = new SplitSubHyperplane<>(subLine1WithSplitPoint, subLine2IntersectWithSplitPoint, SplitType.INTERSECTION);
        final SubHyperplane<Euclidean2D> expectedSubLine1SplitWithSplitPoint = new SplitSubHyperplane<>(subLine1WithSplitPoint, subLine2IntersectWithSplitPoint);
        assertEquals(expectedSubLine1SplitWithSplitPoint, subLine1.split(line2));
        assertEquals(expectedSubLine1SplitWithSplitPoint, line2.split(subLine1));

        // Test split with intersection on one line and intersection point not on the split line
        final SubLine subLine1WithSplitPointOff = new SubLine<>(line1, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 2.5)));
        final SubLine subLine2IntersectWithSplitPointOff = new SubLine<>(line2, new IntervalsSet(new Interval(1, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 1), new Interval(1.5, 2.5)));
        final SplitSubHyperplane<Euclidean2D> subLine1SplitWithSplitPointOff = new SplitSubHyperplane<>(subLine1WithSplitPointOff, subLine2IntersectWithSplitPointOff, SplitType.EMPTY);
        final SubHyperplane<Euclidean2D> expectedSubLine1SplitWithSplitPointOff = new SplitSubHyperplane<>(subLine1WithSplitPointOff, null);
        assertEquals(expectedSubLine1SplitWithSplitPointOff, subLine1.split(line2));
        assertNull(line2.split(subLine1));

        // Test split with intersection on one line and intersection point not on the split line and intersection points on both lines
        final SubLine subLine1WithSplitPointOffAndIntersection = new SubLine<>(line1, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 2.5), new Interval(2, Double.POSITIVE_INFINITY)));
        final SubLine subLine2IntersectWithSplitPointOffAndIntersection = new SubLine<>(line2, new IntervalsSet(new Interval(1, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 1), new Interval(1.5, 2.5), new Interval(3, Double.POSITIVE_INFINITY)));
        final SplitSubHyperplane<Euclidean2D> subLine1SplitWithSplitPointOffAndIntersection = new SplitSubHyperplane<>(subLine1WithSplitPointOffAndIntersection, subLine2IntersectWithSplitPointOffAndIntersection, SplitType.INTERSECTION);
        final SubHyperplane<Euclidean2D> expectedSubLine1SplitWithSplitPointOffAndIntersection = new SplitSubHyperplane<>(subLine1WithSplitPointOffAndIntersection, null);
        assertEquals(expectedSubLine1SplitWithSplitPointOffAndIntersection, subLine1.split(line2));
        assertNull(line2.split(subLine1));
    }
}