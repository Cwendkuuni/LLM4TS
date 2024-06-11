package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testCreateSubLine() {
        // Given
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);

        // When
        SubLine subLine = new SubLine(start, end);

        // Then
        assertEquals(line, subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());
    }

    @Test
    public void testCreateSubLineFromSegment() {
        // Given
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        Segment segment = new Segment(start, end, line);

        // When
        SubLine subLine = new SubLine(segment);

        // Then
        assertEquals(line, subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());
    }

    @Test
    public void testCreateSubLineFromHyperplaneAndRegion() {
        // Given
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Line2D(1.0, 0.0, 2.0), null);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0));

        // When
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        // Then
        assertEquals(hyperplane, subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        // Given
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(5.0, 6.0);
        Vector2D end2 = new Vector2D(7.0, 8.0);
        Line line = new Line(start1, end1);
        IntervalsSet region = new IntervalsSet(new Interval(start1.getX(), end1.getX()),
                new Interval(start2.getX(), end2.getX()));
        SubLine subLine = new SubLine(line, region);

        // When
        List<Segment> segments = subLine.getSegments();

        // Then
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);
        assertEquals(start1, segment1.getStart());
        assertEquals(end1, segment1.getEnd());
        assertEquals(start2, segment2.getStart());
        assertEquals(end2, segment2.getEnd());
    }

    @Test
    public void testIntersection() {
        // Given
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(3.0, 4.0);
        Vector2D end2 = new Vector2D(5.0, 6.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // When
        Vector2D intersection = subLine1.intersection(subLine2, true);

        // Then
        assertNull(intersection);

        // Given different lines
        start2 = new Vector2D(1.0, 2.0);
        end2 = new Vector2D(3.0, 4.0);
        line2 = new Line(start2, end2);

        // When
        intersection = subLine1.intersection(subLine2, true);

        // Then
        assertNotNull(intersection);
        assertEquals(new Vector2D(2.0, 3.0), intersection);
    }
}