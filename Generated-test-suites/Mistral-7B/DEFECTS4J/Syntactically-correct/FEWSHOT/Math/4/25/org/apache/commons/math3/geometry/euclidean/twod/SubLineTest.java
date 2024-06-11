package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

/**
 * This class tests the methods of the {@code SubLine} class.
 */
public class SubLineTest {

    /**
     * Test constructor with hyperplane and remaining region.
     */
    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        // Given
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0));

        // When
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        // Then
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor with two endpoints.
     */
    @Test
    public void testConstructorWithTwoEndpoints() {
        // Given
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);

        // When
        SubLine subLine = new SubLine(start, end);

        // Then
        assertEquals(new Line(start, end), subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    /**
     * Test constructor with a segment.
     */
    @Test
    public void testConstructorWithSegment() {
        // Given
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        Segment segment = new Segment(start, end, new Line(start, end));

        // When
        SubLine subLine = new SubLine(segment);

        // Then
        assertEquals(segment.getLine(), subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegments() {
        // Given
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        IntervalsSet remainingRegion = new IntervalsSet(
                new Interval(0.0, 1.0),
                new Interval(1.5, 2.0));
        SubLine subLine = new SubLine(line, remainingRegion);

        // When
        List<Segment> segments = subLine.getSegments();

        // Then
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(1.5, 1.5), new Vector2D(2.0, 2.0), line), segments.get(1));
    }

    /**
     * Test intersection method with another sub-line and includeEndPoints = true.
     */
    @Test
    public void testIntersectionWithAnotherSubLineAndIncludeEndPointsTrue() {
        // Given
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        IntervalsSet intervals1 = new IntervalsSet(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0));
        IntervalsSet intervals2 = new IntervalsSet(
                new Interval(1.5, 2.5),
                new Interval(3.5, 4.0));
        SubLine subLine1 = new SubLine(line1, intervals1);
        SubLine subLine2 = new SubLine(line2, intervals2);

        // When
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        // Then
        assertNotNull(intersectionPoint);
        assertEquals(new Vector2D(2.0, 2.0), intersectionPoint);
    }

    /**
     * Test intersection method with another sub-line and includeEndPoints = false.
     */
    @Test
    public void testIntersectionWithAnotherSubLineAndIncludeEndPointsFalse() {
        // Given
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        IntervalsSet intervals1 = new IntervalsSet(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0));
        IntervalsSet intervals2 = new IntervalsSet(
                new Interval(1.5, 2.5),
                new Interval(3.5, 4.0));
        SubLine subLine1 = new SubLine(line1, intervals1);
        SubLine subLine2 = new SubLine(line2, intervals2);

        // When
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);

        // Then
        assertNull(intersectionPoint);
    }

    /**
     * Test intersection method with another sub-line and one sub-line is empty.
     */
    @Test
    public void testIntersectionWithAnotherSubLineAndOneIsEmpty() {
        // Given
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervals1 = new IntervalsSet(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0));
        IntervalsSet intervals2 = new IntervalsSet(new Interval());
        SubLine subLine1 = new SubLine(line1, intervals1);
        SubLine subLine2 = new SubLine(line2, intervals2);

        // When
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        // Then
        assertNull(intersectionPoint);
    }

    /**
     * Test intersection method with another sub-line and both are empty.
     */
    @Test
    public void testIntersectionWithAnotherSubLineAndBothAreEmpty() {
        // Given
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervals1 = new IntervalsSet(new Interval());
        IntervalsSet intervals2 = new IntervalsSet(new Interval());
        SubLine subLine1 = new SubLine(line1, intervals1);
        SubLine subLine2 = new SubLine(line2, intervals2);

        // When
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        // Then
        assertNull(intersectionPoint);
    }

    /**
     * Test split method with another hyperplane.
     */
    @Test
    public void testSplitWithAnotherHyperplane() {
        // Given
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        IntervalsSet intervals1 = new IntervalsSet(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0));
        IntervalsSet intervals2 = new IntervalsSet(
                new Interval(1.5, 2.5),
                new Interval(3.5, 4.0));
        SubLine subLine1 = new SubLine(line1, intervals1);
        SubLine subLine2 = new SubLine(line2, intervals2);

        // When
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(1.5, 1.5));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> splitSubHyperplane = subLine1.split(hyperplane);

        // Then
        assertNotNull(splitSubHyperplane);
        assertTrue(splitSubHyperplane instanceof SplitSubHyperplane);
        SplitSubHyperplane<Euclidean2D> split = (SplitSubHyperplane<Euclidean2D>) splitSubHyperplane;
        assertEquals(subLine1, split.getSubHyperplane1());
        assertEquals(subLine2, split.getSubHyperplane2());
    }

    /**
     * Test side method with another hyperplane.
     */
    @Test
    public void testSideWithAnotherHyperplane() {
        // Given
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));

        // When
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(1.5, 1.5));
        Side side = subLine1.side(hyperplane);

        // Then
        assertEquals(Side.MINUS, side);
    }

    /**
     * Helper method to build an intervals set from two points.
     * @param start start point
     * @param end end point
     * @return an intervals set
     */
    private IntervalsSet buildIntervalSet(final Vector2D start, final Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(),
                               line.toSubSpace(end).getX());
    }
}